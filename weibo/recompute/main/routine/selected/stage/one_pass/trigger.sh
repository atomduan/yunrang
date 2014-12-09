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

BCMD_HADOOP="${SH_CDH4}/bin-mapreduce1/hadoop"

start_time="$1"
shift
end_time="$1"
shift

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
	cmd="sudo -u search ${cmd} 2>&1 | grep -v 'javax.naming.NameNotFoundException'"
	r_log "${cmd}"
	eval "${cmd}"
}

#Do current incremental recompute routine

if [ -n "${start_time}" ] && [ -n "${end_time}" ]; then
	type_dir="incremental"
	do_one_sampling_routine ${start_time} ${end_time} ${type_dir}
else
	r_log "start_time[${start_time}] and end_time [${end_time}] can not be empty"
fi

#MARK COMPUTE
HDFS_TOUCH_CMD="${BCMD_HADOOP} fs -touchz"
cmd="${HDFS_TOUCH_CMD} ${ST_COMPUTE}/_COMPLETE"
cmd="sudo -u search ${cmd}"
r_log "${cmd}"
eval "${cmd}"
