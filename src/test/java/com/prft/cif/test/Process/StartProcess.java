package com.prft.cif.test.Process;

import com.prft.cif.test.FileRead.CurateHive;

public class StartProcess {

    public static void main(String args[]){
//        String metadataFile = "C:/Users/arajag01/excelfiles/ME_FIN_Cash_Detail.xlsx";
//        CurateHive.getData(metadataFile);
        CurateHive curateHive = new CurateHive();
//        System.out.println(curateHive.testString);
        curateHive.createConnection();
    }
}
