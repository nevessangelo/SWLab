package uff.ic.swlab.datacrawler;

public abstract class Crawler<T> {

    public abstract boolean hasNext();

    public abstract T next();

    public abstract void close();
}
