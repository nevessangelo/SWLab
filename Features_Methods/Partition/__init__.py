import ConnectionMySQL as connection
import Bank_Methods as method
import Methods as method_
import collections


lista_parte1 = []
lista_parte2 = []
lista_parte3 = []
db = connection.conexaoMysql()

def Separate_Groups(list_datasets, num_linkset, num_class, num_proprety, num_categories):
    dict_datasets = {}
    for i in list_datasets:
        sum_ = 0
        qLS = 0
        qCL = 0
        qP = 0
        qType = 0
        
        name_dataset = method.GetNameDataset(i, db)
        
        if(num_linkset > 0):
            qLS = method.AmountLS(name_dataset, db)
                
        if(num_class > 0):
            qCL = method.AmountCL(name_dataset, db)
        
        if(num_proprety > 0):
            qP = method.AmountqP(name_dataset, db)
        
        if(num_categories > 0):
            qType = method.AmountqType(name_dataset, db)
            
            
        sum_ = qLS + qCL + qP + qType
        dict_datasets[i] = sum_
        
    dict_ordered = collections.OrderedDict(sorted(dict_datasets.items(), key=lambda t: t[1]))
    cont = 1
    for id_dataset, qLS in dict_ordered.items():
        flag = 0
        if(cont == 1 and flag == 0):
            lista_parte1.append(str(id_dataset))
            cont = cont + 1
            flag = 1
        if(cont == 2 and flag == 0):
            lista_parte2.append(str(id_dataset))
            cont = cont + 1
            flag = 1
        if(cont == 3 and flag == 0):
            lista_parte3.append(str(id_dataset))
            cont = 1
            flag = 1
    
    


def Partition(entrada, type):
    
    dict_datasets = {}
    list_lifescience = []
    list_publications = []
    list_geography = []
    list_socialweb = []
    list_government = []
    list_nongroup = []
    list_media = []
    list_user = []
    list_linguistic = []
    list_crossdomain = []
    
    list_datasets = method.Datasets(db, entrada)
    num_linkset = method.CheckLS(entrada, db)
    num_class  = method.CheckCLASS(entrada, db)
    num_proprety = method.CheckPROPRETY(entrada, db)
    num_categories = method.CheckCATEGORIES(entrada, db)
    
    for i in list_datasets:
        name_dataset = method.GetNameDataset(i, db)
        group = method.GetGroup(name_dataset, db)
        dict_datasets[i] = group
    
    for id_dataset, group in dict_datasets.items():
            if(group == "lifesciences"):
                list_lifescience.append(id_dataset)
            
            if(group == "publications"):
                list_publications.append(id_dataset)
            
            if(group == "geography"):
                list_geography.append(id_dataset)
            
            if(group == "socialweb"):
                list_socialweb.append(id_dataset)
            
            if(group == "government"):
                list_government.append(id_dataset)
                
            if(group == "non-group"):
                list_nongroup.append(id_dataset)
                
            if(group == "media"):
                list_media.append(id_dataset)
            
            if(group == "usergeneratedcontent"):
                list_user.append(id_dataset)
                
            if(group == "linguistics"):
                list_linguistic.append(id_dataset)
            
            if(group == "crossdomain"):
                list_crossdomain.append(id_dataset)
    
    
    if(len(list_lifescience) != 0):
        Separate_Groups(list_lifescience, num_linkset, num_class, num_proprety, num_categories)
    
    if(len(list_publications) != 0):
        Separate_Groups(list_publications, num_linkset, num_class, num_proprety, num_categories)
    
    if(len(list_geography) != 0):
        Separate_Groups(list_geography, num_linkset, num_class, num_proprety, num_categories)
    
    if(len(list_socialweb) != 0):
        Separate_Groups(list_socialweb, num_linkset, num_class, num_proprety, num_categories)
        
    if(len(list_government) != 0):
        Separate_Groups(list_government, num_linkset, num_class, num_proprety, num_categories)
    
    if(len(list_nongroup) != 0):
        Separate_Groups(list_nongroup, num_linkset, num_class, num_proprety, num_categories)
    
    if(len(list_media) != 0):
        Separate_Groups(list_media, num_linkset, num_class, num_proprety, num_categories)
    
    if(len(list_user) != 0):
        Separate_Groups(list_user, num_linkset, num_class, num_proprety, num_categories)
    
    if(len(list_linguistic) != 0):
        Separate_Groups(list_linguistic, num_linkset, num_class, num_proprety, num_categories)
    
    if(len(list_crossdomain) != 0):
        Separate_Groups(list_crossdomain, num_linkset, num_class, num_proprety, num_categories)
    
    
    print (len(lista_parte1))
    print (len(lista_parte2))
    print (len(lista_parte3))
    
    list_total_dataset = lista_parte1 + lista_parte2 + lista_parte3
    list_all_list_Space_search = []
    list_ = []
    list__ = []
    list___ = []
    
    #criar loop aqui
    
    
    desvio_padrao = 0
    if(type == "cosseno" or type == "TI" or type == "SN"):
        rotation = 1
        list_treinamento = lista_parte2 + lista_parte3
        list_ = method_.Methods(type, lista_parte1, list_treinamento, num_linkset, num_class, num_proprety, num_categories, db, rotation)
    
        rotation = 2
        list_treinamento_ = lista_parte1 + lista_parte2
        list__ = method_.Methods(type, lista_parte3, list_treinamento_, num_linkset, num_class, num_proprety, num_categories, db, rotation)
    
        rotation = 3
        list_treinamento_ = lista_parte1 + lista_parte3
        list___ = method_.Methods(type, lista_parte2, list_treinamento_, num_linkset, num_class, num_proprety, num_categories, db, rotation)
    
        list_all_list_Space_search = list_ + list__ + list___
        
        desvio_padrao = method.desvio_padrao(list_all_list_Space_search)
        print(desvio_padrao)
        
    else:
        rotation = 1
        list_treinamento = lista_parte2 + lista_parte3
        method_.Methods(type, lista_parte1, list_treinamento, num_linkset, num_class, num_proprety, num_categories, db, rotation)
    
        rotation = 2
        list_treinamento_ = lista_parte1 + lista_parte2
        method_.Methods(type, lista_parte3, list_treinamento_, num_linkset, num_class, num_proprety, num_categories, db, rotation)
    
        rotation = 3
        list_treinamento_ = lista_parte1 + lista_parte3
        method_.Methods(type, lista_parte2, list_treinamento_, num_linkset, num_class, num_proprety, num_categories, db, rotation)
        
    
    
    db.close()
    
    
