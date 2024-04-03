package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.example.constants.GeneralConstants.PASSWORD_PLACEHOLDER;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AfterRegistrationResponse {

    private String username;
    private String password;

    @Override
    public String toString() {
        return "AfterRegistrationResponse{" +
                "username=" + username + '\'' +
                ", password=" + PASSWORD_PLACEHOLDER + '}';
    }
}
