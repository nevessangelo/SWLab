import os
import Bank_Methods as methods
import sys
def exportFiles(id_query, documento, score, query_origem,  rotation, dir_path):
    rank_document = 0
    #dir_path = os.path.dirname(sys.modules['__main__'].__file__)+str("/Result/Cosseno/Quers"+str(rotation)+".txt")
    with open(dir_path, 'a') as the_file:
        the_file.write('%-10s %-10s %-160s %-15s %-160s %s' % (str(id_query), "QO", str(documento), "0", str(score), "STANDARD")) 
        the_file.write("\n")
        
    the_file.close()
    
def exportrelevants(dict,rotation,lista_treinamento, db, dir_path):
    #dir_path = os.path.dirname(sys.modules['__main__'].__file__)+str("/Result/Cosseno/Relevants"+str(rotation)+".txt")
    with open(dir_path, 'a') as the_file:
        for id_query, dataset_origem in dict.items():
            for i in lista_treinamento:
                boolean_linkset = methods.LSMAP(i,dataset_origem, db)
                if(boolean_linkset > 0):
                    relevante = 1
                else:
                    relevante = 0
        
                the_file.write(str(id_query) + " " + "0" + " " + str(i) + " " + str(relevante))
                the_file.write("\n")
        
    the_file.close()   
            
    