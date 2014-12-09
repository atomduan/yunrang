# -*- coding: utf-8 -*
__author__ = 'juntaoduan'

import re as regex
import os

import urllib
import urllib2
from PIL import Image, ImageFile

from psycopg2.extensions import ISOLATION_LEVEL_AUTOCOMMIT
from psycopg2.extras import DictCursor
from psycopg2 import connect
from psycopg2 import Binary
from cStringIO import StringIO


#ssh -qTfnN -L 5432:10.21.114.22:5432 sh-a0-sv0059.sh.idc.yunyun.com
def fetching_log_operation():
    #Configuration
    host = '10.180.8.13'
    username = 'postgres'
    password = 'postgres'
    database = 'szmall'
    port = '5433'
    connection = connect(database=database, host=host, port=port, user=username, password=password)
    try:
        connection.set_isolation_level(ISOLATION_LEVEL_AUTOCOMMIT)
        cursor = connection.cursor(cursor_factory=DictCursor)
        try:
            def get_prod_table_count():
                query = 'select count(*) as cnt from prod;'
                cursor.execute(query)
                r = cursor.fetchone()
                return r['cnt']

            def get_columns_names(table_name):
                query = "select column_name from information_schema.columns where table_name='%s';" % table_name
                long_str = ''
                cursor.execute(query)
                for r in cursor.fetchall():
                    column_name = r['column_name']
                    long_str = long_str + '\n,' + column_name
                return long_str

            def insert_img():
                response = urllib2.urlopen(
                    'http://img02.taobaocdn.com/bao/uploaded/i2/T1_x9iXmXoXXX_RD2a_122449.jpg')
                img_bytes = response.read()
                cursor.execute("UPDATE prod_img SET img=%s WHERE prod_id='wbARuHdHxD'", (Binary(img_bytes),))

            insert_img()
        finally:
            cursor.close()
    finally:
        connection.close()


fetching_log_operation()

