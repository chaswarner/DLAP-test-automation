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
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.codehaus.jettison.json.JSONArray;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Assert;
import cucumber.api.java.After;
import cucumber.api.java.Before;

import java.io.File;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

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
    private String onboardingDirPublih;
    private String onboardingBaseStg;
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
//
//        onboardingDirCurateStg=rb.getString("onboarding.dir.curate.stg").trim();
//        onboardingDirPublishStg=rb.getString("onboarding.dir.publish.stg").trim();
//        onboardingBaseStg=rb.getString("onboarding.base.stg").trim();
//        onboardingDir=rb.getString("onboarding.dir").trim();
//        onboardingDirPublih=rb.getString("onboarding.dir.publish").trim();


//        System.out.println("copied from "+onboardingDirCurateStg+" -->"+onboardingDir);
//        FileUtils.copyDirectory(new File(onboardingDirCurateStg), new File(onboardingDir));
//        System.out.println("copied from "+onboardingDirPublishStg+" -->"+onboardingDirPublih);
//        FileUtils.copyDirectory(new File(onboardingDirPublishStg), new File(onboardingDirPublih));

        // Sleep thread ?  I don't think there's a notification to plug in...
//        Thread.sleep(30000);

//        for (File file : beforeOnboardingFilelist) {
//
////            System.out.println("before onboarding file list "+file.getAbsolutePath());
////            System.out.println("Absolute File path with completed "+file.getAbsolutePath() + ".completed");
////            System.out.println("Onboarding File path with completed "+""+onboardingDir+"\\"+file.getName() + ".completed");
//            if(file.getParent().endsWith("publish"))
//                assertTrue(new File(""+onboardingDirPublih+"\\"+file.getName() + ".completed").exists());
//            else
//                assertTrue(new File(""+onboardingDir+"\\"+file.getName() + ".completed").exists());
//        }




        //Check for .completed file creation
        String completedFileName = wbFilePath+".completed";
        String errorFileName = wbFilePath+".error";
        System.setProperty("user.name","csaload1");


        // throw exception if file not found or .error file found instead


        // Set-up REST client
        restClient = CIFInjector.createInstance(NavigatorRestClient.class);
        restClient.setUsername("csaload1");
        restClient.setPasswordPlain("C$@l0adP120d");
        restClient.setPasswordEncrypted("SthNm1MbsRMNcBUYw88hbA==:/crmsdMrCILWlZeaouNiMA==");
        String[] extensions = new String[]{"xlsx"};
//        List<File> beforeOnboardingFilelist = (List<File>) FileUtils.listFiles(new File("/"));
        File root = new File("/");
        System.out.println("###################################################$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//        System.out.println(beforeOnboardingFilelist.toString());
        System.out.println(root.getAbsolutePath() + "::"+root.getPath());
        System.out.println("###################################################$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

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

    @Given("^I have parsed a curate workbook$")
    public void parsing_data() throws Throwable {
        MetadataWorkbook metadataWorkbook = CIFInjector.createInstance(MetadataWorkbook.class, "workbookmapping.properties");
        dataset = metadataWorkbook.getDataset(wbfile, 0, "curate");
        System.out.println("DATASET ::: " + dataset.toString());
        System.out.println("DATASET ATTRIBUTES ::: " + dataset.getAttributes());
        System.out.println("Table Name ::: " + dataset.getName());
    }

    @When("^I query HBase for the row keys in the data set$")

    public void database_columns() throws Throwable {
         Connection conn =null;
         Configuration conf=null;
         TableName tableName = TableName.valueOf("dev_cif:filepattern");
         conf = HBaseConfiguration.create();
         conf.set("hbase.zookeeper.quorum", "mclmp01vr.bcbsma.com,mclmp02vr.bcbsma.com,mclmp03vr.bcbsma.com");
         conf.set("hbase.zookeeper.property.clientPort", "2181");
        System.out.println("conf set - ================================");
        System.out.println(conf.toString());
         conn = ConnectionFactory.createConnection(conf);
         System.out.println("Connection created successfully *@$#%@$#%@$#$##@#@@#$@#$@#$");
/*        Admin admin = conn.getAdmin();
        if (!admin.tableExists(tableName)) {
            admin.createTable(new HTableDescriptor(tableName).addFamily(new HColumnDescriptor("cf")));
        }*/

        // Instantiating HBaseAdmin class
        HBaseAdmin admin = new HBaseAdmin(conf);

        // Getting all the list of tables using HBaseAdmin object
        HTableDescriptor[] tableDescriptor = admin.listTables();

        // printing all the table names.
        for (int i=0; i<tableDescriptor.length;i++ ) {
            System.out.println(" #$$###$#$##$@$#%@$#%@$#$##@#@@#$@#$@#$ TABLE NAMES  :: ");
            System.out.println(tableDescriptor[i].getNameAsString());
        }
        Table table = conn.getTable(tableName);

        Scan scan = new Scan();
        ResultScanner scanner1 = table.getScanner(scan);
        System.out.println("Scan - line 190... #$$###$#$##$ *@$#%@$#%@$#$##@#@@#$@#$@#$");

        for (Result scn :scanner1){
            System.out.println("Hbase table scan-->"+scn);
            System.out.println("Key **>"+table.get(new Get(Bytes.toBytes("mo_.*_(fx|di)_.*_cddm.csv.*"))));
        }
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
