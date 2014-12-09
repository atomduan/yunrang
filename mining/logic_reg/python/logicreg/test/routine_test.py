# -*- coding: utf-8 -*-
__author__ = 'juntaoduan'

import os
from routine.logicreg import *

data_dir = os.getcwd() + '/data_set'


#Vector is primarily represented with a column (in vertical direction)
def logicreg_import_training_data():
    file = None
    try:
        file = open(data_dir + '/spect/SPECT.train')
        #get Y X training data
        lines = map(lambda line: map(int, line.split(',')), file.readlines())
        #Y vector and X matrix
        Y = []
        X = []
        for l in lines:
            Y = Y + [l[0]]
            X = X + [l[1:]]
    finally:
        file.close()
        #change Y and w to vertical direction
    return Y, X


#Vector is primarily represented with a column (in vertical direction)
def logicreg_import_testing_data():
    file = None
    try:
        file = open(data_dir + '/spect/SPECT.test')
        #get Y X training data
        lines = map(lambda line: map(int, line.split(',')), file.readlines())
        #Y vector and X matrix
        Y = []
        X = []
        for l in lines:
            Y = Y + [l[0]]
            X = X + [l[1:]]
    finally:
        file.close()
    return Y, X


def logicreg_training_test():
    Y, X = logicreg_import_training_data()
    Y_test, X_test = logicreg_import_testing_data()
    w = logicreg_training_operation(Y, X, Y_test, X_test)
    print transpose(w)


######################## Run Test ##########################
logicreg_training_test()
