package uff.ic.swlab.datacrawler;

public class Semaphore {

    private int instances = 0;

    public Semaphore(int instances) {
        this.instances = instances;
    }

    public synchronized void acquire() {
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

    public synchronized void release() {
        instances++;
        notifyAll();
    }
}
