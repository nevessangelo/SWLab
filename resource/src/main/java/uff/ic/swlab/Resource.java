package uff.ic.swlab;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.LangBuilder;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFLanguages;

public class Resource extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String DOMAIN = "localhost";
    private static final String SPARQL_PORT = ":8080";
    private static final String SPARQL_URL_TEMPLATE = "http://" + DOMAIN + SPARQL_PORT + "/fuseki/%1s";

    @Override
    public void init(ServletConfig config) throws ServletException {
        org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        Lang lang = LangBuilder.create("RDFa", "text/html").addFileExtensions("htm", "html", "xhtml", "xhtm").build();
        RDFLanguages.register(lang);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getParameter("id");
        String accept = request.getHeader("Accept");
        Lang lang = detectRequestedLang(accept);
        Model model = getDescription(uri);

        try (OutputStream httpReponse = response.getOutputStream()) {
            if (lang == null) {
                response.setStatus(HttpServletResponse.SC_SEE_OTHER);
                response.setHeader("Location", "http://linkeddata.uriburner.com/about/html/" + request.getRequestURL() + "/" + uri);
                response.sendRedirect("http://linkeddata.uriburner.com/about/html/" + request.getRequestURL() + uri);
            } else {
                response.setContentType(lang.getContentType().getContentType());
                RDFDataMgr.write(httpReponse, model, lang);
            }

            httpReponse.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().write("Error: POST method not implemented.");
    }

    private Lang detectRequestedLang(String accept) {
        Lang[] langs = {Lang.RDFXML, Lang.TURTLE, Lang.TTL, Lang.TRIG, Lang.TRIX, Lang.JSONLD, Lang.RDFJSON,
            Lang.RDFTHRIFT, Lang.NQUADS, Lang.NQ, Lang.NTRIPLES, Lang.N3, Lang.NT};
        for (Lang lang : langs)
            if (accept.toLowerCase().contains(lang.getHeaderString().toLowerCase()))
                return lang;
        return null;
    }

    private static Model getDescription(String uri) throws UnsupportedEncodingException {

        Model model = ModelFactory.createDefaultModel();

        for (String service : listFusekiServices())
            try {
                String sparqlURL = String.format(SPARQL_URL_TEMPLATE, service);
                String query = ""
                        + "construct {?s ?p ?o.}\n"
                        + "where {{?s ?p ?o.}\n"
                        + "       UNION {GRAPH ?g {?s ?p ?o.}}\n"
                        + "       filter(regex(str(?s), \"/%1s$\")\n"
                        + "              || regex(str(?o), \"/%2s$\"))\n"
                        + "}";
                query = String.format(query, uri, uri);
                QueryExecution q = QueryExecutionFactory.sparqlService(sparqlURL, query);
                q.execConstruct(model);
            } catch (Exception e) {
            }
        return model;
    }

    private static List<String> listFusekiServices() {
        List<String> services = new ArrayList<>();
        List<String> configFiles = new ArrayList<>();
        String fuseki_home = System.getenv("FUSEKI_BASE");
        File dir = new File(fuseki_home + "/configuration");

        try {
            for (File file : dir.listFiles())
                if (file.isFile())
                    configFiles.add(file.getName());
        } catch (Exception e) {
        }
        for (String configFile : configFiles)
            try {
                Model model = ModelFactory.createDefaultModel();
                model.read("file:///" + fuseki_home + "/configuration/" + configFile);
                String query = ""
                        + "prefix fuseki: <http://jena.apache.org/fuseki#>\n"
                        + "select ?name\n"
                        + "where {[] a fuseki:Service;\n"
                        + "       fuseki:name ?name.\n"
                        + "}";
                QueryExecution q = QueryExecutionFactory.create(query, model);
                ResultSet result = q.execSelect();
                while (result.hasNext())
                    services.add(result.next().get("name").toString());
            } catch (Exception e) {
            }
        return services;
    }

}
