import math
from decimal import Decimal

def similaridade(intercesao, uniao):
    if(intercesao == 0) or (uniao == 0):
        return 0
    else: 
        print intercesao
        print uniao
        #result = math.log(intercesao) / math.log(uniao)
        result = math.log(intercesao) - math.log(uniao)
        return result