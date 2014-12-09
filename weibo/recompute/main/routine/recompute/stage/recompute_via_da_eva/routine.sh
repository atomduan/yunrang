#!/bin/bash -
project_home=/data1/warehouse
source ${project_home}/main/cluster.ini ${project_home}
current_path=$(cd `dirname $(which $0)`; pwd)
stage_home=$(cd ${current_path}; cd ../.; pwd)

task_name="$1"
shift 1
if [ -z "${task_name}" ]; then
	echo "[${current_path}]: task_name can not be EMPTY! exit."
	exit 1
fi
source ${stage_home}/stage.ini "${task_name}"

time_tag="$1"
shift

function is_process_finished() {
	memcached_queue_address='127.0.0.1'
	memcached_queue_port=11233
	remained_items=`echo stats \
		| nc ${memcached_queue_address} ${memcached_queue_port} \
		| grep 'queue_da_eva_in_items' \
		| cut -d' ' -f3 | sed 's/\r//g'`
	if [ ${remained_items} -gt 0 ]; then
		return 1
	fi
	return 0
}

function attr_filter() {
	cat /dev/stdin	| grep -E '(^@$|^@ID:|^@TIME:)'
}

function merge_filter() {
	cat /dev/stdin	| grep -E '(^@$|^@ID:|^@TIME:|^@QI:)'
}

function file_action_pre_process() {
	${current_path}/php/off_getqueue_forinput.php > /dev/null
	${current_path}/php/off_getqueue_foroutput.php > /dev/null
}

function do_process_one_file() {
	source_file_path=$1
	shift
	tmp_file_path_attr=$1
	shift
	tmp_file_path_merge=$1
	shift
	target_file_path_attr=$1
	shift
	target_file_path_merge=$1
	shift

	if [ -f "${target_file_path_attr}" ] && [ -f "${target_file_path_merge}" ] ; then
		r_log "${target_file_path}'s sub result already exsit...skip this one"
		return 0
	fi

	r_log "[recompute_via_da_eva_routine] source file on processing is ${source_file_path}......"
	file_action_pre_process
	zcat ${source_file_path} | ${current_path}/php/kprod.php
	while ! is_process_finished
	do
		sleep 2
	done
	${current_path}/php/off_getqueue_foroutput.php | \
		tee >(attr_filter > ${tmp_file_path_attr}) \
		    >(merge_filter > ${tmp_file_path_merge}) \
		    >/dev/null
	if ! [ -f "${target_file_path_attr}" ]; then
		mv ${tmp_file_path_attr} ${target_file_path_attr}
	fi
	if ! [ -f "${target_file_path_merge}" ]; then
		mv ${tmp_file_path_merge} ${target_file_path_merge}
	fi
}

function process_one_file() {
	source_file_path=$1
	shift
	source_file_root=$1
	shift
	target_file_root=$1
	shift
	#fork target file path
	tf_root_attr=${target_file_root}/attr
	tf_root_merge=${target_file_root}/merge

	target_file_path_attr=${source_file_path/$source_file_root/$tf_root_attr}.attr
	target_file_path_merge=${source_file_path/$source_file_root/$tf_root_merge}.merge

	#check target dir exsit
	target_file_dir_attr=$(dirname ${target_file_path_attr})
	if ! [ -d "${target_file_dir_attr}" ]; then
		cmd="mkdir -p ${target_file_dir_attr}"
		r_log "${cmd}"
		eval ${cmd}
	fi
	target_file_dir_merge=$(dirname ${target_file_path_merge})
	if ! [ -d "${target_file_dir_merge}" ]; then
		cmd="mkdir -p ${target_file_dir_merge}"
		r_log "${cmd}"
		eval ${cmd}
	fi
	#check target file exsit
	tmp_file_path_attr=${target_file_dir_attr}/on_process_file.tmp.attr
	tmp_file_path_merge=${target_file_dir_merge}/on_process_file.tmp.merge
	if [ -n "${target_file_path_attr}" ] && [ -n ${target_file_path_merge} ]; then
		do_process_one_file ${source_file_path} ${tmp_file_path_attr} ${tmp_file_path_merge} \
			${target_file_path_attr} ${target_file_path_merge}
	else
		r_log "${target_file_path_attr} or ${target_file_path_merge} already exsit......"
	fi
}

PROCESS_LOCK_DIR=${PROJECT_HOME}/tmp/routine/recompute/stage/recompute_via_da_eva
if ! [ -d "${PROCESS_LOCK_DIR}" ]; then
	cmd="mkdir -p ${PROCESS_LOCK_DIR}"
	r_log "${cmd}"
	eval ${cmd}
fi
PROCESS_STUB=${PROCESS_LOCK_DIR}/stub
PROCESS_LOCK=${PROCESS_LOCK_DIR}/process.lock

function release_process_lock() {
	if [ -n "${PROCESS_LOCK}" ] && [ -e "${PROCESS_LOCK}" ]; then
		unlink ${PROCESS_LOCK}
	fi
}

function process_recompute_routine() {
	target_file_root=${ST_RECOMPUTE_VIA_DA_EVA}
	if ! [ -d "${target_file_root}" ]; then
		cmd="mkdir -p ${target_file_root}/attr"
		r_log "${cmd}"
		eval ${cmd}
		cmd="touch ${target_file_root}/attr/_INIT_"
		r_log "${cmd}"
		eval ${cmd}
		cmd="mkdir -p ${target_file_root}/merge"
		r_log "${cmd}"
		eval ${cmd}
		cmd="touch ${target_file_root}/merge/_INIT_"
		r_log "${cmd}"
		eval ${cmd}
	fi

	source_file_root="${HISTORY_AR}"
	if [ -d ${source_file_root} ]; then
		while read line
		do
			source_file_path=${line}
			if [ -n "${time_tag}" ]; then
				if ! echo "${source_file_path}" | grep -E "${time_tag}" > /dev/null; then
					msg="[recompute_via_da_eva]: time_tag policy"
					msg="${msg}, file[${source_file_path}] filted out by time_tag[${time_tag}]"
					r_log "${msg}"
					continue
				fi
			fi
			if ! [ -z "${source_file_path}" ]; then
				while true
				do
					if ! [ -e "${PROCESS_LOCK}" ]; then
						#try to aquire process lock
						if ln -s ${PROCESS_STUB} ${PROCESS_LOCK} 2>/dev/null; then
							trap "release_process_lock; exit 0" INT TERM EXIT
							echo "pid_$$_:${task_name}:${source_file_path}:${target_file_root}" > ${PROCESS_LOCK}
							process_one_file ${source_file_path} ${source_file_root} ${target_file_root}
							release_process_lock
							trap - INT TERM EXIT
							#give other task more opportunity
							sleep 2
							break
						fi
					fi
					sleep 1
				done
			fi
		done< <(find ${source_file_root} -type f | sort | grep part-)
	fi
	cmd="touch ${target_file_root}/_COMPLETE"
	r_log "${cmd}"
	eval ${cmd}
}

process_recompute_routine
