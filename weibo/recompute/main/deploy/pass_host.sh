#! /bin/bash -
project_home=/data1/warehouse
source ${project_home}/main/cluster.ini ${project_home}
current_path=$(cd `dirname $(which $0)`; pwd)

function do_host_pass() {
        list="$@"
        for addr in ${list}
        do
		echo ${addr} "-----------------------------------------------------------------------------"
                ssh -n -o StrictHostKeyChecking=no ${addr} java -version 2>&1
        done
}

do_host_pass ${SERVER_LIST}
