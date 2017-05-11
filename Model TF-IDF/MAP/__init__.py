#coding: utf-8



import random
import Metodos


def exportFiles(id_query, documento, score, query_origem, lista_total_dataset, arquivo):
    rank_document = 0
    with open('/home/angelo/Área de Trabalho/Results'+str(arquivo)+'.txt', 'a') as the_file:
        the_file.write('%-10s %-10s %-160s %-15s %-160s %s' % (str(id_query), "QO", str(documento), "0", str(score), "STANDARD")) 
        the_file.write("\n")
        
    the_file.close()

    
def exportFiles2(dict, lista_total_dataset,arquivo, lista_treinamento):
    datasets_treinamento = []
    relevante = 0;
    for i in lista_treinamento:
        nome_treinamento = Metodos.GetNomeDataset(i)
        datasets_treinamento.append(nome_treinamento)
    
    for id_query, dataset_origem in dict.iteritems():
        for i in datasets_treinamento:           
            boolean_linkset = Metodos.VerificaLSMAP(i,dataset_origem)
            if(boolean_linkset > 0):
                relevante = 1
            else:
                relevante = 0
            
            with open('/home/angelo/Área de Trabalho/Quers'+str(arquivo)+'.txt', 'a') as the_file:
                the_file.write(str(id_query) + " " + "0" + " " + str(i) + " " + str(relevante))
                the_file.write("\n")
                
            the_file.close()
                
        
        
         
             
   
    
    
        
        

        
        