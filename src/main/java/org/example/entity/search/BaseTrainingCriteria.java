package org.example.entity.search;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.example.entity.Training;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.example.constants.PersistenceLayerConstants.TRAINING_DATE_FIELD_NAME;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseTrainingCriteria {

    private LocalDate fromDate;
    private LocalDate toDate;

    public List<Predicate> formPredicatesForJpaCriteria(CriteriaBuilder builder, Root<Training> root) {
        List<Predicate> predicates = new ArrayList<>();

        if(Objects.nonNull(fromDate)) {
            predicates.add(builder.greaterThanOrEqualTo(root.get(TRAINING_DATE_FIELD_NAME), fromDate));
        }
        if(Objects.nonNull(toDate)) {
            predicates.add(builder.lessThanOrEqualTo(root.get(TRAINING_DATE_FIELD_NAME), toDate));
        }

        return predicates;
    }

    protected void addPredicateWithNeighboringTable(List<Predicate> predicates, String fieldName, String neighborTableFieldName,
                                                    Object value, CriteriaBuilder builder, Root<Training> root) {
        if(Objects.nonNull(value)) {
            if (!(value instanceof String) || StringUtils.isNotEmpty((String) value)) {
                predicates.add(builder.equal(root.get(fieldName).get(neighborTableFieldName), value));
            }
        }
    }
}
