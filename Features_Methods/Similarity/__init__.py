import math
def cosseno(a, b):
    verifica_a = all(v == 0 for v in a)
    verifica_b = all(v == 0 for v in b)
    if(verifica_a or verifica_b):
        return 0
    else:
        return sum([i*j for i,j in zip(a, b)])/(math.sqrt(sum([i*i for i in a]))* math.sqrt(sum([i*i for i in b])))
    
def TI(a, b):
    return (a/float(b))

def SN(get_pa, get_ra,d):
    result_division = get_pa/float(d)
    return (get_ra + result_division)