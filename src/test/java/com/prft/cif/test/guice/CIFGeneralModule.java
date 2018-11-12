package com.prft.cif.test.guice;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.prft.cif.test.context.CIFContext;
import com.prft.cif.test.context.CIFSparkContext;
import org.apache.log4j.Logger;
import org.apache.spark.sql.SparkSession;

/**
 * This     is a Guice module that binds properties file to the Guice Instance.
 * <p></p>
 *
 * @author Anant Gowerdhan
 * @version 1.0
 * @see AbstractModule
 * @since 05/08/2018
 */
public class CIFGeneralModule extends AbstractModule {
    private final Logger LOGGER = Logger.getLogger(this.getClass());

    private boolean spakApp;

    public CIFGeneralModule(boolean sparkApp){
        this.spakApp = sparkApp;
    }
    @Override
    protected void configure() {
        try {
            if(spakApp) {
                CIFSparkContext context = new CIFSparkContext();
                bind(new TypeLiteral<CIFContext<SparkSession>>() {
                }).annotatedWith(Names.named("sparksession")).toInstance(context);
            }
        } catch (Exception e) {
            LOGGER.error("Could not load properties: ");
        }
    }
}
