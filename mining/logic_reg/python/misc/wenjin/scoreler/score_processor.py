# -*- coding: utf-8 -*
__author__ = 'juntaoduan'

from numpy import exp

#ALL WJ ActionType Statistical information
wj_all_ip_count = 182474
wj_all_actions = 4649747

wj_all_detail = 3811782
wj_all_library = 140189
wj_all_recommend = 6174
wj_all_null = 55

wj_all_docget = 661652
wj_all_docget_read_online = 511038
wj_all_docget_download_file = 150614

wj_all_share = 2148
wj_all_share_to_sina = 1292
wj_all_share_to_tencent = 856

wj_all_extend = 27747
wj_all_extend_baidu = 18247
wj_all_extend_google = 9441
wj_all_extend_douban = 37
wj_all_extend_world_cat = 7


def get_init_actstmap():
    return {'library': [], 'detail': [], 'author': [], 'comment': [], 'leavecomment': [], 'docget': [], 'share': [],
            'recommend': [], 'extend': [], 'null': []}


def do_compute_normalize_process():
    file = open('../data-set/order-ipres-actscore.txt', 'r')
    target = open('../data-set/all-result-marginal.txt', 'w+')
    last_ipres = ''
    last_ipres_package = []
    for line in file:
        code = line.strip().split('|')
        if code[0] != last_ipres:
            process_ipres_package(last_ipres, last_ipres_package, target)
            last_ipres_package = []
        last_ipres_package += [code[1]]
        last_ipres = code[0]


def process_ipres_package(iprestr, package, target):
    if '' != iprestr and [] != package:
        ipres = iprestr.strip().split(';')
        ipstr, resid = ipres[0], ipres[1]
        #filte bad resid items
        if resid != 'null':
            actstmap = get_init_actstmap()
            for actstr in package:
                acts = actstr.strip().split(';')
                act_type, act_opt, score = acts[0], acts[1], acts[2]
                actstmap[act_type].append([act_opt, int(score)])
                pass
            marginal_score = compute_marginal_score(ipstr, resid, actstmap)
            formal_ipstr = convert_ip(ipstr)
            target.writelines('%s,%s,%s\n' % (formal_ipstr, resid, marginal_score))
            target.flush()


def convert_ip(ipstr):
    ips = ipstr.split('.')
    formalstr = 16777216L * long(ips[0]) + 65536L * long(ips[1]) + 256 * long(ips[2]) + long(ips[3])
    return formalstr


# Obey to The law of diminishing marginal utility
def compute_marginal_score(ip, resid, actstmap):
    library = actstmap['library']
    detail = actstmap['detail']
    docget = actstmap['docget']
    share = actstmap['share']
    recommend = actstmap['recommend']
    extend = actstmap['extend']
    ##################### Marginal Settings ##############
    level_1_raw, amp_1 = 0, 40
    level_2_raw, amp_2 = 0, 80
    level_3_raw, amp_3 = 0, 160
    level_4_raw, amp_4 = 0, 320
    ##################### LEVEL ONE ##############
    #Search and see this books detail
    if None != detail:
        for act in detail:
            level_1_raw += act[1]
    # Extend search this book on website
    if None != extend:
        for act in extend:
            level_1_raw += act[1]
    # recommend this book
    if None != recommend:
        for act in recommend:
            level_1_raw += act[1]
    ##################### LEVEL TWO ##############
    # Read or Download This book
    if None != docget:
        for act in docget:
            # Read this book online
            if 1 == act[0]:
                level_2_raw += act[1]
            # Download this book
            if 2 == act[0]:
                level_2_raw += act[1]
    ##################### LEVEL THREE ##############
    # Share this book to other site
    if None != share:
        for act in share:
            level_3_raw += act[1]
    ##################### LEVEL FOUR ##############
    # Check out this real book from library
    if None != library:
        for act in library:
            level_4_raw += act[1]
    #################### COMPUTE LEVEL MARGINAL SCORE #################
    level_1_marginal = marginalFunc(amp_1, level_1_raw)
    level_2_marginal = marginalFunc(amp_2, level_2_raw)
    level_3_marginal = marginalFunc(amp_3, level_3_raw)
    level_4_marginal = marginalFunc(amp_4, level_4_raw)
    ###############################################################
    marginal_score = level_1_marginal+level_2_marginal+level_3_marginal+level_4_marginal
    return int(marginal_score) + 1


def marginalFunc(amplitude, raw_score):
    logistic = 1.0 / (1.0 + exp(-1.0 * raw_score))
    marginalScore = amplitude * (logistic - 0.5)
    return marginalScore

do_compute_normalize_process()