package org.example.dao.impl.hibernate;

import lombok.Getter;
import org.example.dao.TrainingTypeDao;
import org.example.entity.TrainingType;
import org.example.entity.TrainingTypeEnum;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

import static org.example.constants.PersistenceLayerConstants.FIND_ALL_TRAINING_TYPES_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_TRAINING_TYPE_BY_NAME_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.TRAINING_TYPE_PARAM;

@Getter
@Repository
public class HibernateTrainingTypeDao implements TrainingTypeDao {

    private final SessionFactory sessionFactory;

    public HibernateTrainingTypeDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<TrainingType> findTrainingType(TrainingTypeEnum trainingType) {
        return getSessionFactory().fromSession(session ->
                session.createNamedQuery(FIND_TRAINING_TYPE_BY_NAME_QUERY_NAME, TrainingType.class)
                        .setParameter(TRAINING_TYPE_PARAM, trainingType)
                        .uniqueResultOptional()
        );
    }

    @Override
    public Collection<TrainingType> findAllTrainingTypes() {
        return getSessionFactory().fromSession(session ->
                session.createNamedQuery(FIND_ALL_TRAINING_TYPES_QUERY_NAME, TrainingType.class)
                        .getResultList()
        );
    }
}
