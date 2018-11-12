package com.prft.cif.test.rest;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class HBaseRestClient extends CIFRestClient {
    @Override
    @Inject
    public void setRestApiURL(@Named("hbase.api.url") String endpoint) {
        super.setRestApiURL(endpoint);
    }
}
