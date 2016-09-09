package uff.ic.swlab.datacrawler;

import org.apache.jena.rdf.model.Model;
import uff.ic.swlab.common.util.SparqlServer;
import uff.ic.swlab.common.util.VoID;

public class Task implements Runnable {

    private static final Semaphore SEMAPHORE = new Semaphore(100);
    private final Model void_;
    private final String[] urls;
    private final String[] sparqlEndPoints;
    private final SparqlServer server;
    private final String graphURI;

    private static class Semaphore {

        private int instances = 0;

        public Semaphore(int instances) {
            this.instances = instances;
        }

        public synchronized void acquire() {
            while (true) {
                if (instances > 0) {
                    instances--;
                    break;
                } else {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }

        public synchronized void release() {
            instances++;
            notifyAll();
        }
    }

    public Task(Model void_, String[] urls, String[] sparqlEndPoints, SparqlServer server, String graphURI) {
        SEMAPHORE.acquire();
        this.void_ = void_;
        this.urls = urls;
        this.sparqlEndPoints = sparqlEndPoints;
        this.server = server;
        this.graphURI = graphURI;
    }

    @Override
    public final void run() {
        run2();
        SEMAPHORE.release();
    }

    public void run2() {
        Model model = void_.add(VoID.retrieveVoID(sparqlEndPoints, urls));
        if (VoID.isVoID(model)) {
            server.putModel(graphURI, model);
        }
    }

}
