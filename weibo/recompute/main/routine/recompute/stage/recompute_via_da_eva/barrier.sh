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

#0->true, 1->false
function is_completed() {
	completed=0
	for target_ip in ${SERVER_LIST}
	do
		cmd="ls ${ST_RECOMPUTE_VIA_DA_EVA}"
		cmd="ssh -n ${target_ip} \"${cmd}\" 2>/dev/null"
		result=$(eval ${cmd})
		if ! echo ${result} | grep -o _COMPLETE > /dev/null; then
			msg="[recompute_via_da_eva_barrier]:"
			msg="${msg} Barrier condition not met on SERVER[${target_ip}]"
			msg="${msg} There is no _COMPLETE tag file under ${ST_RECOMPUTE_VIA_DA_EVA}/"
			r_log "${msg}"
			completed=1
			break
		fi
	done	
	return ${completed}
}

check_interval=300
while ! is_completed 
do
	sleep ${check_interval}
done
r_log "[recompute_via_da_eva_barrier]: Barrier condition is met. Pass this barrier."
