package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.List;

import static org.example.constants.PersistenceLayerConstants.FIND_ALL_TRAINERS_NOT_ASSIGNED_TO_TRAINEE_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_ALL_TRAINERS_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_TRAINER_BY_USERNAME_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_TRAINER_WITH_TRAININGS_BY_USERNAME_QUERY_NAME;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@NamedQuery(
        name = FIND_TRAINER_BY_USERNAME_QUERY_NAME,
        query = "FROM Trainer WHERE username = :username"
)
@NamedQuery(
        name = FIND_TRAINER_WITH_TRAININGS_BY_USERNAME_QUERY_NAME,
        query = "SELECT DISTINCT t FROM Trainer t " +
        "LEFT JOIN FETCH t.trainings " +
        "WHERE t.username = :username"
)
@NamedQuery(
        name = FIND_ALL_TRAINERS_QUERY_NAME,
        query = "FROM Trainer"
)
@NamedQuery(
        name = FIND_ALL_TRAINERS_NOT_ASSIGNED_TO_TRAINEE_QUERY_NAME,
        query = "FROM Trainer er WHERE NOT EXISTS (FROM er.trainees ee WHERE ee.username = :username)"
)
@Entity
@Table(name = "trainers")
public class Trainer extends User {

    @ManyToOne
    @JoinColumn(name = "specialization")
    private TrainingType specialization;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "trainer")
    private List<Training> trainings = new ArrayList<>();

    @Builder.Default
    @ToString.Exclude
    @ManyToMany(mappedBy = "trainers", fetch = FetchType.EAGER)
    private List<Trainee> trainees = new ArrayList<>();

    @Override
    public void initByCsvRecord(CSVRecord record) {
        super.initByCsvRecord(record);
        setSpecialization(TrainingType.builder().name(TrainingTypeEnum.valueOf(record.get(6))).build());
    }
}
