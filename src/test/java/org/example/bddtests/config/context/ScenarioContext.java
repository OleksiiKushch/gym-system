package org.example.bddtests.config.context;

import io.cucumber.spring.ScenarioScope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ScenarioScope(proxyMode = ScopedProxyMode.NO)
public class ScenarioContext {

    private final Map<String, Object> context;

    public ScenarioContext() {
        context = new HashMap<>();
    }

    /**
     * Get attribute for current scenario.
     */
    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(context.get(key));
    }

    /**
     * Set attribute for current scenario.
     */
    public void put(String key, Object object) {
        context.put(key, object);
    }
}
