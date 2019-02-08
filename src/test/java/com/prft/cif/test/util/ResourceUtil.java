package com.prft.cif.test.util;

import java.util.ResourceBundle;

public class ResourceUtil {

    private static ResourceBundle rb =null;

    public void loadResources() throws Exception{
        rb = ResourceBundle.getBundle("cif");
    }
    public String getOnbCurStg() throws Exception{
        return rb.getString("onboarding.dir.curate.stg").trim();
    }
    public String getOnbpubStg() throws Exception{
        return rb.getString("onboarding.dir.publish.stg").trim();
    }
    public String getBaseStg() throws Exception{
        return rb.getString("onboarding.base.stg").trim();
    }
    public String getOnbDir() throws Exception{
        return rb.getString("onboarding.dir").trim();
    }
    public String getOnbDirPub() throws Exception{
        return rb.getString("onboarding.dir.publish").trim();
    }
    public String getDbUrl() throws Exception{
        return rb.getString("db.url").trim();
    }

}
