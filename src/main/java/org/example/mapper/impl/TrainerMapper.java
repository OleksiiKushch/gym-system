package org.example.mapper.impl;

import org.example.dto.TrainerDto;
import org.example.entity.Trainer;
import org.example.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TrainerMapper implements Mapper<TrainerDto, Trainer> {

    @Override
    public Trainer map(TrainerDto source) {
        return Optional.ofNullable(source)
                .map(dto -> (Trainer) Trainer.builder()
                        .userId(dto.getId())
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .password(dto.getPassword())
                        .specialization(dto.getSpecialization())
                        .build())
                .orElseGet(Trainer::new);
    }
}
