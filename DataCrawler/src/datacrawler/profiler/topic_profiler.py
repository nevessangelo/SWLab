'''
Created on 27 de fev de 2016

@author: lapaesleme
'''
import logging
from datacrawler.crawler import db_name
from datacrawler.profiler import db_name2

def extract_features(input_collection1='voids', input_collection2='datasets', output_collection='datasets_temp', matchings='matchings_temp'):
    return 'Dataset profiles have been created successfully.'