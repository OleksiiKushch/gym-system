package org.example.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.dao.TrainingSearchDao;
import org.example.entity.Training;
import org.example.entity.search.BaseTrainingCriteria;
import org.example.entity.search.TraineeTrainingsCriteria;
import org.example.entity.search.TrainerTrainingsCriteria;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Repository
public class TrainingSearchDaoImpl implements TrainingSearchDao {

    private final EntityManager entityManager;

    @Override
    public Iterable<Training> findTraineeTrainingsByCriteria(TraineeTrainingsCriteria criteria) {
        return findByCriteria(criteria);
    }

    @Override
    public Iterable<Training> findTrainerTrainingsByCriteria(TrainerTrainingsCriteria criteria) {
        return findByCriteria(criteria);
    }

    private Collection<Training> findByCriteria(BaseTrainingCriteria criteria) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Training> query = builder.createQuery(Training.class);
        Root<Training> root = query.from(Training.class);

        List<Predicate> predicates = criteria.formPredicatesForJpaCriteria(builder, root);

        query.select(root).where(predicates.toArray(new Predicate[0]));

        return getEntityManager().createQuery(query).getResultList();
    }
}
