package ic.uff.datacrawler;

import java.net.MalformedURLException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import ic.uff.utils.VoID;

public class Main {

	public static void main(String[] args) throws MalformedURLException {
		Logger.getRootLogger().setLevel(Level.OFF);

		String sparqlEndPoint = "http://acm.rkbexplorer.com/sparql/";
		String[] urls = {
				"http://acm.rkbexplorer.com/models/void.ttl",
				"http://acm.rkbexplorer.com/id/",
				"http://acm.rkbexplorer.com/id/998550" };

		String sparqlEndPoint2 = "http://dbpedia.org/sparql";
		String[] urls2 = {
				"http://dbpedia.org/resource",
				"http://dbpedia.org/resource/Cidade_das_Artes" };

		Model void_ = VoID.getVoID(sparqlEndPoint2, urls2);

		StmtIterator iter = void_.listStatements();
		while (iter.hasNext()) {
			System.out.println(iter.next().toString());
		}
	}

}
