#coding: utf-8



import random
import Metodos




def exportFiles(id_query, documento, score, query_origem, lista_total_dataset, arquivo):
    rank_document = random.randint(1,500)
    with open('/home/angelo/Área de Trabalho/Results'+str(arquivo)+'.txt', 'a') as the_file:
        the_file.write(str(id_query) + "          " + "Q0" + "     " + str(documento) + "                              " + str(rank_document) + "            " + str(score) + "           " + "STANDARD")
        the_file.write("\n")
        
    the_file.close()
    
  #  relevante = Metodos.VerificaLSTeste(query_origem, lista_total_dataset)
    
   # with open('/home/angelo/Área de Trabalho/Quers.txt', 'a') as the_file:
   #     the_file.write(str(id_query) + " " + "0" + " " + str(documento) + " " + str(relevante))
   #     the_file.write("\n")
        
   # the_file.close()
    
    
def exportFiles2(dict, lista_total_dataset, arquivo):
    for id_query, dataset_origem in dict.iteritems(): 
        for i in lista_total_dataset:      
            relevante = Metodos.VerificaLSMAP(dataset_origem, i)
            nome_documento = Metodos.GetNomeDataset(i)
            with open('/home/angelo/Área de Trabalho/Quers'+str(arquivo)+'.txt', 'a') as the_file:
                the_file.write(str(id_query) + " " + "0" + " " + str(nome_documento) + " " + str(relevante))
                the_file.write("\n")
                
            the_file.close()
                
        
        
         
             
   
    
    
        
        

        
        