package uff.ic.swlab.datacrawler;

import uff.ic.swlab.utils.SparqlServer;
import uff.ic.swlab.utils.VoID;

public class Task implements Runnable {

    private static final Semaphore SEMAPHORE = new Semaphore(1000);

    private SparqlServer server;
    private String authority;
    private String[] sparqlEndPoints;
    private String[] urls;

    public Task(SparqlServer server, String authority, String[] sparqlEndPoints, String[] urls) {
        SEMAPHORE.acquire();
        this.server = server;
        this.authority = authority;
        this.sparqlEndPoints = sparqlEndPoints;
        this.urls = urls;
    }

    @Override
    public final void run() {
        run2();
        SEMAPHORE.release();
    }

    public void run2() {
        server.putModel(authority, VoID.retrieveVoID(sparqlEndPoints, urls));
    }

}
