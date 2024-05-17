package org.example.bddtests.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.bddtests.config.context.ScenarioContext;
import org.example.bddtests.constants.BddTestsConstants;
import org.example.bddtests.helpers.clients.UserHttpClient;
import org.example.bddtests.helpers.mappers.CucumberFeatureDataMapper;
import org.example.config.security.bruteforceprevent.LoginAttemptService;
import org.example.config.security.jwt.JwtManager;
import org.example.dto.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest // Not required, just so the IDE doesn't warn on @Autowired.
public class LoginStepDef {

    private static final String USERNAME_IS_NOT_PRESENT_ERROR_MSG = "Username is not present in the UserDetails.";

    @Autowired
    ScenarioContext scenarioContext;
    @Autowired
    UserHttpClient client;
    @Autowired
    CucumberFeatureDataMapper<LoginForm> mapper;
    @Autowired
    JwtManager jwtManager;
    @Autowired
    private LoginAttemptService loginAttemptService;

    @Before("@RestartLoginBruteforceProtection")
    @After("@RestartLoginBruteforceProtection")
    public void resetLoginBruteforceProtectionService() {
        loginAttemptService.init();
    }

    @Given("user prepares login request")
    public void userPrepareRequest(DataTable dataTable) {
        var loginForm = mapper.map(dataTable.asMap(String.class, String.class));

        scenarioContext.put(BddTestsConstants.LOGIN_FORM, loginForm);
    }

    @When("user sends login request")
    public void userSendsLoginRequest() throws JsonProcessingException {
        var loginForm = scenarioContext.get(BddTestsConstants.LOGIN_FORM, LoginForm.class);
        var responseEntity = client.loginUser(loginForm);

        scenarioContext.put(BddTestsConstants.LAST_RESPONSE, responseEntity);
    }

    @Then("user should be logged in successfully")
    public void checkUserLogin() {
        var responseEntity = scenarioContext.get(BddTestsConstants.LAST_RESPONSE, ResponseEntity.class);
        var form = scenarioContext.get(BddTestsConstants.LOGIN_FORM, LoginForm.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String jwtToken = (String) responseEntity.getBody();
        assertNotNull(jwtToken);
        UserDetails userDetails = jwtManager.isTokenValid(jwtToken)
                .orElseThrow(() -> new AssertionError(USERNAME_IS_NOT_PRESENT_ERROR_MSG));
        assertEquals(form.getUsername(), userDetails.getUsername());

        scenarioContext.put(BddTestsConstants.JWT_TOKEN, jwtToken);
    }
}
