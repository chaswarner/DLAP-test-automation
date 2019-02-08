package com.prft.cif.test;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"json","json:target/cucumber-reports/cucumber.json"},
        tags = {"~@Ignore","~@SmokeTest","@DataTest"},
        features = "src/test/resources/com/prft/cif/test/"
        )
public class RunCukesTest {
/*        public static void main(String[] args) throws Throwable {
                String[] arguments = {"--tags @SmokeTest"};
                cucumber.api.cli.Main.main(arguments);
        }*/
}




