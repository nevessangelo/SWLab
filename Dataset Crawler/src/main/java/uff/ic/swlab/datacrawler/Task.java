package uff.ic.swlab.datacrawler;

import org.apache.jena.rdf.model.Model;
import uff.ic.swlab.common.util.SparqlServer;
import uff.ic.swlab.common.util.VoID;

public class Task implements Runnable {

    private static final int RUNNING_TIMEOUT = 300000;
    private static final int MAX_EXISTING_INSTANCES = 100;
    private static final InstanceCounter INSTANCE_COUNTER = new InstanceCounter(MAX_EXISTING_INSTANCES);

    private final Model void_;
    private final String[] urls;
    private final String[] sparqlEndPoints;
    private final SparqlServer server;
    private final String graphURI;

    private static class InstanceCounter {

        private int instances = 0;

        public InstanceCounter(int instances) {
            this.instances = instances;
        }

        public synchronized void createInstance() {
            while (true)
                if (instances > 0) {
                    instances--;
                    break;
                } else
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                    }
        }

        public synchronized void releaseInstance() {
            instances++;
            notifyAll();
        }
    }

    public Task(Model void_, String[] urls, String[] sparqlEndPoints, SparqlServer server, String graphURI) {
        INSTANCE_COUNTER.createInstance();
        this.void_ = void_;
        this.urls = urls;
        this.sparqlEndPoints = sparqlEndPoints;
        this.server = server;
        this.graphURI = graphURI;
    }

    private void setTimeout(long timeout) {
        (new Thread() {
            private final Thread t = Thread.currentThread();
            private final long TIMEOUT = timeout;

            @Override
            public void run() {
                try {
                    t.join(TIMEOUT);
                } catch (InterruptedException ex) {
                    t.interrupt();
                }
            }
        }).start();
    }

    @Override
    public final void run() {
        setTimeout(RUNNING_TIMEOUT);
        runTask();
        INSTANCE_COUNTER.releaseInstance();
    }

    private void runTask() {
        try {
            Model model = void_.add(VoID.retrieveVoID(urls, sparqlEndPoints));
            if (model.size() > 5 && VoID.isVoID(model))
                server.putModel(graphURI, model);
        } catch (InterruptedException e1) {
            System.out.println(String.format("Thread interrupted. (%1s)", graphURI));
        } catch (Throwable e2) {
            System.out.println(String.format("Thread error. (%1s)", graphURI));
        }
    }

}
