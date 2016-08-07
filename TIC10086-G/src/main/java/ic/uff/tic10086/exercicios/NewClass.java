package ic.uff.tic10086.exercicios;

import static ic.uff.tic10086.exercicios.Mashup.SAME_AS_RULES;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.List;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.WebContent;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.vocabulary.ReasonerVocabulary;

public class NewClass {

    public static void main(String[] args) {
        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run() throws FileNotFoundException, MalformedURLException, IOException {
        Model model = ModelFactory.createDefaultModel();

        String doc = ""
                + "@prefix : <http://swlab.ic.uff.br/resource/>.\n"
                + "@prefix owl: <http://www.w3.org/2002/07/owl#>.\n"
                + "\n"
                + ":a :p :b.\n"
                + ":a owl:sameAs :b.\n"
                + ":e owl:sameAs :f.\n"
                + ":f owl:sameAs :g.\n"
                + ":g owl:sameAs :a.\n"
                + ":b owl:sameAs :c.\n"
                + ":c owl:sameAs :d.";
        Reader sr = new StringReader(doc);
        Reader br = new BufferedReader(sr);
        //model.read(br, null, Lang.TURTLE.getName());

        //model = DBpediaSearch.search("Pedra da Gávea", 7, 0, model);
//        Lang lang = Lang.JSONLD;
//        URLConnection yc = (new URL("http://swlab.ic.uff.br:3030/dbpedia.temp/data")).openConnection();
//        yc.setRequestProperty(HttpHeaders.ACCEPT, lang.getHeaderString());
//        yc.setRequestProperty(HttpHeaders.ACCEPT_ENCODING, "UTF-8");
//        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
//        model.read(in, null, lang.getName());
        model = ModelFactory.createDefaultModel();
        String describeQuery = "construct {?s ?p ?o.} where {?s ?p ?o.}";
        try (QueryExecution exec2 = new QueryEngineHTTP("http://swlab.ic.uff.br:3030/dbpedia.temp/sparql", describeQuery);) {
            ((QueryEngineHTTP) exec2).setModelContentType(WebContent.contentTypeRDFXML);
            exec2.execConstruct(model);
            DatasetAccessor accessor = DatasetAccessorFactory.createHTTP("http://swlab.ic.uff.br:3030/draft.temp/data");
            accessor.putModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BufferedReader br2 = new BufferedReader(new FileReader(SAME_AS_RULES));
        List rules2 = Rule.parseRules(Rule.rulesParserFromReader(br2));
        // Convert DBpedia schema to Schema.org schema.
        Reasoner reasoner2 = new GenericRuleReasoner(rules2);
        reasoner2.setParameter(ReasonerVocabulary.PROPruleMode, "forward");
        InfModel inf2 = ModelFactory.createInfModel(reasoner2, model);

        System.out.println("========================================================================");
        String query = ""
                + "PREFIX sch: <http://schema.org/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "\n"
                + "CONSTRUCT {?s owl:sameAS ?o.}\n"
                + "WHERE {?s owl:sameAs ?o.\n"
                //+ "       FILTER REGEX(str(?s), \"http://" + DOMAIN + "/resource/id2-\")"
                //+ "       FILTER (REGEX(str(?p), \"http://schema.org/\") || REGEX(str(?o), \"http://schema.org/\"))"
                + "}";
        QueryExecution exec = QueryExecutionFactory.create(query, inf2);
        exec.execConstruct().write(System.out, Lang.TURTLE.getName());
        System.out.println("========================================================================");
    }
}
