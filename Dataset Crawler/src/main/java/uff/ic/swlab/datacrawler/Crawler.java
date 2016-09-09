package uff.ic.swlab.datacrawler;

public abstract class Crawler<T> implements AutoCloseable {

    public abstract boolean hasNext();

    public abstract T next();
}
