package se.ff.cc;

import com.prft.cif.conversion.MetadataWorkbook;
import com.prft.cif.guice.inject.CIFInjector;
import com.prft.cif.metadata.CIFDataset;
import com.prft.cif.rest.CIFRestClient;
import com.prft.cif.rest.NavigatorRestClient;
import com.prft.cif.util.CIFDatasetUtil;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
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
    File wbfile;
    CIFDataset dataset;

    @Before
    public void setUp() throws Exception {
        System.out.println("############### SetUp Phase @ Before ##################");
//        //set up REST client here
        restClient = CIFInjector.createInstance(NavigatorRestClient.class);
        System.out.println(restClient.toString());
        restClient.setUsername("csaload1");
        restClient.setPasswordPlain("C$@l0adP120d");
        restClient.setPasswordEncrypted("SthNm1MbsRMNcBUYw88hbA==:/crmsdMrCILWlZeaouNiMA==");
    }

    @Given("^I have parsed a workbook$")
    public void some_start_condition() throws Throwable {
        wbfile = new File("C:\\Users\\cwarne01\\Documents\\workbooks\\ME_FIN_Cash_Detail.xlsx");
        MetadataWorkbook metadataWorkbook = CIFInjector.createInstance(MetadataWorkbook.class, "workbookmapping.properties");
        dataset = metadataWorkbook.getDataset(wbfile, 0, "curate");
        Thread.sleep(5000);
        System.out.println("DATASET ::: "+dataset.toString());
        System.out.println("DATASET ATTRIBUTES ::: "+dataset.getAttributes());
        System.out.println("BUSINESS DOMAIN ::: "+dataset.getBusinessDomain());
        System.out.println("CONTACT EMAIL ::: "+dataset.getContactEmail());
    }

    @When("^I query Cloudera Navigator for the expected Hive database$")
    public void something_is_done() throws Throwable {
        String databaseName = CIFDatasetUtil.getDatabaseName(dataset);
        System.out.println("DATABASENAME     "+databaseName);
        String query = "?limit=2&offset=0&query=((type:database)AND(originalName:"+databaseName+")AND(sourceType:HIVE))";
        restClient.setQuery(query);
        response = restClient.get();
        System.out.println("$#$#$###$#$#"+response);
    }

    @Then("^I should see Hive DB created with appropriate name in the correct location$")
    public void something_should_happen() throws Throwable {
        JSONArray jsonArr = new JSONArray(response);
        System.out.println(jsonArr.getJSONObject(0).getString("sourceType"));
        assertEquals(jsonArr.getJSONObject(0).getString("sourceType"), "HIVE");
    }


}
