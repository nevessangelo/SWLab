package uff.ic.swlab.datacrawler;

import org.apache.jena.rdf.model.Model;
import uff.ic.swlab.utils.SparqlServer;
import uff.ic.swlab.utils.VoID;

public class Task implements Runnable {

    private final Model void_;
    private final String authority;
    private final String[] urls;
    private final String[] sparqlEndPoints;
    private final SparqlServer server;
    private static final Semaphore SEMAPHORE = new Semaphore(100);

    public Task(Model void_, String authority, String[] urls, String[] sparqlEndPoints, SparqlServer server) {
        SEMAPHORE.acquire();
        this.void_ = void_;
        this.authority = authority;
        this.urls = urls;
        this.sparqlEndPoints = sparqlEndPoints;
        this.server = server;
    }

    @Override
    public final void run() {
        run2();
        SEMAPHORE.release();
    }

    public void run2() {
        server.putModel(authority, void_.add(VoID.retrieveVoID(sparqlEndPoints, urls)));
    }

}
