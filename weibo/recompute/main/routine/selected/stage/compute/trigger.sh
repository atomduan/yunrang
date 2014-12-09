#!/bin/bash -
project_home=/data1/warehouse
source ${project_home}/main/cluster.ini ${project_home}
current_path=$(cd `dirname $(which $0)`; pwd)
stage_home=$(cd ${current_path}; cd ../.; pwd)

task_name="$1"
shift
if [ -z "${task_name}" ]; then
	r_log "[${current_path}]: task_name can not be EMPTY! exit."
	exit 1
fi
source ${stage_home}/stage.ini "${task_name}"

HISTORY_UPPER_LIMIT=4
HISTORY_LOWER_LIMIT=0
BCMD_HADOOP="${SH_CDH4}/bin-mapreduce1/hadoop"

function do_one_sampling_routine() {
	start_time="$1"
	shift
	end_time="$1"
	shift
	type_dir="$1"
	shift

	_SEP_="@_@"
	REDUCE_NUM=600
	JAR_PATH=${current_path}/mapreduce-client.jar

	time_dir="${start_time}_${end_time}"
	hadoop_payload="${start_time}$_SEP_${end_time}$_SEP_${ST_COMPUTE}/${type_dir}/${time_dir}"
	hadoop_opts="-submitByName ShCdh4HBaseSamplingTask"
	hadoop_opts="${hadoop_opts} -reducerNum ${REDUCE_NUM}"
	hadoop_opts="${hadoop_opts} -payLoad ${hadoop_payload}"

	cmd="${BCMD_HADOOP} jar ${JAR_PATH} ${hadoop_opts}"
	cmd="sudo -u search ${cmd}"
	r_log "${cmd}"
	eval "${cmd}"
}

function get_time_list() {
	curr=${HISTORY_LOWER_LIMIT}
	while true
	do
		if [ ${curr} -lt ${HISTORY_UPPER_LIMIT} ]; then
			echo `date -d "${curr} week ago" +%Y-%m-%d`
		else
			break
		fi
		curr=$(( ${curr} + 1 ))
	done
}

#Do current recompute routine
time_list=$(get_time_list)
start_time=''
end_time=''
for t in ${time_list}; do
	start_time="${t}"
	if [ -n "${start_time}" ] && [ -n "${end_time}" ]; then
		type_dir="recent"
		do_one_sampling_routine ${start_time} ${end_time} ${type_dir}
	fi
	end_time="${start_time}"
done


#Do current incremental recompute routine
start_week="${HISTORY_UPPER_LIMIT}"
end_week=$(( ${start_week} - 1 ))
start_time=`date -d "${start_week} week ago" +%Y-%m-%d`
end_time=`date -d "${end_week} week ago" +%Y-%m-%d`
if [ -n "${start_time}" ] && [ -n "${end_time}" ]; then
	type_dir="incremental"
	do_one_sampling_routine ${start_time} ${end_time} ${type_dir}
fi

#MARK COMPUTE
HDFS_TOUCH_CMD="${BCMD_HADOOP} fs -touchz"
cmd="${HDFS_TOUCH_CMD} ${ST_COMPUTE}/_COMPLETE"
cmd="sudo -u search ${cmd}"
r_log "${cmd}"
eval "${cmd}"
