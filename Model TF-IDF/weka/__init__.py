#coding: utf-8
import Features
def ExportFileWeka(vetores_treinamento, vetor_aux, lista_ls_selecionados):
    lista_classes = []
    for i in lista_ls_selecionados:
        get_classe = i.split("/")
        classe = get_classe[-1]
        lista_classes.append(classe)
    
    print()
    
    output_filename = '/home/angelo/Área de Trabalho/treinamento.arff'
    with open(output_filename,"w") as fp:
        cont = 1
        fp.write('''@RELATION features ''')
        fp.write("\n\n\n")
        for i in vetor_aux:
            fp.write("@ATTRIBUTE feature_"+str(cont) +" "+ "REAL")
            fp.write("\n")
            cont = cont + 1
        
        fp.write("@ATTRIBUTE class {"+ ",".join(str(x) for x in lista_classes) + "}")
        fp.write("\n\n\n")
        fp.write("@DATA")
        fp.write("\n")
        for i in vetores_treinamento:
            for nome_dataset, vetor_features in i.iteritems():
                lista_ls = Features.GetLinkSet(nome_dataset)
                for j in lista_ls:
                    if(j in lista_ls_selecionados):
                        print j
                        print vetor_features
                        for k in vetor_features:
                            fp.write(str(k)+",")
                            
                        get_classe = j.split("/")
                        print get_classe
                        classe = get_classe[-1]
                        print classe
                        fp.write(str(classe))
                        fp.write("\n")

                
    