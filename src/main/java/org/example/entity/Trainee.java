package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.example.constants.PersistenceLayerConstants.DELETE_TRAINEE_BY_ID_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.DELETE_TRAINEE_BY_USERNAME_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_ALL_TRAINEES_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_TRAINEE_BY_USERNAME_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_TRAINEE_WITH_TRAININGS_BY_USERNAME_QUERY_NAME;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@NamedQuery(
        name = FIND_TRAINEE_BY_USERNAME_QUERY_NAME,
        query = "FROM Trainee WHERE username = :username"
)
@NamedQuery(
        name = FIND_TRAINEE_WITH_TRAININGS_BY_USERNAME_QUERY_NAME,
        query = "SELECT DISTINCT t FROM Trainee t " +
        "LEFT JOIN FETCH t.trainings " +
        "WHERE t.username = :username"
)
@NamedQuery(
        name = FIND_ALL_TRAINEES_QUERY_NAME,
        query = "FROM Trainee"
)
@NamedQuery(
        name = DELETE_TRAINEE_BY_ID_QUERY_NAME,
        query = "DELETE Trainee WHERE id = :id"
)
@NamedQuery(
        name = DELETE_TRAINEE_BY_USERNAME_QUERY_NAME,
        query = "DELETE Trainee WHERE username = :username"
)
@Entity
@Table(name = "trainees")
@OnDelete(action = OnDeleteAction.CASCADE)
public class Trainee extends User {

    private LocalDate dateOfBirthday;
    private String address;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "trainee")
    private List<Training> trainings = new ArrayList<>();

    @Builder.Default
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private List<Trainer> trainers = new ArrayList<>();

    @Override
    public void initByCsvRecord(CSVRecord record) {
        super.initByCsvRecord(record);
        setDateOfBirthday(LocalDate.parse(record.get(6)));
        setAddress(record.get(7));
    }

    public void addTrainer(Trainer trainer) {
        trainers.add(trainer);
        trainer.getTrainees().add(this);
    }
}
