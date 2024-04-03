package org.example.dao.impl.hibernate;

import lombok.Getter;
import org.example.dao.TraineeDao;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

import static org.example.constants.PersistenceLayerConstants.DELETE_TRAINEE_BY_ID_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.DELETE_TRAINEE_BY_USERNAME_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_ALL_TRAINEES_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_TRAINEE_BY_USERNAME_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_TRAINEE_WITH_TRAININGS_BY_USERNAME_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.ID_PARAM;
import static org.example.constants.PersistenceLayerConstants.USERNAME_PARAM;

@Getter
@Repository
public class HibernateTraineeDao extends HibernateAbstractUserDao<Trainee> implements TraineeDao {

    private final SessionFactory sessionFactory;

    public HibernateTraineeDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Trainee> findById(Integer id) {
        return findById(Trainee.class, id);
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        return findByUsername(FIND_TRAINEE_BY_USERNAME_QUERY_NAME, Trainee.class, username);
    }

    @Override
    public Optional<Trainee> findWithTrainingsByUsername(String username) {
        return findByUsername(FIND_TRAINEE_WITH_TRAININGS_BY_USERNAME_QUERY_NAME, Trainee.class, username);
    }

    @Override
    public Collection<Trainee> findAll() {
        return findAll(FIND_ALL_TRAINEES_QUERY_NAME, Trainee.class);
    }

    @Override
    public void updateTrainersList(String username, Collection<Trainer> newTrainers) {
        getSessionFactory().inTransaction(session -> {
            Trainee trainee = session.createNamedQuery(FIND_TRAINEE_BY_USERNAME_QUERY_NAME, Trainee.class)
                    .setParameter(USERNAME_PARAM, username)
                    .getSingleResult();
            trainee.getTrainers().clear();
            trainee.getTrainers().addAll(newTrainers);
            session.merge(trainee);
        });
    }

    @Override
    public void remove(Integer id) {
        getSessionFactory().inTransaction(session ->
                session.createNamedQuery(DELETE_TRAINEE_BY_ID_QUERY_NAME, null)
                        .setParameter(ID_PARAM, id)
                        .executeUpdate()
        );
    }

    @Override
    public void removeByUsername(String username) {
        getSessionFactory().inTransaction(session ->
            session.createNamedQuery(DELETE_TRAINEE_BY_USERNAME_QUERY_NAME, null)
                    .setParameter(USERNAME_PARAM, username)
                    .executeUpdate());
    }
}
