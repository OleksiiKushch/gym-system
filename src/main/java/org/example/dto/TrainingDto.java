package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.TrainingTypeEnum;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDto {

    private Integer id;
    private String traineeUsername;
    private String trainerUsername;
    private String trainingName;
    private TrainingTypeEnum trainingType;
    private LocalDate trainingDate;
    private Integer duration;
}
