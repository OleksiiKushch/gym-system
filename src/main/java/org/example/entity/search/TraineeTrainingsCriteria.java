package org.example.entity.search;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.entity.Training;
import org.example.entity.TrainingTypeEnum;

import java.util.List;

import static org.example.constants.PersistenceLayerConstants.FIRST_NAME_FIELD_NAME;
import static org.example.constants.PersistenceLayerConstants.NAME_FIELD_NAME;
import static org.example.constants.PersistenceLayerConstants.TRAININEE_FIELD_NAME;
import static org.example.constants.PersistenceLayerConstants.TRAININER_FIELD_NAME;
import static org.example.constants.PersistenceLayerConstants.TRAINING_TYPE_FIELD_NAME;
import static org.example.constants.PersistenceLayerConstants.USERNAME_FIELD_NAME;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeTrainingsCriteria extends BaseTrainingCriteria {

    private String traineeUsername;
    private String trainerFirstName;
    private TrainingTypeEnum trainingType;

    @Override
    public List<Predicate> formPredicatesForJpaCriteria(CriteriaBuilder builder, Root<Training> root) {
        List<Predicate> predicates = super.formPredicatesForJpaCriteria(builder, root);

        addPredicateWithNeighboringTable(predicates, TRAININEE_FIELD_NAME, USERNAME_FIELD_NAME, traineeUsername, builder, root);
        addPredicateWithNeighboringTable(predicates, TRAININER_FIELD_NAME, FIRST_NAME_FIELD_NAME, trainerFirstName, builder, root);
        addPredicateWithNeighboringTable(predicates, TRAINING_TYPE_FIELD_NAME, NAME_FIELD_NAME, trainingType, builder, root);

        return predicates;
    }
}
