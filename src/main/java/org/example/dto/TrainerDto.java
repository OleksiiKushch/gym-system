package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.TrainingTypeEnum;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerDto {

    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private TrainingTypeEnum specialization;
    private Boolean isActive;
    private List<TraineeDto> trainees;
}
