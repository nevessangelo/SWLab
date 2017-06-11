<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Entity Relatedness Test Dataset - V3</title>
    </head>
    <body>
        <h1 style="text-align:center">Entity Relatedness Test Dataset (v3)</h1>
        <br><br><br>
        <div style="margin:auto; text-align:justify; width:70%; height:90%">
            <p>&emsp;&emsp;The entity relatedness problem refers to the question
                of computing the relationship paths that better describe the connectivity between a given
                entity pair. This dataset supports the evaluation of approaches that address the entity
                relatedness problem. It covers two familiar domains, music and movies, and uses data
                available in IMDb and last.fm, which are popular reference datasets in these domains. The
                dataset contains 20 entity pairs from each of these domains and, for each entity pair, a
                ranked list with 50 relationship paths. It also contains entity ratings and property
                relevance scores for the entities and properties used in the paths.</p>
            <br>
            <iframe style="border:0; width:100%; height:351px"  src="https://widgets.figshare.com/articles/5007983/embed?show_title=1">
            </iframe>
        </div>
        <c:import url="http://rdf-translator.appspot.com/convert/detect/rdfa/http://swlab.ic.uff.br/void.ttl" />
    </body>
</html>