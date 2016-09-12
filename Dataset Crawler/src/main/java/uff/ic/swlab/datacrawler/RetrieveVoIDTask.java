package uff.ic.swlab.datacrawler;

import org.apache.jena.rdf.model.Model;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import uff.ic.swlab.common.util.Config;
import uff.ic.swlab.common.util.SparqlServer;
import uff.ic.swlab.common.util.VoID;

public class RetrieveVoIDTask implements Runnable {

    private static final InstanceCounter INSTANCE_COUNTER = new InstanceCounter(Config.TASK_INSTANCES);

    private final Model void_;
    private final String[] urls;
    private final String[] sparqlEndPoints;
    private final SparqlServer server;
    private final String graphURI;

    private static class InstanceCounter {

        private int instances;

        public InstanceCounter(int instances) {
            this.instances = instances;
        }

        public synchronized void startInstance() {
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

        public synchronized void finilizeInstance() {
            instances++;
            notifyAll();
        }
    }

    public RetrieveVoIDTask(Model void_, String[] urls, String[] sparqlEndPoints, String graphURI, SparqlServer server) {
        INSTANCE_COUNTER.startInstance();
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
        setTimeout(Config.TASK_RUNNING_TIMEOUT);
        runTask();
        INSTANCE_COUNTER.finilizeInstance();
    }

    private void runTask() {
        try {
            Model model = void_.add(VoID.retrieveVoID(urls, sparqlEndPoints));
            if (model.size() > 5 && VoID.isVoID(model))
                server.putModel(graphURI, model);
            else
                Logger.getLogger("datacrawler").log(Priority.INFO, String.format("Dataset discarded. (%1s)", graphURI));
        } catch (InterruptedException e1) {
            Logger.getLogger("datacrawler").log(Priority.WARN, String.format("Thread interrupted. (%1s)", graphURI));
        } catch (Throwable e2) {
            Logger.getLogger("datacrawler").log(Priority.ERROR, String.format("Thread error. (%1s)", graphURI));
        }
    }

}
