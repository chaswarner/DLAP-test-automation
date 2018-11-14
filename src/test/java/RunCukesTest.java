import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"json","json:target/cucumber-reports/cucumber.json"},
        tags = {"~@Ignore","@SmokeTest"}
        )
public class RunCukesTest {
}
