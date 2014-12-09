# -*- coding: utf-8 -*
__author__ = 'juntaoduan'
#!/usr/bin/python2.6
# -*- coding: utf-8 -*-
#
# Copyright 2011 Yunrang Inc. All Rights Reserved.
#
# Modified from __author__ = 'quj@yunrang.com (Jing Qu)'
#

import urllib2
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


def do_fetch():
    rand = random.Random()
    file = open('../data_set/uniq_total_unsupported_cn', 'r')
    rstl = open('../data_set/2013_cell_id_weibo', 'w+')
    utus = open('../data_set/uniq_total_unsupported_sina_cn', 'w+')
    socket.setdefaulttimeout(30)
    headers = {'Content-Type': 'application/json'}
    success_count = 0
    total_count = 0
    fail_count = 0
    for line in file:
        total_count += 1
        p = line.strip().replace('"', '').split(',')
        body = {"cell_towers": [{"cell_id": p[2],
                                 "location_area_code": p[1],
                                 "mobile_country_code": 460,
                                 "mobile_network_code": p[0].replace('460', '')}]
        }
        body = json.dumps(body)
        fetcher = UrlFetcher()
        resp = fetcher.fetch(url='https://api.weibo.com/2/location/mobile/get_location.json?source=2667205658', headers=headers, body=body)
        try:
            r = json.loads(resp.body)['location']
            rstl.writelines(p[0]+","+p[1]+","+p[2]+","+r['longitude']+","+r['latitude']+"\r\n")
            success_count += 1
        except Exception:
            utus.writelines(line)
            fail_count += 1
            pass
        rstl.flush()
        utus.flush()
        if total_count % 100 == 0:
            print "total: %d  success: %d  fail: %d" % (total_count, success_count, fail_count)
        time.sleep(rand.randint(1, 7)/10)


#test....
do_fetch()