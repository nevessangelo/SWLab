
def do_colaborative_filtering(DB2, features, skip=0, limit=50):
    features = list(DB2.features.aggregate([{'$match':{'uri':{'$in':features}}}, {'$project':{'_id':0, 'id':1}}]))
    features = [f['id'] for f in features]
    results = list(DB2.prob_coocurr.aggregate([{'$match':{'f1':{'$in':features}}}
                                               , {'$group':{'_id':'$f2', 'sum':{'$sum':{'$log10':'$prob'}}}}
                                               , {'$project':{'f2':'$_id', 'prod':{ '$pow': [ 10, '$sum' ] }}}
                                               , {'$lookup':{'from':'prob_target', 'localField':'f2', 'foreignField':'f2', 'as': 'prob_target'}}
                                               , {'$unwind':'$prob_target'}
                                               , {'$project': {'_id':0, 'f2':1 , 'score':{'$multiply':['$prod', '$prob_target.prob']}}}
                                               , {'$sort' : { 'score' :-1} }
                                               , {'$skip':skip}
                                               , {'$limit':limit}
                                               , {'$lookup':{'from':'datasets2', 'localField':'f2', 'foreignField':'matching', 'as': 'metadata'}}
                                               , {'$unwind':'$metadata'}
                                               , {'$project':{'_id':0
                                                             , 'datasets':'$metadata.summaries'
                                                             }}
                                              ], allowDiskUse=True))
        
    return results

def do_similarity_filtering(DB2, features, skip=0, limit=50):
    return []

def do_text_search(DB2, keywords, skip=0, limit=50):
    results = list(DB2.datasets.aggregate([{'$match':{'$text': {'$search': keywords}}}
                                           , {'$project':{'summaries':1, 'score':{ '$meta': 'textScore' }}}
                                           , {'$sort' : { 'score' :-1} }
                                           , {'$skip':skip}
                                           , {'$limit':limit}
                                           , {'$project':{'_id':0
                                                         , 'datasets':'$summaries'
                                                         }}
                                          ], allowDiskUse=True))
    
    return results