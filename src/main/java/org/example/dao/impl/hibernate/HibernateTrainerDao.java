package org.example.dao.impl.hibernate;

import lombok.Getter;
import org.example.dao.TrainerDao;
import org.example.entity.Trainer;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

import static org.example.constants.PersistenceLayerConstants.FIND_ALL_TRAINERS_NOT_ASSIGNED_TO_TRAINEE_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_ALL_TRAINERS_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_TRAINER_BY_USERNAME_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_TRAINER_WITH_TRAININGS_BY_USERNAME_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.USERNAME_PARAM;

@Getter
@Repository
public class HibernateTrainerDao extends HibernateAbstractUserDao<Trainer> implements TrainerDao {

    private final SessionFactory sessionFactory;

    public HibernateTrainerDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Trainer> findById(Integer id) {
        return findById(Trainer.class, id);
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        return findByUsername(FIND_TRAINER_BY_USERNAME_QUERY_NAME, Trainer.class, username);
    }

    @Override
    public Optional<Trainer> findWithTrainingsByUsername(String username) {
        return findByUsername(FIND_TRAINER_WITH_TRAININGS_BY_USERNAME_QUERY_NAME, Trainer.class, username);
    }

    @Override
    public Collection<Trainer> findAll() {
        return findAll(FIND_ALL_TRAINERS_QUERY_NAME, Trainer.class);
    }

    @Override
    public Collection<Trainer> findAllThatNotAssignedOnTrainee(String traineeUsername) {
        return getSessionFactory().fromSession(session ->
                session.createNamedQuery(FIND_ALL_TRAINERS_NOT_ASSIGNED_TO_TRAINEE_QUERY_NAME, Trainer.class)
                        .setParameter(USERNAME_PARAM, traineeUsername)
                        .getResultList()
        );
    }
}
