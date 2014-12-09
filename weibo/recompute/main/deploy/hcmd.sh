#! /bin/bash -
project_home=/data1/warehouse
source ${project_home}/main/cluster.ini ${project_home}
current_path=$(cd `dirname $(which $0)`; pwd)

#HDFS_BASE_CMD_CONFIG
hopt="$1"
shift
case "${hopt}" in
	-sh)
		HADOOP_BIN="${SH_CDH4}/bin-mapreduce1"
	;;
	-bj)
		HADOOP_BIN="${BJ_CDH3}/bin"
	;;
	-h|--help|-?|*)
		cat <<EOF
Usage:
	(-sh|-bj) hadoop <command>
EOF
		exit 1
	;;
esac

cmd="$1"
shift
opts="$@"
shift

cmd="${HADOOP_BIN}/${cmd} ${opts} 2>&1 | grep -v 'WARN util.NativeCodeLoader'"
eval ${cmd}
