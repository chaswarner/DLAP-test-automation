package com.prft.cif.test.guice;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.prft.cif.test.guice.listener.CIFLog4JTypeListener;

/**
 * @author Anant Gowerdhan
 * @since 07/18/2018
 */
public class CIFLoggerModule extends AbstractModule {
    @Override
    protected void configure() {
        bindListener(Matchers.any(), new CIFLog4JTypeListener());
    }
}
