'''
Created on 19 de jan de 2016

@author: Luiz Leme
@email: lapaesleme@ic.uff.br
'''
import logging
from rdflib.plugins.parsers.pyRdfa import UnresolvableReference


def load_data(filename, verbose=False):
    import os
    import json
    dataset = None
    if verbose == True: print ('Loading file {}...'.format(filename))
    if os.path.isfile(filename): 
        with open(filename, 'r', encoding='utf-8') as f: dataset = json.loads(f.read())
    if verbose == True: print ('Done.')
    return dataset

def save_data(dataset, filename, verbose=False):
    import json
    import os.path
    tempFilename = filename + '._trash'
    if verbose == True: print ('Saving dataset...')
    with open(tempFilename, 'w', encoding='utf-8') as f: f.write(json.dumps(dataset))
    if os.path.isfile(filename): os.remove(filename)
    os.rename(tempFilename, filename)
    if verbose == True: print ('Done.')
    
def get_proj_path(*path):
    from os.path import expanduser, dirname, join
    parent = lambda h, i = 0: dirname(parent(h, i + 1)) if (i < h) else __file__ 
    return join(parent(4), *path)

from url_normalize import url_normalize
def normalize_url(url, charset='utf-8'):
    try: return url_normalize(url, charset=charset)
    except: return url
    
from io import StringIO
from chardet import detect
from urllib.request import urlopen, Request
import gzip
def get_url_content(url):
    content = None
    encoding = None
    try:
        request = Request(url)
        # request.add_header('Accept-Encoding', 'gzip, deflate, compress, identity')
        request.add_header('User-Agent', 'lapaesleme')
        request.add_header('From', 'lapaesleme@ic.uff.br')
        request.add_header('Accept', '*/*, text/html;q=0')
        with urlopen(request, timeout=60) as response:
            if response.info().get('Content-Encoding') in ['gzip', 'deflate', 'compress'] :
                content = gzip.GzipFile(fileobj=StringIO(response.read())).read()
            else: 
                content = response.read()
            encoding = response.headers.get_content_charset()
            if (not encoding): encoding = detect(content)['encoding']
            content = content.decode(encoding, 'backslashreplace')
    except Exception: pass
    except: pass
    return content, encoding
        

from rdflib import Graph
from rdflib.parser import Parser
from rdflib.plugin import register
from rdflib.namespace import VOID
from rdflib.plugins.sparql import prepareQuery
register('html', Parser, 'rdflib.plugins.parsers.structureddata', 'StructuredDataParser')
register('hturtle', Parser, 'rdflib.plugins.parsers.hturtle', 'HTurtleParser')
register('mdata', Parser, 'rdflib.plugins.parsers.structureddata', 'MicrodataParser')
register('n3', Parser, 'rdflib.plugins.parsers.notation3', 'N3Parser')
register('nquads', Parser, 'rdflib.plugins.parsers.nquads', 'NQuadsParser')
register('nt', Parser, 'rdflib.plugins.parsers.nt', 'NTParser')
register('rdfa', Parser, 'rdflib.plugins.parsers.structureddata', 'RDFaParser')
register('trix', Parser, 'rdflib.plugins.parsers.trix', 'TriXParser')
register('turtle', Parser, 'rdflib.plugins.parsers.notation3', 'TurtleParser')
register('xml', Parser, 'rdflib.plugins.parsers.rdfxml', 'RDFXMLParser')
def _parse_rdf(data, format_='n3'):
    void = None
    try:
        g = Graph().parse(data=data, format=format_)
        void = g.serialize(format='n3')
        void = void.decode(detect(void)['encoding'], 'backslashreplace')
        void = void if (len(g) > 3) else None
    except Exception: pass
    except: pass
    return void


from concurrent.futures import ThreadPoolExecutor
def parse_rdf(url):
    void = None
    try:
        content, _ = get_url_content(url)
        if (content):
            executor = ThreadPoolExecutor(max_workers=10)
            for f in ['turtle', 'hturtle', 'xml', 'n3', 'nt', 'trix', 'nquads']:
                void = executor.submit(_parse_rdf, content, format_=f).result(timeout=30)
                if (void): break
            executor.shutdown(wait=False)
    except Exception: pass
    except: pass
    if (not void): logging.error('VoID URL empty: {}'.format(url.encode('utf-8', 'backslashreplace')))
    return void
        
        

import nltk
import string
import unicodedata
from nltk.corpus import stopwords
_exclusions = stopwords.words('portuguese')
def normalize(_string):
    result = _string
    result = result.lower()
    result = ''.join([_c for _c in unicodedata.normalize('NFD', result) if unicodedata.category(_c) != 'Mn'])
    result = ''.join([_c if _c not in string.punctuation else ' ' for _c in result])
    result = ' '.join([t for t in nltk.word_tokenize(result) if t not in _exclusions and len(t) > 1])
    return result

def fast_normalize(_string):
    result = _string
    result = result.lower()
    result = ''.join((c for c in unicodedata.normalize('NFD', result) if unicodedata.category(c) != 'Mn'))
    return result

def hmean(values):
    try: return len(values) / sum(((1.0 / val) for val in values)) if all((v > 0 for v in values)) else -sys.float_info.max
    except: return -sys.float_info.max

import sys
import Levenshtein
_tksim_threshold = 0.95
def token_similarity(string1, string2):
    set1 = set(normalize(string1).split())
    set2 = set(normalize(string2).split())
    sims = []
    diffs = []
    for item1 in set1:
        for item2 in set2:
            sim = 1 - Levenshtein.distance(item1, item2) / (len(item1) + len(item2))  # @UndefinedVariable
            if sim > _tksim_threshold:
                sims.append(item1)
                sims.append(item2)
    sims = list(set(sims))
    for item1 in set1:
        if item1 not in sims:
            diffs.append(item1)
    for item2 in set2:
        if item2 not in sims:
            diffs.append(item2)
    diffs = list(set(diffs))
    sim = len(sims) / (len(sims) + len(diffs)) if len(sims) + len(diffs) > 0 else 0
    diff = 1 - (len(diffs) / (len(sims) + len(diffs)) if len(sims) + len(diffs) > 0 else 0)
    scores = [sim, diff]
    return hmean(scores)

