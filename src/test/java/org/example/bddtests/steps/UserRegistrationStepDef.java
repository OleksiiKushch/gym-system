package org.example.bddtests.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang.StringUtils;
import org.example.bddtests.config.context.ScenarioContext;
import org.example.bddtests.constants.BddTestsConstants;
import org.example.bddtests.helpers.clients.TraineeHttpClient;
import org.example.bddtests.helpers.mappers.CucumberFeatureDataMapper;
import org.example.dao.TraineeDao;
import org.example.dto.form.RegistrationTraineeForm;
import org.example.entity.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest // Not required, just so the IDE doesn't warn on @Autowired.
public class UserRegistrationStepDef {

    @Autowired
    ScenarioContext scenarioContext;
    @Autowired
    TraineeHttpClient client;
    @Autowired
    CucumberFeatureDataMapper<RegistrationTraineeForm> mapper;
    @Autowired
    TraineeDao traineeDao;

    @Given("trainee prepares registration request")
    public void traineePrepareRequest(DataTable dataTable) {
        var registrationTraineeForm = mapper.map(dataTable.asMap(String.class, String.class));

        scenarioContext.put(BddTestsConstants.REGISTRATION_TRAINEE_FORM, registrationTraineeForm);
    }

    @When("trainee sends registration request")
    public void traineeSendRequest() throws JsonProcessingException {
        var registrationTraineeForm = scenarioContext.get(BddTestsConstants.REGISTRATION_TRAINEE_FORM, RegistrationTraineeForm.class);

        var responseEntity = client.registerTrainee(registrationTraineeForm);

        scenarioContext.put(BddTestsConstants.LAST_RESPONSE, responseEntity);
    }

    @Then("trainee with the username {string} has successfully registered")
    public void checkRegisteredTrainee(String username) {
        var responseEntity = scenarioContext.get(BddTestsConstants.LAST_RESPONSE, ResponseEntity.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Optional<Trainee> result = traineeDao.findByUsername(username);
        assertTrue(result.isPresent());

        scenarioContext.put(BddTestsConstants.USERNAEM, username);
        scenarioContext.put(BddTestsConstants.TRAINEE, result.get());
    }

    @And("all trainee required fields are present")
    public void checkAllRequiredFieldsPresent() {
        var trainee = scenarioContext.get(BddTestsConstants.TRAINEE, Trainee.class);
        var username = scenarioContext.get(BddTestsConstants.USERNAEM, String.class);
        var form = scenarioContext.get(BddTestsConstants.REGISTRATION_TRAINEE_FORM, RegistrationTraineeForm.class);

        checkAllRequiredTraineeFields(trainee, username, form);
    }

    @And("some trainee optional fields are present and next are not: {string}")
    public void checkAllOptionalFieldsPresent(String notProvidedOptionalFields) {
        var trainee = scenarioContext.get(BddTestsConstants.TRAINEE, Trainee.class);
        var form = scenarioContext.get(BddTestsConstants.REGISTRATION_TRAINEE_FORM, RegistrationTraineeForm.class);

        Set<String> fieldsToBeNull = Arrays.stream(notProvidedOptionalFields.split(BddTestsConstants.MSG_DELIMITER))
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toSet());

        if (fieldsToBeNull.contains(BddTestsConstants.ADDRESS)) {
            assertNull(trainee.getAddress());
        } else {
            assertEquals(form.getAddress(), trainee.getAddress());
        }

        if (fieldsToBeNull.contains(BddTestsConstants.DATE_OF_BIRTHDAY)) {
            assertNull(trainee.getDateOfBirthday());
        } else {
            assertEquals(form.getDateOfBirthday(), trainee.getDateOfBirthday().toString());
        }
    }

    private void checkAllRequiredTraineeFields(Trainee trainee, String username, RegistrationTraineeForm form) {
        assertTrue(trainee.getIsActive());
        assertEquals(username, trainee.getUsername());
        assertEquals(form.getFirstName(), trainee.getFirstName());
        assertEquals(form.getLastName(), trainee.getLastName());
    }
}
