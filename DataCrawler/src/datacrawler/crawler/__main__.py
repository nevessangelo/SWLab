'''
Created on 19 de jan de 2016

@author: Luiz Leme
@email: lapaesleme@ic.uff.br
'''
import logging
from datacrawler.crawler import db_name, INSERT, UPSERT, REPLACE, BEGINING, RECENTLY, stats

logging.basicConfig(filename='data_crawler.log', filemode='w', level=logging.INFO)
logging.info('Data_Crawler version: 0.0.1')

def _initialize(recreateDB=False):
    from pymongo import MongoClient
    with MongoClient() as client:
        DB = client[db_name]

        if (recreateDB):
            MongoClient().drop_database(db_name)
            print('DB has been recreated.')
        for c in DB.collection_names():
            if (c not in ['catalogs', 'deletions', 'datasets', 'voids', 'system.indexes']):
                DB.drop_collection(c)

        DB.deletions.drop_indexes()
        DB.datasets.drop_indexes()
        DB.voids.drop_indexes()


def _finalize():
    from pymongo import MongoClient
    from pymongo import ASCENDING, TEXT, HASHED
    with MongoClient() as client:
        DB = client[db_name]

        DB.deletions.create_index([('name_uri', ASCENDING)])
        DB.deletions.create_index([('name_uri_3', ASCENDING)])
        DB.deletions.create_index([('last_update_at', ASCENDING)])

        DB.datasets.create_index([('id', ASCENDING)])
        DB.datasets.create_index([('name', HASHED)])
        DB.datasets.create_index([('extras2.name_uri', ASCENDING)], unique=True)
        DB.datasets.create_index([('extras2.name_uri_3', ASCENDING)], unique=True)
        DB.datasets.create_index([('extrsa2.url', HASHED)])
        DB.datasets.create_index([('extras2.namespaces', ASCENDING)])
        DB.datasets.create_index([('extras2.catalog_home', ASCENDING)])
        DB.datasets.create_index([('name', TEXT), ('title', TEXT), ('notes', TEXT), ('tags.name', TEXT)])
        DB.voids.create_index([('extras2.name_uri', ASCENDING)], unique=True)
        DB.voids.create_index([('extras2.name_uri_3', ASCENDING)], unique=True)


def _main(recreateDB=False):
    from datacrawler.crawler.update_voids import update_voids
    from datacrawler.crawler.ckan_crawler import harvest_data as harvest_data_from_ckan
    from datacrawler.crawler.web_crawler import harvest_data as harvest_data_from_web

    _initialize(recreateDB=recreateDB)
    print('Starting main tasks of crawling...')
    print(stats())
    # print('Starting datasets update from CKAN (operation: {})...'.format(INSERT))
    # print(harvest_data_from_ckan(oper=INSERT, since=BEGINING))
    # print('Starting datasets update from CKAN (operation: {})...'.format(UPSERT))
    # print(harvest_data_from_ckan(oper=UPSERT, since=RECENTLY))
    print('Starting datasets update from CKAN (operation: {})...'.format(REPLACE))
    print(harvest_data_from_ckan(oper=REPLACE, since=BEGINING))
    # print('Starting datasets update from Web (operation: {})...'.format(UPSERT))
    # print(harvest_data_from_web(oper=UPSERT, since=RECENTLY))
    print('Starting VoIDs update (operation: {})...'.format(INSERT))
    print(update_voids(oper=INSERT))
    print('Starting VoIDs update (operation: {})...'.format(UPSERT))
    print(update_voids(oper=UPSERT))
    print(stats())
    _finalize()
    return 'Main tasks of crawling have finished.'


def _start(func=_main, repeat=False, *args, **kwargs):
    import time
    while (True):
        print('Staring data crawling cycle (operation: {}).'.format(func.__name__))
        print(func(*args, **kwargs))
        if (not repeat):
            break
        print('Waiting for restarting crawling cycle (operation: {}).'.format(func.__name__))
        time.sleep(60 * 60 * 24 * 3)
    return 'Cycles of {} have finished.'.format(func.__name__)

if __name__ == '__main__':
    print(_start(repeat=True, recreateDB=False))
