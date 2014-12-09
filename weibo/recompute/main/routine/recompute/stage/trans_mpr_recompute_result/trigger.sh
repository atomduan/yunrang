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
#this is a empty stage now.
cmd="${DEPLOY_HOME}/rmcmd.sh mkdir -p ${ST_TRANS_MPR_RECOMPUTE_RESULT}"
r_log "${cmd}"
eval ${cmd}
#so just mark this stage complete
cmd="${DEPLOY_HOME}/rmcmd.sh touch ${ST_TRANS_MPR_RECOMPUTE_RESULT}/_COMPLETE"
r_log "${cmd}"
eval ${cmd}
