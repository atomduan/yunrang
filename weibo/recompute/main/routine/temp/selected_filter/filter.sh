#!/bin/bash -
project_home=/data1/warehouse
source ${project_home}/main/cluster.ini ${project_home}
current_path=$(cd `dirname $(which $0)`; pwd)

src_path="${TARGET_HOME}/tmp/history_selected_filter_dedup_1024/1414141552733"
des_path="${TARGET_HOME}/tmp/filter/1414141552733"

if ! [ -d "${des_path}" ]; then
	mkdir -p ${des_path}
fi

cmd="find ${src_path} -type f | grep part-"
while read line
do
	src_file=${line}
	des_file=${src_file/$src_path/$des_path}
	cmd="${current_path}/filterdata.py ${src_file} | grep -vE '(^$|@id)' > ${des_file}"
	eval ${cmd}
done < <(eval ${cmd})
echo "FINISH"
