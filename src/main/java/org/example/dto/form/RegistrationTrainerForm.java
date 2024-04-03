package org.example.dto.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.example.constants.GeneralConstants.PASSWORD_PLACEHOLDER;

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
