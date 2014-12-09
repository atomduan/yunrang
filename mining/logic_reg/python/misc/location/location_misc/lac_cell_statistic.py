# -*- coding: utf-8 -*
__author__ = 'juntaoduan'

from math import sqrt


def degree_to_arc(digree):
    return (digree / 360) * 3.1415926538979 * 2


def do_statistic():
    file = open('../data_set/2013_cell_id_juhe_langterm', 'r')
    count = 0
    half_count = 0
    half_total = 0.0
    one_kilo_count = 0
    one_kilo_total = 0.0
    big_count = 0
    big_total = 0.0
    exception_count = 0
    for line in file:
        try:
            p = line.strip().split(';')
            l = p[3].split(',')
            body = {'mnc': p[0],
                    'lac': p[1],
                    'cid': p[2],
                    'old_lng': float(l[0]),
                    'old_lat': float(l[1]),
                    'old_lng_o': float(l[2]),
                    'old_lat_o': float(l[3]),
                    'new_lat': float(l[4]),
                    'new_lng': float(l[5]),
                    'new_lat_o': float(l[6]),
                    'new_lng_o': float(l[7])}
            d_lat_dist = degree_to_arc(body['old_lat'] - body['new_lat']) * 6300
            d_lng_dist = degree_to_arc(body['old_lng'] - body['new_lng']) * 6300
            d_dist = sqrt(d_lat_dist * d_lat_dist + d_lng_dist * d_lng_dist)
            count += 1
            if d_dist < 0.5:
                half_count += 1
                half_total += d_dist
            if 0.5 <= d_dist < 1.0:
                one_kilo_count += 1
                one_kilo_total += d_dist
            if 1.0 <= d_dist < 30:
                big_count += 1
                big_total += d_dist
            if d_dist > 30:
                exception_count +=1
                print l, d_dist
        except ValueError:
            pass
    print 'total count=', count
    print 'little than half_count=', half_count, '; little than half mean=', (half_total/half_count), '; ratio=',((float(half_count)/count)*100),'%'
    print 'little than one_kilo_count=', one_kilo_count, '; little than one kilo mean=', (one_kilo_total/one_kilo_count),'; ratio=',((float(one_kilo_count)/count)*100),'%'
    print 'bigger than one kilo count=', big_count, '; bigger than one kilo mean=', (big_total/big_count),'; ratio=',((float(big_count)/count)*100),'%'
    print 'exception count=', exception_count, '; ratio=',((float(exception_count)/count)*100),'%'



do_statistic()