package org.example.dto.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.TrainingTypeEnum;
import org.example.validation.StringEnum;

import static org.example.constants.GeneralConstants.SPECIALIZATION_ENUM_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTrainerForm {

    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    @StringEnum(enumClass = TrainingTypeEnum.class, enumName = SPECIALIZATION_ENUM_NAME)
    private String specialization;
    @NotNull
    private Boolean isActive;
}
