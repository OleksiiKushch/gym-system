package org.example.config;

import org.example.dto.request.TrainerWorkloadRequest;
import org.example.dto.response.TraineeTrainingResponse;
import org.example.dto.response.TrainerTrainingResponse;
import org.example.entity.Training;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ModelMapperConfig {

    private final List<Converter<?,?>> converters;

    public ModelMapperConfig(List<Converter<?,?>> converters) {
        this.converters = converters;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        converters.forEach(modelMapper::addConverter);

        modelMapper.addMappings(trainingToTraineeTrainingResponseMap());
        modelMapper.addMappings(trainingToTrainerTrainingResponseMap());
        modelMapper.addMappings(trainingToTrainerWorkloadRequest());

        return modelMapper;
    }

    private PropertyMap<Training, TraineeTrainingResponse> trainingToTraineeTrainingResponseMap() {
        return new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setTrainerName(source.getTrainer().getFirstName());
            }
        };
    }

    private PropertyMap<Training, TrainerTrainingResponse> trainingToTrainerTrainingResponseMap() {
        return new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setTraineeName(source.getTrainee().getFirstName());
            }
        };
    }

    private PropertyMap<Training, TrainerWorkloadRequest> trainingToTrainerWorkloadRequest() {
        return new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setTrainerUsername(source.getTrainer().getUsername());
                map().setTrainerFirstName(source.getTrainer().getFirstName());
                map().setTrainerLastName(source.getTrainer().getLastName());
                map().setActive(source.getTrainer().getIsActive());
                map().setTrainingDate(source.getTrainingDate());
                map().setTrainingDuration(source.getTrainingDuration());
            }
        };
    }
}
