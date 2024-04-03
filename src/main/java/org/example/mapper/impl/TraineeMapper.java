package org.example.mapper.impl;

import lombok.Getter;
import org.example.dto.TraineeDto;
import org.example.entity.Trainee;
import org.example.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Getter
@Component
public class TraineeMapper implements Mapper<TraineeDto, Trainee> {

    private final StringToLocalDateMapper stringToLocalDateMapper;

    public TraineeMapper(StringToLocalDateMapper stringToLocalDateMapper) {
        this.stringToLocalDateMapper = stringToLocalDateMapper;
    }

    @Override
    public Trainee map(TraineeDto source) {
        return Optional.ofNullable(source)
                .map(dto -> (Trainee) Trainee.builder()
                        .userId(dto.getId())
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .password(dto.getPassword())
                        .dateOfBirthday(getStringToLocalDateMapper().map(dto.getDateOfBirthday()))
                        .address(dto.getAddress())
                        .build())
                .orElseGet(Trainee::new);
    }
}
