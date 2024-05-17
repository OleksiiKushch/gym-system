package org.example.bddtests.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.bddtests.config.context.ScenarioContext;
import org.example.bddtests.constants.BddTestsConstants;
import org.example.bddtests.helpers.clients.TrainerHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest // Not required, just so the IDE doesn't warn on @Autowired.
public class TrainerReport {

    @Autowired
    ScenarioContext scenarioContext;
    @Autowired
    private TrainerHttpClient client;

    @When("user sent a request for a report for the trainer with the username {string}")
    public void sendRequestForReport(String trainerUsername) {
        var jwtToken = scenarioContext.get(BddTestsConstants.JWT_TOKEN, String.class);

        var responseEntity = client.formReport(jwtToken, trainerUsername);

        scenarioContext.put(BddTestsConstants.LAST_RESPONSE, responseEntity);
    }

    @Then("check the report for the trainer, it should be: {string}")
    public void checkReport(String strReport) {
        var responseEntity = scenarioContext.get(BddTestsConstants.LAST_RESPONSE, ResponseEntity.class);

        assertEquals(strReport, responseEntity.getBody());
    }
}
