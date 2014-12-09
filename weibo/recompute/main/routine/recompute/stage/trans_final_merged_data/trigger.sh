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

function transfer_result() {
	source_path="$1"
	shift
	dest_path="$1"
	shift
	cmd="${DEPLOY_HOME}/rmcmd.sh mkdir -p ${dest_path}"
	r_log "${cmd}"
	eval ${cmd}
	cmd="${DEPLOY_HOME}/transfer.sh -sh get ${source_path} ${dest_path}"
	r_log "${cmd}"
	eval ${cmd}
}

function dir_exsit() {
	BCMD_HADOOP="${SH_CDH4}/bin-mapreduce1/hadoop"
	dir_name="$1"
	shift
	cmd="${BCMD_HADOOP} fs -ls ${dir_name} 2>&1 | grep -o 'No such file or directory' 2>&1"
	echo `eval ${cmd}`
}

result_path=${ST_HBASE_MERGE_RECOMPUTE_RESULT}/merged_result

attr_source_path=${result_path}/mergedAttr
test_attr_result=`dir_exsit ${attr_source_path}`
attr_dest_path=${ST_TRANS_FINAL_MERGED_DATA}/mergedAttr
if [ -z "${test_attr_result}" ]; then
	transfer_result ${attr_source_path} ${attr_dest_path}
else
	cmd="${DEPLOY_HOME}/rmcmd.sh mkdir -p ${attr_dest_path}"
	r_log "${cmd}"
	eval ${cmd}
	cmd="${DEPLOY_HOME}/rmcmd.sh touch ${attr_dest_path}/_EMPTY"
	r_log "${cmd}"
	eval ${cmd}
fi

index_source_path=${result_path}/mergedIndex
test_index_result=`dir_exsit ${index_source_path}`
index_dest_path=${ST_TRANS_FINAL_MERGED_DATA}/mergedIndex
if [ -z "${test_index_result}" ]; then
	transfer_result ${index_source_path} ${index_dest_path}
else
	cmd="${DEPLOY_HOME}/rmcmd.sh mkdir -p ${index_dest_path}"
	r_log "${cmd}"
	eval ${cmd}
	cmd="${DEPLOY_HOME}/rmcmd.sh touch ${index_dest_path}/_EMPTY"
	r_log "${cmd}"
	eval ${cmd}
fi
