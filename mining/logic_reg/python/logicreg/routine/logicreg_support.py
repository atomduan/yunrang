# -*- coding: utf-8 -*-

__author__ = 'juntaoduan'

from numpy import transpose, append, dot, log, subtract, exp
from numpy.linalg import inv
from numpy.matrixlib import matrix


#W param shrinking functions
def logicreg_matrix_row_shrink(w, indexes):
    return transpose(logicreg_matrix_column_shrink(transpose(w), indexes))


def logicreg_matrix_row_shrink_recovery(w, indexes):
    return transpose(logicreg_Matrix_column_shrink_recovery(transpose(w), indexes))


#Sample matrix X shrinking functions
def logicreg_matrix_column_shrink(X, indexes):
    sorted_indexes = sorted(indexes, reverse=True)
    X_reduce = X
    for i in sorted_indexes:
        X_head = X_reduce[:, 0:i]
        X_tail = X_reduce[:, i + 1:]
        X_reduce = append(X_head, X_tail, 1)
    return X_reduce


def logicreg_Matrix_column_shrink_recovery(X, indexes):
    sorted_indexes = sorted(indexes)
    X_pad = X
    zero_pad = matrix(X[:, 0] * 0)
    for i in sorted_indexes:
        X_head = append(X_pad[:, 0:i], zero_pad, 1)
        X_tail = X_pad[:, i:]
        X_pad = append(X_head, X_tail, 1)
    return X_pad


#Tough work, need to be optimized through using L-BFGS-B
def logicreg_matrix_inverse(H):
    return inv(H)


#To compute Log likelihood function value base on P_base
def logicreg_log_likelihood_func(Y, P):
    return dot(transpose(Y), log(P)) + dot(transpose(subtract(1, Y)), log(subtract(1, P)))


#To compute the logistic statistic vector base on data sample matrix X and last approximated param w
def logicreg_func(X, w, cdp=600, eps=0.0000001):
    Z = dot(X, w)
    #Big-Z boundary control
    for i in range(Z.shape[0]):
        if abs(Z[i, 0]) > cdp:
            Z[i, 0] = (abs(Z[i, 0]) / Z[i, 0]) * cdp
    P = 1.0 / (1.0 + exp(-1.0 * Z))
    #One-Zero boundary control
    for i in range(P.shape[0]):
        P[i, 0] = min(max(P[i, 0], eps), 1 - eps)
    return P


#To evaluate the accuracy for the given param w base on sample Y, X,
def logicreg_accuracy(Y, X, w):
    right_test_count = 0.
    wrong_test_count = 0.
    for i in range(Y.shape[0]):
        Pi = logicreg_func(X[i], w)
        if abs(Y[i, 0] - Pi) > 0.5:  #check whether two factors are equal,
            wrong_test_count += 1   #not equal
        else:
            right_test_count += 1   #equal
    accuracy = right_test_count / (right_test_count + wrong_test_count)
    return accuracy