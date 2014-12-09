#! /bin/bash -
project_home=/data1/warehouse
source ${project_home}/main/cluster.ini ${project_home}
current_path=$(cd `dirname $(which $0)`; pwd)

current_script_name=`basename $0`
cmd="$@"
ssh_cmd="ssh -n -o StrictHostKeyChecking=no"

if [ -n "${cmd}" ]; then
	for addr in ${SH_CDH4_LIST};
	do
		cmd_total="echo \\\`hostname -i\\\`	"
		cmd_total="${cmd_total}-----------------------------------------------------------------------"
		cmd_total="${cmd_total};${cmd}"
		cmd_total="${ssh_cmd} ${addr} \"${cmd_total}\" 2>&1"
		eval ${cmd_total}
	done
	sleep 3
	while true
	do
		indicate=$(ps -ef | grep ${current_script_name} | grep -v "grep" | grep "${ssh_cmd}")
		if [ -z "${indicate}" ]; then
			echo "current ${current_script_name} execution complete"
			break
		else
			sleep 1
		fi
	done
else
	echo "command can not be empty..."
fi
