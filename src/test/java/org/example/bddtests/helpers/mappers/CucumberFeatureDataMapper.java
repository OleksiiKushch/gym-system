package org.example.bddtests.helpers.mappers;

import java.util.Map;

public interface CucumberFeatureDataMapper<T> {

    T map(Map<String, String> data);
}
