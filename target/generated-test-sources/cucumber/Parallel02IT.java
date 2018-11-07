import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        strict = true,
        features = {"C:/Users/cwarne01/git/DLAP-test-automation/src/test/resources/se/ff/cc/CashDetail.feature"},
        plugin = {"json:C:/Users/cwarne01/git/DLAP-test-automation/target/cucumber-parallel/2.json"},
        monochrome = false,
        tags = {},
        glue = {"se.ff.cc"})
public class Parallel02IT {
}
