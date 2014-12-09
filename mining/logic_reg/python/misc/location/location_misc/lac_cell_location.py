#!/usr/bin/python2.6
# -*- coding: utf-8 -*-
#
# Copyright 2011 Yunrang Inc. All Rights Reserved.
#
# Modified from __author__ = 'quj@yunrang.com (Jing Qu)'
#

__author__ = 'juntaoduan'

import urllib2
import urllib
import httplib
import time
import socket
import random
import json


class HTTPResponse(object):
    headers = None
    status = None
    body = None
    final_url = None

    def __init__(self, final_url=None, status=None, headers=None, body=None):
        self.final_url = final_url
        self.status = status
        self.headers = headers
        self.body = body

    def __repr__(self):
        return ("[HTTP Status Code: %r --- Request URL: %s --- Response: %s" %
                (self.status, self.final_url, self.body))


class UrlFetcher(object):
    def fetch(self, url, body=None, headers=None):
        if headers is None:
            headers = {}
        req = urllib2.Request(url, data=body, headers=headers)
        resp = HTTPResponse()
        try:
            f = urllib2.urlopen(req)
            try:
                resp = self._makeResponse(f, resp)
            finally:
                f.close()
        except (urllib2.HTTPError, httplib.BadStatusLine):
            resp.status = 404
        return resp

    def _makeResponse(self, urllib2_response, resp):
        try:
            resp.body = urllib2_response.read()
        except socket.timeout:
            resp.status = 404
            return resp
        resp.final_url = urllib2_response.geturl()
        resp.headers = dict(urllib2_response.info().items())
        if hasattr(urllib2_response, 'code'):
            resp.status = urllib2_response.code
        else:
            resp.status = 200
        return resp


def do_fetch(current_point=0):
    rand = random.Random()
    file = open('../data_set/2013_cell_id', 'r')
    rstl = open('../data_set/2013_cell_id_juhe_langterm', 'w+')
    socket.setdefaulttimeout(30)
    headers = {'Accept': '*/*',
               'Accept-Charset': 'UTF-8,*;q=0.5',
               'Accept-Encoding': 'gzip,deflate,sdch',
               'Accept-Language': 'en-US,en;q=0.8',
               'Connection': 'keep-alive',
               'Content-Type': 'application/x-www-form-urlencoded',
               'Cookie': 'Hm_lvt_77ed49d6fe336c98c503925ed80126f2=1369896899; Hm_lpvt_77ed49d6fe336c98c503925ed80126f2=1369896899; Hm_lvt_5d12e2b4eed3b554ae941c0ac43c330a=1369897123; Hm_lpvt_5d12e2b4eed3b554ae941c0ac43c330a=1369897898',
               'Host': 'lbs.juhe.cn',
               'Origin': 'http://lbs.juhe.cn',
               'Referer': 'http://lbs.juhe.cn/cellmap/',
               'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.43 Safari/537.31',
               'X-Requested-With': 'XMLHttpRequest'}
    count = 0
    for line in file:
        count += 1
        if count > current_point:
            p = line.strip().replace('"', '').split(',')
            body = {'mnc': p[0],
                    'lac': p[1],
                    'cid': p[2],
                    'hex': 10}
            print body
            fetcher = UrlFetcher()
            resp = fetcher.fetch(url='http://lbs.juhe.cn/cellmap/api.php', body=urllib.urlencode(body), headers=headers)
            try:
                r = json.loads(resp.body)['data']
                rstl.writelines('%s;%s;%s;%s,%s,%s,%s,%s,%s,%s,%s\n' % (p[0], p[1], p[2], p[6], p[7], p[8], p[9], r['lat'], r['lon'], r['o_lat'], r['o_lon']))
            except TypeError:
                wait_one_hour()
            rstl.flush()
            time.sleep(rand.randint(1, 4))


def wait_one_hour():
    deci_seconds = 0
    while True:
        print 'a hour waiting, we have wait seconds :', (deci_seconds*300)
        deci_seconds += 1
        time.sleep(300)
        if deci_seconds*300 > 3600:
            break

#test....
do_fetch(current_point = 0)