#!/bin/bash -
project_home=/data1/warehouse
source ${project_home}/main/cluster.ini ${project_home}
current_path=$(cd `dirname $(which $0)`; pwd)

routine_log_name=mprsubmit_routine.log
routine_log_path=${LOG_HOME}/hadoop_embeded/routine

if ! [ -d "${routine_log_path}" ]; then
	mkdir -p ${routine_log_path}
fi

submit_job_name='ArchiveMergeJob'

iparams="-submitByName ${submit_job_name} -reducerNum 1"
cmd="${current_path}/mprsubmit.sh ${iparams}"
eval ${cmd}
