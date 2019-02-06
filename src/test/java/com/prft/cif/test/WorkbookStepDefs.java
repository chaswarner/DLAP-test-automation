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
import org.junit.BeforeClass;

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
    private String dbURL;
    private static Connection conn = null;
    private static Configuration conf = null;
    private static final String env = "test_";
    String sourceSystemCode = null;
    private String finalRowKeyName;
    private String hiveTableName;
    private String hiveSCDTableName;

    String tableName;
    String[] metadataCellVals;
    String rowkey;
    Result rowkeyresult;

    ////    String wbFilePath = "./target/test-classes/fixtures/Test_ME_FIN_Cash_Detail_DateFormatChange.xlsx";
//    File wbfile = new File(wbFilePath);
    ArrayList<String> columnname = new ArrayList<>();
    ArrayList<String> wbcolumnnames = new ArrayList<>();
    ArrayList<String> dbcolumnnames = new ArrayList<>();
    ArrayList<String> columntype = new ArrayList<>();

    private static ResourceBundle rb = ResourceBundle.getBundle("cif");

    List<File> beforeOnboardingFilelist;

    private static boolean dunit = false;

    @Before
    public void setUp() throws Exception {

        scanWorkbook();  // This method run once for each scenario

        if (!dunit) {
            System.out.println("dunit variable value:-->"+dunit);
            dunit = true;

            // do the beforeAll stuff...
            addShutdownHook();
        }
        // Place workbook .xlsx file at hdfs://dlap_tst/cif/onboarding/
//        ResourceBundle rb = ResourceBundle.getBundle("cif");

        System.out.println("In setup() method");
        System.out.println("final row key "+finalRowKeyName);
        System.out.println("hive table name "+hiveTableName);
        System.out.println("hive SCD table name "+hiveSCDTableName);

    }

    public void scanWorkbook() throws Exception {

        onboardingDirCurateStg = rb.getString("onboarding.dir.curate.stg").trim();
        onboardingDirPublishStg = rb.getString("onboarding.dir.publish.stg").trim();
        onboardingBaseStg = rb.getString("onboarding.base.stg").trim();
        onboardingDir = rb.getString("onboarding.dir").trim();
        onboardingDirPublih = rb.getString("onboarding.dir.publish").trim();
        dbURL = rb.getString("db.url").trim();
        System.out.println("dbURL ----> "+dbURL);

        String[] extensions = new String[]{"xlsx"};
        beforeOnboardingFilelist = (List<File>) FileUtils.listFiles(new File(onboardingBaseStg), extensions, true);



        System.out.println("************In scanWorkbook method***********");
        onboardingBaseStg = rb.getString("onboarding.base.stg").trim();
//        String[] extensions = new String[]{"xlsx"};
//        beforeOnboardingFilelist = (List<File>) FileUtils.listFiles(new File(onboardingBaseStg), extensions, true);

        System.out.println("Absolute path of file -->" + beforeOnboardingFilelist.get(0).getAbsolutePath());

        TableName tableName = TableName.valueOf("test_cif:dataset");

        //Extracting the row key from workbook using POI
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(new File(beforeOnboardingFilelist.get(0).getAbsolutePath()));
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        org.apache.poi.ss.usermodel.Row rownum = sheet.getRow(2);
        Cell cellnum = rownum.getCell(1);
//        String cellval = dataFormatter.formatCellValue(cellnum);
        metadataCellVals = new String[10];
        int j = 0;
        System.out.println(Arrays.toString(metadataCellVals));
        for (int i = 2; i <= 5; i++) {
            rownum = sheet.getRow(i);
            cellnum = rownum.getCell(1);
            System.out.println("Cell Value -->" + cellnum);
            metadataCellVals[j] = dataFormatter.formatCellValue(cellnum);
            j++;
        }
        System.out.println(metadataCellVals.toString());
        if (metadataCellVals[2].equals("publish")) {
            metadataCellVals[2] = "publish";
        } else {
            metadataCellVals[2] = "curate";
        }
        finalRowKeyName = metadataCellVals[2] + "_" + metadataCellVals[1] + "." + metadataCellVals[0] + "." + metadataCellVals[3];
        hiveTableName = env + metadataCellVals[2] + "_" + metadataCellVals[1] + "." + metadataCellVals[0];
        hiveSCDTableName = env + metadataCellVals[2] + "_" + metadataCellVals[1] + "." + metadataCellVals[0] + "_scd";
        System.out.println("Hbase Row key:finalrowkeyname -->" + finalRowKeyName);
        System.out.println("Hive Table Name:hiveTableName -->" + hiveTableName);
    }

    @Given("^I have copy the workbook in staging directory$")
    public void checkGivenOnboarding() throws Throwable {

        System.out.println("List of file before onboarding--> " + beforeOnboardingFilelist.toString());
        for (File file : beforeOnboardingFilelist) {
            if (file.getParent().endsWith("publish")) {
                System.out.println("COPYING file from publish stg to onboarding publish dir " + onboardingDirPublishStg + " -->" + onboardingDirPublih);
                FileUtils.copyFileToDirectory(new File(file.getPath()), new File(onboardingDirPublih), false);
            } else {
                System.out.println("COPYING file from curate stg to onboarding curate dir " + onboardingDirCurateStg + " -->" + onboardingDir);
                FileUtils.copyFileToDirectory(new File(file.getPath()), new File(onboardingDir), false);
            }
        }

    }

    @When("^onbarding process kicks off$")
    public void checkWhenOnboarding() throws Throwable {
        System.out.println("WAITING 30 SECONDS .......");
//        // Sleep thread ?  I don't think there's a notification to plug in...
        Thread.sleep(30000);
    }

    @Then("^I should see completed file$")
    public void checkThenOnboarding() throws Throwable {

        System.out.println("File list is -->" + beforeOnboardingFilelist.toString());
        System.out.println("workbook completed file name"+beforeOnboardingFilelist.get(0).getAbsolutePath()+".completed");
        System.out.println("File is exit or not -->" + new File(beforeOnboardingFilelist.get(0).getAbsolutePath()+".completed").exists());
//        assertTrue(new File("" + onboardingDir + "/" + beforeOnboardingFilelist.get(0).getName() + ".completed").exists());

        for (File file : beforeOnboardingFilelist) {
            System.out.println("before onboarding file list " + file.getAbsolutePath());
            System.out.println("Absolute File path with completed " + file.getAbsolutePath() + ".completed");
            System.out.println("Onboarding File path with completed " + "" + onboardingDir + "/" + file.getName() + ".completed");
            if (file.getParent().endsWith("publish")) {
                System.out.print("Publish wb exists ?...-->");
                assertTrue(new File("" + onboardingDirPublih + "/" + file.getName() + ".completed").exists());
            } else {
                System.out.print("check for assert true...-->");
                System.out.println("Curate wb exists ?...-->" + new File("" + onboardingDir + "/" + file.getName() + ".completed").exists());
                assertTrue(new File("" + onboardingDir + "/" + file.getName() + ".completed").exists());

            }
        }
    }


    @Given("^I have parsed a workbook$")
    public void some_start_condition() throws Throwable {
        ArrayList<String> curateColNames = new ArrayList<String>();
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(new File(beforeOnboardingFilelist.get(0).getAbsolutePath()));
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
        for (int i = 46; i <= sheet.getLastRowNum(); i++) {
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
        if (columnname.size() == CurateColumn.size()) {
            for (int i = 0; i < columnname.size(); i++) {
                String actual = columnname.get(i);
                String expected = CurateColumn.get(i);
                assertEquals(actual, expected);
            }
        }
        if (columntype.size() == CurateDataType.size()) {
            for (int i = 0; i < columntype.size(); i++) {
                String actual = columntype.get(i);
                String expected = CurateDataType.get(i);
                assertEquals(actual, expected);
            }
        }
    }

    @When("^I query Hive for the expected database name$")
    public void something_is_done() throws Throwable {
        java.sql.Connection conn =null;
        Statement stmt=null;
        ResultSet rs =null;

        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            //STEP 3: Open a connection-
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(dbURL, "", "");
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            String invalidateSql = "invalidate metadata "+hiveTableName;
            String invalidatescdSql = "invalidate metadata "+hiveSCDTableName;
            System.out.println("SQL invalidate metadata for --> "+invalidateSql);
            System.out.println("SQL invalidate metadata for --> "+invalidatescdSql);
            stmt.execute(invalidatescdSql);
            stmt.execute(invalidateSql);
            sql = "describe "+hiveTableName;
            System.out.println("SQL describe --> "+sql);
            rs = stmt.executeQuery(sql);
            System.out.println("Result set fromm --->"+rs.toString());
            while (rs.next()) {
                //Retrieve by column name
                String cname = rs.getString("name");
//                String ctype = rs.getString("Column Type");

                //Display values
                columnname.add(cname);
                System.out.println("@when2  COLUMN NAME  :  " + cname);
//                System.out.println(", Column Type: " + ctype);
//                columntype.add(ctype);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            rs.close();
            stmt.close();
            conn.close();

        }


    }

    @Then("^I should see Hive DB columns that match the columns from the workbook$")
    public void something_should_happen() throws Throwable {
// compare columnname array with wbcolumnnames array
        wbcolumnnames.remove("Column Name");
        wbcolumnnames.add("timestamp_");
        System.out.println("WB Column names   :  " + wbcolumnnames.toString());
        System.out.println("DB Column names   :  " + columnname.toString());
        System.out.println(columnname.equals(wbcolumnnames));
        assertEquals(wbcolumnnames, columnname);
    }

    @Given("^I have parsed a curate workbook$")
    public void parsing_data() throws Throwable {
        System.out.println("finalrowkeyname in @Given tag   ::  "+finalRowKeyName);
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
        System.out.println("Conn.getTable Success - 0    : : " + table.toString());
        System.out.println("ROWKEYVALUES    ::   " + table.get(new Get(Bytes.toBytes(finalRowKeyName))));
        rowkeyresult = table.get(new Get(Bytes.toBytes(finalRowKeyName)));
        conn.close();


    }


    @Then("^I should see expected row keys in hbase$")
    public void workbook_columns() throws Throwable {
        assertNotNull(rowkeyresult);

    }

    @After
    public void tearDown() throws Exception {

    }

    public void addShutdownHook() throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Shutdown Hook------>");
                Statement stmt = null;
                java.sql.Connection conn=null;
                org.apache.hadoop.hbase.client.Connection hbaseconn=null;

                for (File file : beforeOnboardingFilelist) {
                    System.out.println("before onboarding file list " + file.getAbsolutePath());
                    System.out.println("Absolute File path with completed " + file.getAbsolutePath() + ".completed");
                    System.out.println("Onboarding File path with completed " + "" + onboardingDir + "/" + file.getName() + ".completed");
                    try {
                        if (file.getParent().endsWith("publish")) {

                            String wbCompletedFileName=onboardingDirPublih + "/" + file.getName() + ".completed";
                            String wbErrorFileName=onboardingDirPublih + "/" + file.getName() + ".error";
                            System.out.print("Publish wb exists ?...-->");
                            if (new File(wbCompletedFileName).exists()) {
                                System.out.println("Publish "+wbCompletedFileName+" is exist");
                                FileUtils.forceDelete(new File(wbCompletedFileName));
                            } else if (new File(wbErrorFileName).exists()) {
                                System.out.println("Publish "+wbErrorFileName+" is exist");
                                FileUtils.forceDelete(new File(wbErrorFileName));
                            }
                        } else {
                            System.out.print("check for assert true...-->");
                            System.out.println("Curate wb exists ?...-->" + new File("" + onboardingDir + "/" + file.getName() + ".completed").exists());

                            String wbCompletedFileName=onboardingDir + "/" + file.getName() + ".completed";
                            String wbErrorFileName=onboardingDir + "/" + file.getName() + ".error";
                            if (new File(wbCompletedFileName).exists()) {
                                System.out.println("Curate "+wbCompletedFileName+" is exist");
                                FileUtils.forceDelete(new File(wbCompletedFileName));
                            } else if (new File(wbErrorFileName).exists()) {
                                System.out.println("Curate "+wbErrorFileName+" is exist");
                                FileUtils.forceDelete(new File(wbErrorFileName));
                            }

                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }

                try {

                    System.out.println("---------------------------------------");
                    System.out.println("-------DELETING HBASE ROW KEY----------");
                    TableName tableName = TableName.valueOf("test_cif:filepattern");
                    conf = HBaseConfiguration.create();
                    conf.set("hbase.zookeeper.quorum", "mclmp01vr.bcbsma.com,mclmp02vr.bcbsma.com,mclmp03vr.bcbsma.com");
                    conf.set("hbase.zookeeper.property.clientPort", "2181");
                    hbaseconn = ConnectionFactory.createConnection(conf);
                    Table table = hbaseconn.getTable(tableName);
                    table.delete(new Delete(Bytes.toBytes(finalRowKeyName)));
                    tableName = TableName.valueOf("test_cif:dataset");
                    table = hbaseconn.getTable(tableName);
                    table.delete(new Delete(Bytes.toBytes(finalRowKeyName)));

                    System.out.println("---------------------------------------");
                    System.out.println("--------- TRUNCATING IMPALA TABLE---------");
                    Class.forName("org.apache.hive.jdbc.HiveDriver");
                    System.out.println("Connecting to database...");
                    conn = DriverManager.getConnection(dbURL, "", "");
                    stmt = conn.createStatement();
                    String truncateSql=null;
                    String invalidateSql = "invalidate metadata "+hiveTableName;
                    String invalidatescdSql = "invalidate metadata "+hiveSCDTableName;
                    stmt.execute(invalidatescdSql);
                    stmt.execute(invalidateSql);
                    truncateSql = "truncate table "+hiveTableName;
                    stmt.execute(truncateSql);

                    System.out.println("---------------------------------------");
                    System.out.println("------DROPPING IMPALA TABLE-------------");
                    String dropSql = "drop table "+hiveTableName;
                    String dropScdSql = "drop table "+hiveSCDTableName;
                    System.out.println("Drop Hive table sql-->"+dropSql);
                    System.out.println("Drop Hive table sql-->"+dropScdSql);
                    stmt.execute(invalidatescdSql);
                    stmt.execute(invalidateSql);
                    stmt.execute(dropSql);
                    stmt.execute(dropScdSql);
                }
                catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    try {
                        hbaseconn.close();
                        stmt.close();
                        conn.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            }
        });

    }
}
