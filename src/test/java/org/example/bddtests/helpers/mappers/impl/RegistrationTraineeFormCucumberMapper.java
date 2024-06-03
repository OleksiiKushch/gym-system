package org.example.bddtests.helpers.mappers.impl;

import io.cucumber.spring.ScenarioScope;
import org.example.bddtests.constants.BddTestsConstants;
import org.example.bddtests.helpers.mappers.CucumberFeatureDataMapper;
import org.example.dto.form.RegistrationTraineeForm;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ScenarioScope
public class RegistrationTraineeFormCucumberMapper implements CucumberFeatureDataMapper<RegistrationTraineeForm> {

    @Override
    public RegistrationTraineeForm map(Map<String, String> data) {
        RegistrationTraineeForm result = new RegistrationTraineeForm();
        result.setFirstName(data.get(BddTestsConstants.FIRST_NAME));
        result.setLastName(data.get(BddTestsConstants.LAST_NAME));
        result.setPassword(data.get(BddTestsConstants.PASSWORD));
        result.setConfirmPassword(data.get(BddTestsConstants.CONFIRMATION_PASSWORD));
        result.setDateOfBirthday(data.get(BddTestsConstants.DATE_OF_BIRTHDAY));
        result.setAddress(data.get(BddTestsConstants.ADDRESS));
        return result;
    }
}
