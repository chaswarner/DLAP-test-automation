package com.prft.cif.test.FileRead;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CurateHive {
    ArrayList<String> columnname = new ArrayList<String>();
//    ArrayList<String> columntype = new ArrayList<String>();

    public static void getData(String excelFile) throws InvalidFormatException {
        String tableName = null;
        ArrayList<String> curateColNames = new ArrayList<String>();
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(new File(excelFile));
        } catch (IOException | InvalidFormatException ioe) {
            System.out.println(ioe);
        }
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        Row rownum = sheet.getRow(2);
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
        }
    }
    public void createConnection() {
        Connection conn;
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
               String cname = rs.getString("Col_name");
//                String ctype = rs.getString("Column Type");

               //Display values
               columnname.add(cname);
               System.out.println(cname);
//                System.out.println(", Column Type: " + ctype);
//                columntype.add(ctype);
           }
       } catch(Exception e){
           e.printStackTrace();
       }

   }
}
