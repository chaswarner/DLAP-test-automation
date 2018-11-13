package com.prft.cif.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ImpalaQuery {
    Connection conn;
    String DB_URL = "jdbc:hive2://hive.prod.bcbsma.com:10000/;principal=hive/hive.prod.bcbsma.com@BCBSMAMD.NET;ssl=true";

    public void createConnection() {
        conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, "", "");

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "select rcpnt_typ, rcpnt_id, cash_rcpt_id from test_curate_fin.cash_detail";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                //Retrieve by column name
                String rcType = rs.getString("rcpnt_typ");
                String cashRcId = rs.getString("cash_rcpt_id");
                String rcId = rs.getString("rcpnt_id");

                //Display values
                System.out.print(", Age: " + rcType);
                System.out.print(", First: " + rcId);
                System.out.println(", Last: " + cashRcId);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}