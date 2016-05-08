'''
Created on 29 de jan de 2016SS

@author: Luiz Leme
@email: lapaesleme@ic.uff.br
'''    
import logging
from datacrawler.profiler import db_name2, collection_names2

logging.basicConfig(filename='profiler.log', filemode='w', level=logging.INFO)
logging.info ('Data_Profiler version: 0.0.1')
        
def _finalize():
    from pymongo import MongoClient
    from pymongo import ASCENDING, TEXT, HASHED
    with MongoClient() as client:
        DB2 = client[db_name2]
        
        DB2.matchings_temp.create_index([('name_uri', ASCENDING)], unique=True)
        DB2.datasets_temp_aggreg.create_index([('matching', ASCENDING)], unique=True)
        DB2.datasets2_temp_aggreg.create_index([('matching', ASCENDING)], unique=True)
        DB2.features_temp.create_index([('uri', ASCENDING)], unique=True)
        DB2.prob_coocurr_temp.create_index([('f1', ASCENDING), ('f2', ASCENDING)], unique=True)
        DB2.prob_target_temp.create_index([('f2', ASCENDING)], unique=True)
        
        DB2.matchings_temp.create_index([('name_uri', HASHED)])
        
        DB2.datasets_temp_aggreg.create_index([('id', ASCENDING)])
        DB2.datasets_temp_aggreg.create_index([('summaries.name', ASCENDING)])
        DB2.datasets_temp_aggreg.create_index([('summaries.extras2.name_uri', ASCENDING)])
        DB2.datasets_temp_aggreg.create_index([('summaries.extras2.name_uri_3', ASCENDING)])
        DB2.datasets_temp_aggreg.create_index([('summaries.extras2.catalog_home', ASCENDING)])
        DB2.datasets_temp_aggreg.create_index([('summaries.name', TEXT), ('summaries.title', TEXT), ('summaries.notes', TEXT), ('tags.name', TEXT)])
        
        DB2.datasets2_temp_aggreg.create_index([('id', HASHED)])
        DB2.datasets2_temp_aggreg.create_index([('matching', HASHED)])
        # DB2.datasets2_temp_aggreg.create_index([('summaries.extras2.name_uri', ASCENDING)])
        # DB2.datasets2_temp_aggreg.create_index([('summaries.extras2.name_uri_3', ASCENDING)])
        
        DB2.features_temp.create_index([('uri', HASHED)])
        # DB2.features_temp.create_index([('id', ASCENDING)], unique=True)
        
        DB2.prob_coocurr_temp.create_index([('f1', HASHED)])
        DB2.prob_coocurr_temp.create_index([('f2', HASHED)])
        DB2.prob_target_temp.create_index([('f2', HASHED)])
        
        try:
            if ('matchings' in DB2.collection_names()): DB2.matchings.rename('matchings_old', dropTarget=True)
            if ('datasets' in DB2.collection_names()): DB2.datasets.rename('datasets_old', dropTarget=True)
            if ('datasets2' in DB2.collection_names()): DB2.datasets2.rename('datasets2_old', dropTarget=True)
            if ('features' in DB2.collection_names()): DB2.features.rename('features_old', dropTarget=True)
            if ('prob_coocurr' in DB2.collection_names()): DB2.prob_coocurr.rename('prob_coocurr_old', dropTarget=True)
            if ('prob_target' in DB2.collection_names()): DB2.prob_target.rename('prob_target_old', dropTarget=True)
            
            DB2.matchings_temp.rename('matchings', dropTarget=True)
            DB2.datasets_temp_aggreg.rename('datasets', dropTarget=True)
            DB2.datasets2_temp_aggreg.rename('datasets2', dropTarget=True)
            DB2.features_temp.rename('features', dropTarget=True)
            DB2.prob_coocurr_temp.rename('prob_coocurr', dropTarget=True)
            DB2.prob_target_temp.rename('prob_target', dropTarget=True)
            print ('New data have been generated.')
        except:
            print ('Error while repositioning to new collections.')
            print ('Rolling back collections...')
            if ('matchings_old' in DB2.collection_names()): DB2.matchings_old.rename('matchings', dropTarget=True)
            if ('datasets_old' in DB2.collection_names()): DB2.datasets_old.rename('datasets', dropTarget=True)
            if ('datasets2_old' in DB2.collection_names()): DB2.datasets2_old.rename('datasets2', dropTarget=True)
            if ('features_old' in DB2.collection_names()): DB2.features_old.rename('features', dropTarget=True)
            if ('prob_coocurr_old' in DB2.collection_names()): DB2.prob_coocurr_old.rename('prob_coocurr', dropTarget=True)
            if ('prob_target_old' in DB2.collection_names()): DB2.prob_target_old.rename('prob_target', dropTarget=True)
            print ('Done.')
         
        for c in DB2.collection_names():
            if (c not in collection_names2): DB2.drop_collection(c)

def _main(recreateDB=False):
    from datacrawler.profiler import stats
    from datacrawler.aggregator.aggregator import aggregate_features
    from datacrawler.search.prob_estimator import compute_prob
    
    print('Starting extraction of features...')
     
    print('Aggregating features...')
    print(aggregate_features(input_collection1='datasets_temp', input_collection2='matchings_temp', output_collection='datasets_temp_aggreg', output_collection2='datasets2_temp_aggreg', output_collection3='features_temp'))
    
    print('Computing probabilities...')
    print(compute_prob(input_collection='datasets_temp_aggreg', output_collection1='prob_coocurr_temp', output_collection2='prob_target_temp'))
    
    _finalize()
    print(stats())
    return 'Features have been extracted successfully.'

if __name__ == '__main__':
    print(_main(recreateDB=False))
