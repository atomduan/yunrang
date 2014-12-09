#! /bin/bash -
curr_path=$(cd `dirname $(which $0)`; pwd)
_SEP_="@_@"
CURR_TIME_STAMP=`date +%s`
##############################################################################


task_class="ShCdh4HbaseMergeResult"
test_base="/production/di/routine/recompute/recompute_test_1112"
ST_TRANS_BACK_OUTTER_RESULT=${test_base}/trans_back_outter_result
ST_RECOMPUTE_VIA_MPR=${test_base}/recompute_via_mpr
ST_HBASE_MERGE_RECOMPUTE_RESULT=/tmp/juntaoduan/hbase_merge_recompute_result
fan_in=${ST_TRANS_BACK_OUTTER_RESULT}
fan_in="${fan_in},${ST_RECOMPUTE_VIA_MPR}"
merge_qi_valid="11"
output_path=${ST_HBASE_MERGE_RECOMPUTE_RESULT}/merged_result
reducer_num=300
_SEP_="@_@"
hadoop_payload="${fan_in}$_SEP_${merge_qi_valid}$_SEP_${output_path}"
hadoop_opts="-submitByName ${task_class} -reducerNum ${reducer_num}"
hadoop_opts="${hadoop_opts} -payLoad ${hadoop_payload}"
##############################################################################


#task_name="ShCdh4HbaseExportTask"
#start_time="2014-11-01"
#end_time="2014-11-01"
#project_prefix="/tmp/juntaoduan/history_simple_task/${CURR_TIME_STAMP}"
#hadoop_payload="${start_time}$_SEP_${end_time}$_SEP_${project_prefix}"
##############################################################################


#task_name="ShCdh4HBaseSamplingTask"
#start_time="2014-09-10"
#end_time="2014-09-11"
#project_prefix="/tmp/juntaoduan/history_simple_task/${CURR_TIME_STAMP}"
#hadoop_payload="${start_time}$_SEP_${end_time}$_SEP_${project_prefix}"
##############################################################################


#task_name="ShCdh4HBaseServiceEmbedTask"
#start_time="2014-10-17"
#end_time="2014-10-24"
#project_prefix="/tmp/juntaoduan/history_simple_task/${CURR_TIME_STAMP}"
#resource_hdfs_path=/tmp/juntaoduan/hot_word_statistics.tar.gz
#cleanup_wait_second=20
#job_name=HOT_WORD_STATIC_QINGTAO
#hadoop_payload="${start_time}$_SEP_${end_time}$_SEP_${project_prefix}"
#hadoop_payload="${hadoop_payload}$_SEP_${resource_hdfs_path}$_SEP_${job_name}"
#hadoop_payload="${hadoop_payload}$_SEP_${cleanup_wait_second}"
##############################################################################


#task_name="ShCdh4RecordExistCheck"
##############################################################################


#task_name="ShCdh4MidDedupTask"
##############################################################################


#task_name="ShCdh4FileBaseTask"
##############################################################################


#task_name="ShCdh4WeiboDocCounter"
#indicator="selected_filter_0928/2014-08_2014-09"
#indicator=""
#input_path="/tmp/juntaoduan/history_selected_filter_dedup_1024"
#input_path="/tmp/juntaoduan/history_simple_task/mid_dedup/1414031434092"
#input_path="/tmp/juntaoduan/history_selected_filter_0928"
#input_path="/tmp/juntaoduan/history_simple_task/1413425687889"
#input_path="/production/di/routine/history_selected/incremental"
#input_path="/tmp/juntaoduan/history_selected_filter_1008_debug/2014-09_2014-10"
#input_path="/production/di/routine/history_selected/recent/2014-09-29_2014-10-06"
#input_path="/tmp/juntaoduan/history_simple_task/1413340115599"
#hadoop_payload="${indicator}$_SEP_${input_path}"
#hadoop_reduce_num=1
##############################################################################


#task_name="ShCdh4FileMappingFilterTask"
##############################################################################


cd ${curr_path}
mvn -Dmaven.test.skip=true clean package
#scp ${curr_path}/target/mapreduce-client.jar 10.21.114.2:/home/juntaoduan
#hadoop_opts="-submitByName ${task_name}"
#
#if [ -n "${hadoop_reduce_num:-}" ]; then
#	hadoop_opts="${hadoop_opts} -reducerNum ${hadoop_reduce_num}"
#fi
#
#if [ -n "${hadoop_payload:-}" ]; then
#	hadoop_opts="${hadoop_opts} -payLoad ${hadoop_payload}"
#fi
#
echo $hadoop_opts

#ssh -n -t 10.21.114.2 "/home/juntaoduan/history_simple_task/deploy.sh ${hadoop_opts}"
