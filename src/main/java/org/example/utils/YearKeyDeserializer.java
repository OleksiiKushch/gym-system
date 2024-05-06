package org.example.utils;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.time.Year;

public class YearKeyDeserializer extends KeyDeserializer {

    @Override
    public Year deserializeKey(String key, DeserializationContext context) {
        return Year.parse(key);
    }
}
