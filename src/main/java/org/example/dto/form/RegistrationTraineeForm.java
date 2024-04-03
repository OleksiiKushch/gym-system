package org.example.dto.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.validation.StringLocalDate;

import static org.example.constants.GeneralConstants.PASSWORD_PLACEHOLDER;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationTraineeForm {

    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    private String password;
    private String confirmPassword;
    @StringLocalDate
    private String dateOfBirthday;
    private String address;

    @ToString.Include(name = "password")
    private String maskPassword() {
        return PASSWORD_PLACEHOLDER;
    }

    @ToString.Include(name = "confirmPassword")
    private String maskConfirmPassword() {
        return PASSWORD_PLACEHOLDER;
    }
}
