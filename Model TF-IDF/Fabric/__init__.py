#coding: utf-8

import ConjuntoFeatures
import Features
import VectorRepresentation as representation
import Metodos
import MAP as filemap
import ConverVector
import weka
import random

def fabricSets(num_linkset, num_class, num_proprety, num_entites, lista_parte1):
    try:
        lista_representacoes = ConjuntoFeatures.RepresentacaoConjunto(lista_parte1, num_linkset, num_class, num_proprety, num_entites)
    except Exception,e: print str(e)        
    return lista_representacoes
    


def fabric(type, num_linkset, num_class, num_proprety , num_entites,  lista_parte1, lista_treinamento, lista_total_dataset, total_datasets, arquivo):
    if(type == "cosseno"):
        id_query = 0
        dict_query = {}
        vetor_aux = []
        lista_ls = []
        #lista_datasetproprety = []
        #lista_datasetclass = []
        lista_representacoes = fabricSets(num_linkset, num_class, num_proprety, num_entites, lista_parte1)
       # if(num_linkset > 0):
       #     lista_datasetls = Features.LSDataset(lista_treinamento, lista_parte1)
       #     lista_ls = Features.ConjuntoLS(lista_datasetls,lista_treinamento)
       #     features = Features.RetiraLS(lista_ls, lista_treinamento)
        features = Features.GetFeatures(lista_parte1, lista_treinamento) 
       # if(num_proprety > 0):
       #     lista_datasetproprety = Features.PropretyDataset(lista_treinamento, lista_parte1)
            
       # if(num_class > 0):
       #     lista_datasetclass = Features.ClassDataset(lista_treinamento, lista_parte1)
            
        vetor_aux = features
        tamanho_vetor = len(vetor_aux)
        print  "Tamanho do vetor de Features: "+str(tamanho_vetor)
        vetores_treinamento = representation.vetores_treinamento(lista_treinamento,tamanho_vetor,vetor_aux,lista_parte1)
        with open('/home/angelo/Área de Trabalho/Resultado'+str(arquivo)+'.txt', 'a') as the_file:
            for i in lista_representacoes:
                for nome_dataset, lista_conjunto in i.iteritems():
                    #1 dataset 5 tipos de representações
                    for k in lista_conjunto:
                        id_query = id_query + 1
                        dict_query[id_query] = nome_dataset
                        vetores_teste = representation.vetores_teste(nome_dataset, k, tamanho_vetor, vetor_aux, total_datasets)
                        rank_similaridade = Metodos.PrepararSimilaridade(vetores_teste, vetores_treinamento)
                        the_file.write("Para Dataset : " + str(nome_dataset))
                        the_file.write("\n")
                        the_file.write("Representação com : " + str(k))
                        the_file.write("\n")
                        teste = sorted(rank_similaridade.items(), key=lambda x: (-x[1], x[0]))
                        for result in teste:
                            the_file.write(str(result[0]) + "-----" + str(result[1]))
                            the_file.write("\n")
                            filemap.exportFiles(id_query, result[0], result[1], nome_dataset, lista_total_dataset, arquivo)
        
        the_file.close()
        filemap.exportFiles2(dict_query, lista_total_dataset, arquivo, lista_treinamento)
    
    if(type == "TI"):
        id_query = 0
        dict_query = {}
        vetor_aux = []
        lista_representacoes = fabricSets(num_linkset, num_class, num_proprety, num_entites, lista_parte1)
        nomes_treinamento = []
        nomes_teste = []
        datasets = []
        ls_no_repeat = []
        dict_probabilidades = {}
        for i in lista_treinamento:
            nome_dataset = Metodos.GetNomeDataset(i)
            nomes_treinamento.append(nome_dataset)
        
        for i in lista_parte1:
            nome_dataset = Metodos.GetNomeDataset(i)
            nomes_teste.append(nome_dataset)
        
        datasets = nomes_teste + nomes_treinamento
        for i in datasets:
            lista_ls = Features.GetLinkSet(i)
            for k in lista_ls:
                if(k not in ls_no_repeat):
                    ls_no_repeat.append(k)
                
                
        for i in ls_no_repeat:
            prob = Metodos.calculoprobabilidade(i, nomes_treinamento)
            dict_probabilidades[i] = prob
            
    
        with open('/home/angelo/Área de Trabalho/Resultado'+str(arquivo)+'.txt', 'a') as the_file:
                for i in lista_representacoes:
                    for nome_dataset, lista_conjunto in i.iteritems():
                        #1 dataset 5 tipos de representações
                        for k in lista_conjunto:
                            id_query = id_query + 1
                            dict_query[id_query] = nome_dataset
                            similaridade = Metodos.PrepararSimilaridadeTi(k, nomes_treinamento, dict_probabilidades)
                            teste = sorted(similaridade.items(), key=lambda x: (-x[1], x[0]))
                            the_file.write("Para Dataset : " + str(nome_dataset))
                            the_file.write("\n")
                            the_file.write("Representação com : " + str(k))
                            the_file.write("\n")
                            for result in teste:
                                the_file.write(str(result[0]) + "-----" + str(result[1]))
                                the_file.write("\n")
                                filemap.exportFiles(id_query, result[0], result[1], nome_dataset, lista_total_dataset, arquivo)
                                
        the_file.close()
        filemap.exportFiles2(dict_query, lista_total_dataset, arquivo, lista_treinamento)
        
    if(type == "JRIP"):  
        #lista_datasetls = Features.LSDataset(lista_treinamento, lista_parte1)
        #lista_ls = Features.ConjuntoLS(lista_datasetls,lista_treinamento)
        #features = Features.RetiraLS(lista_ls, lista_treinamento)
        
        features = Features.GetFeatures(lista_parte1, lista_treinamento)
        vetor_aux = features
        tamanho_vetor = len(vetor_aux)
        print "Tamanho do vetor de features "+ str(tamanho_vetor)
        vetores_treinamento = representation.vetores_treinamento(lista_treinamento,tamanho_vetor,vetor_aux,lista_parte1)
        weka.ExportFileWeka(vetores_treinamento, vetor_aux)
        #weka.ExportFileWekaMultiClass(vetores_treinamento, vetor_aux)
        
        #lista_representacoes = fabricSets(num_linkset, num_class, num_proprety, num_entites, lista_parte1)
        #for i in lista_representacoes:
            
        
        
        
            
            
     
                    