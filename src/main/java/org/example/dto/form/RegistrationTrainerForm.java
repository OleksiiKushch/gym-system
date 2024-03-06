package org.example.dto.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.TrainingTypeEnum;
import org.example.validation.StringEnum;

import static org.example.constants.GeneralConstants.PASSWORD_PLACEHOLDER;
import static org.example.constants.GeneralConstants.SPECIALIZATION_ENUM_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationTrainerForm {

    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    private String password;
    private String confirmPassword;
    @NotEmpty
    @StringEnum(enumClass = TrainingTypeEnum.class, enumName = SPECIALIZATION_ENUM_NAME)
    private String specialization;

    @Override
    public String toString() {
        return "RegistrationTrainerForm{" +
                "firstName=" + firstName +
                ", lastName=" + lastName +
                ", password=" + PASSWORD_PLACEHOLDER +
                ", confirmPassword=" + PASSWORD_PLACEHOLDER +
                ", specialization=" + specialization + '}';
    }
}
