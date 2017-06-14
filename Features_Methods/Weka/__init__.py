import Bank_Methods as methods
import Sets as set_


def ExportFileBinarioTest(vector_features, vetores_teste, dataset, dir_path_, id_query):
    file_name_vector = dataset.split("/")
    filename = file_name_vector[-1]
    output_filename = dir_path_+str(filename)+':'+str(id_query)+'.arff'
    with open(output_filename,"w") as fp:
        fp.write('''@RELATION features ''')
        fp.write("\n\n\n")
        for i in vector_features:
            get_feature = i.split("/")
            feature = get_feature[-1]
            fp.write("@ATTRIBUTE feature_"+str(feature) +" "+ "REAL")
            fp.write("\n")
        
        fp.write("@ATTRIBUTE class {0,1}")
        fp.write("\n\n\n")
        fp.write("@DATA")
        fp.write("\n")
        
        for x in vetores_teste:
            fp.write(str(x)+",")
            
        fp.write(str("?"))
        fp.write("\n")
    
    fp.close()

    

def ExportFilesBinario(vector_features, dataset, datasets_test, datasets_tranning, dict_tfidf_tranning_, rotation, dir_path, num_linkset, num_types, db):
    
    name_file_tranning = dataset.split("/")
    name_file = name_file_tranning[-1]
    
    output_filename = dir_path+str(name_file)+'.arff'
    with open(output_filename,"w") as fp:
        fp.write('''@RELATION features ''')
        fp.write("\n\n\n")
        for i in vector_features:
            get_feature = i.split("/")
            feature = get_feature[-1]
            fp.write("@ATTRIBUTE feature_"+str(feature) +" "+ "REAL")
            fp.write("\n")
        
        fp.write("@ATTRIBUTE class {0,1}")
        fp.write("\n\n\n")
        fp.write("@DATA")
        fp.write("\n")
        
        for i in datasets_tranning:
            list_representation = []
            representation_types = []
            representation_ls = []
            list_features_ls = []
            if(num_linkset > 0):
                list_ls = methods.GetLinkSet(i, db)
                if(dataset in list_ls):
                    list_ls.remove(dataset)
                
                for k in list_ls:
                    if(k in vector_features):
                        list_features_ls.append(k)
    
                    
                if(len(list_features_ls) >= 8):
                    representation_ls = set_.create_set(10, list_features_ls)
                
            #if(num_types > 0):
            #    list_types = methods.GetTypes(i, db)
            #    if(len(list_types) >= 8):
            #        representation_types = set_.create_set(10, list_types)
            
            list_representation = representation_ls + representation_types
            
            for k in list_representation:
                aux = []
                for j in range(len(vector_features)):
                    aux.append(0)
                    
                for feature in k:
                    vetor = dict_tfidf_tranning_[i]
                    posicao = vector_features.index(feature)
                    tf_idf = vetor[posicao]
                    del aux[posicao]
                    aux.insert(posicao, tf_idf)
                
                for x in aux:
                    fp.write(str(x)+",")
                
                boolean_linkset = methods.LSMAP(i,dataset, db)
                if(boolean_linkset > 0):
                    relevante = 1
                else:
                    relevante = 0
                    
                fp.write(str(relevante))
                fp.write("\n")
                    
            
                
                
                
               
        
    fp.close()
    