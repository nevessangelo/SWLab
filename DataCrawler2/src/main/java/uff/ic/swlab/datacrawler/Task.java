package uff.ic.swlab.datacrawler;

import org.apache.jena.rdf.model.Model;
import uff.ic.swlab.utils.SparqlServer;
import uff.ic.swlab.utils.VoID;

public class Task implements Runnable {

    private static final Semaphore SEMAPHORE = new Semaphore(1000);

    private final SparqlServer server;
    private final String authority;
    private final String[] sparqlEndPoints;
    private final String[] urls;
    private final Model void_;

    public Task(SparqlServer server, String authority, String[] sparqlEndPoints, String[] urls, Model void_) {
        SEMAPHORE.acquire();
        this.server = server;
        this.authority = authority;
        this.sparqlEndPoints = sparqlEndPoints;
        this.urls = urls;
        this.void_ = void_;
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
