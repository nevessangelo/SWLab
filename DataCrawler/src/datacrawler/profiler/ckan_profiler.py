'''
Created on 27 de fev de 2016

@author: lapaesleme
'''
from urllib.request import urlparse

from datacrawler.crawler import db_name
from datacrawler.profiler import db_name2
from datacrawler.profiler.matcher import get_matching


def _is_valid_uri(uri, _datasets):
    try: 
        if (urlparse(uri)[0] in ['http', 'https']):
            if (_datasets.find_one({'extras2.name_uri':uri})): 
                return True
        else: return False
    except: 
        return False

def _get_uri(_datasets, id_, cat):
    try: return _datasets.find_one({'id':id_, 'extras2.catalog_home':cat})['name']
    except: return id_
    
def extract_features(input_collection='datasets', output_collection='datasets_temp', matchings='matchings_temp'):
    import re
    from pymongo import MongoClient, ASCENDING

    with MongoClient() as client:
        DB = client[db_name]
        DB2 = client[db_name2]
        
        _datasets = DB[input_collection]
        _datasets2 = DB2[output_collection]
        _matchings = DB2[matchings]
        
        _datasets.create_index([('id', ASCENDING)])
        _datasets.create_index([('extras2.name_uri', ASCENDING)], unique=True)
    
        p = re.compile('.*links:.*', re.IGNORECASE)
        for metadata in _datasets.find():
                
            if ('extras' in metadata or 'relationshis' in metadata):
                extras = metadata['extras'] if ('extras' in metadata) else []
                relationships = metadata['relationships_as_subject'] if ('relationships_as_subject' in metadata) else [] 
                
                b = metadata['extras2']['catalog_dataset_api']
                linksets1 = [(b.format(e['key'][6:].strip()), 'linkset') for e in extras if p.match(e['key'])] 
                linksets2 = [(b.format(_get_uri(_datasets, r['__extras']['object_package_id'], metadata['extras2']['catalog_home'])), 'linkset') for r in relationships if r['type'] == 'links_to'] if relationships else []
                
                features = linksets1 + linksets2
                features = list(set(features))
                features = [{'uri':f[0], 'type':f[1]} for f in features if (_is_valid_uri(f[0], _datasets))]
                
                metadata_ = {}
                metadata_['matching'] = get_matching(_matchings, metadata['extras2']['name_uri'])
                metadata_['features'] = features
                if (features): _datasets2.insert_one(metadata_)
    
    return 'Dataset profiles have been created successfully.'
        



