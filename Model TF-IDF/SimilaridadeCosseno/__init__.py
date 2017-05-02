#coding: utf-8

'''
Created on 25 de out de 2016

@author: angelo
'''
import math


def calculocosseno(a, b):
    verifica_a = all(v == 0 for v in a)
    verifica_b = all(v == 0 for v in b)
    if(verifica_a or verifica_b):
        return 0
    else:
        return sum([i*j for i,j in zip(a, b)])/(math.sqrt(sum([i*i for i in a]))* math.sqrt(sum([i*i for i in b])))


    
                
    