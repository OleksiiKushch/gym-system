package org.example.dto.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.TrainingTypeEnum;
import org.example.validation.StringEnum;
import org.example.validation.StringLocalDate;

import static org.example.constants.GeneralConstants.TRAINING_TYPE_ENUM_NAME;

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
    @StringEnum(enumClass = TrainingTypeEnum.class, enumName = TRAINING_TYPE_ENUM_NAME)
    private String trainingType;
    @NotEmpty
    @StringLocalDate
    private String trainingDate;
    @NotEmpty
    private String duration;
}
