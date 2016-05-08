'''
Created on 27 de fev de 2016

@author: lapaesleme
'''
import logging
import re
from urllib.request import urlparse

from rdflib import Graph
import rdflib
from rdflib.namespace import FOAF
from rdflib.namespace import VOID
from rdflib.plugins.sparql import prepareQuery

from datacrawler.crawler import db_name
from datacrawler.profiler import db_name2
from datacrawler.profiler.matcher import get_matching
from datacrawler.utils import normalize_url


def _get_namespaces(void, format_='n3'):    
    get_column = lambda t: next(((str(t[i]), 'uriSpace') for i in range(1) if (t[i]))) if(t) else (None, None)
    namespaces = []
    try:
        for r in void['resources']:
            v = r['void']
            g = Graph()
            g.parse(data=v, format=format_)
            qs = 'SELECT ?urispc '
            qs = qs + 'WHERE {?s void:uriSpace ?urispc. ?s a void:Dataset. '
            qs = qs + 'FILTER NOT EXISTS {[] void:objectsTarget ?s.} '
            qs = qs + 'FILTER NOT EXISTS {[] void:target ?s.} '
            qs = qs + '}'
            q = prepareQuery(qs, initNs={ 'void': VOID })
            namespaces.extend((get_column(t)[0] for t in (t for t in g.query(q) if t)))
    except Exception: pass
    except: pass
    return namespaces

def _update_namespaces():
    from pymongo import MongoClient
    
    with MongoClient() as client:
        DB = client[db_name]
        
        _datasets = DB.datasets
        _voids = DB.voids
        
        for metadata in _datasets.find():
            extras = metadata['extras'] if 'extras' in metadata else []
            namespaces = next(([e['value']] for e in extras if e['key'] == 'namespace'), [])
            namespaces.extend(_get_namespaces(_voids.find_one({'extras2.name_uri':metadata['extras2']['name_uri']})))
            namespaces = [normalize_url(ns) for ns in namespaces]
            namespaces = list(set(namespaces))
    
            _datasets.find_one_and_update({'_id': metadata['_id']}, { '$set': { 'extras2.namespaces': namespaces } })
    
    return 'Namespaces have been updated successfully.'


_get_feature = lambda t: next(((str(t[i]), ['class', 'property', 'vocabulary'][i]) for i in range(len(t)) if (t[i])))
_qs = 'SELECT ?class ?prop ?vocab '
_qs = _qs + 'WHERE {{[] void:classPartition ?o. ?o void:class ?class.} '
_qs = _qs + 'UNION {[] void:propertyPartition ?o. ?o void:property ?prop.} '
_qs = _qs + 'UNION {[] void:vocabulary ?vocab.} '
_qs = _qs + '}'
_q = prepareQuery(_qs, initNs={ 'void': VOID })
_p = re.compile('.*www\.w3\.org.*', re.IGNORECASE)
def _get_schema(void):
    g = rdflib.Graph().parse(data=void, format='n3')
    r = [_get_feature(t) for t in g.query(_q) if (not _p.match(_get_feature(t)[0]))]
    return r

def _get_datasets(_datasets, linkset, namespace, home, homepage):
    try:
        datasets1 = [d['extras2']['name_uri'] for d in _datasets.find({'extras2.namespaces':normalize_url(namespace)})] if namespace else []
        datasets2 = [d['extras2']['name_uri'] for d in _datasets.find({'extras2.url':normalize_url(home)})] if (home) else []
        datasets3 = [d['extras2']['name_uri'] for d in _datasets.find({'extras2.url':normalize_url(homepage)})] if (homepage) else []
        datasets = set(datasets1 + datasets2 + datasets3)
        return datasets
    except: return []

_qs2 = 'SELECT ?linkset ?urispace ?home ?homepage '
_qs2 = _qs2 + 'WHERE { '
_qs2 = _qs2 + '{?linkset a void:Linkset. ?linkset void:target ?d. '
_qs2 = _qs2 + 'FILTER NOT EXISTS {?d void:subset ?linkset.} '
_qs2 = _qs2 + 'OPTIONAL {?d void:uriSpace ?urispace.} '
_qs2 = _qs2 + 'OPTIONAL {?d foaf:homepage ?home.} '
_qs2 = _qs2 + 'OPTIONAL {?d foaf:home ?homepage.}} '
_qs2 = _qs2 + 'UNION {?linkset a void:Linkset. ?linkset void:objectsTarget ?d. '
_qs2 = _qs2 + 'OPTIONAL {?d void:uriSpace ?urispace.} '
_qs2 = _qs2 + 'OPTIONAL {?d foaf:homepage ?home.} '
_qs2 = _qs2 + 'OPTIONAL {?d foaf:home ?homepage.}} '
_qs2 = _qs2 + '}'
_q2 = prepareQuery(_qs2, initNs={ 'void': VOID, 'foaf':FOAF })
def _get_linksets(void, DB):
    try:
        g = rdflib.Graph().parse(data=void, format='n3')
        return [(d, 'linkset') for t in g.query(_q2) for d in _get_datasets(DB.datasets, str(t[0]), str(t[1]), str(t[2]), str(t[3]))]
    except: []

def _is_valid_url(url):
    try: 
        if (urlparse(url)[0] in ['http', 'https']): return True
        else: return False
    except: 
        return False

def extract_features_from_void(void, DB):
    linksets3 = _get_linksets(void, DB)
    schema = _get_schema(void)
    features = linksets3 + schema
    features = [normalize_url(f) for f in features]
    features = list(set(features))
    features = [{'uri':f[0], 'type':f[1]} for f in features if (_is_valid_url(f[0]))]
    return features

def extract_features(input_collection1='voids', input_collection2='datasets', output_collection='datasets_temp', matchings='matchings_temp'):
    from pymongo import MongoClient
    from pymongo import ASCENDING, HASHED

    _update_namespaces()
    with MongoClient() as _client:
        DB = _client[db_name]
        DB2 = _client[db_name2]    
            
        _voids = DB[input_collection1]
        _datasets = DB[input_collection2]
        _datasets2 = DB2[output_collection]
        _matchings = DB2[matchings]
        
        DB.datasets.create_index([('extrsa2.url', HASHED)])
        DB.datasets.create_index([('extras2.namespaces', ASCENDING)])
    
        for metadata in _datasets.find():
            void = _voids.find_one({'extras2.name_uri':metadata['extras2']['name_uri']})
                
            if (void):
                linksets3 = [l for r in void['resources'] for l in _get_linksets(r['void'], DB)]
                schema = [f for r in void['resources'] for f in _get_schema(r['void'])]
                 
                features = linksets3 + schema
                features = [normalize_url(f) for f in features]
                features = list(set(features))
                features = [{'uri':f[0], 'type':f[1]} for f in features if (_is_valid_url(f[0]))]
         
                if (features):
                    metadata_ = {}
                    metadata_['matching'] = get_matching(_matchings, metadata['extras2']['name_uri'])
                    metadata_['features'] = features
                    _datasets2.insert_one(metadata_)
    
    return 'Dataset profiles have been created successfully.'
        




