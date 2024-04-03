package org.example.dto.form.search;

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
public class SearchTraineeTrainingsPayload {

    @StringLocalDate
    private String fromDate;
    @StringLocalDate
    private String toDate;
    private String trainerFirstName;
    @StringEnum(enumClass = TrainingTypeEnum.class, enumName = TRAINING_TYPE_ENUM_NAME)
    private String TrainingType;
}
