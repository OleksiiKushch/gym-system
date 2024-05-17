package org.example.bddtests;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {
                "src/test/resources/bddtests/features/integrationtests/"
        },
        glue = {
                "org.example.bddtests.config.integrationtests",
                "org.example.bddtests.steps"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports/integration-tests/cucumber-pretty.html"
        },
//        tags = "",      // for example: "@Test1 and @Test2", "@Test1 or @Test2", "not @Test1"
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class RunCucumberIntegrationTests {
}
