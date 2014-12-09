#!/bin/bash -
project_home=/data1/warehouse
source ${project_home}/main/cluster.ini ${project_home}
current_path=$(cd `dirname $(which $0)`; pwd)

current_date=`date`
current_timestamp=`date +%s`

MASTER_ADDR='10.77.104.110'
CURR_HOST_ADDR=`hostname -i`

#PATH INITIALIZATION
current_script_name=`basename $0`
current_script_path=${current_path}/${current_script_name}

#SERVER CONFIG INITIALIZATION
server_arr_G=()
server_arr_size_G=0

#INITIALIZE SERVER_LIST ARRAY
index=0
for ipaddr in ${SERVER_LIST}
do
	server_arr_G[index]=${ipaddr}
	index=$(( ${index} + 1 ))	
done
server_arr_size_G=${index}

#LOG PATH INITIALIZATION
function r_log() {
	msg="$@"
	shift
	log_date=`date`
	echo "[${CURR_HOST_ADDR} ${log_date}]:${msg}"
}

#HDFS_BASE_CMD_CONFIG
hopt="$1"
shift
case "${hopt}" in
	-sh)
		BCMD_HADOOP="${SH_CDH4}/bin-mapreduce1/hadoop"
	;;
	-bj)
		BCMD_HADOOP="${BJ_CDH3}/bin/hadoop"
	;;
	*)
		cat <<EOF
Usage:
	(-sh|-bj) get <hdfs_src_dir> <local_dest_dir>
	(-sh|-bj) put <local_src_dir> <hdfs_dest_dir>
EOF
		exit 1
	;;
esac

function hdfs_list_cmd() {
	hdfs_source_path="$1"
	shift
	HDFS_LIST_CMD="${BCMD_HADOOP} fs -ls -R ${hdfs_source_path} 2>/dev/null"
	HDFS_LIST_CMD="${HDFS_LIST_CMD} | awk '{indicator=substr(\$0,0,1); if (indicator==\"-\") print \$0}'"
	HDFS_LIST_CMD="${HDFS_LIST_CMD} | grep -v _SUCCESS | grep -v _logs | grep -v _COMPLETE | grep -v _log | grep -v _COPY"
	HDFS_LIST_CMD="${HDFS_LIST_CMD} | tr -s ' ' | cut -d ' ' -f8"
	for file_path in `eval ${HDFS_LIST_CMD}`
	do
		cmd="echo ${file_path/${hdfs_source_path}/} | tr -d ' '"
		check=`eval ${cmd}`
		if [ -n "${check}" ]; then
			echo ${file_path}
		fi
	done
}

function do_task_fetching() {
	file_path="$1"
	shift
	dest_path="$1"
	shift
	HDFS_FETCH_CMD="${BCMD_HADOOP} fs -copyToLocal"
	if ! [ -d "${dest_path}" ]; then
		r_log "${dest_path} does not exsit, create it..."
		mkdir -p ${dest_path}
	fi
	file_name=`basename ${file_path}`
	target_file="${dest_path}/${file_name}"
	if ! [ -f "${target_file}" ]; then
		cmd="${HDFS_FETCH_CMD} ${file_path} ${dest_path}/"
		r_log "${cmd}"
		eval ${cmd}
	else
		r_log "${target_file} already exsit skip it......"	
	fi
}

function do_task_batch_dispatching() {
	target_ip="$1"
	shift
	hdfs_source_path="$1"
	shift
	local_root_path="$1"
	shift
	task_files="$@"
	shift

	if [ -n ${target_ip} ]; then
		for file_path in ${task_files}
		do
			if [ -n ${file_path} ]; then
				dest_path=${file_path/$hdfs_source_path/$local_root_path}
				dest_path=`echo ${dest_path} | tr -s "/"`
				dest_path=`dirname ${dest_path}`
				cmd="${current_script_path} ${hopt} --task_fetching ${file_path} ${dest_path}"
				cmd="ssh -n ${target_ip} \"${cmd}\""
				r_log "${cmd}"
				eval ${cmd} 
			fi
		done
		cmd="touch ${local_root_path}/_COMPLETE"
		cmd="ssh -n ${target_ip} \"${cmd}\""
		r_log "${cmd}"
		eval ${cmd}
	fi
}

function do_fetch_task_dispatching() {
	if [ ${CURR_HOST_ADDR} = ${MASTER_ADDR} ]; then
		hdfs_source_path="$1"
		shift
		local_root_path="$1"
		shift
		task_arr_G=()
		index=0
		while read line
		do
			file_path=$line
			arr_index=$(( ${index} % ${server_arr_size_G} ))
			task_files="${task_arr_G[arr_index]} ${file_path}"
			task_arr_G[arr_index]=${task_files}
			index=$(( ${index} + 1 ))	
		done< <(hdfs_list_cmd ${hdfs_source_path})

		task_arr_length=${#task_arr_G[@]}
		index=0
		while [ ${index} -lt ${task_arr_length} ]
		do
			task_files=${task_arr_G[index]}
			arr_index=$(( ${index} % ${server_arr_size_G} ))
			target_ip=${server_arr_G[arr_index]}
			if [ -n "${task_files}" ]; then
				do_task_batch_dispatching ${target_ip} ${hdfs_source_path} ${local_root_path} ${task_files} &
			fi
			index=$(( ${index} + 1 ))	
		done
	else
		r_log "current machine[${CURR_HOST_ADDR}] is not master machine[${MASTER_ADDR}]"
	fi
}

function do_task_gathering() {
	local_source_path="$1"
	shift
	hdfs_root_path="$1"
	shift
	HDFS_MKDIR_CMD="${BCMD_HADOOP} fs -mkdir -p"
	HDFS_PUT_CMD="${BCMD_HADOOP} fs -copyFromLocal"
	HDFS_TOUCH_CMD="${BCMD_HADOOP} fs -touchz"
	if [ -d "${local_source_path}" ]; then
		#MAKE POSSIBLE DIRS
		while read file_dir
		do
			if [ -n "${file_dir}" ]; then
				dest_dir=${file_dir/$local_source_path/$hdfs_root_path}
				dest_dir=`echo ${dest_dir} | tr -s "/"`
				cmd="${HDFS_MKDIR_CMD} ${dest_dir}"
				r_log "${cmd}"
				eval ${cmd}
			fi
		done< <(echo ${local_source_path};
				find ${local_source_path} -type d | sort | 
					awk 'BEGIN{last=""}{curr=$0; if (index(curr, last)==0) print last; last = curr}END{print last}')

		#CREATE LOG DIR FOR MONITORING
		cmd="${HDFS_MKDIR_CMD} ${hdfs_root_path}/_logs"
		r_log "${cmd}"
		eval ${cmd}

		#TRANSFER FILES
		while read file_path
		do
			file_dir=`dirname ${file_path}`
			file_name=`basename ${file_path}`
			dest_path=${file_dir/$local_source_path/$hdfs_root_path}/${CURR_HOST_ADDR}_${file_name}
			dest_path=`echo ${dest_path} | tr -s "/"`
			cmd="${HDFS_PUT_CMD} ${file_path} ${dest_path}"
			r_log "${cmd}"
			eval ${cmd}
		done< <(find ${local_source_path} -type f | grep -v _COMPLETE | grep -v _COPY) 

		#MARK COMPLETE
		cmd="${HDFS_TOUCH_CMD} ${hdfs_root_path}/_logs/${CURR_HOST_ADDR}_COMPLETE"
		r_log "${cmd}"
		eval ${cmd}
	fi
}

function do_gather_task_dispatching() {
	if [ ${CURR_HOST_ADDR} = ${MASTER_ADDR} ]; then
		local_source_path="$1"
		shift
		hdfs_root_path="$1"
		shift
		for target_ip in ${SERVER_LIST};
		do
			cmd="${current_script_path} ${hopt} --task_gathering ${local_source_path} ${hdfs_root_path}"
			cmd="ssh -n ${target_ip} \"${cmd}\""
			r_log "${cmd}"
			eval ${cmd} &
		done
	else
		r_log "current machine[${CURR_HOST_ADDR}] is not master machine[${MASTER_ADDR}]"
	fi
}

function do_dir_normalization() {
	arg_path="$@"
	shift
	nor_path=`dirname ${arg_path}`/`basename ${arg_path}`
	nor_path=`echo ${nor_path} | tr -s "/"`
	echo ${nor_path}
}

#--------------------TASK INNTERFACE--------------------#

cmd="$1"
shift
arg_1="$1"
shift
arg_2="$1"
shift

if [ -z "${arg_1}" ]; then
	echo "arg_1 can not be empty...."
	exit 1
fi

if [ -z "${arg_2}" ]; then
	echo "arg_2 can not be empty...."
	exit 1
fi

arg_1=`do_dir_normalization ${arg_1}`
arg_2=`do_dir_normalization ${arg_2}`

case "${cmd}" in
	get)
		do_fetch_task_dispatching ${arg_1} ${arg_2} 2>&1 &
	;;
	--task_fetching)
		do_task_fetching ${arg_1} ${arg_2} 2>&1
	;;
	put)
		do_gather_task_dispatching ${arg_1} ${arg_2} 2>&1 &
	;;
	--task_gathering)
		do_task_gathering ${arg_1} ${arg_2} 2>&1
	;;
	*)
		cat <<EOF
Usage:
	-put <local_src_dir> <hdfs_dest_dir>
	(-sh|-bj) get <hdfs_src_dir> <local_dest_dir>
	(-sh|-bj) put <local_src_dir> <hdfs_dest_dir>
EOF
	;;
esac
