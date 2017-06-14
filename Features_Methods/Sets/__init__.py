import operator as op
from functools import reduce
import Bank_Methods as methods
import random

def ncr(n, r):
    r = min(r, n-r)
    if r == 0: return 1
    numer = reduce(op.mul, range(n, n-r, -1))
    denom = reduce(op.mul, range(1, r+1))
    return numer//denom

def create_set(number, list):
    n = len(list)
    all_combinations = ncr(n, 5)
    if(all_combinations >= number):
        num_representacoes = number
    else:
        num_representacoes = all_combinations
    
    res = []
    while len(res) < num_representacoes:
        samp = random.sample(list, 5)
        if(samp not in res):
            res.append(samp)
    
    return res

                
            
def create_sets_test(number, list_teste, num_linkset, num_class, num_proprety, num_types, db, vector_features):
   
    list_return = []
    
    
    for i in list_teste:
        list_ls = []
        lista_sets_ls = []
        lista_sets_class = []
        lista_sets_proprety = []
        lista_sets_types = []
        list_ = []
        dict_ = {}
        if(num_linkset > 0):
            list_ls = methods.GetLinkSet(i, db)
            for j in list_ls:
                bool_ls = methods.LSTest(j, list_teste, db)
                if(bool_ls == 1):
                    list_ls.remove(j)
                
            
            for k in list_ls:
                if(k in vector_features):
                    list_.append(k)
           
            if(len(list_) >= 5):    
                lista_sets_ls = create_set(number, list_)
            
        
        #if(num_class > 0):
        #    list_class = methods.GetClass(i, db)
        #    lista_sets_class = create_set(number, list_class)
        
       #if(num_proprety > 0):
       #     list_proprety = methods.GetProprety(i, db)
       #     lista_sets_proprety = create_set(number, list_proprety)
        
        if(num_types > 0):
            list_types = methods.GetTypes(i, db)
            lista_sets_types = create_set(number, list_types)
            
        list_sets = lista_sets_ls + lista_sets_class + lista_sets_proprety + lista_sets_types
            
        
        
        dict_[i] = list_sets
        list_return.append(dict_)
        
        
    
        
    return list_return
        
def create_sets_testTI(number, list_teste, num_linkset, num_class, num_proprety, num_types, db):
    list_return = []
    
    
    for i in list_teste:
        list_ls = []
        lista_sets_ls = []
        lista_sets_class = []
        lista_sets_proprety = []
        lista_sets_types = []
        dict_ = {}
        if(num_linkset > 0):
            list_ls = methods.GetLinkSet(i, db)
            for j in list_ls:
                bool_ls = methods.LSTest(j, list_teste, db)
                if(bool_ls == 1):
                    list_ls.remove(j)
                
        
            if(len(list_ls) >= 5):    
                lista_sets_ls = create_set(number, list_ls)
            
        
        #if(num_class > 0):
        #    list_class = methods.GetClass(i, db)
        #    lista_sets_class = create_set(number, list_class)
        
       #if(num_proprety > 0):
       #     list_proprety = methods.GetProprety(i, db)
       #     lista_sets_proprety = create_set(number, list_proprety)
        
        if(num_types > 0):
            list_types = methods.GetTypes(i, db)
            lista_sets_types = create_set(number, list_types)
            
        list_sets = lista_sets_ls + lista_sets_class + lista_sets_proprety + lista_sets_types
            
        
        
        dict_[i] = list_sets
        list_return.append(dict_)
        
        
    
        
    return list_return
        
            
        
        
    
    