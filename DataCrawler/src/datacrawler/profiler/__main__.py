'''
Created on 29 de jan de 2016SS

@author: Luiz Leme
@email: lapaesleme@ic.uff.br
'''    
import logging
from datacrawler.profiler import db_name2, collection_names2

logging.basicConfig(filename='profiler.log', filemode='w', level=logging.INFO)
logging.info ('Data_Profiler version: 0.0.1')

def _initialize(recreateDB=False):
    from pymongo import MongoClient
    with MongoClient() as client:
        DB2 = client[db_name2]
        
        if (recreateDB): MongoClient().drop_database(db_name2); print('DB has been recreated.')
        for c in DB2.collection_names():
            if (c not in collection_names2): DB2.drop_collection(c)

def _main(recreateDB=False):
    from datacrawler.profiler import stats
    from datacrawler.profiler.matcher import match
    from datacrawler.profiler.token_profiler import extract_features as extract_text_features
    from datacrawler.profiler.ckan_profiler import extract_features as extract_features_from_ckan
    from datacrawler.profiler.void_profiler import extract_features as extract_features_from_void
    from datacrawler.profiler.topic_profiler import extract_features as extract_topic_features 
    
    print('Starting extraction of features...')
    _initialize(recreateDB=recreateDB)
     
    print('Matching datasets...')
    print(match(input_collection='datasets', output_collection='matchings_temp')) 
     
    print('Extracting text features...')
    print(extract_text_features(input_collection='datasets', output_collection='datasets_temp', matchings='matchings_temp'))
    
    print('Extracting features from CKAN...')
    print(extract_features_from_ckan(input_collection='datasets', output_collection='datasets_temp', matchings='matchings_temp'))
    
    print('Extracting features from VoIDs...')
    print(extract_features_from_void(input_collection1='voids', input_collection2='datasets', output_collection='datasets_temp', matchings='matchings_temp'))
    
    print('Extracting topics...')
    print(extract_topic_features(input_collection1='voids', input_collection2='datasets', output_collection='datasets_temp', matchings='matchings_temp'))
    
    print(stats())
    return 'Features have been extracted successfully.'

if __name__ == '__main__':
    print(_main(recreateDB=False))
