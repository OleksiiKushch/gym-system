package org.example.bddtests.helpers.mappers.impl;

import io.cucumber.spring.ScenarioScope;
import org.example.bddtests.constants.BddTestsConstants;
import org.example.bddtests.helpers.mappers.CucumberFeatureDataMapper;
import org.example.dto.form.LoginForm;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ScenarioScope
public class LoginFormCucumberMapper implements CucumberFeatureDataMapper<LoginForm> {

    @Override
    public LoginForm map(Map<String, String> data) {
        LoginForm result = new LoginForm();
        result.setUsername(data.get(BddTestsConstants.USERNAME));
        result.setPassword(data.get(BddTestsConstants.PASSWORD));
        return result;
    }
}
