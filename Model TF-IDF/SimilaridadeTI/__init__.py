import math
from decimal import Decimal

def similaridade(intercesao, uniao):
    if(intercesao == 0) or (uniao == 0):
        return 0
    else: 
        result = intercesao/uniao
        return result