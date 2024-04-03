package org.example.entity;

import org.apache.commons.csv.CSVRecord;

public interface CsvRecordInitializer {

    void initByCsvRecord(CSVRecord record);
}
