import ConnectionMysql as connect
import Metodos


def GetFeatures(lista_teste, lista_treinamento):
    datasets_teste = []
    datasets_treinamento = []
    features = []
    all_ls  = []
    void = "void"
    rkbexplorer = "rkbexplorer"
    id = "id"
    aux = "http://www.johngoodwin.me.uk/family/"
    for i in lista_teste:
        nome_dataset = Metodos.GetNomeDataset(i)
        datasets_teste.append(nome_dataset)
    
    for i in lista_treinamento:
        nome_dataset = Metodos.GetNomeDataset(i)
        datasets_treinamento.append(nome_dataset)

        
    db = connect.conexaoMysql()
    cursor_ls = db.cursor()
    sql_ls = "SELECT features FROM `Features'` WHERE tipo_feature = 'Linkset'"
    try:
        cursor_ls.execute(sql_ls)
        result_ls  = cursor_ls.fetchall()
        for row_ls in result_ls:
            nome_linkset = row_ls[0]
            if(nome_linkset.find(aux) == -1):
                if(nome_linkset.find(id) == -1) or (nome_linkset.find(void) == -1):   
                    if(nome_linkset not in all_ls): 
                        all_ls.append(nome_linkset)
    
    except:
        print "Erro ao pegar todos os LS"
    
    
    for i in all_ls:
        if(i in datasets_teste):
            all_ls.remove(i)
            
    
    for i in all_ls:
        if(i in datasets_treinamento):
            features.append(i)
    
    
    return features
    
    

def RetiraLS(lista_ls, lista_treinamento):
    datasets_treinamento = []
    features = []
    for i in lista_treinamento:
        nome_dataset = Metodos.GetNomeDataset(i)
        datasets_treinamento.append(nome_dataset)
    
    
    for i in lista_ls:
        if(i in datasets_treinamento):
            features.append(i)
        
    return features
   # for i in lista_treinamento:
   #     nome_dataset = Metodos.GetNomeDataset(i)
   #     lista_ls_dataset = GetLinkSet(nome_dataset)
   #     for k in lista_ls_dataset:
   #         result = VerificaLSTreinamento(k, lista_treinamento)
   #         if(result == 0):
   #             if(k in lista_ls):
   #                 print "oi"
   #                 lista_ls.remove(k)
    
   # return lista_ls
            
    

def ConjuntoLS(lista_datasets,lista_treinamento):
    datasets_treinamento = []
    for i in lista_treinamento:
        nome_dataset = Metodos.GetNomeDataset(i)
        datasets_treinamento.append(nome_dataset)
        
    lista_retorno = []
    for i in lista_datasets:
        for nome_dataset, lista_ls in i.iteritems():
            for ls in lista_ls:
                if(ls not in lista_retorno) and (ls in datasets_treinamento):
                    lista_retorno.append(ls) 
    
    return lista_retorno


def ClassDataset(lista_treinamento, lista_teste):
    lista_retorno = []
    for i in lista_treinamento:
        nome_dataset = Metodos.GetNomeDataset(i)
        lista_class = Getproprety(nome_dataset)
        for k in lista_class:
            if(k not in lista_retorno):
                lista_retorno.append(k)
            
            
    for i in lista_teste:
            nome_dataset = Metodos.GetNomeDataset(i)
            lista_class = Getproprety(nome_dataset)
            for k in lista_class:
                if(k not in lista_retorno):
                    lista_retorno.append(k)
                
                
    return lista_retorno


def PropretyDataset(lista_treinamento, lista_teste):
    lista_retorno = []
    for i in lista_treinamento:
        nome_dataset = Metodos.GetNomeDataset(i)
        lista_proprety = Getproprety(nome_dataset)
        for k in lista_proprety:
            if(k not in lista_retorno):
                lista_retorno.append(k)
            
            
    for i in lista_teste:
            nome_dataset = Metodos.GetNomeDataset(i)
            lista_proprety = Getproprety(nome_dataset)
            for k in lista_proprety:
                if(k not in lista_retorno):
                    lista_retorno.append(k)
                
                
    return lista_retorno
        

def LSDataset(lista_treinamento, lista_teste):
    lista_dicionario = []
    for i in lista_treinamento:
            dicionario_datasets = {}
            nome_dataset = Metodos.GetNomeDataset(i)
            lista_ls = GetLinkSet(nome_dataset)
            for j in lista_ls:
                verifica = VerificaLSTeste(j, lista_teste)
                if(verifica == 1):
                    lista_ls.remove(j)
                
    
            dicionario_datasets[nome_dataset] = lista_ls
            lista_dicionario.append(dicionario_datasets)
    
        
    #for i in lista_teste:
    #        dicionario_datasets = {}
    #        nome_dataset = Metodos.GetNomeDataset(i)
    #        lista_ls = GetLinkSet(nome_dataset)
    #        dicionario_datasets[nome_dataset] = lista_ls
    #        lista_dicionario.append(dicionario_datasets)
        
    return lista_dicionario

def VerificaLSTreinamento(linkset, lista_treinamento):
    for i in lista_treinamento:
        nome_dataset = Metodos.GetNomeDataset(i)
        if(linkset == nome_dataset):
            return 1
        
    return 0

def VerificaLSTeste(linkset, lista_teste):
    for i in lista_teste:
        nome_dataset = Metodos.GetNomeDataset(i)
        if(linkset == nome_dataset):
            return 1
        
    return 0

def GetLinkSet(i):
    void = "void"
    rkbexplorer = "rkbexplorer"
    id = "id"
    aux = "http://www.johngoodwin.me.uk/family/"
    lista_ls = []
    db = connect.conexaoMysql()
    cursor_ls = db.cursor()
    sql_ls = "SELECT features FROM Features WHERE nome_dataset = '"+str(i)+"' AND tipo_feature = 'Linkset'"
    try:
        cursor_ls.execute(sql_ls)
        result_ls  = cursor_ls.fetchall()
        for row_ls in result_ls:
            nome_linkset = row_ls[0]
        #    vetor_nome_linkset = nome_linkset.split("/")
        #    if(void in vetor_nome_linkset):
        #        if(rkbexplorer in vetor_nome_linkset):
        #            nome_linkset = "http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/rest/dataset/"+str(vetor_nome_linkset[2])
        #            lista_ls.append(nome_linkset)
        #    else:
            if(nome_linkset.find(aux) == -1):
                if(nome_linkset.find(id) == -1) or (nome_linkset.find(void) == -1):    
                    lista_ls.append(nome_linkset)
            
    except:
        print"Erro ao pegar o LinkSet do dataset: " + str(i)
        
    db.close()
    return lista_ls



def GetClass(i):
    lista_class = []
    db = connect.conexaoMysql()
    cursor_class = db.cursor()
    sql_class = "SELECT features FROM Features WHERE nome_dataset = '"+str(i)+"' AND tipo_feature = 'Class'"
    try:
        cursor_class.execute(sql_class)
        result_class  = cursor_class.fetchall()
        for row_class in result_class:
            nome_class = row_class[0]
            lista_class.append(nome_class)
    except:
        print"Erro ao pegar o Class do dataset: " + str(i)
        
    db.close()
    return lista_class

def Getproprety(i):
    lista_proprety = []
    db = connect.conexaoMysql()
    cursor_proprety = db.cursor()
    sql_proprety = "SELECT features FROM Features WHERE nome_dataset = '"+str(i)+"' AND tipo_feature = 'Property'"
    try:
        cursor_proprety.execute(sql_proprety)
        result_proprety  = cursor_proprety.fetchall()
        for row_proprety in result_proprety:
            nome_proprety = row_proprety[0]
            lista_proprety.append(nome_proprety)
    except:
        print"Erro ao pegar o Proprety do dataset: " + str(i)
        
    db.close()
    return lista_proprety