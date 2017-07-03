'''
Created on 28 de fev de 2016

@author: lapaesleme
'''
from datacrawler.crawler import db_name
from datacrawler.profiler import db_name2

def get_matching(matchings, name_uri):
    matching = matchings.find_one({'name_uri': name_uri})
    return matching['matching'] if (matching) else None

from pymongo import MongoClient
def match(input_collection='datasets', output_collection='matchings_temp'):
    from pymongo import ASCENDING, HASHED
    from datacrawler.utils import token_similarity
    with MongoClient() as client:
        DB = client[db_name]
        DB2 = client[db_name2]
        
        _datasets = DB[input_collection]
        _matchings = DB2[output_collection + '_']
        _matchings2 = DB2[output_collection]
        
        _datasets.create_index([('name', HASHED)])
        _matchings.create_index([('name_uris', ASCENDING)])
        
        for m1 in _datasets.find({}, {'title':1, 'name':1, 'extras2.name_uri':1}):
            if (not _matchings.find_one({'name_uris': m1['extras2']['name_uri']})):
                match = False
                for m2 in _datasets.find({'name':m1['name']}, {'title':1, 'name':1, 'extras2.name_uri':1}):
                    if (m1['extras2']['name_uri'] != m2['extras2']['name_uri'] and token_similarity(m1['title'], m2['title']) > 0.9): 
                        _matchings.insert({'name_uris':[m1['extras2']['name_uri'], m2['extras2']['name_uri']]}); match = True; break
                if (not match):
                    _matchings.insert({'name_uris':[m1['extras2']['name_uri']]})

                
        _matchings.aggregate([{'$unwind':'$name_uris'}
                              , {'$project':{'_id':0, 'matching':'$_id', 'name_uri':'$name_uris'}}
                              , {'$out':output_collection}])
        _matchings2.create_index([('name_uri', HASHED)])
        
    return 'Dataset matching done.'
                
    
    
