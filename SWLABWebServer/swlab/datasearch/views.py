from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from django.views.decorators.http import require_http_methods
from pymongo.mongo_client import MongoClient

from datacrawler.crawler import db_name
from datacrawler.profiler import db_name2
from datacrawler.profiler.void_profiler import extract_features_from_void
from datacrawler.utils import parse_rdf

from datasearch.search import do_colaborative_filtering
from datasearch.search import do_text_search
from datasearch.search import do_similarity_filtering


def index(request):
    return render(request, 'datasearch/index.html', {})
    
@require_http_methods(['GET', 'POST'])
@csrf_exempt
def search(request):
    with MongoClient() as client:
        DB = client[db_name]
        DB2 = client[db_name2]
        
        method = request.POST.get('method', None)
        if (method): request.session['method'] = method
        method = method if method else request.session.get('method', None)
        
        limit = request.session.get('limit', 50)
        skip = request.session.get('skip', 0)
        command = request.POST.get('command', None)
        if (command == 'next'): skip = skip + limit
        elif (command == 'previous'): skip = skip - limit
        elif (command == 'top'): skip = 0
        skip = skip if skip >= 0 else 0
        request.session['skip'] = skip
        request.session['limit'] = limit
        
        if (method == 'keywords'):
            keywords = request.POST.get('query', None)
            if (keywords): request.session['query'] = keywords
            keywords = keywords if keywords else request.session.get('query', None)
            
            results = do_text_search(DB2, keywords, skip=skip, limit=limit)
        elif (method == 'collaborative'):
            try:
                void = request.POST.get('void', None)
                if (not void): void = parse_rdf(request.POST.get('query', None))
                features = [f['uri'] for f in extract_features_from_void(void, DB)]
                if (features): request.session['features'] = features
                features = features if (features) else request.session.get('features', [])
            except: features = request.session.get('features', [])
                
            results = do_colaborative_filtering(DB2, features, skip=skip, limit=limit)
        elif (method == 'similarity'):
            
            results = do_similarity_filtering(DB2, features, skip=skip, limit=limit)
        else: return []
    
    return render(request, 'datasearch/result.html', {'results':results})
    

