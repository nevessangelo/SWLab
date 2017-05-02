import ConnectionMysql as connect
import random
import Metodos
import Features


def RepresentacaoConjunto(lista_teste, num_linkset, num_class, num_proprety, num_entites):
    lista_dicionarios = []
    res_final = []
    for i in lista_teste:
        dicionario = {}
        verifica = -1
        lista_ls = []
        nome_dataset = Metodos.GetNomeDataset(i)
        lista_ls = Features.GetLinkSet(nome_dataset)
        for j in lista_ls:
            verifica = Features.VerificaLSTeste(j, lista_teste)
            if(verifica == 1):
                aux = lista_ls.index(j)
                lista_ls.remove(j)
                    
        res = [] 
        samp = []
        samp2 = []
        samp3 = []
        while len(res) < 5: #5
            if(num_linkset > 0):
                samp = random.sample(lista_ls, 5)
            if(num_proprety > 0):
                lista_proprety = Features.Getproprety(nome_dataset)
                samp2 = random.sample(lista_proprety, 5)
                 
            if(num_class > 0):
                lista_class = Features.GetClass(nome_dataset)
                samp3 = random.sample(lista_class, 5)
                
            samp_final = samp + samp2 + samp3
            if(samp_final not in res):
                res.append(samp_final)
            
            
        dicionario[nome_dataset] = res
        lista_dicionarios.append(dicionario)
        
    return lista_dicionarios
        
                

        
    
    