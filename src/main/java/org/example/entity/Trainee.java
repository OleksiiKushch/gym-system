package org.example.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.csv.CSVRecord;

import java.time.LocalDate;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class Trainee extends User {

    private Integer userId;
    private LocalDate dateOfBirthday;
    private String address;

    @Override
    public void initByCsvRecord(CSVRecord record) {
        super.initByCsvRecord(record);
        setDateOfBirthday(LocalDate.parse(record.get(6)));
        setAddress(record.get(7));
    }
}
