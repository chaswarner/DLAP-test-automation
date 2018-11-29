package com.prft.cif.test;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;

import java.io.File;
import java.util.List;

public class CurateStepDefs {

    String response;
    String body = "{\"name\":\"tenant\"}";
    String query = "metadata?key=test_curate_fin";
    File wbfile;


    @Before
    public void setUp() throws Exception {

//        //set up REST client


    }

    @Given("^a data set in the RAW zone and its counterpart in the curate zone$")
    public void some_start_condition() throws Throwable {
//        wbfile = new File("./src/test/resources/fixtures/ME_FIN_Cash_Detail.xlsx");
//        MetadataWorkbook metadataWorkbook = CIFInjector.createInstance(MetadataWorkbook.class, "workbookmapping.properties");
//        dataset = metadataWorkbook.getDataset(wbfile, 0, "curate");
//        System.out.println("DATASET ::: "+dataset.toString());
//        System.out.println("DATASET ATTRIBUTES ::: "+dataset.getAttributes());
//        System.out.println("BUSINESS DOMAIN ::: "+dataset.getBusinessDomain());
//        System.out.println("CONTACT EMAIL ::: "+dataset.getContactEmail());
    }

    @When("^I query Cloudera Navigator for the data set's onboarded workbook metadata$")
    public void something_is_done() throws Throwable {
////    Query Cloudera
//        restClient = CIFInjector.createInstance(NavigatorRestClient.class);
//        restClient.setUsername("csaload1");
//        restClient.setPasswordPlain("C$@l0adP120d");
//        restClient.setPasswordEncrypted("SthNm1MbsRMNcBUYw88hbA==:/crmsdMrCILWlZeaouNiMA==");
//        restClient.setEndpoint("63205918");
//        String completeHiveTableName = CIFDatasetUtil.getSCDHiveTableName(dataset);
//        CIFSchema schema = dataset.getSchemas().get(0);
//
//        String columnName = schema.getName();
//        String hiveTableName = completeHiveTableName.split("\\.")[1];
//        String databaseName = completeHiveTableName.split("\\.")[0];
//        String query = "?query=(sourceType%3AHive)AND(originalName%3A" + columnName + ")AND(parentPath%3A*%2F" + databaseName + "%2F" + hiveTableName + ")";
//        restClient.setQuery(query);
//        String getResponse = restClient.get();
//        JSONObject jsonObj = null;
//        String identity = "";
//
////        jsonObj = new JSONObject(getResponse.substring(1, getResponse.length() - 1));
//        System.out.println("#$##$##$$$#$#$##$####$      DATASET/OBJECT IDENTITY   33333333333333333333333");
////        System.out.println("identity ===>" + jsonObj.get("identity"));
//        System.out.println("identity ===>" + getResponse);
//        System.out.println("#$##$##$$$#$#$##$####$33333333333333333333333");
////        identity = jsonObj.get("identity").toString();
//
////        response = restClient.put(body, CIFRestClient.CONTENT_TYPE_JSON);
//        System.out.println(response);

    }

    @Then("^fields flagged for encryption in the metadata should be actually encrypted in the curate zone$")
    public void something_should_happen() throws Throwable {
//        JSONObject jsonObj = new JSONObject(response);
//        assertEquals(jsonObj.getString("name"), "tenant");
    }

}
