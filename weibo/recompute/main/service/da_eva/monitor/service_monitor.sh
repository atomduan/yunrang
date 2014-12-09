#!/bin/bash -
DA_EVA_HOME=/data1/minisearch/da_eva

while :
do
        FLAG=$(ps -ef | grep 'da_eva '|grep -v user|grep -v mgeo|grep -v loop|grep -v grep)
        if [ "$FLAG" != "" ];then
                sleep 15
                continue
        else
		ulimit -c unlimited
		cd ${DA_EVA_HOME}/bin
        	nohup ./da_eva -d ../conf/ -f da.conf -l ../log/ > /dev/null &
    	fi
done

