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

#Init stage target
cmd="${BCMD_HADOOP} fs -mkdir -p ${ST_HBASE_EXPORT_INCREMENTAL}"
cmd="sudo -u search ${cmd}"
r_log "${cmd}"
eval ${cmd}

#Config mpr payload
task_class="ShCdh4HbaseExportTask"
start_time=`date -d '14 day ago' +%Y-%m-%d`
end_time=`date -d '8 day ago' +%Y-%m-%d`

shard_dir="${start_time}_${end_time}"
output_path="${ST_HBASE_EXPORT_INCREMENTAL}/${shard_dir}"
_SEP_="@_@"
hadoop_payload="${start_time}$_SEP_${end_time}$_SEP_${output_path}"
hadoop_opts="-submitByName ${task_class}"
hadoop_opts="${hadoop_opts} -payLoad ${hadoop_payload}"

#Run mpr job
jar_path=${current_path}/mapreduce-client.jar
cmd="${BCMD_HADOOP} jar ${jar_path} ${hadoop_opts} 2>&1 | grep -v 'DNS name not found'"
cmd="sudo -u search ${cmd}"
r_log "${cmd}"
eval ${cmd}

#Mark mpr job complete
cmd="${BCMD_HADOOP} fs -touchz ${ST_HBASE_EXPORT_INCREMENTAL}/_COMPLETE"
cmd="sudo -u search ${cmd}"
r_log "${cmd}"
eval ${cmd}
