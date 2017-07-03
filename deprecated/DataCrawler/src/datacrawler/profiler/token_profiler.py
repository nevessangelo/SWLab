'''
Created on 3 de mar de 2016

@author: lapaesleme
'''
from datacrawler.crawler import db_name
from datacrawler.profiler import db_name2
from datacrawler.profiler.matcher import get_matching


def extract_features(input_collection='datasets', output_collection='datasets_temp', matchings='matchings_temp'):
    from pymongo import MongoClient
    with MongoClient() as client:
        DB = client[db_name]
        DB2 = client[db_name2]
        
        _datasets = DB[input_collection]
        _datasets2 = DB2[output_collection]
        _matchings = DB2[matchings]
        
        for metadata in _datasets.find():
            metadata_ = {}
            metadata_['matching'] = get_matching(_matchings, metadata['extras2']['name_uri'])
            metadata_['summary'] = {}
            metadata_['summary']['name'] = metadata['name'] if ('name' in metadata) else None
            metadata_['summary']['title'] = metadata['title'] if ('title' in metadata) else None
            metadata_['summary']['notes'] = metadata['notes'] if ('notes' in metadata) else None
            metadata_['summary']['extras2'] = {}
            metadata_['summary']['extras2']['name_web'] = metadata['extras2']['name_web'] if ('extras2' in metadata and 'name_web' in metadata['extras2']) else None
            # metadata_['summary']['extras2']['name_uri'] = metadata['extras2']['name_uri'] if ('extras2' in metadata and 'name_uri' in metadata['extras2']) else None
            # metadata_['summary']['extras2']['name_uri3'] = metadata['extras2']['name_uri3'] if ('extras2' in metadata and 'name_uri3' in metadata['extras2']) else None
            metadata_['summary']['extras2']['catalog_name'] = metadata['extras2']['catalog_name'] if ('extras2' in metadata and 'catalog_name' in metadata['extras2']) else None
            metadata_['summary']['extras2']['catalog_home'] = metadata['extras2']['catalog_home'] if ('extras2' in metadata and 'catalog_home' in metadata['extras2']) else None
            metadata_['tags'] = metadata['tags'] if ('tags' in metadata) else []
            _datasets2.insert(metadata_)
        
    return 'Dataset profiles have been created successfully.'
