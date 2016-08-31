package uff.ic.swlab.draft;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import uff.ic.swlab.utils.Resource;
import uff.ic.swlab.utils.SparqlServer;
import uff.ic.swlab.utils.VoID;

public class Test1 {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run(String[] args) {
        Logger.getRootLogger().setLevel(Level.OFF);
        System.out.println("Test1 started.");
        //String[] sparqls = {"http://acm.rkbexplorer.com/sparql"};
        String[] sparqls = {};
        String[] urls = {"http://acm.rkbexplorer.com/models/void.ttl",
            "http://acm.rkbexplorer.com/id/998550",
            "http://acm.rkbexplorer.com/id/"};

        SparqlServer server = new SparqlServer();
        server.dataURL = "http://localhost:8080/fuseki/void/data";

        server.putModel(Resource.getAuthority(urls), VoID.getVoID(sparqls, urls));

        //model.write(System.out, Lang.TURTLE.getName());
        System.out.println("Done.");
    }
}
