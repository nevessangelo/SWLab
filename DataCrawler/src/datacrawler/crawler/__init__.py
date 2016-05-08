'''
Created on 19 de jan de 2016

@author: Luiz Leme
@email: lapaesleme@ic.uff.br
'''
import logging

logging.getLogger("root").setLevel(logging.ERROR)
logging.getLogger("rdflib").setLevel(logging.ERROR)
logging.getLogger("requests").setLevel(logging.ERROR)

INSERT = 'INSERT'
UPSERT = 'UPSERT'
REPLACE = 'REPLACE'
BEGINING = '1000-01-01T00:00:00.000Z'
RECENTLY = '2016-01-01T00:00:00.000Z'

db_name = 'data_catalog'

def stats():
    from pymongo import MongoClient

    with MongoClient() as client:
        DB = client[db_name]
        _deletions = DB.deletionsFS
        _datasets = DB.datasets
        _voids = DB.voids
    
    stats = ''
    stats = stats + '================================================\n'
    stats = stats + '{} metadata documents currently stored.\n'
    stats = stats + '{} void documents currently stored.\n'
    stats = stats + '{} deletion documents currently stored.\n'
    stats = stats + '================================================'
    stats = stats.format(_datasets.count(), _voids.count(), _deletions.count())
    return stats




