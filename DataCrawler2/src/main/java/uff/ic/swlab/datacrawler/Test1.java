package uff.ic.swlab.datacrawler;

import org.apache.jena.rdf.model.Model;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import uff.ic.swlab.utils.SparqlServer;
import uff.ic.swlab.utils.VoID;

public class Test1 {

    public static void main(String[] args) {
        Logger.getRootLogger().setLevel(Level.OFF);
        System.out.println("Test1 started.");
        String[] sparqls = {"http://acm.rkbexplorer.com/sparql"};
        String[] urls = {"http://acm.rkbexplorer.com/id/999"};
        Model model = VoID.getVoID(sparqls, urls);

        String name_uri = "https://datahub.io/api/rest/dataset/rkb-explorer-acm";
        SparqlServer server = new SparqlServer();
        server.dataURL = "http://localhost:8080/fuseki/void/data";
        server.putModel(name_uri, model);

        //model.write(System.out, Lang.TURTLE.getName());
        System.out.println("Done.");
    }
}
