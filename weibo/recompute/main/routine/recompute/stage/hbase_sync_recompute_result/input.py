#!/usr/bin/env python

#

# opyright 2012 Yunrang Inc. All Rights Reserved.

__author__ = 'jianyesun@yunrang.com (Jianye Sun)'

# run the mapreduce task for statistics

import os
import sys
from readlist import ReadList
from arglist import ArgList

# link
#hadoop = '/opt/hadoop/program/bin-mapreduce1/hadoop'
hadoop = '/data1/warehouse/main/service/hadoop/client/sh_cdh4/program/bin-mapreduce1/hadoop'

jarpath = 'jar/hadooptool-hbase.jar'

# input args
conf = 'mr.conf'
argv = sys.argv
if (len(argv) == 3):
   if (argv[1] == '-conf'):
      conf = argv[2]
      print conf

readlist = ReadList()

# read mr-conf
argconf = readlist.load_inputpath(conf)

# parse conf
arglist = ArgList()
args = arglist.parseArgs(argconf)

print '<------- input args ------->'
print args

if (arglist.is_input_path):
   inputpath = args[arglist.input_path]
if (arglist.is_num_map):
   num_map = args[arglist.num_map]
if (arglist.is_num_reduce):
   num_reduce = args[arglist.num_reduce]
if (arglist.is_output_path):
   outputpath = args[arglist.output_path]
if arglist.is_input_list:
   input_list = args[arglist.input_list]
   inputpath = readlist.load_inputpathString(input_list)

if arglist.is_jar_file:
   jarpath = args[arglist.jar_file]

if arglist.is_aid:
   aid = args[arglist.aid]

props = ''
if arglist.is_props:
   props = args[arglist.props]

command = hadoop + ' jar ' + jarpath + ' -i '+ inputpath + ' -m ' + num_map + ' -r ' + num_reduce + ' -o ' + outputpath
command = command + ' -aid ' + aid
if props != '':
   command = command + ' -props ' + props
print '<------- command ------->'
print '' + command  

os.system(command)

