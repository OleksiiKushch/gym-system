package org.example.config.storage.hibernate;

import jakarta.annotation.PostConstruct;
import org.example.entity.TrainingType;
import org.example.entity.TrainingTypeEnum;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static org.example.constants.PersistenceLayerConstants.FIND_TRAINING_TYPE_BY_NAME_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.TRAINING_TYPE_PARAM;

@Component
public class InitEssentialData {

    private final SessionFactory sessionFactory;

    public InitEssentialData(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @PostConstruct
    public void importTrainingTypes() {
        sessionFactory.inTransaction(session ->
            Arrays.stream(TrainingTypeEnum.values())
                    .filter(trainingType -> isNotExists(session, trainingType))
                    .forEach(trainingType -> session.persist(TrainingType.builder().name(trainingType).build())));
    }

    private boolean isNotExists(Session session, TrainingTypeEnum trainingType) {
        return session.createNamedQuery(FIND_TRAINING_TYPE_BY_NAME_QUERY_NAME, TrainingType.class)
                .setParameter(TRAINING_TYPE_PARAM, trainingType)
                .uniqueResultOptional()
                .isEmpty();
    }
}
