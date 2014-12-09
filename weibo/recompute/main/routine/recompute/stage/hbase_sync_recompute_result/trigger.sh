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

#refresh paths.lst
cmd="echo '' > ${current_path}/paths.lst"
r_log "${cmd}"
eval ${cmd}

SYNC_BACK_PATH=${ST_HBASE_MERGE_RECOMPUTE_RESULT}/merged_result/syncBack

BCMD_HADOOP="${SH_CDH4}/bin-mapreduce1/hadoop"
function hdfs_list_cmd() {
	hdfs_source_path="$1"
	shift
	HDFS_LIST_CMD="${BCMD_HADOOP} fs -ls ${hdfs_source_path} 2>/dev/null"
	HDFS_LIST_CMD="${HDFS_LIST_CMD} | awk '{indicator=substr(\$0,0,1); if (indicator==\"-\") print \$0}'"
	HDFS_LIST_CMD="${HDFS_LIST_CMD} | grep -v _SUCCESS | grep -v _logs | grep 'part-' "
	HDFS_LIST_CMD="${HDFS_LIST_CMD} | grep -v _COMPLETE | grep -v _log | grep -v _COPY"
	HDFS_LIST_CMD="${HDFS_LIST_CMD} | tr -s ' ' | cut -d ' ' -f8"
	for file_path in `eval ${HDFS_LIST_CMD}`
	do
		cmd="echo ${file_path/${hdfs_source_path}/} | tr -d ' '"
		check=`eval ${cmd}`
		if [ -n "${check}" ]; then
			echo ${file_path}
		fi
	done
}

src_list=$(hdfs_list_cmd ${SYNC_BACK_PATH}) 


cmd="${BCMD_HADOOP} fs -mkdir -p ${ST_HBASE_SYNC_RECOMPUTE_RESULT}"
cmd="sudo -u search ${cmd}"
r_log "${cmd}"
eval ${cmd}

if [ -n "${src_list}" ]; then
	echo "${SYNC_BACK_PATH}" > ${current_path}/paths.lst
	cd ${current_path}	
	if [ -n "${current_path}" ]; then
		cmd="sudo rm -rf ${current_path}/target/*"
		r_log "${cmd}"
		eval ${cmd}
	fi
	cmd="${current_path}/input.py"
	cmd="sudo -u search ${cmd}"
	r_log "${cmd}"
	eval ${cmd}
	#Mark mpr job complete
	cmd="${BCMD_HADOOP} fs -touchz ${ST_HBASE_SYNC_RECOMPUTE_RESULT}/_COMPLETE"
	cmd="sudo -u search ${cmd}"
	r_log "${cmd}"
	eval ${cmd}
else
	msg="${ST_HBASE_SYNC_RECOMPUTE_RESULT}/_COMPLETE added."
	msg="${msg} Due to emppty result yield from ${SYNC_BACK_PATH} is empty"
	r_log "${msg}"
	cmd="${BCMD_HADOOP} fs -touchz ${ST_HBASE_SYNC_RECOMPUTE_RESULT}/_COMPLETE"
	cmd="sudo -u search ${cmd}"
	r_log "${cmd}"
	eval ${cmd}
fi
