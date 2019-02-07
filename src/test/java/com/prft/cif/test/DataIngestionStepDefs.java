package com.prft.cif.test;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.io.FileUtils;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class DataIngestionStepDefs {


    @Given("^I have copy the data file in HDFS directory$")
    public void checkGivenOnboarding() throws Throwable {
        System.out.println(" In Given --->");
    }

    @When("^Inotify process kicks off$")
    public void checkWhenOnboarding() throws Throwable {
        System.out.println(" In When --->");
    }

    @Then("^I should see data in hive table$")
    public void checkThenOnboarding() throws Throwable {

        System.out.println(" In Then --->");

    }

}
