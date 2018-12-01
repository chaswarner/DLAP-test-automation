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
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.codehaus.jettison.json.JSONArray;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.nio.file.Path;
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
import static org.junit.Assert.assertNotNull;
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
    private static final String env = "test_";
    String sourceSystemCode = null, finalRowKeyName;
    String tableName;
    String[] metadataCellVals;
    String rowkey;
    Result rowkeyresult;

    String wbFilePath = "./target/test-classes/fixtures/Test_ME_FIN_Cash_Detail_DateFormatChange.xlsx";
    File wbfile = new File(wbFilePath);
    ArrayList<String> columnname = new ArrayList<>();
    ArrayList<String> wbcolumnnames = new ArrayList<>();
    ArrayList<String> dbcolumnnames = new ArrayList<>();
    ArrayList<String> columntype = new ArrayList<>();


    private static ResourceBundle rb = ResourceBundle.getBundle("cif");

    @Before
    public void setUp() throws Exception {
        // Place workbook .xlsx file at hdfs://dlap_tst/cif/onboarding/
        ResourceBundle rb = ResourceBundle.getBundle("cif");

//        onboardingDirCurateStg = rb.getString("onboarding.dir.curate.stg").trim();
//        onboardingDirPublishStg = rb.getString("onboarding.dir.publish.stg").trim();
//        onboardingBaseStg = rb.getString("onboarding.base.stg").trim();
          onboardingDir = rb.getString("onboarding.dir").trim();
//        onboardingDirPublih = rb.getString("onboarding.dir.publish").trim();
//        datafileStgDir = rb.getString("datafile.stg").trim();
//        hdfsDatafileStgDir = rb.getString("hdfs.staging.folder").trim();
//        prefixDataFile = rb.getString("prefix.data.file").trim();
//        postfixDataFile = rb.getString("postfix.data.file").trim();
//
//        String[] extensions = new String[]{"xlsx"};
//        List<File> beforeOnboardingFilelist = (List<File>) FileUtils.listFiles(new File(onboardingBaseStg), extensions, true);
//
//        System.out.println("copied from local to local "+onboardingDirCurateStg+" -->"+onboardingDir);
//        FileUtils.copyDirectory(new File(onboardingDirCurateStg), new File(onboardingDir));
//        System.out.println("copied from local to local "+onboardingDirPublishStg+" -->"+onboardingDirPublih);
//        FileUtils.copyDirectory(new File(onboardingDirPublishStg), new File(onboardingDirPublih));
//        FileUtils.copyFile(wbfile, new File("/dlap_tst/cif/onboarding/Test_ME_FIN_Cash_Detail_DateFormatChange.xlsx"));
        System.out.println("COPYING WORKBOOK TO ONBOARDING DIRECTORY");
        FileUtils.copyFileToDirectory(wbfile,new File(onboardingDir));
        System.out.println("WAITING 30 SECONDS .......");
//        // Sleep thread ?  I don't think there's a notification to plug in...
        Thread.sleep(30000);
//
        System.out.println(".completed FILE EXISTS  ?  :  "+new File("/dlap_tst/cif/onboarding/Test_ME_FIN_Cash_Detail_DateFormatChange.xlsx.completed").exists());
        System.out.println(".error     FILE EXISTS  ?  :  "+new File("/dlap_tst/cif/onboarding/Test_ME_FIN_Cash_Detail_DateFormatChange.xlsx.error").exists());

//        for (File file : beforeOnboardingFilelist) {
//
//            System.out.println("before onboarding file list " + file.getAbsolutePath());
////            System.out.println("Absolute File path with completed "+file.getAbsolutePath() + ".completed");
////            System.out.println("Onboarding File path with completed "+""+onboardingDir+"\\"+file.getName() + ".completed");
//            if (file.getParent().endsWith("publish"))
//                assertTrue(new File("" + onboardingDirPublih + "/" + file.getName() + ".completed").exists());
//            else
//                assertTrue(new File("" + onboardingDir + "/" + file.getName() + ".completed").exists());
//
//        }


        // throw exception if file not found or .error file found instead

    }

    @Given("^I have parsed a workbook$")
    public void some_start_condition() throws Throwable {
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
        tableName = cellval;
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

    @When("^I query Hive for the expected database name$")
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
            String invalidateSql = "invalidate metadata test_curate_fin.cif_test_cash_detail";
            String invalidatescdSql = "invalidate metadata test_curate_fin.cif_test_cash_detail_scd";
            stmt.execute(invalidatescdSql);
            stmt.execute(invalidateSql);
            sql = "describe "+env+"curate_fin.cif_test_cash_detail";
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

    @Then("^I should see Hive DB columns that match the columns from the workbook$")
    public void something_should_happen() throws Throwable {
// compare columnname array with wbcolumnnames array
        wbcolumnnames.remove("Column Name");
        wbcolumnnames.add("timestamp_");
        System.out.println("WB Column names   :  "+wbcolumnnames.toString());
        System.out.println("DB Column names   :  "+columnname.toString());
        System.out.println(columnname.equals(wbcolumnnames));
        assertEquals(wbcolumnnames,columnname);
    }

    @Given("^I have parsed a curate workbook$")
    public void parsing_data() throws Throwable {
        TableName tableName = TableName.valueOf("test_cif:dataset");

        //Extracting the row key from workbook using POI
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
//        String cellval = dataFormatter.formatCellValue(cellnum);
        metadataCellVals = new String[10];
        int j =0;
        System.out.println(Arrays.toString(metadataCellVals));
        for(int i = 2; i<=5; i++){
            rownum = sheet.getRow(i);
            cellnum = rownum.getCell(1);
            System.out.println("CELLNUM$$$$$$$$$   ::  "+cellnum);
            System.out.println("J  $$$$$$$$$   ::  "+j);
            metadataCellVals[j] = dataFormatter.formatCellValue(cellnum);
            j++;
        }
        System.out.println(metadataCellVals);
        if (metadataCellVals[2].equals("publish")){
            metadataCellVals[2] = "publish";
        }else {
            metadataCellVals[2] = "curate";
        }
        finalRowKeyName = metadataCellVals[2] + "_" + metadataCellVals[1] + "." + metadataCellVals[0] + "." + metadataCellVals[3];
        System.out.println("finalrowkeyname   ::  "+finalRowKeyName);
    }

    @When("^I query HBase for the row keys in the data set$")

    public void database_columns() throws Throwable {
        TableName tableName = TableName.valueOf("test_cif:dataset");

        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "mclmp01vr.bcbsma.com,mclmp02vr.bcbsma.com,mclmp03vr.bcbsma.com");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conn = ConnectionFactory.createConnection(conf);
        System.out.println("Connection Success - 0");
        Table table = conn.getTable(tableName);
        System.out.println("Conn.getTable Success - 0    : : " +table.toString());
        System.out.println("ROWKEYVALUES    ::   "+table.get(new Get(Bytes.toBytes(finalRowKeyName))));
        rowkeyresult = table.get(new Get(Bytes.toBytes(finalRowKeyName)));

    }


    @Then("^I should see expected row keys in hbase$")
    public void workbook_columns() throws Throwable {
        assertNotNull(rowkeyresult);
    }

    @After
    public void tearDown() throws Exception {
        if (new File("/dlap_tst/cif/onboarding/Test_ME_FIN_Cash_Detail_DateFormatChange.xlsx.completed").exists()){
            System.out.println("DELETING .completed FILE");
            FileUtils.forceDelete(new File("/dlap_tst/cif/onboarding/Test_ME_FIN_Cash_Detail_DateFormatChange.xlsx.completed"));

        } else if (new File("/dlap_tst/cif/onboarding/Test_ME_FIN_Cash_Detail_DateFormatChange.xlsx.error").exists()) {
            System.out.println("DELETING .error FILE");
            FileUtils.forceDelete(new File("/dlap_tst/cif/onboarding/Test_ME_FIN_Cash_Detail_DateFormatChange.xlsx.error"));
        }

        System.out.println("DELETING HBASE ROW KEY");
        TableName tableName = TableName.valueOf("test_cif:filepattern");
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "mclmp01vr.bcbsma.com,mclmp02vr.bcbsma.com,mclmp03vr.bcbsma.com");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conn = ConnectionFactory.createConnection(conf);
        Table table = conn.getTable(tableName);
        table.delete(new Delete(Bytes.toBytes("curate_fin.cif_test_cash_detail.v1")));
        tableName = TableName.valueOf("test_cif:dataset");
        table = conn.getTable(tableName);
        table.delete(new Delete(Bytes.toBytes("curate_fin.cif_test_cash_detail.v1")));

        System.out.println("TRUNCATING IMPALA TABLE");


        System.out.println("FINAL TABLE NAME TO TRUNCATE AND DROP  :  test_curate_fin.cif_test_cash_detail");
        Statement stmt = null;
        java.sql.Connection conn;
        String DB_URL = "jdbc:hive2://impala.dr.bcbsma.com:21050/;principal=impala/impala.dr.bcbsma.com@BCBSMAMD.NET;ssl=true";

//        String DB_URL = "jdbc:hive2://hive.dr.bcbsma.com:10000/;principal=hive/hive.dr.bcbsma.com@BCBSMAMD.NET;ssl=true";
        try {

            Class.forName("org.apache.hive.jdbc.HiveDriver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, "", "");
            stmt = conn.createStatement();
            String truncateSql;
            String invalidateSql = "invalidate metadata test_curate_fin.cif_test_cash_detail";
            String invalidatescdSql = "invalidate metadata test_curate_fin.cif_test_cash_detail_scd";
            stmt.execute(invalidatescdSql);
            stmt.execute(invalidateSql);
            truncateSql = "truncate table test_curate_fin.cif_test_cash_detail";
            stmt.execute(truncateSql);

        } catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("DROPPING IMPALA TABLE");

        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, "", "");
            stmt = conn.createStatement();
            String dropSql;
            dropSql = "drop table "+finalTableName;
            String dropScdSql = "drop table "+finalTableName+"_scd";
            System.out.println(dropSql);
            System.out.println(dropScdSql);
            String invalidateSql = "invalidate metadata test_curate_fin.cif_test_cash_detail";
            String invalidatescdSql = "invalidate metadata test_curate_fin.cif_test_cash_detail_scd";
            stmt.execute(invalidatescdSql);
            stmt.execute(invalidateSql);
            stmt.execute(dropSql);
            stmt.execute(dropScdSql);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
