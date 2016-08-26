package ic.uff.utils;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.WebContent;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class VoID {

	public static Model getVoID(String sparqlEndPoint, String[] urls) {
		Model void_ = ModelFactory.createDefaultModel();

		void_.add(VoID.getVoidFromFile(urls));
		void_.add(VoID.getVoidFromSparql("http://acm.rkbexplorer.com/sparql/"));

		return void_;
	}

	private static Model getVoidFromFile(String[] urls) {
		Set<String> voidURLs = listPossibleVoIDURLs(urls);

		Model void_ = ModelFactory.createDefaultModel();
		for (String url : voidURLs)
			try {
				RDFDataMgr.read(void_, url);
			} catch (Exception e1) {
				Lang[] langs = { Lang.TTL, Lang.RDFXML, Lang.N3, Lang.JSONLD, Lang.TRIG };
				boolean read = false;
				for (Lang l : langs)
					try {
						RDFDataMgr.read(void_, url, l);
						read = true;
						break;
					} catch (Exception e2) {
					}
				if (!read)
					try {
					readRDFa(void_, url);
					} catch (Exception e) {
					}
			}
		return void_;
	}

	private static Model getVoidFromSparql(String sparqlEndPoint) {
		String from = "";
		String queryString = "construct {?s ?p ?o}\n %1swhere {?s ?p ?o.}";
		for (String n : listVoIDGraphURIs(sparqlEndPoint))
			from += String.format("from <%1s>\n", n);
		queryString = String.format(queryString, from);

		try (QueryExecution exec = new QueryEngineHTTP(sparqlEndPoint, queryString)) {
			((QueryEngineHTTP) exec).setModelContentType(WebContent.contentTypeRDFXML);
			((QueryEngineHTTP) exec).setTimeout(5000);
			return exec.execConstruct();
		} catch (Exception e) {
		}

		return ModelFactory.createDefaultModel();
	}

	private static void readRDFa(Model model, String urlString) throws UnsupportedEncodingException {
		String url = URLEncoder.encode(urlString, "UTF-8");
		model.read("http://rdf-translator.appspot.com/convert/rdfa/xml/" + url);
	}

	private static List<String> listVoIDGraphURIs(String sparqlEndPoint) {
		List<String> graphNames = new ArrayList<>();

		String name;
		String queryString = "select distinct ?g where {graph ?g {?s ?p ?o.}}";

		try (QueryExecution exec = new QueryEngineHTTP(sparqlEndPoint, queryString)) {
			((QueryEngineHTTP) exec).setTimeout(5000);
			ResultSet rs = exec.execSelect();
			while (rs.hasNext()) {
				name = rs.next().getResource("g").getURI();
				if (name.contains("void"))
					graphNames.add(name);
			}
		} catch (Exception e) {
		}

		return graphNames;
	}

	private static Set<String> listPossibleVoIDURLs(String[] urls) {
		Set<String> voidURLs = new HashSet<>();

		try {
			for (String u : urls) {
				URL url = new URL(u);
				String protocol = url.getProtocol();
				String auth = url.getAuthority();
				String newPath = protocol + "://" + auth;
				voidURLs.add(newPath);
				voidURLs.add(newPath + "/.well-known/void");
				voidURLs.add(newPath + "/.well-known/void.ttl");
				voidURLs.add(newPath + "/void");
				voidURLs.add(newPath + "/void.ttl");
				String[] path = url.getPath().split("/");
				for (int i = 1; i < path.length; i++) {
					if (!path[i].equals("void.ttl") && !path[i].equals("void")) {
						newPath += "/" + path[i];
						voidURLs.add(newPath + "/void");
						voidURLs.add(newPath + "/void.ttl");
					}
				}
			}
		} catch (Exception e) {
		}

		return voidURLs;
	}

}
