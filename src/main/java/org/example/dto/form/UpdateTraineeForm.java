package org.example.dto.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTraineeForm {

    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    private String dateOfBirthday;
    private String address;
}
