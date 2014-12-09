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

time_tag="$1"
shift

BCMD_HADOOP="${SH_CDH4}/bin-mapreduce1/hadoop"

#Init stage target
cmd="${BCMD_HADOOP} fs -mkdir -p ${ST_HBASE_EXPORT_FIELDS_COMPARE_SNAPSHOT}"
cmd="sudo -u search ${cmd}"
r_log "${cmd}"
eval ${cmd}

function do_one_sampling_routine() {
	#Config mpr payload
	task_class="ShCdh4HbaseFieldsSnapshot"
	start_time="$1"
	shift
	end_time="$1"
	shift
	
	if [ -n "${time_tag}" ]; then
		check_line="virtual_path/${start_time}_${end_time}"
		if ! echo "${check_line}" | grep -E "${time_tag}" > /dev/null; then
			msg="[hbase_export_fields_compare_snapshot]: time_tag flter:"
			msg="${msg} check_line[${check_line}] filted by tag[${time_tag}]"
			r_log "${msg}"
			return 0
		fi
	fi

	shard_dir="${start_time}_${end_time}"
	output_path="${ST_HBASE_EXPORT_FIELDS_COMPARE_SNAPSHOT}/datas/${shard_dir}"
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
}


HISTORY_UPPER_LIMIT_MONTH=45
HISTORY_LOWER_LIMIT_MONTH=1


function get_month_list() {
        echo `date +%Y-%m-%d`
        echo `date +%Y-%m-01`
        curr_month=${HISTORY_LOWER_LIMIT_MONTH}
        while true
        do
                if [ ${curr_month} -lt ${HISTORY_UPPER_LIMIT_MONTH} ]; then
                        echo `date -d "${curr_month} month ago" +%Y-%m-01`
                else
                        break
                fi
                curr_month=$(( ${curr_month} + 1 ))
        done
}


month_list=`get_month_list`
start_month=''
end_month=''


for month in ${month_list}; do
        start_month="${month}"
        if [ -n "${start_month}" ] && [ -n "${end_month}" ]; then
                do_one_sampling_routine ${start_month} ${end_month}
        fi
        end_month="${start_month}"
done

#Mark mpr job complete
cmd="${BCMD_HADOOP} fs -touchz ${ST_HBASE_EXPORT_FIELDS_COMPARE_SNAPSHOT}/_COMPLETE"
cmd="sudo -u search ${cmd}"
r_log "${cmd}"
eval ${cmd}
