package uff.ic.swlab.commons.util;

import java.util.concurrent.TimeoutException;

public abstract class TaskExecutor {

    public static void executeTask(Runnable task, String description, long timeout) throws TimeoutException, InterruptedException {
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
        thread.join(timeout);
        if (thread.isAlive()) {
            thread.stop();
            throw new TimeoutException(String.format("task timed out (%1s).", description));
        }
    }
}
