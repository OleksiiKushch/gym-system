package org.example.config.storage.inmemory;

import java.util.Set;

public class InMemoryStorageWithIntId<V> extends InMemoryStorage<Integer, V> {

    private static final String DATASOURCE_OWERFLOW_EXCEPTION_MSG = "Datasource is overflow";

    public Integer findNextId() {
        Set<Integer> keys = keySet();
        if (keys.isEmpty()) {
            return 1;
        }
        return keys.stream()
                .max(Integer::compareTo)
                .filter(id -> id < Integer.MAX_VALUE)
                .orElseThrow(() -> new RuntimeException(DATASOURCE_OWERFLOW_EXCEPTION_MSG)) + 1;
    }
}
