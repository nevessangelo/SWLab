'''
Created on 17 de fev de 2016

@author: Luiz Leme
@email: lapaesleme@ic.uff.br
'''
from datetime import datetime
import logging

from ckanapi import RemoteCKAN

from datacrawler.crawler import db_name, INSERT, UPSERT, REPLACE, BEGINING, RECENTLY
from datacrawler.utils import normalize_url


def _load_catalogs():
    from datacrawler.utils import load_data, get_proj_path
    return load_data(get_proj_path('dat', 'catalogs.json'), verbose=False)

def _feed_json_db(oper, _datasets, _dataset_ids, dataset_ids, catalog):
    _catalog = RemoteCKAN(catalog['home'], user_agent='datacrawler')

    for dataset_id in dataset_ids:
        _dataset_ids.insert({'name_uri':dataset_id}, check_keys=False)

        name_uri = catalog['home'] + catalog['dataset_api'].format(dataset_id)
        name_uri_3 = catalog['home'] + catalog['dataset_api_3'].format(dataset_id)
        name_web = catalog['home'] + catalog['dataset_wui'].format(dataset_id)
        metadata_old = _datasets.find_one({'extras2.name_uri': name_uri})
        if (oper == INSERT and metadata_old): continue
        metadata = _catalog.action.package_show(id=dataset_id)

        extras2 = {}
        extras2['name_uri'] = name_uri
        extras2['name_uri_3'] = name_uri_3
        extras2['name_web'] = name_web
        extras2['catalog_home'] = catalog['home']
        extras2['catalog_name'] = catalog['name']
        extras2['catalog_dataset_api'] = catalog['home'] + catalog['dataset_api']
        extras2['catalog_dataset_api_3'] = catalog['home'] + catalog['dataset_api_3']
        extras2['last_modified_at'] = str(datetime.now().strftime('%Y-%m-%d %H:%M:%S.%f'))
        extras2['homepage'] = normalize_url(metadata['url'])
        extras2['url'] = normalize_url(metadata['url'])
        extras2['namespaces'] = [next((e['value'] for e in metadata['extras'] if e['key'] == 'namespace'), [])] if 'extras' in metadata else []
        extras2['examples'] = [r['url'] for r in metadata['resources']
                            if ('description' in r and 'example' in r['description'])
                            or ('format' in r and 'example' in r['format'])
                            or ('url' in r and 'example' in r['url'])] if 'resources' in metadata else []
        extras2['voids'] = [r['url'] for r in metadata['resources']
                            if ('description' in r and 'void' in r['description'])
                            or ('format' in r and 'void' in r['format'])
                            or ('url' in r and 'void' in r['url'])] if 'resources' in metadata else []
        extras2['sparql_endpoints'] = [r['url'] for r in metadata['resources']
                                      if ('description' in r and 'sparql' in r['description'])
                                      or ('format' in r and 'sparql' in r['format'])
                                      or ('url' in r and 'sparql' in r['url'])] if 'resources' in metadata else []
        metadata['extras2'] = extras2

        if (not metadata_old):
            try:
                _datasets.insert(metadata, check_keys=False)
                logging.info('Metadata {} has been inserted.'.format(name_uri))
            except: logging.error('Insert error: ' + name_uri); print('*** Error!')
        elif ((metadata['metadata_modified'] != metadata_old['metadata_modified']) or oper == REPLACE):
            try:
                _datasets.delete_one({'extras2.name_uri': name_uri})
                _datasets.insert(metadata, check_keys=False)
                logging.info('Metadata {} has been updated.'.format(name_uri))
            except: logging.error('Update error: ' + name_uri); print('*** Error!')

def _feed_rdf_db(oper, _datasets, _dataset_ids, dataset_ids, catalog):
    pass

def harvest_new_data():
    harvest_data(oper=INSERT, since=BEGINING)

def harvest_data_updates():
    harvest_data(oper=UPSERT, since=RECENTLY)

def harvest_data_replacements():
    harvest_data(oper=REPLACE, since=BEGINING)

def harvest_data(oper=INSERT, since=BEGINING):
    import json
    from pymongo import MongoClient
    from pymongo import ASCENDING
    from datacrawler.utils import get_url_content

    with MongoClient() as client:
        if (oper in [INSERT, UPSERT, REPLACE]):
            DB = client[db_name]
            DB.dataset_ids.drop()
            DB.datasets.create_index([('extras2.name_uri', ASCENDING)], unique=True)
            DB.catalogs.drop()
            DB.catalogs.insert_many(_load_catalogs())

            # _catalogs = _load_catalogs()
            _catalogs = DB.catalogs
            _deletions = DB.deletions
            _dataset_ids = DB.dataset_ids
            _datasets = DB.datasets

            for catalog in _catalogs.find():
                offset = 0
                limit = 500
                dataset_ids = [None]
                search_api = catalog['home'] + catalog['search_api']
                while dataset_ids:
                    try:
                        logging.info(search_api.format(since, offset, limit))
                        response, _ = get_url_content(search_api.format(since, offset, limit))
                        dataset_ids = json.loads(response)['results']

                        _feed_json_db(oper, _datasets, _dataset_ids, dataset_ids, catalog)
                        _feed_rdf_db(oper, _datasets, _dataset_ids, dataset_ids, catalog)
                    except: dataset_ids = []
                    offset = offset + limit

            DB.dataset_ids.create_index([('name_uri', ASCENDING)])
            for metadata in _datasets.find({}, { 'extras2' : 1, '_id':0 }):
                if (not _dataset_ids.find_one({'name_uri':metadata['extras2']['name_uri']})):
                    _deletions.insert_one({'name_uri':metadata['extras2']['name_uri'], 'last_update_at': datetime.now()})

            _dataset_ids.drop()
        else: logging.error('Invalid harvest operation.')

    return 'Datasets have been updated.'
