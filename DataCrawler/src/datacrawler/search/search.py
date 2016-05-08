'''
Created on 29 de jan de 2016

@author: Luiz Leme
@email: lapaesleme@ic.uff.br
'''
from pymongo import MongoClient

from datacrawler.profiler import db_name2

def search(features,offset=0,limit=10):
    with MongoClient() as client:
        DB2 = client[db_name2]
        prob = DB2.prob_coocurr
        prob.aggregate([{'$match':{'f1':{'$in':features}}}
                        , {'$group':{'_id':'$f2', 'sum':{'$sum':{'$log10':'$prob'}}}}
                        , {'$project':{'f2':'$_id', 'prod':{ '$pow': [ 10, '$sum' ] }}}
                        , {'$lookup':{'from':'prob_target', 'localField':'f2', 'foreignField':'f2', 'as': 'prob_target'}}
                        , {'$unwind':'$prob_target'}
                        , {'$project': {'_id':0, 'f2':1 , 'score':{'$multiply':['$prod', '$prob_target.prob']}}}
                        , {'$sort' : { 'score' :-1} }
                        , {'$out':'temp1'}], allowDiskUse=True)

        
if __name__ == '__main__':
    features = []
    features = features + [('https://datahub.io/api/rest/dataset/rkb-explorer-citeseer', 'likset')]
    features = features + [('https://datahub.io/api/rest/dataset/rkb-explorer-southampton', 'likset')]
    features = features + [('https://datahub.io/api/rest/dataset/rkb-explorer-oai', 'likset')]
    features = features + [('https://datahub.io/api/rest/dataset/rkb-explorer-citeseer', 'likset')]
    features = features + [('https://datahub.io/api/rest/dataset/rkb-explorer-ieee', 'likset')]
    search([f[0] for f in features])
