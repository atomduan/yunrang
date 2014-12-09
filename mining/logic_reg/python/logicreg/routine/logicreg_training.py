# -*- coding: utf-8 -*-

__author__ = 'juntaoduan'

from numpy import zeros, size, diagflat, array, transpose
from numpy.linalg import norm
from numpy.matrixlib import matrix
from numpy.ma import multiply

from logicreg_support import *


#Main Logistic Regression Training Routine
def logicreg_mle_training(Y, X, Y_test, X_test):
    init_dim = X.shape[1]
    #Get training data set
    w_opt = transpose(matrix(zeros(X.shape[1])))
    accuracy_control = 0.75
    ill_history = []
    old_accuracy = 0.0
    training_round = 0
    while True:
        training_round += 1
        #Param training -- core process
        w_opt = logicreg_mle_iteration(Y, X, w_opt)
        #Quality control: Param accuracy computing.
        accuracy = logicreg_accuracy(Y_test, X_test, w_opt)
        print 'Accuracy is:', accuracy, ' Training round:', training_round
        #Quality control: Param validate and filter, X and w_opt will be shrunk
        Y, X, w_opt, ill_arr = logicreg_likelihood_ratio_filter(Y, X, w_opt)
        #Terminating policy
        if size(ill_arr) > 0 or accuracy < accuracy_control:
            if training_round < 20 or old_accuracy != accuracy:
                #Initialize the w_opt to new dim size with zero value
                w_opt = transpose(matrix(zeros(X.shape[1])))
                X_test = logicreg_matrix_column_shrink(X_test, ill_arr)
                ill_history += [ill_arr]
                old_accuracy = accuracy
                continue
            else:
                print 'Terminated, can not met accuracy requiredment, current Accuracy is:', accuracy
                break
        else:
            print 'Finished, current Accuracy is:', accuracy
            break
    if size(w_opt) > 0:
        #Recover the shrunk w to initial size (fill blanks with '0')
        return logicreg_w_dim_recovery(w_opt, ill_history)
    else:
        print 'There is no param left for this logic model, maybe you should divide the data set first...'
        return transpose(matrix(zeros(init_dim)))


#Recover the shrunk w to initial size (fill blanks with '0')
def logicreg_w_dim_recovery(w_reduced, history):
    w_opt = w_reduced
    while size(history) > 0:
        #Notice: sort in ascent order~! As it was shrunk in descent order previously.
        #see function logicreg_matrix_H_shrink
        w_opt = logicreg_matrix_row_shrink_recovery(w_opt, history.pop())
    w_recovered = w_opt
    return w_recovered


#MLE Routine
def logicreg_mle_iteration(Y, X, w):
    w_result = None
    #Terminating control params
    w_ctrl = 0.0001
    G_ctrl = 0.00001
    L_ctrl = 0.000001
    #Initialize looping params
    w_base = w
    P_base = logicreg_func(X, w_base)
    #Initialize monitoring params
    iteration_round = 1
    while True:
        #Log-likelihood gradients
        G = dot(transpose(X), subtract(Y, P_base))
        #Terminating control 1: G ~ 0
        if norm(G) < G_ctrl:
            print 'G condition met, iteration_round:', iteration_round
            w_result = w_base
            break
        else:
            #Sample_diag for Hessian matrix computation
            D = diagflat(multiply(P_base, subtract(1, P_base)))
            #Hessian matrix H
            H = multiply(-1, dot(dot(transpose(X), D), X))
            #Each new param W is approximated base on the latest w and sample data set Y, X
            w_new = subtract(w_base, dot(logicreg_matrix_inverse(H), G))
            #Terminating control 2: w_new ~ w_base
            if norm(subtract(w_new, w_base)) < w_ctrl:
                print 'w condition met, iteration_round:', iteration_round
                w_result = w_new
                break
            else:
                P_new = logicreg_func(X, w_new)
                #Terminating control 3: L_new ~ L_base
                if abs(logicreg_log_likelihood_func(Y, P_new) - logicreg_log_likelihood_func(Y, P_base)) < L_ctrl:
                    print 'L condition met, iteration_round:', iteration_round
                    w_result = w_new
                    break
                else:
                    #Prepare params for next loop
                    P_base = P_new
                    w_base = w_new
                    iteration_round += 1
                    continue
    return w_result


#Likelihood Ratio Validation Routine
#Confidance:    3.84 -> 5%
#               2.71 -> 10%
def logicreg_likelihood_ratio_filter(Y, X, w_opt, threshold=3.84):
    dim_size = w_opt.shape[0]
    r = logicreg_likelihood_ratio_chi_square_norm(Y, X, w_opt)
    #The abnormal parameter indexes, which will be removed from w_opt before next training iteration
    ill_arr = []
    for i in range(dim_size):
        if r[i] < threshold:
            ill_arr += [i]
    X_reduce = logicreg_matrix_column_shrink(X, ill_arr)
    w_reduce = logicreg_matrix_row_shrink(w_opt, ill_arr)
    return Y, X_reduce, w_reduce, ill_arr


#L-R's Chi-square(dim=1) Normalization Routine
def logicreg_likelihood_ratio_chi_square_norm(Y, X, w):
    dim_size = w.shape[0]
    ratio_arr = array(zeros(dim_size))
    #Raw score computation
    P = logicreg_func(X, w)
    L_base = logicreg_log_likelihood_func(Y, P)
    for i in range(dim_size):
        X_reduced, w_reduced = logicreg_mle_iteration_reduce_single_dim(Y, X, i)
        P_reduced = logicreg_func(X_reduced, w_reduced)
        L_reduced = logicreg_log_likelihood_func(Y, P_reduced)
        ratio = -2 * (L_reduced - L_base)
        ratio_arr[i] = ratio
    return transpose(matrix(ratio_arr))


#Functional method for likelihood ratio chi square test
def logicreg_mle_iteration_reduce_single_dim(Y, X, i):
    ill_arr = [i]
    #Only one index
    X_reduce = logicreg_matrix_column_shrink(X, ill_arr)
    w_init = transpose(matrix(zeros(X_reduce.shape[1])))
    w_reduced = logicreg_mle_iteration(Y, X_reduce, w_init)
    return X_reduce, w_reduced