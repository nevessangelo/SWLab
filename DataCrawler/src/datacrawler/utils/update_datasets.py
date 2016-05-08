'''
Created on 11 de mar de 2016

@author: Luiz Leme
'''
from pymongo import MongoClient

from datacrawler.crawler import db_name

def _load_catalogs():
    from datacrawler.utils import load_data, get_proj_path
    return load_data(get_proj_path('dat', 'catalogs.json'), verbose=False)

if __name__ == '__main__':
    with MongoClient() as client:
        DB = client[db_name]
        _datasets = DB.datasets
        
        _catalogs = _load_catalogs()
        
        for c in _catalogs:
            DB.catalogs.insert_one(c)
        
        #for m in _datasets.find():
        #    cat = next((c for c in _catalogs if c['home'] == m['extras2']['catalog_home']))
            
        #    _datasets.update_one({'_id':m['_id']}, {'$set':{'extras2.name_web':cat['home'] + cat['dataset_wui'].format(m['name'])}})
        #    _datasets.update_one({'_id':m['_id']}, {'$set':{'extras2.catalog_name':cat['name']}})
