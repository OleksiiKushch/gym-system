package org.example.dto.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.example.constants.GeneralConstants.PASSWORD_PLACEHOLDER;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

    @Override
    public String toString() {
        return "LoginForm{" +
                "username=" + username +
                ", password=" + PASSWORD_PLACEHOLDER + '}';
    }
}
