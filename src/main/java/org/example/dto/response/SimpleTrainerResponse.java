package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleTrainerResponse {

    private String username;
    private String firstName;
    private String lastName;
    private String specialization;
}
