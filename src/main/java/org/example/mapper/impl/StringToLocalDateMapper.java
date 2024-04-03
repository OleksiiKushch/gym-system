package org.example.mapper.impl;

import org.apache.commons.lang3.StringUtils;
import org.example.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class StringToLocalDateMapper implements Mapper<String, LocalDate> {

    @Override
    public LocalDate map(String source) {
        return Optional.ofNullable(source)
                .filter(StringUtils::isNotEmpty)
                .map(LocalDate::parse)
                .orElse(null);
    }
}
