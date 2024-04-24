package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;
import java.time.Year;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {

    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean isActive;
    private Map<Year, Map<Month, Integer>> history;
}
