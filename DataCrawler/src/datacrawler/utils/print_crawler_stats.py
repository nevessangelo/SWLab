'''
Created on 2 de mar de 2016

@author: lapaesleme
'''
from datacrawler.crawler import stats as crawler_stats
from datacrawler.profiler import stats as profiler_stats
print(crawler_stats())
print(profiler_stats())