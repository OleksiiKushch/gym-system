package org.example.dao.impl.hibernate;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import org.example.dao.TrainingDao;
import org.example.entity.Training;
import org.example.entity.search.BaseTrainingCriteria;
import org.example.entity.search.TraineeTrainingsCriteria;
import org.example.entity.search.TrainerTrainingsCriteria;
import org.hibernate.SessionFactory;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Getter
@Repository
public class HibernateTrainingDao implements TrainingDao {

    private final SessionFactory sessionFactory;

    public HibernateTrainingDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void insert(Training training) {
        getSessionFactory().inTransaction(session -> session.persist(training));
    }

    @Override
    public void update(Training training) {
        getSessionFactory().inTransaction(session -> session.merge(training));
    }

    @Override
    public Optional<Training> findByName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Training> findTraineeTrainingsByCriteria(TraineeTrainingsCriteria criteria) {
        return findTrainingsByCriteria(criteria);
    }

    @Override
    public Collection<Training> findTrainerTrainingsByCriteria(TrainerTrainingsCriteria criteria) {
        return findTrainingsByCriteria(criteria);
    }

    private Collection<Training> findTrainingsByCriteria(BaseTrainingCriteria criteria) {
        return getSessionFactory().fromSession(session -> {
            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Training> query = builder.createQuery(Training.class);
            Root<Training> root = query.from(Training.class);

            List<Predicate> predicates = criteria.formPredicatesForHibernateCriteria(builder, root);

            query.select(root).where(predicates.toArray(new Predicate[0]));

            return session.createQuery(query).list();
        });
    }
}
