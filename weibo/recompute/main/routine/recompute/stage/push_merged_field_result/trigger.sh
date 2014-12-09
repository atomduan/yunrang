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

cmd="${DEPLOY_HOME}/transfer.sh -bj put"
cmd="${cmd} ${ST_TRANS_FINAL_MERGED_DATA}"
cmd="${cmd} ${ST_PUSH_MERGED_FIELD_RESULT}"

r_log "${cmd}"
eval ${cmd}
