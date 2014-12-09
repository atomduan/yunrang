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

cmd="${DEPLOY_HOME}/rmcmd.sh mkdir -p ${ST_TRANS_INCREMENTAL}"
r_log "${cmd}"
eval ${cmd}

BCMD_HADOOP="${SH_CDH4}/bin-mapreduce1/hadoop"
function hdfs_list_cmd() {
	hdfs_source_path="$1"
	shift
	HDFS_LIST_CMD="${BCMD_HADOOP} fs -ls -R ${hdfs_source_path} 2>/dev/null"
	HDFS_LIST_CMD="${HDFS_LIST_CMD} | awk '{indicator=substr(\$0,0,1); if (indicator==\"-\") print \$0}'"
	HDFS_LIST_CMD="${HDFS_LIST_CMD} | grep -v _SUCCESS | grep -v _logs "
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

src_list=$(hdfs_list_cmd ${ST_HBASE_EXPORT_INCREMENTAL}) 
if [ -n "${src_list}" ]; then
	cmd="${DEPLOY_HOME}/transfer.sh -sh get"
	cmd="${cmd} ${ST_HBASE_EXPORT_INCREMENTAL}"
	cmd="${cmd} ${ST_TRANS_INCREMENTAL}"
	r_log "${cmd}"
	eval ${cmd}
else
	msg="${ST_TRANS_INCREMENTAL}/_COMPLETE added."
	msg="${msg} Due to emppty result yield from ${ST_HBASE_EXPORT_INCREMENTAL} is empty"
	r_log "${msg}"
	cmd="${DEPLOY_HOME}/rmcmd.sh touch ${ST_TRANS_INCREMENTAL}/_COMPLETE"
	r_log "${cmd}"
	eval ${cmd}
	msg="${ST_TRANS_INCREMENTAL}/_AR_COMPLETE added."
	msg="${msg} Due to emppty result yield from ${ST_HBASE_EXPORT_INCREMENTAL} is empty"
	r_log "${msg}"
	cmd="${DEPLOY_HOME}/rmcmd.sh touch ${ST_TRANS_INCREMENTAL}/_AR_COMPLETE"
	r_log "${cmd}"
	eval ${cmd}
fi
