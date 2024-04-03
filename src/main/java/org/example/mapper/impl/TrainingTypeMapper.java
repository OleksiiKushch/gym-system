package org.example.mapper.impl;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.example.entity.TrainingType;
import org.example.entity.TrainingTypeEnum;
import org.example.exception.AppException;
import org.example.mapper.Mapper;
import org.example.service.TrainingTypeService;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.example.constants.GeneralConstants.TRAINING_TYPE_NOT_FOUND_EXCEPTION_MSG;

@Getter
@Component
public class TrainingTypeMapper implements Mapper<String, TrainingType> {

    private final TrainingTypeService trainingTypeService;

    public TrainingTypeMapper(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    @Override
    public TrainingType map(String source) {
        return Optional.ofNullable(source)
                .filter(StringUtils::isNotEmpty)
                .map(TrainingTypeEnum::valueOf)
                .map(this::findTrainingType)
                .orElse(null);
    }

    private TrainingType findTrainingType(TrainingTypeEnum trainingTypeEnum) {
        return getTrainingTypeService().getTrainingTypeForName(trainingTypeEnum)
                .orElseThrow(() -> new AppException(fromExceptionMessage(trainingTypeEnum)));
    }

    private String fromExceptionMessage(TrainingTypeEnum trainingTypeEnum) {
        return String.format(TRAINING_TYPE_NOT_FOUND_EXCEPTION_MSG, trainingTypeEnum);
    }
}
