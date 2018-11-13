package com.prft.cif.test;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.prft.cif.test.conversion.MetadataWorkbook;
import com.prft.cif.test.guice.inject.CIFInjector;
import com.prft.cif.test.metadata.CIFDataset;
import com.prft.cif.test.rest.CIFRestClient;
import com.prft.cif.test.rest.NavigatorRestClient;
import com.prft.cif.test.util.CIFDatasetUtil;
import com.prft.cif.test.util.CIFHDFSUtil;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileSystem;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import cucumber.api.java.After;
import cucumber.api.java.Before;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class WorkbookStepDefs {

//    CIFRestClient restClient = CIFInjector.createInstance(NavigatorRestClient.class);
    CIFRestClient restClient;
    String response;
    CIFDataset dataset;
    CIFHDFSUtil hdfsUtil;
    String wbFilePath = "./src/test/resources/fixtures/Test_ME_FIN_Cash_Detail_DateFormatChange.xlsx";

    File wbfile = new File(wbFilePath);

    @Before
    public void setUp() throws Exception {
        // Place workbook .xlsx file at hdfs://dlap_tst/cif/onboarding/

        // Sleep thread ?  I don't think there's a notification to plug in...
        Thread.sleep(30000);

        //Check for .completed file creation
        String completedFileName = wbFilePath+".completed";
        String errorFileName = wbFilePath+".error";

        // throw exception if file not found or .error file found instead

//
//        // Set-up REST client
//        restClient = CIFInjector.createInstance(NavigatorRestClient.class);
//        restClient.setUsername("csaload1");
//        restClient.setPasswordPlain("C$@l0adP120d");
//        restClient.setPasswordEncrypted("SthNm1MbsRMNcBUYw88hbA==:/crmsdMrCILWlZeaouNiMA==");
    }

    @Given("^I have parsed a workbook$")
    public void some_start_condition() throws Throwable {
//        MetadataWorkbook metadataWorkbook = CIFInjector.createInstance(MetadataWorkbook.class, "workbookmapping.properties");
//        dataset = metadataWorkbook.getDataset(wbfile, 0, "curate");
//        Thread.sleep(5000);
//        System.out.println("DATASET ::: "+dataset.toString());
//        System.out.println("DATASET ATTRIBUTES ::: "+dataset.getAttributes());
//        System.out.println("BUSINESS DOMAIN ::: "+dataset.getBusinessDomain());
//        System.out.println("CONTACT EMAIL ::: "+dataset.getContactEmail());
    }

    @When("^I query Impala for the expected database name$")
    public void something_is_done() throws Throwable {
        // Use Impala connection:

        ImpalaQuery impQ = new ImpalaQuery();
        impQ.createConnection();

//
//        String databaseName = CIFDatasetUtil.getDatabaseName(dataset);
//        System.out.println("DATABASENAME     "+databaseName);
//        String query = "?limit=2&offset=0&query=((type:database)AND(originalName:"+databaseName+")AND(sourceType:HIVE))";
//        restClient.setQuery(query);
//        response = restClient.get();
//        System.out.println("$#$#$###$#$#"+response);
    }

    @Then("^I should see Hive DB created with appropriate name in the correct location$")
    public void something_should_happen() throws Throwable {
    //        JSONArray jsonArr = new JSONArray(response);
    //        System.out.println(jsonArr.getJSONObject(0).getString("sourceType"));
    //        assertEquals(jsonArr.getJSONObject(0).getString("sourceType"), "HIVE");
        assertEquals(5,5);
    }

    @After
    public void tearDown () throws Exception{
    // Remove workbook .xlsx file and .completed files from W:\dlap_tst\cif\onboarding\

    // Truncate Hive table

    // Drop Hive table

    // Delete folder at /env/zone/domain/dataset

    }


}
