#coding: utf-8
import Features

def ExportFileBinario(lista_dicionarios, vetor_aux, dataset):
    nome_arquivo_vetor = dataset.split("/")
    nome_arquivo = nome_arquivo_vetor[-1]
        
    output_filename = '/home/angelo/Área de Trabalho/Binario/'+str(nome_arquivo)+'.arff'
    with open(output_filename,"w") as fp:
        fp.write('''@RELATION features ''')
        fp.write("\n\n\n")
        for i in vetor_aux:
            get_feature = i.split("/")
            feature = get_feature[-1]
            fp.write("@ATTRIBUTE feature_"+str(feature) +" "+ "REAL")
            fp.write("\n")
            
        fp.write("@ATTRIBUTE class {0,1}")
        fp.write("\n\n\n")
        fp.write("@DATA")
        fp.write("\n")
        
        for i in lista_dicionarios:
            for nome_dataset, representacao in i.iteritems():
                for k in representacao:
                    aux = []
                    for j in range(len(vetor_aux)):
                        aux.append(0)
                    
                    for feature in k:
                        
                        posicao = vetor_aux.index(feature)
        #                del aux[posicao]
        #                aux.insert(posicao, object)
                       
                    
                    
                    
            
        
    
    

def ExportFileWekaMultiClass(vetores_treinamento, vetor_aux):
    lista_classes = []
    for i in vetor_aux:
        get_classe = i.split("/")
        classe = get_classe[-1]
        lista_classes.append(classe)
    
    output_filename = '/home/angelo/Área de Trabalho/treinamento_2.arff'
    with open(output_filename,"w") as fp:
        fp.write('''@RELATION features ''')
        fp.write("\n\n\n")
        for i in vetor_aux:
            get_feature = i.split("/")
            feature = get_feature[-1]
            fp.write("@ATTRIBUTE feature_"+str(feature) +" "+ "REAL")
            fp.write("\n")
        
        fp.write("@ATTRIBUTE class {"+ ",".join(str(x) for x in lista_classes) + "}")
        fp.write("\n\n\n")
        fp.write("@DATA")
        fp.write("\n")
        
        for i in vetores_treinamento:
            for nome_dataset, vetor_features in i.iteritems():
                lista_ls = Features.GetLinkSet(nome_dataset)
                for k in lista_ls:
                    if(k in vetor_aux):
                        for x in vetor_features:
                            fp.write(str(x)+",")
                        
                        get_classe = k.split("/")
                        classe = get_classe[-1]
                        fp.write(str(classe))
                        fp.write("\n")
    


def ExportFileWeka(vetores_treinamento, vetor_aux):
    lista_classes = []
    for i in vetor_aux:
        get_classe = i.split("/")
        classe = get_classe[-1]
        lista_classes.append(classe)
    
    
    output_filename = '/home/angelo/Área de Trabalho/treinamento.arff'
    with open(output_filename,"w") as fp:
        fp.write('''@RELATION features ''')
        fp.write("\n\n\n")
        for i in vetor_aux:
            get_feature = i.split("/")
            feature = get_feature[-1]
            fp.write("@ATTRIBUTE feature_"+str(feature) +" "+ "REAL")
            fp.write("\n")
        
        fp.write("@ATTRIBUTE class {"+ ",".join(str(x) for x in lista_classes) + "}")
        fp.write("\n\n\n")
        fp.write("@DATA")
        fp.write("\n")
        
        for i in vetores_treinamento:
            for nome_dataset, vetor_features in i.iteritems():
                lista_ls = Features.GetLinkSet(nome_dataset)
                for k in lista_ls:
                    aux = []
                    for j in range(len(vetor_aux)):
                        aux.append(0)
                        
                    if(k in vetor_aux):
                        posicao = vetor_aux.index(k)
                        for j, val in  enumerate(vetor_features):
                            if(j != posicao):
                                del aux[j]
                                aux.insert(j, val)
                        
                        for x in aux:
                            fp.write(str(x)+",")
                        
                        get_classe = k.split("/")
                        classe = get_classe[-1]
                        fp.write(str(classe))
                        fp.write("\n")
                        
                #fp.write("\n")
                #fp.write(str(nome_dataset))
                #fp.write("\n")
                            
                       

                
    