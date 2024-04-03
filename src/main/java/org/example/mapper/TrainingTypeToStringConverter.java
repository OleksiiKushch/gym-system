package org.example.mapper;

import org.example.entity.TrainingType;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TrainingTypeToStringConverter extends AbstractConverter<TrainingType, String>  {

    @Override
    protected String convert(TrainingType source) {
        return Optional.ofNullable(source)
                .map(trainingType -> trainingType.getName().name())
                .orElse(null);
    }
}
