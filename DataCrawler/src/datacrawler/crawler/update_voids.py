'''
Created on 2 de mar de 2016

@author: lapaesleme
'''
import logging
from datacrawler.crawler import db_name, INSERT, REPLACE

import re
from datetime import datetime
from datacrawler.utils import parse_rdf
def _get_void(_datasets, _voids, _id, oper=INSERT):
    metadata = _datasets.find_one({'_id':_id})
    name = metadata['name']
    name_uri = metadata['extras2']['name_uri']
    name_uri_3 = metadata['extras2']['name_uri_3']
    void = _voids.find_one({'extras2.name_uri':name_uri})
    void = void if(void) else {'name':name, 'extras2':{'name_uri': name_uri, 'name_uri_3': name_uri_3}, 'resources':[]}
    
    updated = False
    updated_resources = []        
    p = re.compile('.*void.*', re.IGNORECASE)
    urls = [r['url'] for r in void['resources']]
    get_resource = lambda url: next((r for r in void['resources'] if r['url'] == url), None)
    
    for r in metadata['resources']:
        if (oper == INSERT and r['url'] in urls): updated_resources.append(get_resource(r['url'])); continue
        if (('description' in r and p.match(r['description'])) or ('format' in r and p.match(r['format'])) or ('url' in r and p.match(r['url']))):
            v_ = parse_rdf(r['url'])
            r_ = {'url':r['url'], 'void':v_, 'last_modified_at':str(datetime.now().strftime('%Y-%m-%d %H:%M:%S.%f'))} if (v_) else None 
            r_ = r_ if (r_) else get_resource(r['url'])
            updated_resources.extend([r_] if (r_) else [])
            updated = True if (v_) else v_
    updated = True if (updated or len(void['resources']) != len(updated_resources)) else updated        
    void['resources'] = updated_resources 
    
    return updated, void
    
def update_voids(oper=INSERT):
    import concurrent.futures
    from concurrent.futures import ThreadPoolExecutor
    from pymongo import MongoClient
    from pymongo import ASCENDING
    
    with MongoClient() as client:
        DB = client[db_name]
        DB.voids.create_index([('extras2.name_uri', ASCENDING)], unique=True)
            
        _datasets = DB.datasets
        _voids = DB.voids
        
        futures = []
        executor = ThreadPoolExecutor(max_workers=20)
        for metadata in _datasets.find({'$or':[{'resources.description':{'$regex' : '.*void.*', '$options': 'i'}},
                                               {'resources.format':{'$regex' : '.*void.*', '$options': 'i'}},
                                               {'resources.url':{'$regex' : '.*void.*', '$options': 'i'}},
                                               ]}, {'_id':1}):
            futures.append(executor.submit(_get_void, _datasets, _voids, metadata['_id'], oper))    
        try:
            for future in concurrent.futures.as_completed(futures, timeout=600):              
                try: 
                    
                    updated, void = future.result()
                    if (void['resources']):
                        if (oper == REPLACE or updated):
                            _voids.replace_one({'extras2.name_uri' : void['extras2']['name_uri']}, void, upsert=True)  
                            logging.info('({}) Dataset: {}, VoIDs: {}.'.format(oper, void['extras2']['name_uri'], [r['url'] for r in void['resources']]))
                    else:
                        void = _voids.find_one_and_delete({'extras2.name_uri' : void['extras2']['name_uri']})
                        if (void): logging.info('({}) Void {} deleted.'.format(oper, void['extras2']['name_uri'], [r['url'] for r in void['resources']]))
                        
                except Exception as e: logging.error('Unexpected error: {}.'.format(e))
                except: logging.error('Unexpected error: ***.')
        except Exception: logging.error('Timeout reached while waiting for a new void document.')
        except: logging.error('Timeout reached while waiting for a new void document.')
        
        print('({}) Shutting down update_voids thread pool...'.format(oper))
        try: executor.shutdown(wait=False)
        except Exception: logging.error('Timeout error while shutting down process pool.')
        except: logging.error('Timeout error while shutting down process pool.')
    
    return 'VoIDs have been updated.'