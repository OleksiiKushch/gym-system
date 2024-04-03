package org.example.dto.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTrainingForm {

    @NotEmpty
    private String traineeUsername;
    @NotEmpty
    private String trainerUsername;
    @NotEmpty
    private String trainingName;
    @NotEmpty
    private String trainingType;
    @NotEmpty
    private String trainingDate;
    @NotEmpty
    private String duration;
}
