package my.example.tests.cucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/java/my/example/tests/features/userValidations.feature",
        plugin = "json:target/jsonReports/cucumber-report.json",
        glue = "my.example.tests.stepDefinitions")
public class UserTestRunner extends AbstractTestNGCucumberTests{
}
