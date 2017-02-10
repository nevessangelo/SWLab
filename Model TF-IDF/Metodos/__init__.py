#coding: utf-8

import ConnectionMysql as connect
import SimilaridadeCosseno as similaridade
from PIL import Image

def exportImagem(particao, lista, nome_dataset):
    print lista
    vetor_nome = nome_dataset.split('/')
    nome = vetor_nome[-1]
    caminho = '/home/angelo/Área de Trabalho/'+ particao + "/" + nome + ".jpg"
    im = Image.fromarray(lista)
    im.save(caminho)
    

def GetMaiorTFIDF(lista):
    return max(lista)

def VerificaLSMAP(verificaLS, dataset):
    nome_dataset = GetNomeDataset(dataset)
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

            
        
        
    