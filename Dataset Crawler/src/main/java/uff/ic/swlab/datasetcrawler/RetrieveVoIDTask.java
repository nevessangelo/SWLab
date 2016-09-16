package uff.ic.swlab.datasetcrawler;

import org.apache.jena.rdf.model.Model;
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
        activateAutoTimeout(Config.TASK_RUNNING_TIMEOUT);
        runTask();
        INSTANCE_COUNTER.finilizeInstance();
    }

    private void runTask() {
        try {
            Model void__ = VoID.retrieveVoID(urls, sparqlEndPoints);
            if (void__.size() == 0)
                Logger.getLogger("datacrawler").log(Priority.INFO, String.format("Empty crawled VoID: (<%1s>).", graphURI));

            void_.add(void__);
            if (void_.size() > 5 && VoID.isVoID(void_))
                server.putModel(graphURI, void_);
            else
                Logger.getLogger("datacrawler").log(Priority.INFO, String.format("Dataset discarded: (<%1s>).", graphURI));
        } catch (InterruptedException e1) {
            Logger.getLogger("datacrawler").log(Priority.WARN, String.format("Thread timed out. (<%1s>)", graphURI));
        } catch (Throwable e2) {
            Logger.getLogger("datacrawler").log(Priority.ERROR, String.format("Thread error (<%1s>). Msg: %2s", graphURI, e2.getMessage()));
        }
    }

    private void activateAutoTimeout(long timeout) {
        (new Thread() {
            private final Thread t = Thread.currentThread();
            private final long TIMEOUT = timeout;

            @Override
            public void run() {
                try {
                    t.join(TIMEOUT);
                } catch (InterruptedException ex) {
                }
                t.interrupt();
            }
        }).start();
    }
}
