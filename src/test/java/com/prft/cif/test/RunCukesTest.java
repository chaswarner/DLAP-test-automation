package com.prft.cif.test;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"json" },
        tags = {"~@Ignore","@SmokeTest"}
        )
public class RunCukesTest {
}
