# -*- coding: utf-8 -*-

__author__ = 'juntaoduan'

from logicreg_training import *


#Formal interface exposed to outside modules for triggering the logistic training operations.
#
#The format of Y and Y_test is (n) array like:
#Exp:
#[[1,1,0.....1]
#the 'n' size of Y and Y_test can be different, but the 'n' size between X,X_test OR Y,Y_test must be equal
#
#The format of X and X_test is (nxp) matrix like.
#Exp:
#[[12, 4, 6, ... 0, 8],
# [ 0, 5, 8, ... 2, 7],
# [-1, 3, 7, ... 6, 9],
# ......
# [ 3....,9, ... 9,10]]
#the 'n' size of X and and X_test can be different, but the 'p' size between X and X_test must be equal
def logicreg_training_operation(Y, X, Y_test, X_test):
    Y = transpose(matrix(Y))
    Y_test = transpose(matrix(Y_test))
    X = matrix([[1] + l[0:] for l in X])#every line of X
    X_test = matrix([[1] + l[0:] for l in X_test])#every line of X_test
    w_result = logicreg_mle_training(Y, X, Y_test, X_test)
    return w_result


#Formal interface exposed to outside modules for querying predict
#X format is (p) array like. Exp: [12, 4, 6, ... 0, 8] (has p elements)
#W format is (p+1) array like.Exp: [1.2, ..... ,.....,4] (has p+1 elements)
def logicreg_logistic_func(X, w):
    X = matrix([1] + X)
    w = transpose(matrix(w))
    result = logicreg_func(X, w)# 1x1 size
    return result[0,0]