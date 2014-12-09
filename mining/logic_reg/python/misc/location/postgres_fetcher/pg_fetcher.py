# -*- coding: utf-8 -*
__author__ = 'juntaoduan'

import re as regex
import os

from psycopg2.extensions import ISOLATION_LEVEL_AUTOCOMMIT
from psycopg2.extras import DictCursor
from psycopg2 import connect

#ssh -qTfnN -L 5432:10.21.114.22:5432 sh-a0-sv0059.sh.idc.yunyun.com
def fetching_log_operation():
    meta_dir = ''
    #Configuration
    pgcatalog = 'szmobile'
    host = '127.0.0.1'
    port = '5432'
    user = 'postgres'
    pgschemas = ['adsactionlog', 'adsrequestlog', 'adsjumplog', 'statisticadvlog']
    connection = connect(database=pgcatalog, host=host, port=port, user=user)
    try:
        connection.set_isolation_level(ISOLATION_LEVEL_AUTOCOMMIT)
        cursor = connection.cursor()
        dict_cursor = connection.cursor(cursor_factory=DictCursor)
        try:
            def log_table_names(schema_name):
                log_table_name_pattern = regex.compile('([a-zA-Z]*_)+([0-9]+)')
                query = "select table_name from information_schema.tables where table_schema = '%s';" % schema_name
                result = []
                dict_cursor.execute(query)
                for r in dict_cursor.fetchall():
                    table_name = r['table_name']
                    if log_table_name_pattern.match(table_name):
                        result += [table_name]
                return result

            def log_columns_names(table_name):
                query = "select column_name from information_schema.columns where table_name='%s';" % table_name
                result = []
                long_str = ''
                dict_cursor.execute(query)
                for r in dict_cursor.fetchall():
                    column_name = r['column_name']
                    result += [column_name]
                    long_str += ',' + column_name
                return result, long_str

            def stepwise_fetching(schema, table_name, data_size, step_length=1000):
                relation = schema + '.' + table_name
                offset = 0
                while offset < data_size:
                    query = "select * from %s limit %d offset %d;" % (relation, step_length, offset)
                    cursor.execute(query)
                    for r in cursor.fetchall():
                        print r
                    offset += step_length
                return relation + ': %d' % data_size

            def data_size_statistic(schema, table_name, file):
                relation = schema + '.' + table_name
                query = "select COUNT(*) as data_size from %s;" % relation
                dict_cursor.execute(query)
                data_size = dict_cursor.fetchone()['data_size']
                if data_size > 0:
                    print "%S: %d" % (relation, data_size)
                    file.writelines("%s, %d\n" % (relation, data_size))

            def renew_open(path, file_name):
                for f in os.listdir(path):
                    if f == file_name: os.remove(path+'/'+file_name)
                return open(path+'/'+file_name, 'w+')

            def generate_adslog_table_file(ipnum):
                file = None
                try:
                    print meta_dir+'/'+"adslog_table_%d" % ipnum
                    file = renew_open(meta_dir, "adslog_table_%d" % ipnum)
                    for schema in pgschemas:
                        count = 0L
                        for table_name in log_table_names(schema):
                            count += 1
                            data_size_statistic(schema, table_name, file)
                            if count % 10 == 0: file.flush()
                finally:
                    file.close()
                #############################__MAIN__PROCESS__################################

            def fetching_operate(opt):
                a,b = log_columns_names('statistic_advlog_2013010321')
                print b
                pass
            fetching_operate(0)
        finally:
            dict_cursor.close()
            cursor.close()
    finally:
        connection.close()

