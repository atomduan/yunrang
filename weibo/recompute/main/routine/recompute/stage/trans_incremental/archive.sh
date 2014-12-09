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

target_path=${ST_TRANS_INCREMENTAL};
if [ -d "${target_path}" ]; then
	target_ar=`find ${target_path} -maxdepth 1 -type d | grep '/201' | grep -v 'grep'`
	if [ -n "${target_ar}" ]; then
		cmd="mv ${target_ar} ${HISTORY_AR}/"	
		r_log "${cmd}"
		eval ${cmd}
		ar_name=`basename ${target_ar}`
		if [ -n "${ar_name}" ]; then
			cmd="ln -s ${HISTORY_AR}/${ar_name} ${target_path}/${ar_name}"
			r_log "${cmd}"
			eval ${cmd}
		fi
	fi
fi
cmd="touch ${target_path}/_AR_COMPLETE"
eval "${cmd}"
