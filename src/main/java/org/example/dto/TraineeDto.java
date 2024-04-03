package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String password;
    private String dateOfBirthday;
    private String address;
}
