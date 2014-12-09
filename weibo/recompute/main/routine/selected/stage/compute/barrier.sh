#!/bin/bash -
project_home=/data1/warehouse
source ${project_home}/main/cluster.ini ${project_home}
current_path=$(cd `dirname $(which $0)`; pwd)
stage_home=$(cd ${current_path}; cd ../.; pwd)

task_name="$1"
shift
if [ -z "${task_name}" ]; then
	echo "[${current_path}]: task_name can not be EMPTY! exit."
	exit 1
fi
source ${stage_home}/stage.ini "${task_name}"

BCMD_HADOOP="${SH_CDH4}/bin-mapreduce1/hadoop"

#0->true, 1->false
function is_stage_completed() {
	completed=0
	cmd="${BCMD_HADOOP} fs -ls ${ST_COMPUTE} 2>&1"
	r_log "[compute-barrier-command] ${cmd}"
	result=$(eval ${cmd})
	if ! echo ${result} | grep -o _COMPLETE; then
		msg="[compute-barrier] Complete condition not met yet."
		msg="${msg} Barrier until '_COMPLETE' file found under hdfs:${ST_COMPUTE}/"
		r_log "${msg}"
		completed=1
	fi
	return ${completed}
}

check_interval=3
while ! is_stage_completed 
do
	sleep ${check_interval}
done
r_log "[compute-barrier] Complete condition is met. Pass this barrier."
