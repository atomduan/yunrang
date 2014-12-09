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

BCMD_HADOOP="${SH_CDH4}/bin-mapreduce1/hadoop"

cmd="${BCMD_HADOOP} fs -mkdir -p ${ST_HBASE_TASK_BASE_HOME}"
cmd="sudo -u search ${cmd}"
r_log "${cmd}"
eval ${cmd}

cmd="${BCMD_HADOOP} fs -chmod 777 ${ST_HBASE_TASK_BASE_HOME}"
cmd="sudo -u search ${cmd}"
r_log "${cmd}"
eval ${cmd}
