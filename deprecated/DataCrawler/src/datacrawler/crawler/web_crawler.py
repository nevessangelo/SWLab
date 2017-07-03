'''
Created on 17 de fev de 2016

@author: Luiz Leme
@email: lapaesleme@ic.uff.br
'''
import logging
from datacrawler.crawler import db_name, INSERT, UPSERT, REPLACE, BEGINING, RECENTLY

def harvest_data(oper=INSERT, since=BEGINING):
    return 'Datasets have been updated.'