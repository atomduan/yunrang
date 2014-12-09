#! /bin/bash -
project_home=/data1/warehouse
source ${project_home}/main/cluster.ini ${project_home}
current_path=$(cd `dirname $(which $0)`; pwd)

cmd="$@"

if [ -n "${cmd}" ]; then
	for addr in ${SERVER_LIST};
	do
		echo ${addr} "-----------------------------------------------------------------------------"
		ssh -n ${addr} "${cmd}"
	done
else
	echo "command can not be empty..."
fi
