'''
Created on 4 de mar de 2016

@author: Luiz Leme
'''
from datacrawler.profiler import db_name2


def aggregate_features(input_collection1='datasets_temp', input_collection2='matchings_temp', output_collection='datasets_temp_aggreg', output_collection2='datasets2_temp_aggreg', output_collection3='features_temp'):
    from pymongo import MongoClient
    with MongoClient() as client:
        DB2 = client[db_name2]
        
        _datasets2 = DB2[input_collection1]
        _datasets2.aggregate([
                              {'$unwind':{'path':'$tags', 'preserveNullAndEmptyArrays':True}}
                              , {'$unwind':{'path':'$features', 'preserveNullAndEmptyArrays':True}}
                              , {'$lookup':{'from':input_collection2, 'localField':'features.uri', 'foreignField':'name_uri', 'as': 'f_matching'}}
                              , {'$unwind':{'path':'$f_matching', 'preserveNullAndEmptyArrays':True}}
                              , {'$project':{'_id':0
                                            , 'matching':1
                                            , 'summary':1
                                            , 'tag':'$tags'
                                            , 'feature':{'uri':{'$ifNull':['$f_matching.matching', '$features.uri']}, 'type':'$features.type'}}}
                              , {'$group': {'_id': '$matching'
                                           , 'summaries': { '$addToSet': '$summary' }
                                           , 'tags': { '$addToSet': '$tag' }
                                           , 'features': { '$addToSet': '$feature'}}}
                              , {'$project':{'_id':0, 'matching':'$_id', 'summaries':1, 'tags':1, 'features':1}}
                              , {'$out':output_collection}
                              ], allowDiskUse=True)
        
        _datasets2 = DB2[output_collection]
        _datasets2.aggregate([{'$project':{'_id':0, 'matching':1, 'summaries':1}}
                              , {'$out':output_collection2}
                              ])
        
        _datasets2 = DB2[input_collection1]
        _datasets2.aggregate([{'$project': {'_id':0, 'features': '$features'}}
                              , {'$unwind': '$features'}
                              , {'$group':{'_id':'$features.uri'
                                           , 'type':{'$first':'$features.type'}}}
                              , {'$lookup':{'from':input_collection2, 'localField':'_id', 'foreignField':'name_uri', 'as': 'f_matching'}}
                              , {'$unwind':{'path':'$f_matching', 'preserveNullAndEmptyArrays':True}}
                              , {'$project':{'_id':0
                                            , 'id':{'$ifNull':['$f_matching.matching', '$_id']}
                                            , 'uri':'$_id'
                                            , 'type':'$type'}}
                              , {'$out':output_collection3}
                              ])
        
    return 'Dataset profiles have been created successfully.'


if __name__ == '__main__':
    # aggregate_features(input_collection1='datasets', input_collection2='matchings', output_collection='datasets_temp_aggreg')
    from pymongo import MongoClient
    with MongoClient() as client:
        DB2 = client[db_name2]
        print (sum([1 for m in DB2.datasets.find() for f in m['features'] if f['type'] == 'linkset' and not m['features']['matching']]))
