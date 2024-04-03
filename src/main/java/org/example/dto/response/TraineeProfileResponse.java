package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeProfileResponse {

    private String username;
    private String firstName;
    private String lastName;
    private String dateOfBirthday;
    private String address;
    private Boolean isActive;
    private List<SimpleTrainerResponse> trainers;
}
