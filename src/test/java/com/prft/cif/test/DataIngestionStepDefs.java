package com.prft.cif.test;

import com.prft.cif.test.util.HdfsUtil;
import com.prft.cif.test.util.WorkbookUtil;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;
import com.prft.cif.test.util.ResourceUtil;

import static org.junit.Assert.assertTrue;

public class DataIngestionStepDefs {
    private static boolean dunit = false;
/*    private static ResourceBundle rb = ResourceBundle.getBundle("cif");
    private String onboardingDir=null;
    private String onboardingDirPublih=null;
    private String onboardingBaseStg=null;
    private String onboardingDirCurateStg=null;
    private String onboardingDirPublishStg=null;*/
    private List<File> beforeOnboardingFilelist=null;
    ResourceUtil ru;
    WorkbookUtil wu;
    HdfsUtil hu;

    public DataIngestionStepDefs(WorkbookUtil wu, ResourceUtil ru, HdfsUtil hu){
        this.ru=ru;
        this.wu=wu;
        this.hu=hu;
    }


    @Given("^I have copy the data file in HDFS directory$")
    public void checkGivenOnboarding() throws Throwable {
        System.out.println("Dataingestion --> In Given --->");
        System.out.println("Dataingestion -->Getting prop from rb--->"+ru.getOnbDir());
        System.out.println("Dataingestion -->Getting Data file Stg dir--->"+ru.getDataFileStg());
        System.out.println("Dataingestion -->Getting Data file HDFS dir--->"+ru.getHdfsDataFileDir());
        System.out.println("Dataingestion -->Getting prop from wu--->"+wu.getHiveTableName());

        hu.copyFileToHdfs(ru.getDataFileStg(),ru.getHdfsDataFileDir());


    }

    @When("^Inotify process kicks off$")
    public void checkWhenOnboarding() throws Throwable {
        System.out.println(" In When --->");
    }

    @Then("^I should see data in hive table$")
    public void checkThenOnboarding() throws Throwable {

        System.out.println(" In Then --->");

    }
/*
    @After
    public void tearDown() throws Exception {
        System.out.println(" In tearDown() --->");
    }*/

}
