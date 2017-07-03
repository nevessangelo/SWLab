'''
Created on 5 de mar de 2016

@author: lapaesleme
'''
from pymongo import HASHED
from pymongo import MongoClient

from datacrawler.profiler import db_name2


def compute_prob(input_collection='datasets_temp_aggreg', output_collection1='prob_coocurr_temp', output_collection2='prob_target_temp'):
    with MongoClient() as client:
        DB2 = client[db_name2]
        
        DB2[input_collection].aggregate([
                                         {'$project': {'_id':0, 'features': '$features', 'features2': '$features'}} 
                                         , {'$unwind': '$features2'}
                                         , {'$match' : {'features2.type':'linkset'}}
                                         , {'$unwind': '$features'}
                                         , {'$project':{'features':1, 'features2':1, 'test':{'$ne':['$features.uri', '$features2.uri']}}}
                                         , {'$match' : {'test':True}}
                                         , {'$group':{'_id':{'f1':'$features.uri', 'f2':'$features2.uri'}, 'count':{'$sum':1}}}
                                         , {'$project': {'_id':0, 'f1':'$_id.f1', 'f2':'$_id.f2', 'count': '$count' }}
                                         , {'$out':'temp1'}
                                         ], allowDiskUse=True)
        DB2.temp1.aggregate([{'$group':{'_id':'$f2', 'count':{'$sum':'$count'} }}
                             , {'$project': {'_id':0, 'f2':'$_id', 'count': '$count' }}
                             , {'$out':'temp2'}], allowDiskUse=True)
        DB2.temp2.create_index([('f2', HASHED)])
        DB2.temp2.aggregate([{'$group':{'_id':None, 'count':{'$sum':'$count'} }}
                             , {'$project': {'_id':0, 'count': '$count' }}
                             , {'$out':'temp3'}], allowDiskUse=True)
        DB2.temp1.aggregate([{'$lookup':{'from':'temp2', 'localField':'f2', 'foreignField':'f2', 'as': 'groupTotal'}}
                            , {'$unwind': '$groupTotal'}
                            , {'$project': {'_id':0, 'f1':1, 'f2':1, 'count1': '$count', 'count2': '$groupTotal.count' , 'prob':{'$divide': [ '$count', '$groupTotal.count' ]}}}
                            , {'$out':output_collection1}], allowDiskUse=True)
        DB2.temp2.aggregate([{'$lookup':{'from':'temp3', 'localField':'null', 'foreignField':'null', 'as': 'groupTotal'}}
                            , {'$unwind': '$groupTotal'}
                            , {'$project': {'_id':0, 'f1':1, 'f2':1, 'count1': '$count', 'count2': '$groupTotal.count' , 'prob':{'$divide': [ '$count', '$groupTotal.count' ]}}}
                            , {'$out':output_collection2}], allowDiskUse=True)
             
        return ('Probabilities have been computed.')

if __name__ == '__main__':
    pass
    
    
    
    
