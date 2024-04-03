package org.example.dto.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static org.example.constants.GeneralConstants.PASSWORD_PLACEHOLDER;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

    @ToString.Include(name = "password")
    private String maskPassword() {
        return PASSWORD_PLACEHOLDER;
    }
}
