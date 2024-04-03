package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.csv.CSVRecord;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Training implements CsvRecordInitializer {

    private Integer traineeId;
    private Integer trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDate trainingDate;
    private Integer trainingDuration;

    @Override
    public void initByCsvRecord(CSVRecord record) {
        setTraineeId(Integer.parseInt(record.get(0)));
        setTrainerId(Integer.parseInt(record.get(1)));
        setTrainingName(record.get(2));
        setTrainingType(TrainingType.builder().name(record.get(3)).build());
        setTrainingDate(LocalDate.parse(record.get(4)));
        setTrainingDuration(Integer.parseInt(record.get(5)));
    }
}
