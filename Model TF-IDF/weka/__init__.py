#coding: utf-8
import Features
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
        
        #for i in vetores_treinamento:
        #    for nome_dataset, vetor_features in i.iteritems():
        #        lista_ls = Features.GetLinkSet(nome_dataset)
        #        for j in lista_ls:
        #            vetor_print = vetor_features
        #            if(j in vetor_aux):
        #                posicao = vetor_aux.index(j)
        #                del vetor_print[posicao]
        #                vetor_print.insert(posicao, 0)
        #                for k in vetor_print:
        #                    fp.write(str(k)+",")
        #                
        #                get_classe = j.split("/")
        #                classe = get_classe[-1]
        #                fp.write(str(classe))
        #                fp.write("\n")
        #                
        #        fp.write("\n")
        #        fp.write(str(nome_dataset))
        #        fp.write("\n")
                            
                       

                
    