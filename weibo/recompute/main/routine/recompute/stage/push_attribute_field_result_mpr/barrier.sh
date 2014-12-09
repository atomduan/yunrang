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

BCMD_HADOOP="${BJ_CDH3}/bin/hadoop"

#0->true, 1->false
function is_stage_completed() {
	completed=0
	cmd="${BCMD_HADOOP} fs -ls ${ST_PUSH_ATTRIBUTE_FIELD_RESULT_MPR}/_logs"
	cmd="${cmd} | grep _COMPLETE | grep -v grep 2>/dev/null"
	r_log "[push-barrier]${cmd}"
	result=$(eval ${cmd})
	for target_ip in ${SERVER_LIST}
	do
		target_mark="${target_ip}_COMPLETE"
		if ! echo ${result} | grep ${target_mark} > /dev/null; then
			msg="[put_attribute_filed_result_mpr_barrier]:"
			msg="${msg} Barrier condition not met on SERVER[${target_ip}]"
			msg="${msg}. There is no ${target_ip}_COMPLETE tag file under"
			msg="${msg} ${ST_PUSH_ATTRIBUTE_FIELD_RESULT_MPR}/_logs/"
			r_log "${msg}"
			completed=1
			break
		fi
	done
	return ${completed}
}

#check_interval=3
#while ! is_stage_completed 
#do
#	sleep ${check_interval}
#done

#cmd="${BCMD_HADOOP} fs -touchz ${ST_PUSH_ATTRIBUTE_FIELD_RESULT_MPR}/_PUSH_COMPLETE"
#r_log "${cmd}"
#eval ${cmd}
r_log "[put_attribute_filed_result_mpr_barrier]: Empty Stage.....Barrier condition is met. Pass this barrier."
