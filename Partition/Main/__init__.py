#coding: utf-8
'''
Created on 25 de out de 2016

@author: angelo
'''
import MySQLdb
import math
from decimal import Decimal
from array import array
import csv

    
   
    
def conexaoMysql():
    db = MySQLdb.connect("localhost","SemanticWeb","123","Base_Datasets")
    return db
    

def calculotf(triplas, total_triplas):
    tf = (triplas/total_triplas)
    return tf

def calculoidf(soma_frequencia, num_datasets):
    idf = math.log((soma_frequencia/num_datasets))
    return idf
    
    

def representacao_vetorial(lista, entrada):
    vetores = []
    db = conexaoMysql()
    cursor = db.cursor()
    sql = "SELECT `classpartition`, `propretypartition`, `LinkSet`, `Entites` FROM `E'` WHERE id_entrada = '"+str(entrada)+"' "
    cursor.execute(sql)
    results = cursor.fetchall()
    for i in lista:
        vetor = []
        vetor.append(i)
        cursor_nome_dataset = db.cursor()
        sql_nome_dataset = "SELECT nome_dataset FROM Dataset WHERE id_dataset = '"+str(i)+"'"
        cursor_nome_dataset.execute(sql_nome_dataset)
        result_nome_dataset  = cursor_nome_dataset.fetchall()
        for row_nome_dataset in result_nome_dataset:
            nome_dataset = row_nome_dataset[0]
        for row in results:
            if(row[0] > 0):
                cursor_class = db.cursor()
                sql_class = "SELECT `frequencia`, `dataset_size`, `features` FROM `Features'` WHERE nome_dataset = '"+nome_dataset+"' AND v_linkset = '0'  AND `tipo_feature` = 'Class' LIMIT "+str(row[0])+" "
                try:
                    cursor_class.execute(sql_class)
                    results_class = cursor_class.fetchall()
                    for row_class in results_class:
                        frequencia = float(row_class[0])
                        dataset_size = float(row_class[1])
                        tf = calculotf(frequencia,dataset_size)
                        cursor_idf = db.cursor()
                        sql_idf = "SELECT SUM(frequencia) as soma_frequencia, COUNT(nome_dataset) FROM `Features'` WHERE  features = '"+row_class[2]+"' AND v_linkset = '0' "
                        try:
                            cursor_idf.execute(sql_idf)
                            results_idf = cursor_idf.fetchall()
                            for row_idf in results_idf:
                                soma_frequencia = float(row_idf[0])
                                soma = int(row_idf[1])
                                idf = calculoidf(soma_frequencia,soma)
                                tf_idf = (tf * idf)
                                vetor.append(tf_idf)
                                
                        except:
                            print "Error"
                except:
                    print "Error"
                    
            if(row[1] > 0):
                cursor_proprety = db.cursor()
                sql_proprety = "SELECT `frequencia`, `dataset_size`, `features` FROM `Features'` WHERE nome_dataset = '"+nome_dataset+"' AND v_linkset = '0' AND `tipo_feature` = 'Property' LIMIT "+str(row[1])+" "
                try:
                    cursor_proprety.execute(sql_proprety)
                    results_proprety = cursor_proprety.fetchall()
                    for row_proprety in results_proprety:
                        frequencia = float(row_proprety[0])
                        dataset_size = float(row_proprety[1])
                        tf = calculotf(frequencia,dataset_size)
                        cursor_idf_proprety = db.cursor()
                        sql_idf_proprety = "SELECT SUM(frequencia) as soma_frequencia, COUNT(nome_dataset) FROM `Features'` WHERE  features = '"+row_proprety[2]+"' AND v_linkset = '0' "
                        try:
                            cursor_idf_proprety.execute(sql_idf_proprety)
                            results_idf_proprety = cursor_idf_proprety.fetchall()
                            for row_idf_proprety in results_idf_proprety:
                                soma_frequencia = float(row_idf_proprety[0])
                                soma = int(row_idf_proprety[1])
                                idf = calculoidf(soma_frequencia,soma)
                                tf_idf = (tf * idf)
                                vetor.append(tf_idf)
                        except:
                            print "Error"  
                except:
                    print "Error" 
                    
            
            if(row[2] > 0):
                cursor_ls = db.cursor()
                sql_ls = "SELECT `frequencia`, `dataset_size`, `features` FROM `Features'` WHERE nome_dataset = '"+nome_dataset+"' AND v_linkset = '0' AND `tipo_feature` = 'Linkset' LIMIT "+str(row[2])+" "
                try:
                    cursor_ls.execute(sql_ls)
                    results_ls = cursor_ls.fetchall()
                    for row_ls in results_ls:
                        frequencia = float(row_ls[0])
                        dataset_size = float(row_ls[1])
                        tf = calculotf(frequencia,dataset_size)
                        cursor_idf_ls = db.cursor()
                        sql_idf_ls = "SELECT SUM(frequencia) as soma_frequencia, COUNT(nome_dataset) FROM `Features'` WHERE features = '"+row_ls[2]+"' AND v_linkset = '0' "
                        try:
                            cursor_idf_ls.execute(sql_idf_ls)
                            results_idf_ls = cursor_idf_ls.fetchall()
                            for row_idf_ls in results_idf_ls:
                                soma_frequencia = float(row_idf_ls[0])
                                soma = int(row_idf_ls[1])
                                idf = calculoidf(soma_frequencia,soma)
                                tf_idf = (tf * idf)
                                vetor.append(tf_idf)       
                        except:
                            print "Error"   
                except:
                    print "Error" 
                    
            if(row[3] > 0):
                cursor_entites = db.cursor()
                sql_entites = "SELECT `frequencia`, `dataset_size`, `features` FROM `Features'` WHERE nome_dataset = '"+nome_dataset+"' AND v_linkset = '0' AND `tipo_feature` = 'Entites' LIMIT "+str(row[3])+" "
                try:
                    cursor_entites.execute(sql_entites)
                    results_entites = cursor_entites.fetchall()
                    for row_entites in results_entites:
                        frequencia = float(row_entites[0])
                        dataset_size = float(row_entites[1])
                        tf = calculotf(frequencia,dataset_size)
                        cursor_idf_entites = db.cursor()
                        sql_idf_entites = "SELECT SUM(frequencia) as soma_frequencia, COUNT(nome_dataset) FROM `Features'` WHERE features = '"+row_entites[2]+"' AND v_linkset = '0'"
                        try:
                            cursor_idf_entites.execute(sql_idf_entites)
                            results_idf_entites = cursor_idf_entites.fetchall()
                            for row_idf_entites in results_idf_entites:
                                soma_frequencia = float(row_idf_entites[0])
                                soma = int(row_idf_entites[1])
                                idf = calculoidf(soma_frequencia,soma)
                                tf_idf = (tf * idf)
                                vetor.append(tf_idf)
                        except:
                            print "Error"    
                except:
                    print "Error"         
        vetores.append(vetor)
        
      
        
    return vetores
        
                    
                
def corteLinkSet(lista_parte1, lista_treinamento):
    for i in lista_treinamento:
        db = conexaoMysql()
        cursor = db.cursor()
        cursor_nome_dataset = db.cursor()
        sql_nome_dataset = "SELECT nome_dataset FROM Dataset WHERE id_dataset = '"+str(i)+"'"
        cursor_nome_dataset.execute(sql_nome_dataset)
        result_nome_dataset  = cursor_nome_dataset.fetchall()
        for row_nome_dataset in result_nome_dataset:
            nome_dataset = row_nome_dataset[0]
        sql = "SELECT features FROM `Features'` WHERE tipo_feature = 'LinkSet' AND nome_dataset = '"+nome_dataset+"' "
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                for j in lista_parte1:
                    cursor_nome_datasetl1 = db.cursor()
                    sql_nome_datasetl1 = "SELECT nome_dataset FROM `Dataset` WHERE id_dataset = '"+str(j)+"'"
                    cursor_nome_datasetl1.execute(sql_nome_datasetl1)
                    result_nome_datasetl1  = cursor_nome_datasetl1.fetchall()
                    for row_nome_datasetl1 in result_nome_datasetl1:
                        nome_datasetl1 = row_nome_datasetl1[0]
                    if(nome_datasetl1 == row[0]):
                        db_partition = conexaoMysql()
                        print row[0] + " nome do dataset de treinamento "+  nome_datasetl1
                        sql_update = "UPDATE `Features'` SET v_linkset = '1' WHERE tipo_feature = 'Linkset' AND features = '"+row[0]+"' AND nome_dataset = '"+nome_dataset+"'"
                        print sql_update
                        try:
                            cursor_update = db_partition.cursor()
                            cursor_update.execute(sql_update)
                            db_partition.commit()
                            cursor_update.close()
                            db_partition.close()
                        except:
                            print "Error"
                            
        except:
            print "Error"
        
        cursor.close()
        db.close()
                
def Partition(entrada):
    lista_parte1 = []
    lista_parte2 = []
    lista_parte3 = []  
    resultado = 0
    db = conexaoMysql()
    cursor = db.cursor()
    sql = "SELECT a.id_dataset, a.qLS, b.id_entrada FROM `Dataset` AS a INNER JOIN  `E'` AS b ON a.qE >= b.Entites AND a.qC >= b.classpartition AND a.qP >= b.propretypartition AND a.qLS >= ( b.Linkset +10 ) AND b.id_entrada = '"+str(entrada)+"' " 
    try:
        cursor.execute(sql)
        results = cursor.fetchall()
        
        for row in results:
            resultado = row[1] % 3
            if (resultado == 0):
                lista_parte1.append(str(row[0]))
            elif (resultado == 1):
                lista_parte2.append(str(row[0]))
            elif (resultado == 2):
                lista_parte3.append(str(row[0]))  
                 
    except:
        print "Error"
    
    db.close()
          
    corteLinkSet(lista_parte1, lista_parte2)
    corteLinkSet(lista_parte1, lista_parte3)
    vetor_teste = representacao_vetorial(lista_parte1,entrada)
    with open("/home/angelo/Área de Trabalho/Teste.csv", "wb") as f:
        writer = csv.writer(f, quoting=csv.QUOTE_ALL)
        for row_teste in vetor_teste:
                writer.writerow(row_teste)
        
        f.close()
    
    
    vetore_final = []
    vetores_lista2 = representacao_vetorial(lista_parte2,entrada)
    vetores_lista3 = representacao_vetorial(lista_parte3,entrada)
    vetore_final.append(vetores_lista2)
    vetore_final.append(vetores_lista3)
    
    with open("/home/angelo/Área de Trabalho/Treinamento.csv", "wb") as f:
        writer = csv.writer(f, quoting=csv.QUOTE_ALL)
        for vetores in vetore_final:
            for linhas_vetores in vetores:
                writer.writerow(linhas_vetores)
        
        f.close()
    
    
def all_linksets():
    db = conexaoMysql()
    cursor = db.cursor()
    sql = "UPDATE `D'` SET v_linkset = '0' "
    try:
        cursor.execute(sql)
        db.commit()
        cursor.close()
        cursor.close()

    except:
        print "Error"
    
      

    
if __name__ == '__main__':     
        entrada = (input('Lembre-se de criar a view Features!! Digite qual a entrada: '))
        all_linksets()
        Partition(entrada)
            


            
        