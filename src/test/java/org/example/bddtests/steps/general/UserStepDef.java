package org.example.bddtests.steps.general;

import io.cucumber.java.en.Then;
import org.example.bddtests.config.context.ScenarioContext;
import org.example.bddtests.constants.BddTestsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.example.bddtests.constants.BddTestsConstants.LAST_RESPONSE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest // Not required, just so the IDE doesn't warn on @Autowired.
public class UserStepDef {

    private static final String ASSERTION_ERROR_MSG = "Full response (body): ";

    @Autowired
    ScenarioContext scenarioContext;

    @Then("user receives error with status code {int} and messages: {string}")
    public void checkErrorMessage(int statusCode, String error) {
        var responseEntity = scenarioContext.get(LAST_RESPONSE, ResponseEntity.class);
        assertEquals(HttpStatus.valueOf(statusCode), responseEntity.getStatusCode());

        String body = (String) responseEntity.getBody();
        assertNotNull(body);
        for (String code : error.split(BddTestsConstants.MSG_DELIMITER)) {
            assertTrue(body.contains(code), ASSERTION_ERROR_MSG + body);
        }
    }
}
