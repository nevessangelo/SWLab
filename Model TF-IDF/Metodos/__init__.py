#coding: utf-8

import ConnectionMysql as connect
import SimilaridadeCosseno as similaridade
import numpy as np
import csv
import Features
import SimilaridadeTI
from decimal import Decimal
import math


def GetGroup(nome_dataset):
    db = connect.conexaoMysql()
    cursor = db.cursor()
    sql = "SELECT group_dataset FROM Groups WHERE nome_dataset = '"+str(nome_dataset)+"'"
    try:
        cursor.execute(sql)
        result  = cursor.fetchall()
        for row in result:
            group = row[0]
    except:
        "Erro ao pegar grupo"
    
    db.close()
    return group

def GetQuantidadeLS(nome_dataset):
    db = connect.conexaoMysql()
    cursor = db.cursor()
    sql = "SELECT qLS FROM Dataset WHERE nome_dataset = '"+str(nome_dataset)+"'"
    try:
        cursor.execute(sql)
        result  = cursor.fetchall()
        for row in result:
            qLS = row[0]
    except:
        "Erro ao pegar quantidade de LS"
    db.close()
    return qLS
    
    
     
        

def InsertPartition(entrada, particao, id_dataset):
    db = connect.conexaoMysql()
    cursor = db.cursor()
    try:
        cursor.execute("INSERT INTO Particao VALUES (0, %s, %s, %s)",(str(entrada), str(particao), str(id_dataset)))
        db.commit()
    except:
        db.rollback()
    
    db.close()
    
    

def VerificaRandom(entrada):
    db = connect.conexaoMysql()
    cursor = db.cursor()
    sql = "SELECT * FROM Particao WHERE entrada = '"+str(entrada)+"'"
    try:
        cursor.execute(sql)
        linhas = cursor.rowcount
    except:
        "Erro ao verificar" 
    
    db.close()
    
    return linhas
        


def calculoprobabilidade(feature, treinamento):
    espaco_amostral = len(treinamento)
    evento = 0
    
    db = connect.conexaoMysql()
    cursor = db.cursor()
    sql = "SELECT nome_dataset FROM Features WHERE features = '"+str(feature)+"' AND tipo_feature = 'Linkset'"
    try:
        cursor.execute(sql)
        result  = cursor.fetchall()
        for row in result:
            if(row[0] in treinamento):
                evento = evento + 1
    
    except:
        "Erro ao calcular probabilidade" 
    
    db.close()
    prob = float(evento)/float(espaco_amostral)
    return prob
        
   
        
    
def PrepararSimilaridadeTi(lista_conjunto, treinamento, dict_probabilidades):
    dict_similaridade = {}
    
    soma = 0
    for k in lista_conjunto:
        get_prob = dict_probabilidades[k]
        if(get_prob == 0):
            log_prob = 0
        else:
            log_prob = math.log(get_prob)
        union_teste = log_prob + soma
        soma = union_teste
    
            
    soma_ = 0
    for k in treinamento:
        lista_ls = Features.GetLinkSet(k)
        multi= 0
        inter = 0
        for i in lista_conjunto:
            if(i in lista_ls):
                get_prob = dict_probabilidades[i]
                if(get_prob == 0):
                    log_prob = 0
                else:
                    log_prob = math.log(get_prob)
                    inter = log_prob + multi
                    multi = inter
        
        for i in lista_ls:
            get_prob = dict_probabilidades[i]
            if(get_prob == 0):
                log_prob = 0
            else:
                log_prob = math.log(get_prob)
            union_treinamento = log_prob + soma_
            soma_ = union_treinamento
    
        union = union_treinamento + union_teste
                 
        try:
            result = SimilaridadeTI.similaridade(inter , union)
        except Exception,e: print str(e)
        dict_similaridade[k] = result
    
    return dict_similaridade
        
            
             
    


def LSTreinamento(lista_treinamento):
    lista_nome = []
    dict_treinamentoLS = {}
    lista_ls = []
    lista_ls_dataset = []
    for i in lista_treinamento:
        nome_dataset = GetNomeDataset(i)
        lista_nome.append(nome_dataset)
        
        
    for i in lista_nome:
        lista_ls = Features.GetLinkSet(i)
        for j in lista_ls:
            if(j in lista_nome):
                lista_ls_dataset.append(j)
                
                       
        dict_treinamentoLS[i] = lista_ls_dataset
        
    return dict_treinamentoLS
    

def GetMaiorTFIDF(lista):
    return max(lista)

def VerificaLSMAP(verificaLS, nome_dataset):
    db = connect.conexaoMysql()
    cursor_ls = db.cursor()
    sql_ls = "SELECT features FROM Features WHERE nome_dataset = '"+str(nome_dataset)+"' AND tipo_feature = 'Linkset'"
    try:
        cursor_ls.execute(sql_ls)
        result_ls  = cursor_ls.fetchall()
        for row_ls in result_ls:
            if(verificaLS == row_ls[0]):
                return 1
            
    except:
        "Erro"
        
    db.close()
    return 0


def PrepararSimilaridade(lista_teste, lista_treinamento):
    dict = {}
    for i in lista_treinamento:
        for nome_treinamento, vetor in i.iteritems():
            resultado_similaridade = similaridade.calculocosseno(vetor, lista_teste)
            dict[nome_treinamento] = resultado_similaridade
        
    
    
    return dict
    


def VerificarLinkSet(entrada):
    db = connect.conexaoMysql()
    cursor = db.cursor()
    sql = "SELECT LinkSet FROM `Entrada` WHERE id_entrada = '"+str(entrada)+"' " 
    try:
        cursor.execute(sql)
        results = cursor.fetchall()
        for row in results:
            num_linkset = row[0]
        
    except:
        print "Erro ao verificar o LinkSet"
    
    return num_linkset


def VerificarClasspartition(entrada):
    db = connect.conexaoMysql()
    cursor = db.cursor()
    sql = "SELECT classpartition FROM `Entrada` WHERE id_entrada = '"+str(entrada)+"' " 
    try:
        cursor.execute(sql)
        results = cursor.fetchall()
        for row in results:
            classpartiton = row[0]
        
    except:
        print "Erro ao verificar o Classpartition"
    
    return classpartiton


def VerificarPropretyPartition(entrada):
    db = connect.conexaoMysql()
    cursor = db.cursor()
    sql = "SELECT propretypartition FROM `Entrada` WHERE id_entrada = '"+str(entrada)+"' " 
    try:
        cursor.execute(sql)
        results = cursor.fetchall()
        for row in results:
            propretypartition = row[0]
        
    except:
        print "Erro ao verificar o PropretyPartition"
    
    return propretypartition

def VerificarEntites(entrada):
    db = connect.conexaoMysql()
    cursor = db.cursor()
    sql = "SELECT Entites FROM `Entrada` WHERE id_entrada = '"+str(entrada)+"' " 
    try:
        cursor.execute(sql)
        results = cursor.fetchall()
        for row in results:
            entites = row[0]
        
    except:
        print "Erro ao verificar o Entites"
    
    return entites


def GetNomeDataset(i):
    db = connect.conexaoMysql()
    cursor_nome_dataset = db.cursor()
    sql_nome_dataset = "SELECT nome_dataset FROM Dataset WHERE id_dataset = '"+str(i)+"'"
    try:
        cursor_nome_dataset.execute(sql_nome_dataset)
        result_nome_dataset  = cursor_nome_dataset.fetchall()
        for row_nome_dataset in result_nome_dataset:
            nome_dataset = row_nome_dataset[0]
    except:
        print "Erro no Pegar o nome do Dataset"
        
    db.close()
    return nome_dataset

        

            
        
        
    