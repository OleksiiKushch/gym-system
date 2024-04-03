package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static org.example.constants.PersistenceLayerConstants.FIND_TRAINING_TYPE_BY_NAME_QUERY_NAME;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(
        name = FIND_TRAINING_TYPE_BY_NAME_QUERY_NAME,
        query = "FROM TrainingType WHERE name = :trainingType"
)
@Entity
@Table(name = "training_type")
public class TrainingType {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private TrainingTypeEnum name;
}
