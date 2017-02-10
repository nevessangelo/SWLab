#coding: utf-8

import ConjuntoFeatures
import Features
import VectorRepresentation as representation
import Metodos
import MAP as filemap
import ConverVector

def fabricSets(num_linkset, num_class, num_proprety, num_entites, lista_parte1):
    lista_representacoes = ConjuntoFeatures.RepresentacaoConjunto(lista_parte1, num_linkset, num_class, num_proprety, num_entites)
    return lista_representacoes
    


def fabric(type, num_linkset, num_class, num_proprety , num_entites,  lista_parte1, lista_treinamento, lista_total_dataset, total_datasets, arquivo):
    if(type == "cosseno"):
        id_query = 0
        dict_query = {}
        vetor_aux = []
        lista_ls = []
        lista_datasetproprety = []
        lista_datasetclass = []
        lista_representacoes = fabricSets(num_linkset, num_class, num_proprety, num_entites, lista_parte1)
        if(num_linkset > 0):
            lista_datasetls = Features.LSDataset(lista_treinamento, lista_parte1)
            lista_ls = Features.ConjuntoLS(lista_datasetls)
        
        if(num_proprety > 0):
            lista_datasetproprety = Features.PropretyDataset(lista_treinamento, lista_parte1)
            
        if(num_class > 0):
            lista_datasetclass = Features.ClassDataset(lista_treinamento, lista_parte1)
            
        vetor_aux = lista_ls + lista_datasetproprety + lista_datasetclass
        tamanho_vetor = len(vetor_aux)
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
        filemap.exportFiles2(dict_query, lista_total_dataset, arquivo)
        
    if(type == "deep"):
        lista_tfidf = []
        lista_representacoes = fabricSets(num_linkset, num_class, num_proprety, num_entites, lista_parte1)
        if(num_linkset > 0):
            lista_datasetls = Features.LSDataset(lista_treinamento, lista_parte1)
            lista_ls = Features.ConjuntoLS(lista_datasetls)
            
        vetor_aux = lista_ls
        tamanho_vetor = len(vetor_aux)
        vetores_treinamento = representation.vetores_treinamento(lista_treinamento,tamanho_vetor,vetor_aux,lista_parte1)
        for i in lista_representacoes:
            for nome_dataset, lista_conjunto in i.iteritems():
                for k in lista_conjunto:
                    vetores_teste = representation.vetores_teste(nome_dataset, k, tamanho_vetor, vetor_aux, total_datasets)
                    for m in vetores_teste:
                        lista_tfidf.append(m)
                        
        for i in vetores_treinamento:
            for k in i:
                v = i[k]
                for m in v:
                    lista_tfidf.append(m)
                    
        
        max_tfidf = Metodos.GetMaiorTFIDF(lista_tfidf)
        vetores_treinamento = ConverVector.convertTreinamento(vetores_treinamento, max_tfidf)
        
        particao = "img_teste"
        for i in lista_representacoes:
            for nome_dataset, lista_conjunto in i.iteritems():
                for k in lista_conjunto:
                    vetores_teste = representation.vetores_teste(nome_dataset, k, tamanho_vetor, vetor_aux, total_datasets)
                    vetores_teste_converted = ConverVector.convertTest(vetores_teste, max_tfidf)
                    Metodos.exportImagem(particao, vetores_teste_converted, nome_dataset)
                    
        
                    
        
        
        
        #teste = ConverVector.convertTreinamento(vetores_treinamento)
        #print teste