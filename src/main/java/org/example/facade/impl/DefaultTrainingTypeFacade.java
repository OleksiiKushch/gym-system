package org.example.facade.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.dto.TrainingTypeDto;
import org.example.facade.TrainingTypeFacade;
import org.example.service.TrainingTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Component
public class DefaultTrainingTypeFacade implements TrainingTypeFacade {

    private final TrainingTypeService trainingTypeService;
    private final ModelMapper modelMapper;

    @Override
    public List<TrainingTypeDto> getTrainingTypes() {
        return getTrainingTypeService().getAllTrainingTypes().stream()
                .map(trainingType -> getModelMapper().map(trainingType, TrainingTypeDto.class))
                .toList();
    }
}
