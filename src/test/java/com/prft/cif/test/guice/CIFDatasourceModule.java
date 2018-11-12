package com.prft.cif.test.guice;

import com.google.inject.PrivateModule;
import com.google.inject.name.Names;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.builtin.UnpooledDataSourceProvider;

import java.util.Properties;
import java.util.Set;


//public class CIFDatasourceModule extends PrivateModule {
//
//    private String configFile;
//    private Class mapperClass;
//
//    public CIFDatasourceModule(String configFile, Class mapperClass) {
//        this.configFile = configFile;
//        this.mapperClass = mapperClass;
//    }
//
//    @Override
//    protected void configure() {
//
//        install(new XMLMyBatisModule() {
//            @Override
//            protected void initialize() {
//                setClassPathResource(configFile);
//            }
//
//        });
//
//        expose(mapperClass);
//    }
//}


public class CIFDatasourceModule extends PrivateModule {

    private String resource;
    private Class mapperClass;

    public CIFDatasourceModule(String resource, Class mapperClass) {
        this.resource = resource;
        this.mapperClass = mapperClass;
    }

    @Override
    protected void configure() {
        install(new MyBatisModule() {
            @Override
            protected void initialize() {
                try {
                    environmentId("datasource");
                    Properties myBatisProperties = new Properties();
                    myBatisProperties.load(this.getClass().getClassLoader().getResourceAsStream(resource));

                    boolean pooled = false;
                    Set keys = myBatisProperties.keySet();
                    for (Object key : keys) {
                        if (key.toString().toLowerCase().startsWith("pool")) {
                            pooled = true;
                        }
                    }
                    if (pooled)
                        bindDataSourceProviderType(PooledDataSourceProvider.class);
                    else
                        bindDataSourceProviderType(UnpooledDataSourceProvider.class);
                    bindTransactionFactoryType(JdbcTransactionFactory.class);
                    addMapperClass(mapperClass);
                    Names.bindProperties(binder(), myBatisProperties);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });
        expose(mapperClass);
    }
}
