package org.example.mapper.impl;

import org.apache.commons.lang3.StringUtils;
import org.example.dto.TraineeDto;
import org.example.entity.Trainee;
import org.example.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class TraineeMapper implements Mapper<TraineeDto, Trainee> {

    @Override
    public Trainee map(TraineeDto source) {
        return Optional.ofNullable(source)
                .map(dto -> (Trainee) Trainee.builder()
                        .userId(dto.getId())
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .password(dto.getPassword())
                        .dateOfBirthday(Optional.ofNullable(dto.getDateOfBirthday())
                                .filter(StringUtils::isNotEmpty)
                                .map(LocalDate::parse)
                                .orElse(null))
                        .address(dto.getAddress())
                        .build())
                .orElseGet(Trainee::new);
    }
}
