def convert(number, max_tfidf):
    return int((number * 65535) / max_tfidf)
    
def convertTest(lista, max_tfidf):
    lista_retorno = []
    for i in lista:
        if(i != 0):
            result = convert(i, max_tfidf)
            lista_retorno.append(result)
        else:
            lista_retorno.append(0)
    
    return lista_retorno

def convertTreinamento(lista_dictionary, max_tfidf):
    for i in lista_dictionary:
        for k in i:
            v = i[k]
            for m in v:
                posicao = v.index(m)
                del v[posicao] 
                result = convert(m, max_tfidf)
                v.insert(posicao, result)
    
    return lista_dictionary


    
    