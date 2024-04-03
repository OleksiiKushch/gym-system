package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeTrainingResponse {

    private String trainingName;
    private String trainingDate;
    private String trainingType;
    private Integer trainingDuration;
    private String trainerName;
}
