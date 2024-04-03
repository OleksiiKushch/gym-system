package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDto {

    private Integer id;
    private String traineeUsername;
    private String trainerUsername;
    private String trainingName;
    private String trainingType;
    private String trainingDate;
    private Integer duration;
}
