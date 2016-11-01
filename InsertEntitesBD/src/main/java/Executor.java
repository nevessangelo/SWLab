/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author angelo
 */
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class Executor {

    public static <T> T execute(Callable<T> task, long timeout) throws InterruptedException, ExecutionException, TimeoutException {
        FutureTask<T> future = new FutureTask<>(task);
        Thread thread = new Thread(future);
        try {
            thread.setDaemon(true);
            thread.start();
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } finally {
            future.cancel(true);
            thread.stop();
        }
    }
}

