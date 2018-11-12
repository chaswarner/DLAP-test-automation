package com.prft.cif.test.rest;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class NavigatorRestClient extends CIFRestClient {

    @Override
    @Inject
    public void setRestApiURL(@Named("navigator.api.url") String url) {
        super.setRestApiURL(url);
    }

}
