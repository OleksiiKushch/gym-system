package org.example.dto.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.example.constants.GeneralConstants.PASSWORD_PLACEHOLDER;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordForm {

    @NotEmpty
    private String currentPassword;
    @NotEmpty
    private String newPassword;
    @NotEmpty
    private String confirmPassword;

    @Override
    public String toString() {
        return "ChangePasswordForm{" +
                "currentPassword=" + PASSWORD_PLACEHOLDER +
                ", newPassword=" + PASSWORD_PLACEHOLDER +
                ", confirmPassword=" + PASSWORD_PLACEHOLDER + '}';
    }
}
