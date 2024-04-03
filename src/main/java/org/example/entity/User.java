package org.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.csv.CSVRecord;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class User implements CsvRecordInitializer {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Boolean isActive;

    @Override
    public void initByCsvRecord(CSVRecord record) {
        setUserId(Integer.parseInt(record.get(0)));
        setFirstName(record.get(1));
        setLastName(record.get(2));
        setUsername(record.get(3));
        setPassword(record.get(4));
        setActive(Boolean.parseBoolean(record.get(5)));
    }

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
