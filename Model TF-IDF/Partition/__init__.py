#coding: utf-8

import ConnectionMysql as connect
import Metodos
import Fabric

def Partition(entrada,type):
    lista_parte1 = []
    lista_parte2 = []
    lista_parte3 = []  
    cont = 0
    db = connect.conexaoMysql()
    cursor = db.cursor()
    sql = "SELECT * FROM `Dataset` AS a INNER JOIN  `Entrada` AS b ON a.qE >= b.Entites AND a.qC >= b.classpartition AND a.qP >= b.propretypartition AND a.qLS >= b.Linkset AND a.qType >= b.type AND b.id_entrada = '"+str(entrada)+"' order by nome_dataset desc limit 3" 
    try:
        cursor.execute(sql)
        results = cursor.fetchall()
    
        for row in results:      
                
            if (cont == 0):
                lista_parte1.append(str(row[0]))
                cont = cont + 1
            elif (cont == 1):
                lista_parte2.append(str(row[0]))
                cont = cont + 1
            elif (cont == 2):
                lista_parte3.append(str(row[0]))
                cont = 0
            
           
    except:
        print "Erro no Particionamento"
    
            
    db.close()
    
    num_linkset = Metodos.VerificarLinkSet(entrada)
    num_class = Metodos.VerificarClasspartition(entrada)
    num_proprety = Metodos.VerificarPropretyPartition(entrada)
    num_entites = Metodos.VerificarEntites(entrada)
    
    total_datasets = len(lista_parte1) + len(lista_parte2) + len(lista_parte3)
    print "Teste: " + str(lista_parte1)
    print "Treinamento: " + str(lista_parte2 + lista_parte3)
    print "Total de "+ str(total_datasets) + " Datasets"
    lista_treinamento = lista_parte2 + lista_parte3
    lista_total_dataset = lista_parte1 + lista_parte2 + lista_parte3
    
    arquivo = 1
    Fabric.fabric(type, num_linkset, num_class, num_proprety, num_entites, lista_parte1, lista_treinamento, lista_total_dataset, total_datasets, arquivo)