package com.prft.cif.test.util;

import cucumber.api.java.en.Given;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.prft.cif.test.util.ResourceUtil;



public class WorkbookUtil {

    ResourceUtil ru;
    private List<File> beforeOnboardingFilelist=null;
    private String hivetblname=null;
    private String hbaseRowKey=null;
    private String hivescdtblname=null;

    public WorkbookUtil(ResourceUtil ru){
        this.ru=ru;
    }

    public void setHiveTableName(String hivetblname){
        this.hivetblname=hivetblname;

    }

    public String getHiveTableName(){
        return this.hivetblname;

    }
    public void setHbaseRowKey(String hbaseRowKey){
        this.hbaseRowKey=hbaseRowKey;
    }
    public String getHbaseRowKey(){
        return this.hbaseRowKey;
    }
    public void setHiveScdTableName(String hivescdtblname){
        this.hivescdtblname=hivescdtblname;
    }
    public String getHivetblname(){
        return this.hivescdtblname;
    }


    public void scanWorkbook() throws Exception {


        String[] extensions = new String[]{"xlsx"};
        beforeOnboardingFilelist = (List<File>) FileUtils.listFiles(new File(ru.getBaseStg()), extensions, true);

        System.out.println("************In scanWorkbook method***********");
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
        String[] metadataCellVals = new String[10];
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

        setHbaseRowKey(metadataCellVals[2] + "_" + metadataCellVals[1] + "." + metadataCellVals[0] + "." + metadataCellVals[3]);
        setHiveTableName(ru.getEnvVar().trim() + metadataCellVals[2] + "_" + metadataCellVals[1] + "." + metadataCellVals[0]);
        setHiveScdTableName(ru.getEnvVar().trim() + metadataCellVals[2] + "_" + metadataCellVals[1] + "." + metadataCellVals[0] + "_scd");

        System.out.println("Hbase Row key:hbaseRowKey -->" +this.hbaseRowKey);
        System.out.println("Hive Table Name:hivetblname -->" + this.hivetblname);
        System.out.println("Hive SCD Table Name:hivescdtblname -->" + this.hivescdtblname);
    }

}
