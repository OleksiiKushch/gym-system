package org.example.dto.form.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.validation.StringLocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchTrainerTrainingsPayload {

    @StringLocalDate
    private String fromDate;
    @StringLocalDate
    private String toDate;
    private String traineeFirstName;
}
