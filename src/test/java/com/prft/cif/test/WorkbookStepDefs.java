package com.prft.cif.test;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;

import org.junit.Assert;
import cucumber.api.java.After;
import cucumber.api.java.Before;

import java.io.File;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WorkbookStepDefs {

    //    CIFRestClient restClient = CIFInjector.createInstance(NavigatorRestClient.class);
    CIFRestClient restClient;
    String response;
    CIFDataset dataset;

/*    @Inject
    private CIFHDFSUtil hdfsUtil;*/


    private String onboardingDir;
    //    private String onboardingDirPublish;
    private String onboardingDirCurateStg;
    private String onboardingDirPublishStg;


    @Inject
    private static Logger logger;


    String wbFilePath = "./src/test/resources/fixtures/ME_FIN_Cash_Detail.xlsx";
    File wbfile = new File(wbFilePath);

    private static ResourceBundle rb = ResourceBundle.getBundle("cif");

    @Before
    public void setUp() throws Exception {
        // Place workbook .xlsx file at hdfs://dlap_tst/cif/onboarding/

//        System.out.println("-->"+rb.getString("onboarding.dir"));
        onboardingDir=rb.getString("onboarding.dir").trim();
//        onboardingDirPublish=rb.getString("onboarding.dir.publish");
        onboardingDirCurateStg=rb.getString("onboarding.dir.curate.stg")+"/*".trim();
        onboardingDirPublishStg=rb.getString("onboarding.dir.publish.stg").trim();


        File curateStg = new File(onboardingDirCurateStg);
        File publishStg = new File(onboardingDirPublishStg);
        File onboardingDirectory = new File(onboardingDir);
//        File onboardingDirectoryPublish = new File(onboardingDirPublish);

        FileUtils.copyFile(curateStg, onboardingDirectory);
        FileUtils.copyDirectory(publishStg, onboardingDirectory);

        // Sleep thread ?  I don't think there's a notification to plug in...
        Thread.sleep(30000);

        String[] extensions = new String[]{"xlsx"};
        List<File> afterOnboardingFilelist = (List<File>) FileUtils.listFiles(new File(onboardingDir), extensions, true);

        for (File file : afterOnboardingFilelist) {

            logger.info("***************** FILE NAME:=====>" + file.getName() + "  *******************");
            assertTrue(new File(file.getAbsolutePath() + ".completed").exists());
        }

        //Check for .completed file creation
        String completedFileName = wbFilePath+".completed";
        String errorFileName = wbFilePath+".error";

        // throw exception if file not found or .error file found instead


        // Set-up REST client
        restClient = CIFInjector.createInstance(NavigatorRestClient.class);
        restClient.setUsername("csaload1");
        restClient.setPasswordPlain("C$@l0adP120d");
        restClient.setPasswordEncrypted("SthNm1MbsRMNcBUYw88hbA==:/crmsdMrCILWlZeaouNiMA==");
    }


    @Given("^I have parsed a workbook$")
    public void some_start_condition() throws Throwable {
        MetadataWorkbook metadataWorkbook = CIFInjector.createInstance(MetadataWorkbook.class, "workbookmapping.properties");
        dataset = metadataWorkbook.getDataset(wbfile, 0, "curate");
        Thread.sleep(5000);
        System.out.println("DATASET ::: "+dataset.toString());
        System.out.println("DATASET ATTRIBUTES ::: "+dataset.getAttributes());
        System.out.println("BUSINESS DOMAIN ::: "+dataset.getBusinessDomain());
        System.out.println("CONTACT EMAIL ::: "+dataset.getContactEmail());
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
