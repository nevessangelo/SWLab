'''
Created on 9 de fev de 2016

@author: Luiz Leme
@email: lapaesleme@ic.uff.br
'''
from datacrawler.crawler import stats
from datacrawler.cleaner.cleaner import clean_datasets, drop_unused_collections

if __name__ == '__main__':
    print(drop_unused_collections())
    print(clean_datasets())
    print(stats())
