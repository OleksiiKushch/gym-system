package org.example.dto.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @Override
    public String toString() {
        return "RegistrationTraineeForm{" +
                "firstName=" + firstName +
                ", lastName=" + lastName +
                ", password=" + PASSWORD_PLACEHOLDER +
                ", confirmPassword=" + PASSWORD_PLACEHOLDER +
                ", dateOfBirthday=" + dateOfBirthday +
                ", address=" + address + '}';
    }
}
