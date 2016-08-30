package uff.ic.swlab.datacrawler;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import uff.ic.swlab.utils.VoID;

public class NewClass1 {

    public static void main(String[] args) {
        Logger.getRootLogger().setLevel(Level.OFF);
        String[] sparqls = {"http://acm.rkbexplorer.com/sparql"};
        String[] urls = {"http://acm.rkbexplorer.com/id/999"};
        Model model = VoID.getVoID(sparqls, urls);
        model.write(System.out, Lang.TURTLE.getName());

    }
}
