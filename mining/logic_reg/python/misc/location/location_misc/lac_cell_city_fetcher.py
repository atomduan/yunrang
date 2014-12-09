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


def do_fetch():
    rand = random.Random()
    file = open('../data_set/city_list', 'r')
    rstl = open('../data_set/city_list_geo', 'w+')
    socket.setdefaulttimeout(30)
    headers = {'Host': 'www.baidu.com',
               'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64; rv:2.0.1) Gecko/20100101 Firefox/4.0.1',
               'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
               'Accept-Language': 'zh-cn,en-us;q=0.7,en;q=0.3', 'Accept-Charset': 'ISO-8859-1,utf-8;q=0.7,*;q=0.7',
               'Referer': 'http://www.baidu.com/'}
    for line in file:
        code = line.strip().split(" ")
        print code[1]
        addr = urllib.quote(code[1])
        fetcher = UrlFetcher()
        resp = fetcher.fetch(url=('http://api.map.baidu.com/geocoder?output=json&address=' + addr), headers=headers)
        try:
            loc = json.loads(resp.body)['result']['location']
            #example:{u'lat': 39.929986, u'lng': 116.395645}
            rstl.writelines('%s;%s;%s;%s\n' % (code[0], code[1], loc['lat'], loc['lng']))
        except TypeError:
            #means the 'location' index is not exist.
            rstl.writelines('%s;%s;%s;%s\n' % (code[0], code[1], '', ''))
        rstl.flush()
        time.sleep(rand.randint(1, 3))

#test....
do_fetch()