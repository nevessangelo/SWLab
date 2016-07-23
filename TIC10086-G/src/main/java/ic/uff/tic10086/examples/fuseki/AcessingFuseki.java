package ic.uff.tic10086.examples.fuseki;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class AcessingFuseki {

    public static void main(String[] args) {
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefix("my", "http://localhost:8080/tic10086lod/resource/");
        model.setNsPrefix("wine", "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#");
        model.setNsPrefix("owl", "http://www.w3.org/2002/07/owl#");
        String uri = "<http://localhost:8080/tic10086lod/resource/123>";

        String serviceURI = "http://localhost:3030/SWLAB";
        String query = "construct {?s ?p ?o. ?o ?p2 ?o2.} where {?s ?p ?o. ?o ?p2 ?o2. filter(?s = <http://localhost:8080/tic10086lod/resource/123>)}";
        QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI, query);
        q.execConstruct(model);

        //FileOutputStream out = new FileOutputStream("./dat/rdf/example.rdf", false);
        model.write(System.out, "TURTLE");
    }
}
