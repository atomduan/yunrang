#!/bin/bash -
project_home=/data1/warehouse
source ${project_home}/main/cluster.ini ${project_home}
current_path=$(cd `dirname $(which $0)`; pwd)
stage_home=$(cd ${current_path}; cd ../stage; pwd)

task_base_name="increment"

function compute_task_name() {
	task_base_name="$1"
	shift
	time_stamp=`date +%Y%m%d_%H%M%S`
	echo "${task_base_name}_${time_stamp}"
}
task_name=$(compute_task_name "${task_base_name}")
echo "task_name :[${task_name}]"
source ${stage_home}/stage.ini "${task_name}"

function barrier() {
	task_name="$1"
	shift
	stage_name="$1"
	shift
	if [ -n "${stage_name}" ]; then
		cmd="${stage_home}/${stage_name}/barrier.sh ${task_name}"
		r_log "${cmd}"
		eval ${cmd}
	fi
}

function trigger() {
	task_name="$1"
	shift
	stage_name="$1"
	shift
	if [ -n "${stage_name}" ]; then
		cmd="${stage_home}/${stage_name}/trigger.sh ${task_name}"
		r_log "${cmd}"
		eval ${cmd}
		barrier "${task_name}" "${stage_name}"
	fi
}

function trigger_routine() {
	#weave current routine
	task_name="$@"
	if [ -n "${task_name}" ]; then
		#fork sub routine
		trigger "${task_name}" "hbase_export_incremental" 		#Done
		trigger "${task_name}" "trans_incremental" 			#Done
	fi
}

trigger_routine "${task_name}"
