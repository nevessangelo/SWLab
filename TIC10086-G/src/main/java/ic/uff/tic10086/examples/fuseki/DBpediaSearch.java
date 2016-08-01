package ic.uff.tic10086.examples.fuseki;

import java.util.ArrayList;
import java.util.List;
import org.apache.jena.ext.com.google.common.base.Joiner;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class DBpediaSearch {

    private static final String queryPrototype = ""
            + "PREFIX  bif: <bif:> \n"
            + "PREFIX  sql: <sql:> \n"
            + "PREFIX  virtrdf: <http://www.openlinksw.com/schemas/virtrdf#> \n"
            + "select (?s1 as ?c1) ?sc ?rank ?g where \n"
            + "  { \n"
            + "    { \n"
            + "      { \n"
            + "        select ?s1 (( ?sc * 3e-1 ) as ?sc) ?o1 (( sql:rnk_scale ( <LONG::IRI_RANK> ( ?s1 ) ) ) as ?rank) ?g where \n"
            + "        { \n"
            + "          <quad> <map> virtrdf:DefaultQuadMap \n"
            + "          { \n"
            + "            graph ?g \n"
            + "            { \n"
            + "              ?s1 ?s1textp ?o1 .\n"
            + "              ?o1 bif:contains ' ( %1s ) ' option ( <score> ?sc ) .\n"
            + "              \n"
            + "            }\n"
            + "           }\n"
            + "         }\n"
            + "       order by desc ( ?sc * 3e-1 + sql:rnk_scale ( <LONG::IRI_RANK> ( ?s1 ) ) ) limit 20 offset 0 \n"
            + "      }";

    public static void main(String[] args) {
        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run() {
        String keywordsString = "Cidade das Artes";
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefix("owl", "http://www.w3.org/2002/07/owl#");

        String serviceURI = "http://dbpedia.org/sparql";
        QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI, prepareQuery(keywordsString));
        //q.execConstruct(model);
        ResultSet rs = q.execSelect();
        while (rs.hasNext())
            System.out.println(rs.toString());

        //FileOutputStream out = new FileOutputStream("./dat/rdf/example.rdf", false);
        //model.write(System.out, "TURTLE");
    }

    private static String prepareQuery(String keywordsString) {
        String[] keywords = keywordsString.split("\\s");
        List<String> keywords2 = new ArrayList<>();
        for (String keyword : keywords)
            if (keyword != null && !keyword.equals(""))
                keywords2.add(keyword.toUpperCase());
        String keywordsList = "'" + Joiner.on("','").join(keywords2) + "'";
        String keywordsConjunction = Joiner.on(" AND ").join(keywords2);
        String query = String.format(queryPrototype, keywordsConjunction);
        return query;
    }
}
