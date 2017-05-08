#coding: utf-8

'''
Created on 25 de out de 2016

@author: angelo
'''
import ConnectionMysql as connect
import Metodos
import math
import Features


def RepresentacaoClasse(vetor_aux, vetores_treinamento):
    print vetor_aux
    dict_datasets_features = {}
    for i in vetores_treinamento:
        for nome_dataset, vetor_features in i.iteritems():
            print "Para Dataset: "+str(nome_dataset)
            print "Vetor Original: "+str(vetor_features)
            lista_classes = []
            total_ls = Features.GetLinkSet(nome_dataset)
            for k in total_ls:
                if(k in vetor_aux):
                    if(nome_dataset == "http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/rest/dataset/rkb-explorer-eprints"):
                        aux = vetor_features
                        posicao = vetor_aux.index(k)
                        #aux[posicao] = 0
                        del aux[posicao]
                        aux.insert(posicao, 0)
                        #print posicao
                        print aux
                        aux = []

def calucloidf_teste(feature, total):
    db = connect.conexaoMysql()
    cursor_idf = db.cursor()
    sql_idf = "SELECT count(nome_dataset) as numero FROM `Features` WHERE  features = '"+str(feature)+"' "
    try:
        cursor_idf.execute(sql_idf)
        results_idf = cursor_idf.fetchall()
        for row_idf in results_idf:
            quantidade_dataset = row_idf[0]
            idf = math.log(total/float(quantidade_dataset))
            
    except:
        print "Erro ao calcular idf do teste"
    
    db.close()
    return idf


def calculotf(nome_dataset, feature):
    tf = 0
    db = connect.conexaoMysql()
    cursor_getfeatures = db.cursor()
    sql_getfeatures = "SELECT `frequencia`, `dataset_size`, `features` FROM `Features'` WHERE nome_dataset = '"+str(nome_dataset)+"' AND features = '"+str(feature)+"'"
    try:
        cursor_getfeatures.execute(sql_getfeatures)
        results_get = cursor_getfeatures.fetchall()
        linhas = cursor_getfeatures.rowcount
        if(linhas == 1):
            for row_get in results_get:
                frequencia = float(row_get[0])
                dataset_size = float(row_get[1])
                tf = frequencia / dataset_size
        else:
            tf = 0
                   
    except:
        print "Erro ao pegar o tf do dataset " + str(nome_dataset)    
    
    db.close()
    return tf

def calucloidf_treinamento(feature, total, lista_teste): 
    lista_nome = []
    for i in lista_teste:
        nome_dataset = Metodos.GetNomeDataset(i)
        lista_nome.append(nome_dataset)
    
    db = connect.conexaoMysql()
    cursor_idf = db.cursor()
    cont = 0
    sql_idf = "SELECT nome_dataset FROM `Features` WHERE features = '"+str(feature)+"' "
    try: 
        cursor_idf.execute(sql_idf)
        results_idf = cursor_idf.fetchall()
        for row_idf in results_idf:
            if(row_idf[0] not in lista_nome):
                cont = cont + 1
            
        idf = math.log(total/float(cont))
    except:
        print "Erro ao calcular idf do treinamento"
        
    
    db.close()
    return idf



def vetores_teste(nome_dataset, lista_conjunto, tamanho_vetor, vetor_aux, total):
    dict_retorno = {}
    vetor_dataset = []
    for j in range(tamanho_vetor):
        vetor_dataset.append(0)
             
    #ls = Features.GetLinkSet(nome_dataset)
    #proprety = Features.Getproprety(nome_dataset)
    
    for i in lista_conjunto:
        if(i in vetor_aux):
            posicao = vetor_aux.index(i)
            del vetor_dataset[posicao]
            valor_tf = calculotf(nome_dataset, i)
            valor_idf = calucloidf_teste(i, total)
            if(valor_tf == 0):
                tf_idf = 0
                vetor_dataset.insert(posicao, tf_idf)
            else:
                tf_idf = abs(valor_tf * valor_idf)
                vetor_dataset.insert(posicao, tf_idf)
          
    return vetor_dataset     
        
                    
                    
def vetores_treinamento(lista_treinamento, tamanho_vetor, vetor_aux, lista_teste):
        tamanho_treinamento = len(lista_treinamento)
        lista_retorno = []
        for i in lista_treinamento:
            dict_retorno = {}
            nome_dataset = Metodos.GetNomeDataset(i)
            ls = Features.GetLinkSet(nome_dataset)
            
            vetor_dataset = []
            for j in range(tamanho_vetor): 
                vetor_dataset.append(0)
            
            if(len(ls) != 0):   
                for k in ls:
                    if(k in vetor_aux):
                        posicao = vetor_aux.index(k)
                        del vetor_dataset[posicao]
                        valor_tf = calculotf(nome_dataset, k)
                        valor_idf = calucloidf_treinamento(k, tamanho_treinamento, lista_teste)
                        tf_idf = abs(valor_tf * valor_idf)
                        vetor_dataset.insert(posicao, tf_idf)
                
                                
            dict_retorno[nome_dataset] = vetor_dataset
            lista_retorno.append(dict_retorno)
    
        return lista_retorno
    

def vectorRepresentationDeep(nome_dataset, lista_conjunto, tamanho_vetor, vetor_aux, lista_treinamento, lista_teste):
    tamanho_treinamento = len(lista_treinamento)
    
    vetor_dataset = []
    for j in range(tamanho_vetor):
        vetor_dataset.append(0)
        
    for i in lista_conjunto:
        if(i in vetor_aux):
            posicao = vetor_aux.index(i)
            del vetor_dataset[posicao]
            valor_tf = calculotf(nome_dataset, i)
            valor_idf = calucloidf_treinamento(i,tamanho_treinamento,lista_teste)
            if(valor_tf == 0):
                tf_idf = 0
                vetor_dataset.insert(posicao, tf_idf)
            else:
                tf_idf = abs(valor_tf * valor_idf)
                vetor_dataset.insert(posicao, tf_idf)
          
    return vetor_dataset     
            
    
    