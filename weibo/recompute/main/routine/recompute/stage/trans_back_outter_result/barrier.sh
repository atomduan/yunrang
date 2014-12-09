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

#0->true, 1->false
function is_stage_completed() {
	completed=0
	cmd="${BCMD_HADOOP} fs -ls ${ST_TRANS_BACK_OUTTER_RESULT}/_logs"
	cmd="${cmd} | grep _COMPLETE | grep -v grep 2>/dev/null"
	r_log "[trans_back_outter_result_barrier]${cmd}"
	result=$(eval ${cmd})
	for target_ip in ${SERVER_LIST}
	do
		target_mark="${target_ip}_COMPLETE"
		if ! echo ${result} | grep ${target_mark} > /dev/null; then
			msg="[trans_back_outter_result_barrier]: Barrier condition not met on SERVER[${target_ip}]"
			msg="${msg}. There is no ${target_ip}_COMPLETE tag file under"
			msg="${msg} ${ST_TRANS_BACK_OUTTER_RESULT}/_logs/"
			r_log "${msg}"
			completed=1
			break
		fi
	done
	return ${completed}
}

check_interval=3
while ! is_stage_completed 
do
	sleep ${check_interval}
done

cmd="${BCMD_HADOOP} fs -touchz ${ST_TRANS_BACK_OUTTER_RESULT}/_COMPLETE"
r_log "${cmd}"
eval ${cmd}
r_log "[trans_back_outter_result_barrier]: Barrier condition is met. Pass this barrier."
