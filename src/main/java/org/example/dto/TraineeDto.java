package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeDto {

    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private LocalDate dateOfBirthday;
    private String address;
    private Boolean isActive;
    private List<TrainerDto> trainers;
}
