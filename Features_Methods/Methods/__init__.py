import Sets as set_
import Bank_Methods as methods
import os
import sys 
import MAP as map
import Weka as weka

def Methods(type, list_parte1, list_treinamento, num_linkset, num_class, num_proprety, num_types, db, rotation):
    if(type == "cosseno"):
        id_query = 0
        dict_query = {}
        datasets_test = []
        datasets_tranning = []
        total_datasets = len(list_parte1) + len(list_treinamento)
        for i in list_parte1:
            name_dataset = methods.GetNameDataset(i, db)
            datasets_test.append(name_dataset)
        
        for i in list_treinamento:
            name_dataset = methods.GetNameDataset(i,db)
            datasets_tranning.append(name_dataset)
            
        
        vector_features = methods.Vector_Tranning_Features(datasets_test, datasets_tranning, num_linkset, num_class, num_proprety, num_types, db)
        size_vector = len(vector_features)
        print ("Making Features")
        print ("Size Vector Features: "+str(len(vector_features)))
        
        
        print ("Number of training datasets: " + str(len(list_treinamento)))
        print ("Making Random Representations")
        
        list_datasets_sets = set_.create_sets_test(5, datasets_test, num_linkset, num_class, num_proprety, num_types, db, vector_features)
        
        
        dir_path = os.path.dirname(sys.modules['__main__'].__file__)+str("/Result/Cosseno/Results"+str(rotation)+".txt")
        dir_path_ = os.path.dirname(sys.modules['__main__'].__file__)+str("/Result/Cosseno/Quers"+str(rotation)+".txt")
        dir_path__ = os.path.dirname(sys.modules['__main__'].__file__)+str("/Result/Cosseno/Relevants"+str(rotation)+".txt")
        list_Space_search = []
        dict_tfidf_tranning = methods.create_tfidf_tranning(datasets_tranning, size_vector, vector_features, datasets_test, num_linkset, num_class, num_proprety, num_types, db)
        with open(dir_path, 'a') as the_file:
            for i in list_datasets_sets:
                for nome_dataset, lista_conjunto in i.items():
                    for k in lista_conjunto:
                        id_query = id_query + 1
                        dict_query[id_query] = nome_dataset
                        vetores_teste = methods.vetores_teste(nome_dataset, k, size_vector, vector_features, total_datasets, db)
                        rank_similaridade = methods.similary(vetores_teste, dict_tfidf_tranning)
                        the_file.write("Para Dataset : " + str(nome_dataset))
                        the_file.write("\n")
                        the_file.write("Representação com : " + str(k))
                        the_file.write("\n")
                        ordern = sorted(rank_similaridade.items(), key=lambda x: (-x[1], x[0]))
                        Space_search = methods.LastRelevant(nome_dataset, ordern, db)
                        list_Space_search.append(Space_search)
                        for result in ordern:
                            the_file.write(str(result[0]) + "-----" + str(result[1]))
                            the_file.write("\n")
                            map.exportFiles(id_query, result[0], result[1], nome_dataset, rotation, dir_path_)
        the_file.close()
        map.exportrelevants(dict_query, rotation, datasets_tranning, db, dir_path__)
        return list_Space_search
        
                    
                    
    if(type == "TI"):
        list_datasets_sets = []
        list_tranning = []
        list_test = []
        ls_no_repeat = []
        dict_prob = {}
        dict_query = {}
        id_query = 0
        
        for i in list_treinamento:
            name_dataset = methods.GetNameDataset(i, db)
            list_tranning.append(name_dataset)
        
        for i in list_parte1:
            name_dataset = methods.GetNameDataset(i, db)
            list_test.append(name_dataset)
        
        print ("Making Random Representations")
        list_datasets_sets = set_.create_sets_testTI(5, list_test, num_linkset, num_class, num_proprety, num_types, db)
            
        datasets = list_test + list_tranning
        
        if(num_linkset > 0):
            for i in datasets:
                list_ls = methods.GetLinkSet(i, db)
                for k in list_ls:
                    if(k not in ls_no_repeat):
                        ls_no_repeat.append(k)
    
            for i in ls_no_repeat:
                prob = methods.Prob_ls(i, list_tranning, db)
                dict_prob[i] = prob
                
        #fazer com class, proprety, categorias
        print("Exporting files")        
        dir_path = os.path.dirname(sys.modules['__main__'].__file__)+str("/Result/TI/Results"+str(rotation)+".txt")
        dir_path_ = os.path.dirname(sys.modules['__main__'].__file__)+str("/Result/TI/Quers"+str(rotation)+".txt")
        dir_path__ = os.path.dirname(sys.modules['__main__'].__file__)+str("/Result/TI/Relevants"+str(rotation)+".txt")
        list_Space_search = []
        with open(dir_path, 'a') as the_file:
            for i in list_datasets_sets:
                for name_dataset, set_list in i.items():
                    for k in set_list:
                        id_query = id_query + 1
                        dict_query[id_query] = name_dataset
                        similarity = methods.similarityTI(k, list_tranning, dict_prob, num_linkset, num_class, num_proprety, num_types, db)
                        ordern = sorted(similarity.items(), key=lambda x: (-x[1], x[0]))
                        Space_search = methods.LastRelevant(name_dataset, ordern, db)
                        list_Space_search.append(Space_search)
                        the_file.write("Para Dataset : " + str(name_dataset))
                        the_file.write("\n")
                        the_file.write("Representação com : " + str(k))
                        the_file.write("\n")
                        for result in ordern:
                            the_file.write(str(result[0]) + "-----" + str(result[1]))
                            the_file.write("\n")
                            map.exportFiles(id_query, result[0], result[1], name_dataset, rotation, dir_path_)
                            
        the_file.close()
        map.exportrelevants(dict_query, rotation, list_tranning, db, dir_path__)
        return list_Space_search
    
    if(type == "JRIP" or type == "J48"):
        id_query = 0
        datasets_test = []
        datasets_tranning = []
        dict_tfidf_tranning_ = []
        list_datasets_sets = []
        
        for i in list_parte1:
            name_dataset = methods.GetNameDataset(i, db)
            datasets_test.append(name_dataset)
        
        for i in list_treinamento:
            name_dataset = methods.GetNameDataset(i,db)
            datasets_tranning.append(name_dataset)
        
        print ("Making Features")
            
        vector_features = methods.Vector_Tranning_Features(datasets_test, datasets_tranning, num_linkset, num_class, num_proprety, num_types, db)    
        print ("Size Vector Features: "+str(len(vector_features)))
        size_vector = len(vector_features)
        
            
        list_datasets_sets = set_.create_sets_test(5, datasets_test, num_linkset, num_class, num_proprety, num_types, db, vector_features)
        
        
        dict_tfidf_tranning_ = methods.create_tfidf_tranning_binario(datasets_tranning, size_vector, vector_features, datasets_test, num_linkset, num_types, db)
    
        print ("Exporting all files tranning: "+str(len(vector_features)))
        
        dir_path = os.path.dirname(sys.modules['__main__'].__file__)+str("/Result/Files_JR8_JRIP/"+str(rotation)+"/")
        
        
        for i in vector_features:
            weka.ExportFilesBinario(vector_features, i, datasets_test, datasets_tranning, dict_tfidf_tranning_, rotation, dir_path, num_linkset, num_types, db)
        
        total_datasets = len(list_parte1) + len(list_treinamento)
        dir_path_ = os.path.dirname(sys.modules['__main__'].__file__)+str("/Result/Files_JR8_JRIP/Test/"+str(rotation)+"/")
        
        for i in list_datasets_sets:
            for nome_dataset, lista_conjunto in i.items():
                for k in lista_conjunto:
                    id_query = id_query + 1               
                    vetores_teste = methods.vetores_teste(nome_dataset, k, size_vector, vector_features, total_datasets, db)   
                    weka.ExportFileBinarioTest(vector_features, vetores_teste, nome_dataset, dir_path_, id_query)
        
        methods.Documents(rotation, datasets_tranning, db)
        
    if(type == "SN"):
        list_test = []
        list_tranning = []
        datasets = []
        ls_no_repeat = []
        dict_popularity = {}
        dict_query = {}
        id_query = 0
        
        for i in list_treinamento:
            name_dataset = methods.GetNameDataset(i, db)
            list_tranning.append(name_dataset)
        
        for i in list_parte1:
            name_dataset = methods.GetNameDataset(i, db)
            list_test.append(name_dataset)
        
        print ("Making Random Representations")
        list_datasets_sets = set_.create_sets_testTI(5, list_test, num_linkset, num_class, num_proprety, num_types, db)
            
        print ("Building popularity")
        
        datasets = list_test + list_tranning
        
        if(num_linkset > 0):
            for i in datasets:
                list_ls = methods.GetLinkSet(i, db)
                for k in list_ls:
                    if(k not in ls_no_repeat):
                        ls_no_repeat.append(k)
    
            for i in ls_no_repeat:
                popularity = methods.Popularity_ls(i, list_tranning, db)
                dict_popularity[i] = popularity
                    
        #fazer com entidade, classe e propriedade    
        print("Exporting files")        
        dir_path = os.path.dirname(sys.modules['__main__'].__file__)+str("/Result/SN/Results"+str(rotation)+".txt")
        dir_path_ = os.path.dirname(sys.modules['__main__'].__file__)+str("/Result/SN/Quers"+str(rotation)+".txt")
        dir_path__ = os.path.dirname(sys.modules['__main__'].__file__)+str("/Result/SN/Relevants"+str(rotation)+".txt")
        list_Space_search = []
        with open(dir_path, 'a') as the_file:
            for i in list_datasets_sets:
                for name_dataset, set_list in i.items():
                    for k in set_list:
                        id_query = id_query + 1
                        dict_query[id_query] = name_dataset
                        similarity = methods.similaritySN(k, list_tranning, dict_popularity, num_linkset, num_class, num_proprety, num_types, db)
                        ordern = sorted(similarity.items(), key=lambda x: (-x[1], x[0]))
                        Space_search = methods.LastRelevant(name_dataset, ordern, db)
                        list_Space_search.append(Space_search)
                        the_file.write("Para Dataset : " + str(name_dataset))
                        the_file.write("\n")
                        the_file.write("Representação com : " + str(k))
                        the_file.write("\n")
                        for result in ordern:
                            the_file.write(str(result[0]) + "-----" + str(result[1]))
                            the_file.write("\n")
                            map.exportFiles(id_query, result[0], result[1], name_dataset, rotation, dir_path_)
                            
        the_file.close()
        map.exportrelevants(dict_query, rotation, list_tranning, db, dir_path__)
        return list_Space_search
                
            
                
                
        
        
        
        
        
        
        
    