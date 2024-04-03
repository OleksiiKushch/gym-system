package org.example.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.csv.CSVRecord;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class Trainer extends User {

    private Integer userId;
    private String specialization;

    @Override
    public void initByCsvRecord(CSVRecord record) {
        super.initByCsvRecord(record);
        setSpecialization(record.get(6));
    }
}
