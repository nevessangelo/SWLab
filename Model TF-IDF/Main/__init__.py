#coding: utf-8

import Partition as partition


def entrada():
    print "\nSelecione: "
    print "\n1- Similaridade de Cosseno "
    print "2- Exportar Treinamento, Teste e Classes com Deep-Learning"
    
def case_1():
    type = 'cosseno'
    entrada = (input('Digite qual a entrada: '))
    partition.Partition(entrada,type)
def case_2():
    type = 'deep'
    entrada = (input('Digite qual a entrada: '))
    partition.Partition(entrada,type)
    
    
def case_defult():
    print "operação inválida"
    
dict = {1: case_1, 2: case_2}

def switch(x):
    try:
        dict[x]()
    except:
        case_defult()
        

if __name__ == '__main__':
    try:
        entrada()
        switch(input('Digite o valor desejado: '))
        print "Fim"
    except:
        print "O valor digitado não é um número"