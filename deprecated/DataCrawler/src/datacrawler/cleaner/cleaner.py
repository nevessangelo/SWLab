'''
Created on 2 de mar de 2016

@author: lapaesleme
'''
from datacrawler.crawler import db_name
from datacrawler.profiler import db_name2

def drop_unused_collections():
    from pymongo import MongoClient
    for name in MongoClient().database_names():
        if (not name in [db_name, db_name2, 'local']):
            MongoClient().drop_database(name)
            
    with MongoClient() as client:
        DB = client[db_name]
        for c in DB.collection_names():
                if (c not in ['deletions', 'datasets', 'voids', 'system.indexes']): DB.drop_collection(c)
    
    with MongoClient() as client:
        DB = client[db_name2]
        for c in DB.collection_names():
                if (c not in ['datasets', 'matchings', 'features', 'system.indexes']): DB.drop_collection(c)
            
    return 'Collections have been dropped.'

from statistics import mean
from itertools import starmap
def f11 (x, y): return (x - y).total_seconds()
def f1 (d): return {'name_uri':d['_id'], 'deltas':list(starmap(f11, zip(sorted(d['updates'][1:]), sorted(d['updates'][:-1]))))}
def f2 (d): return {'name_uri':d['name_uri'], 'deltas':d['deltas'], 'mean_delta':mean(d['deltas'])}
def clean_datasets():
    import os
    from datetime import datetime, timedelta
    from multiprocessing.pool import Pool
    from pymongo import MongoClient
    
    with MongoClient() as client:
        DB = client[db_name]
        _deletions = DB.deletions
        _datasets = DB.datasets
        _voids = DB.voids
        
        _deletions.delete_many({'last_update_at' : {'$lt':datetime.now() - timedelta(days=4)}})
        count1 = _datasets.count()  
        deletions = _deletions.aggregate([{"$group": {"_id": "$name_uri", "updates": {"$push": "$last_update_at"}}}])
        deletions = [d for d in deletions if len(d['updates']) >= 4]
        with Pool(os.cpu_count()) as p:
            deletions = p.map(f1, deletions)
            deletions = p.map(f2, deletions)
            deletions = [d['name_uri'] for d in deletions if d['mean_delta'] >= (3600 * 24 * 3 * 1.0)]
            for d in deletions:
                _datasets.delete_one({'extras2.name_uri' : d['name_uri']})  
                _voids.delete_one({'extras2.name_uri' : d['name_uri']})
                _deletions.delete_many({'name_uri' : d['name_uri']})
        count2 = _datasets.count()
        if (count1 - count2 > 0): print('{} datasets have been deleted.'.format(count1 - count2))
        
    return 'Cleaning has finished.'




    