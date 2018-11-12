package com.prft.cif.test.guice.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.prft.cif.test.guice.CIFLoggerModule;
import com.prft.cif.test.guice.CIFPropertiesModule;

/**
 * This is a generic injector.
 * <p></p>
 *
 * @param <T>
 * @author Anant Gowerdhan
 */
public class CIFInjector<T> {

    private Class<T> bindingClass;

    private CIFInjector(Class<T> bindingClass) {
        this.bindingClass = bindingClass;

    }

    private <T> T getInstance() {

        T instance = null;
        Injector guiceInjector = Guice.createInjector(new CIFPropertiesModule("cif.properties"), new CIFLoggerModule());

        instance = (T) guiceInjector.getInstance(this.bindingClass);
        return instance;
    }

    private <T> T getInstance(String propertiesFile) {

        T instance = null;
        Injector guiceInjector = Guice.createInjector(new CIFPropertiesModule(propertiesFile), new CIFLoggerModule());
        instance = (T) guiceInjector.getInstance(this.bindingClass);

        return instance;
    }

    public static <T> T createInstance(Class<T> bindingClass) {
        CIFInjector<T> cif = new CIFInjector<T>(bindingClass);

        return cif.getInstance();
    }

    public static <T> T createInstance(Class<T> bindingClass, String propertiesFile) {
        CIFInjector<T> cif = new CIFInjector<T>(bindingClass);

        return cif.getInstance(propertiesFile);
    }

}
