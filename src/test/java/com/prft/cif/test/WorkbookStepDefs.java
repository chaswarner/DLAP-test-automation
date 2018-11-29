package com.prft.cif.test;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
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

    String response;
/*    @Inject
    private CIFHDFSUtil hdfsUtil;*/


    private String onboardingDir;
    private String onboardingDirPublih;
    private String onboardingBaseStg;
    private String onboardingDirCurateStg;
    private String onboardingDirPublishStg;
    private static Connection conn =null;
    private static Configuration conf=null;



    String wbFilePath = "./src/test/resources/fixtures/Test_ME_FIN_Cash_Detail_DateFormatChange.xlsx";
    File wbfile = new File(wbFilePath);
    ArrayList<String> columnname = new ArrayList<>();
    ArrayList<String> wbcolumnnames = new ArrayList<>();
    ArrayList<String> dbcolumnnames = new ArrayList<>();
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
//        System.setProperty("java.security.krb5.conf", "src/main/resources/krb5.conf");
//        System.setProperty("sun.security.krb5.debug", "true");




        // throw exception if file not found or .error file found instead


        // Set-up REST client
//

    }

    @Given("^I have parsed a workbook$")
    public void some_start_condition() throws Throwable {
        String tableName = null;
        ArrayList<String> curateColNames = new ArrayList<String>();
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(wbfile);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        org.apache.poi.ss.usermodel.Row rownum = sheet.getRow(2);
        Cell cellnum = rownum.getCell(1);
        String cellval = dataFormatter.formatCellValue(cellnum);
        System.out.println("Cell Value :: " + cellval);
        ArrayList<String> CurateColumn = new ArrayList<String>();
        ArrayList<String> CurateDataType = new ArrayList<String>();
        for(int i=46; i<=sheet.getLastRowNum();i++) {
            rownum = sheet.getRow(i);
            cellnum = rownum.getCell(11);
            String CurateCellValue = dataFormatter.formatCellValue(cellnum);
            CurateColumn.add(CurateCellValue);
            cellnum = rownum.getCell(12);
            String CurateCellType = dataFormatter.formatCellValue(cellnum);
            CurateDataType.add(CurateCellType);
            System.out.println(CurateCellValue);
            wbcolumnnames.add(CurateCellValue);
        }
        if(columnname.size()== CurateColumn.size() ) {
            for (int i = 0; i < columnname.size(); i++) {
                String actual = columnname.get(i);
                String expected = CurateColumn.get(i);
                assertEquals(actual, expected);
            }
        }
            if(columntype.size()== CurateDataType.size() ) {
                for (int i = 0; i < columntype.size(); i++) {
                    String actual = columntype.get(i);
                    String expected = CurateDataType.get(i);
                    assertEquals(actual, expected);
                }
            }
    }

    @When("^I query Impala for the expected database name$")
    public void something_is_done() throws Throwable {
        java.sql.Connection conn;
        String DB_URL = "jdbc:hive2://impala.dr.bcbsma.com:21050/;principal=impala/impala.dr.bcbsma.com@BCBSMAMD.NET;ssl=true";
//        String DB_URL = "jdbc:hive2://hive.dr.bcbsma.com:10000/;principal=hive/hive.dr.bcbsma.com@BCBSMAMD.NET;ssl=true";

        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            //STEP 3: Open a connection-
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, "", "");
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "describe test_curate_fin.cash_detail";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                //Retrieve by column name
                String cname = rs.getString("name");
//                String ctype = rs.getString("Column Type");

                //Display values
                columnname.add(cname);
                System.out.println("COLUMN NAME  :  "+cname);
//                System.out.println(", Column Type: " + ctype);
//                columntype.add(ctype);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Then("^I should see Hive DB created with appropriate name in the correct location$")
    public void something_should_happen() throws Throwable {
// compare columnname array with wbcolumnnames array
        System.out.println("WB Column names   :  "+wbcolumnnames.toString());
        System.out.println("DB Column names   :  "+columnname.toString());
        System.out.println(columnname.equals(wbcolumnnames));
        assertEquals(wbcolumnnames,columnname);
    }

    @Given("^I have parsed a curate workbook$")
    public void parsing_data() throws Throwable {
//        MetadataWorkbook metadataWorkbook = CIFInjector.createInstance(MetadataWorkbook.class, "workbookmapping.properties");
//        dataset = metadataWorkbook.getDataset(wbfile, 0, "curate");
//        System.out.println("DATASET ::: " + dataset.toString());
//        System.out.println("DATASET ATTRIBUTES ::: " + dataset.getAttributes());
//        System.out.println("Table Name ::: " + dataset.getName());
    }

    @When("^I query HBase for the row keys in the data set$")

    public void database_columns() throws Throwable {
        TableName tableName = TableName.valueOf("dev_cif:filepattern");

        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "mclmp01vr.bcbsma.com,mclmp02vr.bcbsma.com,mclmp03vr.bcbsma.com");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
//        conf.set("hadoop.security.authentication", "kerberos");
//        conf.set("hbase.security.authentication", "kerberos");
//        conf.set("hbase.master.kerberos.principal", "hbase/_HOST@BCBSMAMD.NET");
//        conf.set("hbase.master.keytab.file", "src/test/resources/krb5.conf");
//        conf.set("hbase.regionserver.kerberos.principal", "hbase/_HOST@BCBSMAMD.NET");
//        conf.set("hbase.regionserver.keytab.file", "src/test/resources/krb5.conf");
        conn = ConnectionFactory.createConnection(conf);
        System.out.println("Connection Success - 0");

/*        Admin admin = conn.getAdmin();
        if (!admin.tableExists(tableName)) {
            admin.createTable(new HTableDescriptor(tableName).addFamily(new HColumnDescriptor("cf")));
        }*/

        Table table = conn.getTable(tableName);
        System.out.println("Conn.getTable Success - 0    : : " +table.toString());
        Scan scan = new Scan();
        System.out.println("Scan Success - 0");
        ResultScanner scanner1 = table.getScanner(scan);
        System.out.println("Scan Success - 1  ::    "+ scanner1.toString());
        for (Result scn :scanner1) {
//            String key = Bytes.toString(scn.getRow());
            System.out.println("Hbase table scan-->" + scn);
//            System.out.println("Key **>" + table.get(new Get(Bytes.toBytes("mo_.*_(fx|di)_.*_cddm.csv.*"))));
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
