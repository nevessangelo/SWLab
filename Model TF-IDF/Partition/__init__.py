#coding: utf-8

import ConnectionMysql as connect
import Metodos
import Fabric
import random
from collections import OrderedDict


def Partition(entrada,type):
    lista_parte1 = []
    lista_parte2 = []
    lista_parte3 = []
    lista_datasets = []
    if(type == "JRIP" or type == "cosseno" or type == "TI"):
        db = connect.conexaoMysql()
        cursor = db.cursor()
        sql = "SELECT DISTINCT a.id_dataset FROM `Dataset` AS a INNER JOIN `Entrada` AS b INNER JOIN `Features'` c ON a.nome_dataset = c.nome_dataset AND a.qE >= b.Entites AND a.qC >= b.classpartition AND a.qP >= b.propretypartition AND a.qLS >= b.Linkset AND a.qType >= b.type AND b.id_entrada = " + str(entrada) + ""
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                lista_datasets.append(str(row[0]))
        
        except:
            print "Erro ao buscar os datasets"
            
    
        num_linkset = Metodos.VerificarLinkSet(entrada)
        if(num_linkset > 0):
            dict_datasets = {}
            for i in lista_datasets:
                nome_dataset = Metodos.GetNomeDataset(i)
                group = Metodos.GetGroup(nome_dataset)
                dict_datasets[i] = group
            
        
        lista_lifescience = []
        lista_publications = []
        lista_geography = []
        lista_socialweb = []
        lista_government = []
        lista_nongroup = []
        lista_media = []
        lista_user = []
        lista_linguistic = []
        lista_crossdomain = []
        for id_dataset, group in dict_datasets.iteritems():
            if(group == "lifesciences"):
                lista_lifescience.append(id_dataset)
            
            if(group == "publications"):
                lista_publications.append(id_dataset)
            
            if(group == "geography"):
                lista_geography.append(id_dataset)
            
            if(group == "socialweb"):
                lista_socialweb.append(id_dataset)
            
            if(group == "government"):
                lista_government.append(id_dataset)
                
            if(group == "non-group"):
                lista_nongroup.append(id_dataset)
                
            if(group == "media"):
                lista_media.append(id_dataset)
            
            if(group == "usergeneratedcontent"):
                lista_user.append(id_dataset)
                
            if(group == "linguistics"):
                lista_linguistic.append(id_dataset)
            
            if(group == "crossdomain"):
                lista_crossdomain.append(id_dataset)
                
                
            
        
        if(len(lista_lifescience) != 0):
            dict_datasets_le = {}
            for i in lista_lifescience:
                nome_dataset = Metodos.GetNomeDataset(i)
                qLS = Metodos.GetQuantidadeLS(nome_dataset)
                dict_datasets_le[i] = qLS
                
            
            dict_ordered = OrderedDict(sorted(dict_datasets_le.items(), key=lambda t: t[1]))
            cont = 1
            for id_dataset, qLS in dict_ordered.items():
                flag = 0
                if(cont == 1 and flag == 0):
                    lista_parte1.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 2 and flag == 0):
                    lista_parte2.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 3 and flag == 0):
                    lista_parte3.append(str(id_dataset))
                    cont = 1
                    flag = 1
        
        if(len(lista_publications) != 0):
            dict_datasets_pub = {}
            for i in lista_publications:
                nome_dataset = Metodos.GetNomeDataset(i)
                qLS = Metodos.GetQuantidadeLS(nome_dataset)
                dict_datasets_pub[i] = qLS
                
            dict_ordered = OrderedDict(sorted(dict_datasets_pub.items(), key=lambda t: t[1]))
            cont = 1
            for id_dataset, qLS in dict_ordered.items():
                flag = 0
                if(cont == 1 and flag == 0):
                    lista_parte1.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 2 and flag == 0):
                    lista_parte2.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 3 and flag == 0):
                    lista_parte3.append(str(id_dataset))
                    cont = 1
                    flag = 1
                    
        if(len(lista_geography) != 0):
            dict_datasets_geo = {}
            for i in lista_geography:
                nome_dataset = Metodos.GetNomeDataset(i)
                qLS = Metodos.GetQuantidadeLS(nome_dataset)
                dict_datasets_geo[i] = qLS
            
            
            dict_ordered = OrderedDict(sorted(dict_datasets_geo.items(), key=lambda t: t[1]))
            cont = 1
            for id_dataset, qLS in dict_ordered.items():
                flag = 0
                if(cont == 1 and flag == 0):
                    lista_parte1.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 2 and flag == 0):
                    lista_parte2.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 3 and flag == 0):
                    lista_parte3.append(str(id_dataset))
                    cont = 1
                    flag = 1
        
        if(len(lista_socialweb) != 0):
            dict_datasets_socialweb = {}
            for i in lista_socialweb:
                nome_dataset = Metodos.GetNomeDataset(i)
                qLS = Metodos.GetQuantidadeLS(nome_dataset)
                dict_datasets_socialweb[i] = qLS
                
            
            dict_ordered = OrderedDict(sorted(dict_datasets_socialweb.items(), key=lambda t: t[1]))
            cont = 1
            for id_dataset, qLS in dict_ordered.items():
                flag = 0
                if(cont == 1 and flag == 0):
                    lista_parte1.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 2 and flag == 0):
                    lista_parte2.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 3 and flag == 0):
                    lista_parte3.append(str(id_dataset))
                    cont = 1
                    flag = 1
        
        if(len(lista_government) != 0):
            dict_datasets_government = {}
            for i in lista_government:
                nome_dataset = Metodos.GetNomeDataset(i)
                qLS = Metodos.GetQuantidadeLS(nome_dataset)
                dict_datasets_government[i] = qLS
                
            dict_ordered = OrderedDict(sorted(dict_datasets_government.items(), key=lambda t: t[1]))
            cont = 1
            for id_dataset, qLS in dict_ordered.items():
                flag = 0
                if(cont == 1 and flag == 0):
                    lista_parte1.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 2 and flag == 0):
                    lista_parte2.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 3 and flag == 0):
                    lista_parte3.append(str(id_dataset))
                    cont = 1
                    flag = 1
                    
        if(len(lista_nongroup) != 0):
            dict_datasets_nongroup = {}
            for i in lista_nongroup:
                nome_dataset = Metodos.GetNomeDataset(i)
                qLS = Metodos.GetQuantidadeLS(nome_dataset)
                dict_datasets_nongroup[i] = qLS
                
            dict_ordered = OrderedDict(sorted(dict_datasets_nongroup.items(), key=lambda t: t[1]))
            cont = 1
            for id_dataset, qLS in dict_ordered.items():
                flag = 0
                if(cont == 1 and flag == 0):
                    lista_parte1.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 2 and flag == 0):
                    lista_parte2.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 3 and flag == 0):
                    lista_parte3.append(str(id_dataset))
                    cont = 1
                    flag = 1
        
        if(len(lista_media) != 0):
            dict_datasets_media = {}
            for i in lista_media:
                nome_dataset = Metodos.GetNomeDataset(i)
                qLS = Metodos.GetQuantidadeLS(nome_dataset)
                dict_datasets_media[i] = qLS
                
            dict_ordered = OrderedDict(sorted(dict_datasets_media.items(), key=lambda t: t[1]))
            cont = 1
            for id_dataset, qLS in dict_ordered.items():
                flag = 0
                if(cont == 1 and flag == 0):
                    lista_parte1.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 2 and flag == 0):
                    lista_parte2.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 3 and flag == 0):
                    lista_parte3.append(str(id_dataset))
                    cont = 1
                    flag = 1
        
        if(len(lista_user) != 0):
            dict_datasets_user = {}
            for i in lista_user:
                nome_dataset = Metodos.GetNomeDataset(i)
                qLS = Metodos.GetQuantidadeLS(nome_dataset)
                dict_datasets_user[i] = qLS
            
            dict_ordered = OrderedDict(sorted(dict_datasets_user.items(), key=lambda t: t[1]))
            cont = 1
            for id_dataset, qLS in dict_ordered.items():
                flag = 0
                if(cont == 1 and flag == 0):
                    lista_parte1.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 2 and flag == 0):
                    lista_parte2.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 3 and flag == 0):
                    lista_parte3.append(str(id_dataset))
                    cont = 1
                    flag = 1
            
        if(len(lista_linguistic) != 0):
            dict_datasets_linguistic = {}
            for i in lista_linguistic:
                nome_dataset = Metodos.GetNomeDataset(i)
                qLS = Metodos.GetQuantidadeLS(nome_dataset)
                dict_datasets_linguistic[i] = qLS
                
            
            dict_ordered = OrderedDict(sorted(dict_datasets_linguistic.items(), key=lambda t: t[1]))
            cont = 1
            for id_dataset, qLS in dict_ordered.items():
                flag = 0
                if(cont == 1 and flag == 0):
                    lista_parte1.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 2 and flag == 0):
                    lista_parte2.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 3 and flag == 0):
                    lista_parte3.append(str(id_dataset))
                    cont = 1
                    flag = 1
            
        if(len(lista_crossdomain) != 0):
            dict_datasets_crossdomain = {}
            for i in lista_crossdomain:
                nome_dataset = Metodos.GetNomeDataset(i)
                qLS = Metodos.GetQuantidadeLS(nome_dataset)
                dict_datasets_crossdomain[i] = qLS
            
            dict_ordered = OrderedDict(sorted(dict_datasets_crossdomain.items(), key=lambda t: t[1]))
            cont = 1
            for id_dataset, qLS in dict_ordered.items():
                flag = 0
                if(cont == 1 and flag == 0):
                    lista_parte1.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 2 and flag == 0):
                    lista_parte2.append(str(id_dataset))
                    cont = cont + 1
                    flag = 1
                if(cont == 3 and flag == 0):
                    lista_parte3.append(str(id_dataset))
                    cont = 1
                    flag = 1
            
            
            
        print len(lista_parte1)
        print len(lista_parte2)
        print len(lista_parte3)
                
           
    num_linkset = Metodos.VerificarLinkSet(entrada)
    num_class = Metodos.VerificarClasspartition(entrada)
    num_proprety = Metodos.VerificarPropretyPartition(entrada)
    num_entites = Metodos.VerificarEntites(entrada)
    
    total_datasets = len(lista_parte1) + len(lista_parte2) + len(lista_parte3)
    
    print "Teste: " + str(lista_parte1)
    print "Treinamento: " + str(lista_parte2 + lista_parte3)
    print "Total de "+ str(total_datasets) + " Datasets"
    lista_total_dataset = lista_parte1 + lista_parte2 + lista_parte3
    
    arquivo = 1
    lista_treinamento = lista_parte2 + lista_parte3
    Fabric.fabric(type, num_linkset, num_class, num_proprety, num_entites, lista_parte1, lista_treinamento, lista_total_dataset, total_datasets, arquivo)
    
    arquivo = 2
    lista_treinamento = lista_parte1 + lista_parte3
    Fabric.fabric(type, num_linkset, num_class, num_proprety, num_entites, lista_parte2, lista_treinamento, lista_total_dataset, total_datasets, arquivo)
    
    arquivo = 3
    lista_treinamento = lista_parte1 + lista_parte2
    Fabric.fabric(type, num_linkset, num_class, num_proprety, num_entites, lista_parte3, lista_treinamento, lista_total_dataset, total_datasets, arquivo)
    