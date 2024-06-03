package org.example.bddtests.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.bddtests.config.context.ScenarioContext;
import org.example.bddtests.constants.BddTestsConstants;
import org.example.bddtests.helpers.clients.TrainingHttpClient;
import org.example.bddtests.helpers.mappers.CucumberFeatureDataMapper;
import org.example.dao.TrainingDao;
import org.example.dto.form.CreateTrainingForm;
import org.example.entity.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest // Not required, just so the IDE doesn't warn on @Autowired.
public class OrganizeTrainingStepDef {

    @Autowired
    ScenarioContext scenarioContext;
    @Autowired
    TrainingHttpClient client;
    @Autowired
    CucumberFeatureDataMapper<CreateTrainingForm> mapper;
    @Autowired
    TrainingDao trainingDao;

    @Given("user prepares training organization request")
    public void prepareRequest(DataTable dataTable) {
        var createTrainingForm = mapper.map(dataTable.asMap(String.class, String.class));

        scenarioContext.put(BddTestsConstants.CREATE_TRAINING_FORM, createTrainingForm);
    }

    @When("user sends a request to organize a new training")
    public void sendRequest() throws JsonProcessingException {
        var createTrainingForm = scenarioContext.get(BddTestsConstants.CREATE_TRAINING_FORM, CreateTrainingForm.class);
        var jwtToken = scenarioContext.get(BddTestsConstants.JWT_TOKEN, String.class);

        var responseEntity = client.createNewTraining(jwtToken, createTrainingForm);

        scenarioContext.put(BddTestsConstants.LAST_RESPONSE, responseEntity);
    }

    @Then("should successfully create a new training session with name {string}")
    public void checkCreatedTraining(String trainingName) {
        var responseEntity = scenarioContext.get(BddTestsConstants.LAST_RESPONSE, ResponseEntity.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Optional<Training> result = trainingDao.findByTrainingName(trainingName);
        assertTrue(result.isPresent());

        scenarioContext.put(BddTestsConstants.TRAINING, result.get());
    }

    @And("all training required fields are present")
    public void checkAllRequiredFieldsPresent() {
        var training = scenarioContext.get(BddTestsConstants.TRAINING, Training.class);
        var form = scenarioContext.get(BddTestsConstants.CREATE_TRAINING_FORM, CreateTrainingForm.class);

        checkAllRequiredTrainingFields(training, form);
    }

    private void checkAllRequiredTrainingFields(Training training, CreateTrainingForm form) {
        assertEquals(form.getTraineeUsername(), training.getTrainee().getUsername());
        assertEquals(form.getTrainerUsername(), training.getTrainer().getUsername());
        assertEquals(form.getTrainingName(), training.getTrainingName());
        assertEquals(form.getTrainingType(), training.getTrainingType().getName().name());
        assertEquals(form.getTrainingDate(), training.getTrainingDate().toString());
        assertEquals(form.getDuration(), training.getTrainingDuration().toString());
    }
}
