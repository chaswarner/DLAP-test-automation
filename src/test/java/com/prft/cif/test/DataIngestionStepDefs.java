package com.prft.cif.test;

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

    public DataIngestionStepDefs(ResourceUtil ru){
        this.ru=ru;
    }

/*    @Before
    public void setUp() throws Exception {
        System.out.println(" In setup() --->");
        ru.loadResources();


        if (!dunit) {
            System.out.println("Data Ingestion --> dunit variable value:-->"+dunit);

             String[] extensions = new String[]{};
            beforeOnboardingFilelist = (List<File>) FileUtils.listFiles(new File(ru.getBaseStg()), extensions, true);
            System.out.println("List of file before onboarding--> " + beforeOnboardingFilelist.toString());
            for (File file : beforeOnboardingFilelist) {
                if (file.getParent().endsWith("publish")) {
                    System.out.println("COPYING file from publish stg to onboarding publish dir " + ru.getOnbpubStg() + " -->" + ru.getOnbDirPub());
                    FileUtils.copyFileToDirectory(new File(file.getPath()), new File(ru.getOnbDirPub()), false);
                } else {
                    System.out.println("COPYING file from curate stg to onboarding curate dir " + ru.getOnbCurStg() + " -->" + ru.getOnbDir());
                    FileUtils.copyFileToDirectory(new File(file.getPath()), new File(ru.getOnbDir()), false);
                }
            }
            Thread.sleep(30000);

            dunit = true;

            // do the beforeAll stuff...
//            addShutdownHook();
        }

        System.out.println("Data Ingestion-->In setup() method");
        System.out.println("Data Ingestion-->getBaseStg---->"+ru.getBaseStg());


    }*/
    @Given("^I have copy the data file in HDFS directory$")
    public void checkGivenOnboarding() throws Throwable {
        System.out.println(" In Given --->");
        System.out.println("Getting prop from rb--->"+ru.getOnbDir());

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
