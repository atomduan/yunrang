#!/bin/bash -
project_home=/data1/warehouse
source ${project_home}/main/cluster.ini ${project_home}
current_path=$(cd `dirname $(which $0)`; pwd)

routine_log_file=mprsubmit_routine.log
routine_log_path=${LOG_HOME}/hadoop_embeded/routine

if ! [ -d "${routine_log_path}" ]; then
        mkdir -p ${routine_log_path}
fi

client_jar_name=mapreduce-client.jar
client_jar_rsyc=/data1/minisearch/upload/warehouse/${client_jar_name}
client_jar_path=${current_path}/lib

if ! [ -f ${client_jar_rsyc} ]; then
        echo "there is no client jar [${client_jar_rsyc}]"
        exit 1
fi

cp ${client_jar_rsyc} ${client_jar_path}/

cmd="${DW_EMBD}/bin/hadoop jar ${client_jar_path}/${client_jar_name}"

params="$@"

if ! [ -z "${params}" ]; then
	cmd="${cmd} ${params}"
fi
echo ${cmd}
eval ${cmd}
