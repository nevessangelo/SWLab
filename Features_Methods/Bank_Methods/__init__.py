import math
import Similarity as similarity
import Similarity

def Datasets(db, entrada):
    list_datasets = []
    cursor = db.cursor()
    sql = "SELECT DISTINCT a.id_dataset FROM `Dataset` AS a INNER JOIN `Entrada` AS b INNER JOIN `Features'` c ON a.nome_dataset = c.nome_dataset AND a.qE >= b.Entites AND a.qC >= b.classpartition AND a.qP >= b.propretypartition AND a.qLS >= b.Linkset AND a.qType >= b.type AND b.id_entrada = " + str(entrada) + ""
    try:
        cursor.execute(sql)
        results = cursor.fetchall()
        for row in results:
            list_datasets.append(str(row[0]))
    except:
        print ("Error fetching Datasets")
    
    cursor.close()
    return list_datasets


def CheckLS(entrada, db):
    cursor = db.cursor()
    sql = "SELECT LinkSet FROM `Entrada` WHERE id_entrada = '"+str(entrada)+"' " 
    try:
        cursor.execute(sql)
        results = cursor.fetchall()
        for row in results:
            num_linkset = row[0]
        
    except:
        print ("Error Checking LinkSet")
    
    cursor.close()
    return num_linkset

def CheckCLASS(entrada, db):
    cursor = db.cursor()
    sql = "SELECT classpartition FROM `Entrada` WHERE id_entrada = '"+str(entrada)+"' " 
    try:
        cursor.execute(sql)
        results = cursor.fetchall()
        for row in results:
            classpartiton = row[0]
        
    except:
        print ("Error Cheking Classpartition")
    
    cursor.close()
    return classpartiton

def CheckPROPRETY(entrada, db):
    cursor = db.cursor()
    sql = "SELECT propretypartition FROM `Entrada` WHERE id_entrada = '"+str(entrada)+"' " 
    try:
        cursor.execute(sql)
        results = cursor.fetchall()
        for row in results:
            propretypartition = row[0]
        
    except:
        print ("Error Cheking PropretyPartition")
        
    cursor.close()
    return propretypartition

def CheckCATEGORIES(entrada, db):
    cursor = db.cursor()
    sql = "SELECT Entites FROM `Entrada` WHERE id_entrada = '"+str(entrada)+"' " 
    try:
        cursor.execute(sql)
        results = cursor.fetchall()
        for row in results:
            categories = row[0]
        
    except:
        print ("Error Cheking Categories")
        
    cursor.close()
    return categories


def GetNameDataset(id, db):
    cursor = db.cursor()
    sql = "SELECT nome_dataset FROM Dataset WHERE id_dataset = '"+str(id)+"'"
    try:
        cursor.execute(sql)
        result = cursor.fetchall()
        for row in result:
            name_dataset = row[0]
    except:
        print ("Error fetching the Dataset name")
        
    cursor.close()
    return name_dataset

def GetGroup(name_dataset, db):
    cursor = db.cursor()
    sql = "SELECT group_dataset FROM Groups WHERE nome_dataset = '"+str(name_dataset)+"'"
    try:
        cursor.execute(sql)
        result  = cursor.fetchall()
        for row in result:
            group = row[0]
    except:
        "Error while selecting group"
    
    cursor.close()
    return group

def AmountLS(name_dataset, db):
    cursor = db.cursor()
    sql = "SELECT qLS FROM Dataset WHERE nome_dataset = '"+str(name_dataset)+"'"
    try:
        cursor.execute(sql)
        result  = cursor.fetchall()
        for row in result:
            qLS = row[0]
    except:
        "Error fetching LS amount from a dataset"
    
    cursor.close()
    return qLS

def AmountCL(name_dataset, db):
    cursor = db.cursor()
    sql = "SELECT qC FROM Dataset WHERE nome_dataset = '"+str(name_dataset)+"'"
    try:
        cursor.execute(sql)
        result  = cursor.fetchall()
        for row in result:
            qC = row[0]
    except:
        "Error fetching QC amount from a dataset"
    
    cursor.close()
    return qC

def AmountqP(name_dataset, db):
    cursor = db.cursor()
    sql = "SELECT qP FROM Dataset WHERE nome_dataset = '"+str(name_dataset)+"'"
    try:
        cursor.execute(sql)
        result  = cursor.fetchall()
        for row in result:
            qP = row[0]
    except:
        "Error fetching qP amount from a dataset"
    
    cursor.close()
    return qP

def AmountqType(name_dataset, db):
    cursor = db.cursor()
    sql = "SELECT qType FROM Dataset WHERE nome_dataset = '"+str(name_dataset)+"'"
    try:
        cursor.execute(sql)
        result  = cursor.fetchall()
        for row in result:
            qType = row[0]
    except:
        "Error fetching qType amount from a dataset"
    
    cursor.close()
    return qType

def GetLinkSet(name_dataset, db):
    void = "void"
    rkbexplorer = "rkbexplorer"
    id = "id"
    aux = "http://www.johngoodwin.me.uk/family/"
    list_ls = []
    cursor = db.cursor()
    sql = "SELECT features FROM Features WHERE nome_dataset = '"+str(name_dataset)+"' AND tipo_feature = 'Linkset'"
    try:
        cursor.execute(sql)
        result  = cursor.fetchall()
        for row in result:
            nome_linkset = row[0]
            
            if(nome_linkset.find(aux) == -1):
                if(nome_linkset.find(id) == -1) or (nome_linkset.find(void) == -1):    
                    list_ls.append(nome_linkset)
            
    except:
        print ("Erro ao pegar o LinkSet do dataset: " + str(name_dataset))
        
    cursor.close()
    return list_ls

def GetClass(name_dataset, db):
    list_class = []
    cursor = db.cursor()
    sql = "SELECT features FROM Features WHERE nome_dataset = '"+str(name_dataset)+"' AND tipo_feature = 'Class'"
    try:
        cursor.execute(sql)
        result  = cursor.fetchall()
        for row in result:
            nome_class = row[0]
            list_class.append(nome_class)
            
    except:
        print ("Erro ao pegar o class do dataset: " + str(name_dataset))
        
    cursor.close()
    return list_class

def GetProprety(name_dataset, db):
    list_proprety = []
    cursor = db.cursor()
    sql = "SELECT features FROM Features WHERE nome_dataset = '"+str(name_dataset)+"' AND tipo_feature = 'Proprety'"
    try:
        cursor.execute(sql)
        result  = cursor.fetchall()
        for row in result:
            nome_proprety = row[0]
            list_proprety.append(nome_proprety)
            
    except:
        print ("Erro ao pegar o Proprety do dataset: " + str(name_dataset))
        
    cursor.close()
    return list_proprety

def GetTypes(name_dataset, db):
    list_types = []
    cursor = db.cursor()
    sql = "SELECT features FROM Features WHERE nome_dataset = '"+str(name_dataset)+"' AND tipo_feature = 'Types'"
    try:
        cursor.execute(sql)
        result  = cursor.fetchall()
        for row in result:
            nome_types = row[0]
            list_types.append(nome_types)
            
    except:
        print ("Erro ao pegar o Types do dataset: " + str(name_dataset))
        
    cursor.close()
    return list_types


def LSTest(linkset, list_teste, db):
    for i in list_teste:
        #name_dataset = GetNameDataset(i, db)
        if(linkset == i):
            return 1
        
    return 0

def create_features_ls(list_treinamento, db):
    all_ls = []
    cursor = db.cursor()
    sql = "SELECT DISTINCT features FROM `Features` WHERE tipo_feature = 'Linkset'"
    try:
        cursor.execute(sql)
        result  = cursor.fetchall()
        
        for row in result:
            name_linkset = row[0]
            if(name_linkset in list_treinamento):
                all_ls.append(name_linkset)
           
                        
    except:
        print ("Error fetching all LS")
    
    print(len(all_ls))
    return all_ls
    #for i in list_treinamento:
    #    if(i in all_ls):
    #        features.append(i)
            
    #for i in all_ls:
    #    if(i in list_treinamento):
    #        features.append(i)
    
     
    #return features

def create_features_types(list_parte1, list_treinamento, db):
    list_types = []
    cursor = db.cursor()
    sql = "SELECT features, name_dataset FROM `Features'` WHERE tipo_feature = 'Types'"
    try:
        cursor.execute(sql)
        result  = cursor.fetchall()
        for row in result:
            name_type = row[0]
            if(row[1] in list_treinamento):
                list_types.append(name_type)
    
    except:
        print ("Error fetching all Types")
    
    cursor.close()
    return list_types
        
    
    
    
def Vector_Tranning_Features(list_parte1, list_treinamento, num_linkset, num_class, num_proprety, num_types, db):
    list_ls_features = []
    list_types_featues = []
    
    if(num_linkset > 0):
        list_ls_features = create_features_ls(list_treinamento, db)
    
    if(num_types > 0):
        list_types_featues = create_features_types(list_parte1, list_treinamento, db)
        
    #fazer com class, proprety, categorias
    final_features = list_ls_features + list_types_featues
    return final_features

def tf(name_dataset, feature, db):
    tf = 0
    cursor = db.cursor()
    sql = "SELECT `frequencia`, `dataset_size`, `features` FROM `Features'` WHERE nome_dataset = '"+str(name_dataset)+"' AND features = '"+str(feature)+"'"
    try:
        cursor.execute(sql)
        results = cursor.fetchall()
        linhas = cursor.rowcount
        #if(linhas == 1):
        for row in results:
            frequencia = row[0]
            dataset_size = row[1]
            tf = frequencia / float(dataset_size)
        
        #else:
        #    tf = 0
    except:
        print ("Error fetching tf from dataset" + str(name_dataset))
    
    cursor.close()
    return tf
    
def calucloidf_tranning(feature, datasets_tranning, datasets_test, db):
    cursor = db.cursor()
    cont = 0
    sql = "SELECT nome_dataset FROM `Features` WHERE features = '"+str(feature)+"' "
    try:
        cursor.execute(sql)
        results = cursor.fetchall()
        for row in results:
            if(row[0] not in datasets_test):
                cont = cont + 1
        
        #idf = math.log(datasets_tranning/float(cont))
        idf = (datasets_tranning/float(cont))
    
    except:
        print ("Error compiling training idf")
    
    cursor.close()
    return idf
                
        
        

def create_tfidf_tranning(datasets_tranning, size_vector, vector_features, datasets_test, num_linkset, num_class, num_proprety, num_types, db):
    size_tranning = len(datasets_tranning)
    list_return = []
    for i in datasets_tranning:
        dict_return = {}
        if(num_linkset > 0):
            ls = GetLinkSet(i, db)
            
        #fazer com class, proprety, categorias
        list_final = ls
        vetor_dataset = []
        for j in range(len(vector_features)): 
            vetor_dataset.append(0)
        
        for k in list_final:
            if(k in vector_features):
                posicao = vector_features.index(k)
                del vetor_dataset[posicao]
                valor_tf = tf(i, k, db)
                valor_idf = calucloidf_tranning(k, size_tranning, datasets_test, db)
                tf_idf = (valor_tf * valor_idf)
                #print (tf_idf)
                vetor_dataset.insert(posicao, tf_idf)
            
        dict_return[i] = vetor_dataset
        list_return.append(dict_return)
        
    
    return list_return
        
def calucloidf_teste(i, total_datasets, db):
    cursor = db.cursor()
    sql = "SELECT count(nome_dataset) as numero FROM `Features` WHERE  features = '"+str(i)+"' "
    try:
        cursor.execute(sql)
        results = cursor.fetchall()
        for row in results:
            quantidade_dataset = row[0]
        
        #idf = math.log(total/float(quantidade_dataset))
        idf = (total_datasets/float(quantidade_dataset))
    
    except:
        print ("Erro ao calcular idf do teste")
    
    cursor.close()
    return idf
            
def vetores_teste(nome_dataset, k, size_vector, vector_features, total_datasets, db):
    dict_return = {}
    vetor_dataset = []
    for j in range(size_vector):
        vetor_dataset.append(0)
    
    for i in k:
        if(i in vector_features):
            posicao = vector_features.index(i)
            del vetor_dataset[posicao]
            valor_tf = tf(nome_dataset, i, db)
            valor_idf = calucloidf_teste(i, total_datasets, db)
            #if(valor_tf == 0):
            #    tf_idf = 0
            #    vetor_dataset.insert(posicao, tf_idf)
            #else:
            
            tf_idf = float(valor_tf * valor_idf)
            vetor_dataset.insert(posicao, tf_idf)
    
    return vetor_dataset  
    
    
def similary(vetores_teste, vetores_treinamento):
    dict = {}
    for i in vetores_treinamento:
        for nome_treinamento, vetor in i.items():
            result = similarity.cosseno(vetor, vetores_teste)
            dict[nome_treinamento] = result
    
    return dict

def LSMAP(name_dataset, verificaLS, db):
    result = 0
    cursor = db.cursor()
    sql = "SELECT features FROM `Features'` WHERE nome_dataset = '"+str(name_dataset)+"' AND features = '"+str(verificaLS)+"' AND tipo_feature = 'Linkset'"
    try:
        cursor.execute(sql)
        result  = cursor.rowcount
    except:
        "Erro"
        
    cursor.close()
    return result

def Prob_ls(feature, list_tranning, db):
    sample_space = len(list_tranning)
    event = 0
    cursor = db.cursor()
    sql = "SELECT nome_dataset FROM Features WHERE features = '"+str(feature)+"' AND tipo_feature = 'Linkset'"
    try:
        cursor.execute(sql)
        result  = cursor.fetchall()
        for row in result:
            if(row[0] in list_tranning):
                event = event + 1
    
    except:
        print ("Error calculating probability") 
    
    cursor.close()
    prob = float(event)/float(sample_space)
    return prob

def getFeaturesST(names_treinamento, set_list ,db):
    list_retorno = []
    cursor = db.cursor()
    for k in set_list:
        sql = "SELECT nome_dataset FROM Features WHERE features = '"+str(k)+"'"
        try:
            cursor.execute(sql)
            result  = cursor.fetchall()
            for row in result:
                if(row[0] in names_treinamento):
                    if(row[0] not in list_retorno):
                        list_retorno.append(row[0])
        except:
            print ("Error")
            
    cursor.close()
    return list_retorno 
    
            
    
    
def ra(list_features, dict_popularity):
    sum_ = 0
    somatorio = 0
    for k in list_features:
        if(k in dict_popularity):
            get_popularity = dict_popularity[k]
        else:
            get_popularity = 0
            
        if(get_popularity == 0):
            result = 0
        else:
            result = 1/float(get_popularity)
            
        somatorio = result + sum_
        sum_ = somatorio
    
    return somatorio
            
def DatasetsObjetcTarget(list_datasets_features, k, db):
    list_retorno = []
    for i in list_datasets_features:
        list_ls = GetLinkSet(i, db)
        if(k in list_ls):
            list_retorno.append(i)
                
    return list_retorno


def similaritySN(set_list, names_treinamento, dict_popularity, num_linkset, num_class, num_proprety, num_types, db):
    list_datasets_features = getFeaturesST(names_treinamento, set_list, db)
    d = len(names_treinamento)
    dict_similarity = {}
    for k in names_treinamento:
        if(k in dict_popularity):
            get_pa = dict_popularity[k]
        else:
            get_pa = 0
            
        if(num_linkset > 0):
            list_ls = DatasetsObjetcTarget(list_datasets_features,k, db)
        #fazer pra class, entidades e propriedades
        
        list_features = list_ls
        get_ra = ra(list_features, dict_popularity)
                
        result_similarity = similarity.SN(get_pa, get_ra,d)
        dict_similarity[k] = result_similarity
        
    return dict_similarity
            
        
        
        
        
        

def similarityTI(set_list, names_treinamento, dict_prob, num_linkset, num_class, num_proprety, num_types, db):
    dict_similarity = {}
    list_features = []
    list_ls = []
    list_class = []
    list_proprety = []
    list_types = []
    sum = 0
    for k in set_list:
        get_prob = dict_prob[k]
        if(get_prob == 0):
            log_prob = 0
        else:
            log_prob = math.log(get_prob, 2)
        
        union_test = log_prob + sum
        sum = union_test
    
    sum_ = 0
    for k in names_treinamento:
        multi= 0
        inter = 0
        if(num_linkset > 0):
            list_ls = GetLinkSet(k, db)
        
        if(num_class > 0):
            list_class = []
        
        if(num_proprety > 0):
            list_proprety = []
        
        if(num_types > 0):
            list_types = []
        
        list_features = list_ls + list_class + list_proprety + list_types
        for i in set_list:
            if(i in list_features):
                get_prob = dict_prob[i]
                if(get_prob == 0):
                    log_prob = 0
                else:
                    log_prob = math.log(get_prob, 2)
                    inter = log_prob + multi
                    multi = inter
        
        for i in list_features:
            get_prob = dict_prob[i]
            if(get_prob == 0):
                log_prob = 0
            else:
                log_prob = math.log(get_prob, 2)
            
            union_tranning = log_prob + sum_
            sum_ = union_tranning
        
        union = union_tranning + union_test
        result = similarity.TI(inter , union)
        dict_similarity[k] = result
    
    return dict_similarity
            
            
        
def create_tfidf_tranning_binario(datasets_tranning, size_vector, vector_features, datasets_test, num_linkset, num_types, db):
    size_tranning = len(datasets_tranning)
    dict_return = {}


    
    for i in datasets_tranning:
        vetor_dataset = []
        list_ls = []
        list_types = []
        list_features = []
       
        
        if(num_linkset > 0):
            list_ls = GetLinkSet(i, db)
        
        if(num_types > 0):
            list_types = GetTypes(i, db)
        
        list_features = list_ls + list_types
       
        
        for j in range(size_tranning): 
            vetor_dataset.append(0)
        
        if(len(list_features) != 0):
            for k in list_features:
                if(k in vector_features):
                    posicao = vector_features.index(k)
                    del vetor_dataset[posicao]
                    valor_tf = tf(i, k, db)
                    valor_idf = calucloidf_tranning(k, size_tranning, datasets_test, db)
                    tf_idf = valor_tf * valor_idf
                    vetor_dataset.insert(posicao, tf_idf) 
        
        dict_return[i] = vetor_dataset
    
    return dict_return
    
def LastRelevant(dataset_test, ordern, db):
    total_rank = 0
    posicao = 1
    for dataset in ordern:
        total_rank = total_rank + 1
        relevante = LSMAP(dataset_test, dataset[0], db)
        if(relevante == 1):
            posicao = total_rank
        
    
    return posicao/float(total_rank)
         
def somar(valores):
    soma = 0
    for v in valores:
        soma += v
        
    return soma

def media(valores):
    soma = somar(valores)
    qtd_elementos = len(valores)
    media = soma / float(qtd_elementos)
    return media

def variancia(valores):
    _media = media(valores)
    print(_media)
    soma = 0
    _variancia = 0
    for valor in valores:
        soma += math.pow( (valor - _media), 2)
                
    _variancia = soma / float( len(valores) )
    return _variancia

def desvio_padrao(valores):
    return math.sqrt( variancia(valores) )
    

def Documents(rotation, datasets_tranning, db):
    cursor = db.cursor()
    for i in datasets_tranning:
        
        try:
            cursor.execute("""INSERT INTO Results VALUES (%s,%s,%s)""",(str(rotation),str(i), 0))
            db.commit()
        except:
            db.rollback()
            
    
    cursor.close()

def Popularity_ls(ls, list_tranning, db):
    cont = 0
    cursor = db.cursor()
    sql = "SELECT nome_dataset FROM Features WHERE features = '"+str(ls)+"' AND tipo_feature = 'Linkset'"
    try:
        cursor.execute(sql)
        result  = cursor.fetchall()
        for row in result:
            if(row[0] in list_tranning):
                cont = cont + 1
    except:
        print ("Error calculating Popularity_ls") 
    
    cursor.close()
    return cont
    
    
    
            