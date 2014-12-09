#!/bin/bash -
project_home=/data1/warehouse
source ${project_home}/main/cluster.ini ${project_home}
current_path=$(cd `dirname $(which $0)`; pwd)
stage_home=$(cd ${current_path}; cd ../stage; pwd)

task_base_name="recompute"

if ! [ "$#" -eq 2 ]; then
	cat <<EOF
invalid params number.
	./routine.sh <task_name_suffix> <time_tag> 
	<task_name_suffix>	used for identify the current recompute routine.
	<time_tag>		used to specify the month to recompute, format"+%Y-%m"
				time_tag is a time hint....
EOF
	exit 1
fi

task_name_suffix="$1"
shift
time_tag="$1"
shift

check_tg=`echo ${time_tag} | awk '$0~/^20[0-9]+-[0-9]+$/{print "MATCH"}' 2>/dev/null`
if [ -z "${check_tg}" ]; then
	cat <<EOF
invalid time_tag format, routine will exit.
	time_tag must be in format "+%Y-%m"
	example: 2014-01
EOF
	exit 1
fi
time_tag="/${time_tag}" #do not forget this back slash /

task_name="${task_base_name}_${task_name_suffix}"
echo "task_name :[${task_name}]"

name_suffix=`echo ${time_tag} | sed -r 's/[^a-zA-Z0-9_-]//g'`
if [ -n "${name_suffix}" ]; then
	task_name="${task_name}_tg_${name_suffix}"
fi
source ${stage_home}/stage.ini "${task_name}"

function barrier() {
	task_name="$1"
	shift
	stage_name="$1"
	shift
	if [ -n "${stage_name}" ]; then
		cmd="${stage_home}/${stage_name}/barrier.sh ${task_name} ${time_tag}"
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
		cmd="${stage_home}/${stage_name}/trigger.sh ${task_name} ${time_tag}"
		r_log "${cmd}"
		eval ${cmd}
		barrier "${task_name}" "${stage_name}"
	fi
}

function trigger_routine() {
	#weave current routine
	task_name="$1"
	shift
	task_existence_check
	if [ -n "${task_name}" ]; then
		#fork sub routine
		trigger "${task_name}" "hbase_task_initialization"

		trigger "${task_name}" "hbase_export_fields_compare_snapshot" &

		trigger "${task_name}" "recompute_via_mpr" &
		
		trigger "${task_name}" "recompute_via_da_eva"
		
		#fork sub routine
		trigger "${task_name}" "push_attribute_field_result" &
		
		trigger "${task_name}" "trans_back_outter_result"
		
		#join sub routine
		barrier "${task_name}" "recompute_via_mpr"
		
		#fork sub routine
		trigger "${task_name}" "trans_mpr_recompute_result" &
		
		barrier "${task_name}" "hbase_export_fields_compare_snapshot"

		trigger "${task_name}" "hbase_merge_recompute_result"
		trigger "${task_name}" "hbase_sync_recompute_result" &
		trigger "${task_name}" "trans_final_merged_data"
		
		#fork sub routine
		trigger "${task_name}" "push_merged_field_result" & 
		
		#join sub routine
		barrier "${task_name}" "trans_mpr_recompute_result"
		trigger "${task_name}" "push_attribute_field_result_mpr" &	
		
		#join sub routine
		barrier "${task_name}" "push_attribute_field_result"			
		barrier "${task_name}" "push_attribute_field_result_mpr"
		barrier "${task_name}" "push_merged_field_result"				
		barrier "${task_name}" "hbase_sync_recompute_result"	
	fi
}

trigger_routine "${task_name}"
