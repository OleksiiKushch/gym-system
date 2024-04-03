package org.example.mapper.impl;

import lombok.Getter;
import org.example.dto.TrainingDto;
import org.example.entity.Training;
import org.example.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Getter
@Component
public class TrainingMapper implements Mapper<TrainingDto, Training> {

    private final TrainingTypeMapper trainingTypeMapper;
    private final StringToLocalDateMapper stringToLocalDateMapper;

    public TrainingMapper(TrainingTypeMapper trainingTypeMapper, StringToLocalDateMapper stringToLocalDateMapper) {
        this.trainingTypeMapper = trainingTypeMapper;
        this.stringToLocalDateMapper = stringToLocalDateMapper;
    }

    @Override
    public Training map(TrainingDto source) {
        return Optional.ofNullable(source)
                .map(dto -> Training.builder()
                        .id(dto.getId())
                        .trainingName(dto.getTrainingName())
                        .trainingType(getTrainingTypeMapper().map(dto.getTrainingType()))
                        .trainingDuration(dto.getDuration())
                        .trainingDate(getStringToLocalDateMapper().map(dto.getTrainingDate()))
                        .build())
                .orElseGet(Training::new);
    }
}
