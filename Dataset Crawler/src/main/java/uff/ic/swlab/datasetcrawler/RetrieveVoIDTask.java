package uff.ic.swlab.datasetcrawler;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import uff.ic.swlab.common.util.Config;
import uff.ic.swlab.common.util.SparqlServer;
import uff.ic.swlab.common.util.VoID;

public class RetrieveVoIDTask implements Runnable {

    private final Model void_;
    private final String[] urls;
    private final String[] sparqlEndPoints;
    private final SparqlServer server;
    private final String graphURI;

    private static final InstanceCounter INSTANCE_COUNTER = new InstanceCounter(Config.TASK_INSTANCES);

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

    @Override
    public final void run() {
        activateAutoTimeout(Thread.currentThread(), Config.TASK_RUNNING_TIMEOUT);
        runTask();
        INSTANCE_COUNTER.finilizeInstance();
    }

    private void runTask() {
        Model void__ = null;

        try {
            void__ = VoID.retrieveVoID(urls, sparqlEndPoints);
        } catch (InterruptedException e1) {
            void__ = ModelFactory.createDefaultModel();
            Logger.getLogger("datacrawler").log(Priority.WARN, String.format("VoID Crawler timed out. (<%1s>)", graphURI));
        }

        if (void__.size() > 0)
            void_.add(void__);
        else
            Logger.getLogger("datacrawler").log(Priority.INFO, String.format("Empty crawled VoID: (<%1s>).", graphURI));

        if (void_ != null && void_.size() > 5 && VoID.isVoID(void_))
            server.putModel(graphURI, void_);
        else
            Logger.getLogger("datacrawler").log(Priority.INFO, String.format("Dataset discarded: (<%1s>).", graphURI));
    }

    private void activateAutoTimeout(Thread thread, long timeout) {
        (new Thread() {
            private final Thread t = thread;
            private final long TIMEOUT = timeout;

            @Override
            public void run() {
                try {
                    t.join(TIMEOUT);
                } catch (InterruptedException ex) {
                }
                while (t.isAlive()) {
                    t.interrupt();
                    try {
                        sleep(5000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }).start();
    }
}
