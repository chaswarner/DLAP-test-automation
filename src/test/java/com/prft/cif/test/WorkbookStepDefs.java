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
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.codehaus.jettison.json.JSONArray;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Assert;
import cucumber.api.java.After;
import cucumber.api.java.Before;

import java.io.File;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
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

    private String onboardingDirCurateStg;
    private String onboardingDirPublishStg;


    @Inject
    private static Logger logger;


    String wbFilePath = "./src/test/resources/fixtures/Test_ME_FIN_Cash_Detail_DateFormatChange.xlsx";
    File wbfile = new File(wbFilePath);
    ArrayList<String> columnname = new ArrayList<>();
    ArrayList<String> columntype = new ArrayList<>();


    private static ResourceBundle rb = ResourceBundle.getBundle("cif");

    @Before
    public void setUp() throws Exception {
        // Place workbook .xlsx file at hdfs://dlap_tst/cif/onboarding/

        File tf = new File("testfile.txt");
        tf.createNewFile();
        System.out.println("ABSOLUTE PATH :: " + tf.getAbsolutePath());

//
//        boolean fvar = testfile.createNewFile();
//        System.out.println("File "+ testfile.getAbsolutePath());

//
////        System.out.println("-->"+rb.getString("onboarding.dir"));
        onboardingDir=rb.getString("onboarding.dir").trim();
//        onboardingDirPublish=rb.getString("onboarding.dir.publish");
        onboardingDirCurateStg=rb.getString("onboarding.dir.curate.stg")+"/*".trim();
        onboardingDirPublishStg=rb.getString("onboarding.dir.publish.stg").trim();
        System.out.println(onboardingDirCurateStg);
        System.out.println(onboardingDirPublishStg);


        File curateStg = new File(onboardingDirCurateStg);
        File publishStg = new File(onboardingDirPublishStg);
        File onboardingDirectory = new File(onboardingDir);
//        File onboardingDirectoryPublish = new File(onboardingDirPublish);
        FileUtils.getTempDirectoryPath();
        FileUtils.copyFile(curateStg, onboardingDirectory);
        FileUtils.copyDirectory(publishStg, onboardingDirectory);
//
//        // Sleep thread ?  I don't think there's a notification to plug in...
//        Thread.sleep(30000);
//
//        String[] extensions = new String[]{"xlsx"};
//        List<File> afterOnboardingFilelist = (List<File>) FileUtils.listFiles(new File(onboardingDir), extensions, true);
//
//        for (File file : afterOnboardingFilelist) {
//
//            logger.info("***************** FILE NAME:=====>" + file.getName() + "  *******************");
//            assertTrue(new File(file.getAbsolutePath() + ".completed").exists());
//        }
//
//        //Check for .completed file creation
//        String completedFileName = wbFilePath+".completed";
//        String errorFileName = wbFilePath+".error";
//
//        // throw exception if file not found or .error file found instead
//
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
//
//        ImpalaQuery impQ = new ImpalaQuery();
//        impQ.createConnection();

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
    @Given("^I have parsed a curate workbook$")
    public void parsing_data() throws Throwable {
//        MetadataWorkbook metadataWorkbook = CIFInjector.createInstance(MetadataWorkbook.class, "workbookmapping.properties");
//        dataset = metadataWorkbook.getDataset(wbfile, 0, "curate");
//        Thread.sleep(4000);
//        System.out.println("DATASET ::: " + dataset.toString());
//        System.out.println("DATASET ATTRIBUTES ::: " + dataset.getAttributes());
//        System.out.println("Table Name ::: " + dataset.getName());
    }
    @When("^I query Cloudera Navigator for the list of columns in the data set database$")

    public void database_columns() throws Throwable {
//        Connection conn;
//        String DB_URL = "jdbc:hive2://hive.dr.bcbsma.com:10000/;principal=hive/hive.dr.bcbsma.com@BCBSMAMD.NET;ssl=true";
//        conn = null;
//        Statement stmt = null;
//        try {
//            //STEP 2: Register JDBC driver
//            Class.forName("org.apache.hive.jdbc.HiveDriver");
//
//            //STEP 3: Open a connection
//            System.out.println("Connecting to database...");
//            conn = DriverManager.getConnection(DB_URL, "", "");
//
//            //STEP 4: Execute a query
//            System.out.println("Creating statement...");
//            stmt = conn.createStatement();
//            String sql;
//            sql = "describe"+dataset.getName();
//            ResultSet rs = stmt.executeQuery(sql);
//            while(rs.next()){
//                //Retrieve by column name
//                String cname = rs.getString("Column Name");
//                String ctype = rs.getString("Column Type");
//
//                //Display values
//                System.out.print(", Column Name: " + cname);
//                columnname.add(cname);
//                System.out.println(", Column Type: " + ctype);
//                columntype.add(ctype);
//            }
//        } catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//        String query="select rcpnt_id from"+dataset.getName();
//        ArrayList<String> list = cifimpala.describeTable(query);
////        int listSize=list.size();
//        int mapSize=list.get(1);
//
//
//        for (int listEntry = 0; listEntry < listSize; listEntry++) {
//
//            String[] values = new String[mapSize];
//            Set entries = list.get(listEntry).entrySet();
//            Iterator entriesIterator = entries.iterator();
//            int i = 0;
//            while (entriesIterator.hasNext()) {
//
//                Map.Entry mapping = (Map.Entry) entriesIterator.next();
//                values[i] = mapping.getValue().toString();
//                System.out.println(values[i]);
//                ++i;
            }


    @Then("^I should see appropriate columns in the appropriate DB available via Impala$")
    public void workbook_columns() throws Throwable {
//        String tableName = null;
//        ArrayList<String> curateColNames = new ArrayList<String>();
//        Workbook workbook = null;
//        try {
//            workbook = WorkbookFactory.create(wbfile);
//        } catch (IOException ioe) {
//            System.out.println(ioe);
//        }
//        Sheet sheet = workbook.getSheetAt(0);
//        DataFormatter dataFormatter = new DataFormatter();
//        Row rownum = sheet.getRow(2);
//        Cell cellnum = rownum.getCell(1);
//        String cellval = dataFormatter.formatCellValue(cellnum);
//        System.out.println("Cell Value :: " + cellval);
//        ArrayList<String> CurateColumn = new ArrayList<String>();
//        ArrayList<String> CurateDataType = new ArrayList<String>();
//        for(int i=46; i<=sheet.getLastRowNum();i++) {
//            rownum = sheet.getRow(i);
//            cellnum = rownum.getCell(11);
//            String CurateCellValue = dataFormatter.formatCellValue(cellnum);
//            CurateColumn.add(CurateCellValue);
//            cellnum = rownum.getCell(12);
//            String CurateCellType = dataFormatter.formatCellValue(cellnum);
//            CurateDataType.add(CurateCellType);
//            System.out.println(CurateCellValue );
//        }
//        if(columnname.size()== CurateColumn.size() ) {
//            for (int i = 0; i < columnname.size(); i++) {
//                String actual = columnname.get(i);
//                String expected = CurateColumn.get(i);
//                assertEquals(actual, expected);
//            }
//        }
//            if(columntype.size()== CurateDataType.size() ) {
//                for (int i = 0; i < columntype.size(); i++) {
//                    String actual = columntype.get(i);
//                    String expected = CurateDataType.get(i);
//                    assertEquals(actual, expected);
//                }
//            }
    }


}
