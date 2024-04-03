package org.example.dto.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.validation.StringLocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTraineeForm {

    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @StringLocalDate
    private String dateOfBirthday;
    private String address;
    @NotNull
    private Boolean isActive;
}
