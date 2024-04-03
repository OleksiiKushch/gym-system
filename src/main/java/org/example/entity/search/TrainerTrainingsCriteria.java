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

import java.util.List;

import static org.example.constants.PersistenceLayerConstants.FIRST_NAME_FIELD_NAME;
import static org.example.constants.PersistenceLayerConstants.TRAININEE_FIELD_NAME;
import static org.example.constants.PersistenceLayerConstants.TRAININER_FIELD_NAME;
import static org.example.constants.PersistenceLayerConstants.USERNAME_FIELD_NAME;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerTrainingsCriteria extends BaseTrainingCriteria {

    private String trainerUsername;
    private String traineeFirstName;

    @Override
    public List<Predicate> formPredicatesForJpaCriteria(CriteriaBuilder builder, Root<Training> root) {
        List<Predicate> predicates = super.formPredicatesForJpaCriteria(builder, root);

        addPredicateWithNeighboringTable(predicates, TRAININER_FIELD_NAME, USERNAME_FIELD_NAME, trainerUsername, builder, root);
        addPredicateWithNeighboringTable(predicates, TRAININEE_FIELD_NAME, FIRST_NAME_FIELD_NAME, traineeFirstName, builder, root);

        return predicates;
    }
}
