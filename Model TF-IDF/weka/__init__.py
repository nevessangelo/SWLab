#coding: utf-8
import Features
def ExportFileWeka(vetores_treinamento, vetor_aux):
    output_filename = '/home/angelo/Área de Trabalho/treinamento.arff'
    with open(output_filename,"w") as fp:
        cont = 1
        fp.write('''@RELATION features ''')
        fp.write("\n\n\n")
        for i in vetor_aux:
            fp.write("@ATTRIBUTE feature_"+str(cont) +" "+ "float")
            fp.write("\n")
            cont = cont + 1
        
        fp.write("@ATTRIBUTE class string")
        fp.write("\n\n\n")
        fp.write("@DATA")
        fp.write("\n")
        for i in vetores_treinamento:
            for nome_dataset, vetor_features in i.iteritems():
                lista_ls = Features.GetLinkSet(nome_dataset)
                for j in lista_ls:
                    for k in vetor_features:
                        fp.write(str(k)+",")
                    
                    fp.write(str(j))
                    fp.write("\n")
                
    