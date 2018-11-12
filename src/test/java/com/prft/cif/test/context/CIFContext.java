package com.prft.cif.test.context;

/**
 * Application context used in the CIF framework must implement the CIFContext
 * @param <T>
 */
public interface CIFContext<T> {
    public <T>T getContext();
    public void close();
    public void stop();
}
