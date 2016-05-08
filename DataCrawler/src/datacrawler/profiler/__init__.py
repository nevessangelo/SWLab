'''
Created on 29 de jan de 2016

@author: Luiz Leme
@email: lapaesleme@ic.uff.br
'''
import logging


logging.getLogger("root").setLevel(logging.ERROR)
logging.getLogger("requests").setLevel(logging.ERROR)
logging.getLogger("rdflib").setLevel(logging.ERROR)

db_name2 = 'dssearch'
collection_names2 = ['matchings', 'datasets', 'datasets2', 'features', 'prob_coocurr', 'prob_target', 'system.indexes']

def stats():
    from pymongo import MongoClient
    with MongoClient() as client:
        DB2 = client[db_name2]
        count = [r for r in DB2.datasets.aggregate([{'$project':{'_id':0, 'features':1}}
                                                    , {'$unwind':'$features'}
                                                    , {'$group': {'_id': '$features.uri', 'type': {'$first': '$features.type'}}}
                                                    , {'$group': {'_id':'$type', 'count': {'$sum':1}}}
                                                    , {'$project':{'_id':0, 'type':'$_id', 'count':1}}
                                                    ], allowDiskUse=True)]
        
        stats = ''
        stats = stats + '====================================================\n'
        stats = stats + '{} dataset documents currently stored.\n'
        stats = stats + '{} dataset2 documents currently stored.\n'
        stats = stats + '{} matching documents currently stored.\n'
        stats = stats + '{} feature documents currently stored.\n'
        stats = stats + '{} features currently stored.\n'
        stats = stats + '===================================================='
        stats = stats.format(DB2.datasets.count(), DB2.datasets2.count(), DB2.matchings.count(), DB2.features.count(), count)
    return stats


