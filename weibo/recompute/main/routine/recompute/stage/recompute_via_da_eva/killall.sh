#!/bin/bash -
project_home=/data1/warehouse
source ${project_home}/main/cluster.ini ${project_home}
current_path=$(cd `dirname $(which $0)`; pwd)
stage_home=$(cd ${current_path}; cd ../.; pwd)

task_name="recompute_test_1203_curr_2"
shift 1
if [ -z "${task_name}" ]; then
	echo "[${current_path}]: task_name can not be EMPTY! exit."
	exit 1
fi
source ${stage_home}/stage.ini "${task_name}"

cmd="ps -ef | grep routine | grep via_da_eva | grep -v grep | grep ${task_name}"
cmd="${cmd} | tr -s ' ' | cut -d ' ' -f2 | xargs -I{} kill {}"
cmd="${DEPLOY_HOME}/rmcmd.sh \"${cmd}\""

r_log "${cmd}"
eval ${cmd}
