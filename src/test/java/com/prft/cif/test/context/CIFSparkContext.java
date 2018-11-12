package com.prft.cif.test.context;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

/**
 * @author Harshil Shah
 * @author Anant Gowerdhan
 */
public class CIFSparkContext implements CIFContext<SparkSession> {

    private Boolean sparkAllowMultipleContext;
    private String sparkSerializer;
    private String sparkKryoRegistrator;
    private String sparkAppName;
    private Integer maxAttempts;
    private String dynamicPartitions;
    private String master;

    private SparkSession sparkContext;

    private SparkConf sparkConf;

    /**
     *
     */
    public CIFSparkContext() {
        sparkConf = new SparkConf();
    }

    /**
     *
     * @param dynamicPartitions
     */
    @Inject (optional = true)
    public void setDynamicPartitions(@Named("spark.sql.sources.partitionOverwriteMode") String dynamicPartitions) {
        sparkConf.set("spark.sql.sources.partitionOverwriteMode", dynamicPartitions);
        this.dynamicPartitions = dynamicPartitions;
    }

    public String getDynamicPartitions() {
        return dynamicPartitions;
    }

    public String getSparkAppName() {
        return sparkAppName;
    }

    @Inject (optional = true)
    public void setSparkAppName(@Named("spark.appname") String sparkAppName) {
        sparkConf.setAppName(sparkAppName);
        this.sparkAppName = sparkAppName;
    }

    @Inject(optional = true)
    public void setSparkAttempts(@Named("spark.yarn.maxAppAttempts") Integer maxAttempts) {
        sparkConf.set("spark.yarn.maxAppAttempts", maxAttempts.toString());
        this.maxAttempts = maxAttempts;
    }

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public Boolean isSparkAllowMultipleContext() {
        return sparkAllowMultipleContext;
    }

    @Inject(optional = true)
    public void setSparkAllowMultipleContext(@Named("spark.driver.allowMultipleContexts") Boolean sparkAllowMultipleContext) {
        sparkConf.set("spark.driver.allowMultipleContexts", sparkAllowMultipleContext.toString());
        this.sparkAllowMultipleContext = sparkAllowMultipleContext;
    }

    public String getSparkSerializer() {
        return sparkSerializer;
    }

    @Inject(optional = true)
    public void setSparkSerializer(@Named("spark.serializer") String sparkSerializer) {
        this.sparkSerializer = sparkSerializer;
    }

    public String getSparkKryoRegistrator() {
        return sparkKryoRegistrator;
    }

    @Inject(optional = true)
    public void setSparkKryoRegistrator(@Named("spark.kryo.registrator") String sparkKryoRegistrator) {
        this.sparkKryoRegistrator = sparkKryoRegistrator;
    }

    @Inject(optional = true)
    public void setMaster(@Named("spark.master") String master) {
        this.master = master;
        sparkConf.setMaster(master);
    }

    public String getMaster() {
        return master;
    }

    @Override
    public SparkSession getContext() {
        sparkContext = SparkSession.builder().appName(sparkAppName).config(sparkConf).getOrCreate();
        return sparkContext;
    }


    @Override
    public void close() {
        //this.sparkContext.close();
    }

    @Override
    public void stop() {

        this.sparkContext.stop();
    }


}
