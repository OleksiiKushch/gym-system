package org.example.bddtests.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.bddtests.config.context.ScenarioContext;
import org.example.bddtests.constants.BddTestsConstants;
import org.example.bddtests.helpers.clients.TrainingTypeHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest // Not required, just so the IDE doesn't warn on @Autowired.
public class TrainingTypeCatalogStepDef {

    @Autowired
    ScenarioContext scenarioContext;
    @Autowired
    TrainingTypeHttpClient client;

    @When("user sends a request to retrieve existing training types")
    public void sendRequest() {
        var jwtToken = scenarioContext.get(BddTestsConstants.JWT_TOKEN, String.class);

        var responseEntity = client.getTrainingTypeCatalog(jwtToken);

        scenarioContext.put(BddTestsConstants.LAST_RESPONSE, responseEntity);
    }

    @Then("user get the following training types: {string}")
    public void verifyTrainingTypes(String trainingTypes) {
        var responseEntity = scenarioContext.get(BddTestsConstants.LAST_RESPONSE, ResponseEntity.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(trainingTypes, responseEntity.getBody());
    }
}
