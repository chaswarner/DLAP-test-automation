package com.prft.cif.test.guice;

import com.google.inject.AbstractModule;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a Guice module that binds arguments to the Guice Instance.
 * <p></p>
 *
 * @author Anant Gowerdhan
 * @version 1.0
 * @see AbstractModule
 * @since 05/08/2018
 */
public class CIFArgumentsModule extends AbstractModule {
    private final Logger LOGGER = Logger.getLogger(this.getClass());

    private List<String> argList;

    /**
     * This constructor accepts the propertiesFile name
     * <p></p>
     *
     * @param args
     */
    public CIFArgumentsModule(String args[]) {
        if (args == null || args.length == 0)
            this.argList = new ArrayList<String>();
        else
            this.argList = new ArrayList<String>(Arrays.asList(args));
        LOGGER.debug("args as list: " + this.argList.toString());
    }

    @Override
    protected void configure() {
        bind(List.class).toInstance(argList);
    }
}
