package org.example.mapper;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
@Component
public class StringToLocalDateConverter extends AbstractConverter<String, LocalDate> {

    private final DateTimeFormatter defaultDateFormatter;

    public StringToLocalDateConverter(DateTimeFormatter defaultDateFormatter) {
        this.defaultDateFormatter = defaultDateFormatter;
    }

    @Override
    protected LocalDate convert(String source) {
        return Optional.ofNullable(source)
                .filter(StringUtils::isNotEmpty)
                .map(strDate -> LocalDate.parse(strDate, getDefaultDateFormatter()))
                .orElse(null);
    }
}
