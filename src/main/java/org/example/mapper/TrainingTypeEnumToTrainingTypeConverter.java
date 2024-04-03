package org.example.mapper;

import lombok.Getter;
import org.example.entity.TrainingType;
import org.example.entity.TrainingTypeEnum;
import org.example.exception.AppException;
import org.example.service.TrainingTypeService;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.example.constants.GeneralConstants.TRAINING_TYPE_NOT_FOUND_EXCEPTION_MSG;

@Getter
@Component
public class TrainingTypeEnumToTrainingTypeConverter extends AbstractConverter<TrainingTypeEnum, TrainingType>  {

    private final TrainingTypeService trainingTypeService;

    public TrainingTypeEnumToTrainingTypeConverter(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    @Override
    protected TrainingType convert(TrainingTypeEnum source) {
        return Optional.ofNullable(source)
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
