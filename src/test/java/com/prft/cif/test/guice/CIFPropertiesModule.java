package com.prft.cif.test.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.apache.log4j.Logger;

import java.util.Properties;

/**
 * This is a Guice module that binds properties file to the Guice Instance.
 * <p></p>
 * @author Anant Gowerdhan
 * @version 1.0
 * @since 05/08/2018
 * @see AbstractModule
 */
public class CIFPropertiesModule extends AbstractModule {
    private final Logger LOGGER = Logger.getLogger(this.getClass());

    private String propertiesFile;

    /**
     * This constructor accepts the propertiesFile name
     * <p></p>
     * @param propertiesFile
     */
    public CIFPropertiesModule(String propertiesFile){
        this.propertiesFile = propertiesFile;
    }

    @Override
    protected void configure() {
        try {
            Properties props = new Properties();
            props.load(this.getClass().getClassLoader().getResourceAsStream(this.propertiesFile));
            bind(Properties.class).toInstance(props);
            Names.bindProperties(binder(), props);
        } catch (Exception e) {
            LOGGER.error("Could not load properties: ");
        }
    }
}
