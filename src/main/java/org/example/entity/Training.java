package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "trainings")
public class Training implements CsvRecordInitializer {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Trainee trainee;

    @ManyToOne
    private Trainer trainer;

    @Column(nullable = false)
    private String trainingName;

    @ManyToOne
    private TrainingType trainingType;

    @Column(nullable = false)
    private LocalDate trainingDate;

    @Column(nullable = false)
    private Integer trainingDuration;

    @Override
    public void initByCsvRecord(CSVRecord record) {
        setTraineeId(Integer.parseInt(record.get(0)));
        setTrainerId(Integer.parseInt(record.get(1)));
        setTrainingName(record.get(2));
        setTrainingType(TrainingType.builder().name(TrainingTypeEnum.valueOf(record.get(3))).build());
        setTrainingDate(LocalDate.parse(record.get(4)));
        setTrainingDuration(Integer.parseInt(record.get(5)));
    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
        trainee.getTrainings().add(this);
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
        trainer.getTrainings().add(this);
    }

    public void setTraineeId(Integer traineeId) {
        this.trainee = Trainee.builder().userId(traineeId).build();
    }

    public void setTrainerId(Integer trainerId) {
        this.trainer = Trainer.builder().userId(trainerId).build();
    }
}
