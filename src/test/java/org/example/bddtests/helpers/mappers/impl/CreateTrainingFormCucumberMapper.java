package org.example.bddtests.helpers.mappers.impl;

import io.cucumber.spring.ScenarioScope;
import org.example.bddtests.constants.BddTestsConstants;
import org.example.bddtests.helpers.mappers.CucumberFeatureDataMapper;
import org.example.dto.form.CreateTrainingForm;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ScenarioScope
public class CreateTrainingFormCucumberMapper implements CucumberFeatureDataMapper<CreateTrainingForm> {

    @Override
    public CreateTrainingForm map(Map<String, String> data) {
        CreateTrainingForm result = new CreateTrainingForm();
        result.setTraineeUsername(data.get(BddTestsConstants.TRAINEE_USERNAME));
        result.setTrainerUsername(data.get(BddTestsConstants.TRAINER_USERNAME));
        result.setTrainingName(data.get(BddTestsConstants.TRAINING_NAME));
        result.setTrainingType(data.get(BddTestsConstants.TRAINING_TYPE));
        result.setTrainingDate(data.get(BddTestsConstants.TRAINING_DATE));
        result.setDuration(data.get(BddTestsConstants.DURATION));
        return result;
    }
}
