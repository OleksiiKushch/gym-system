package org.example.dao.impl.hibernate;

import org.example.entity.TrainingType;
import org.example.entity.TrainingTypeEnum;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.function.Function;

import static org.example.constants.PersistenceLayerConstants.FIND_TRAINING_TYPE_BY_NAME_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.TRAINING_TYPE_PARAM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HibernateTrainingTypeDaoUnitTest {

    private static final TrainingTypeEnum TRAINING_TYPE = TrainingTypeEnum.BOXING;

    @InjectMocks
    HibernateTrainingTypeDao testInstance;

    @Mock
    SessionFactory sessionFactory;
    @Mock
    Session session;

    @Captor
    ArgumentCaptor<Function<Session, Optional<TrainingType>>> singleReturnSessionFuncArgCaptor;

    @Mock
    TrainingType trainingType;
    @Mock
    Query<TrainingType> query;

    @Test
    void shouldFindByTrainingType() {
        when(sessionFactory.fromSession(singleReturnSessionFuncArgCaptor.capture())).thenReturn(Optional.of(trainingType));
        when(session.createNamedQuery(FIND_TRAINING_TYPE_BY_NAME_QUERY_NAME, TrainingType.class)).thenReturn(query);
        when(query.setParameter(TRAINING_TYPE_PARAM, TRAINING_TYPE)).thenReturn(query);
        when(query.uniqueResultOptional()).thenReturn(Optional.of(trainingType));

        var actualResult = testInstance.findTrainingType(TRAINING_TYPE);

        Function<Session, Optional<TrainingType>> sessionFunction = singleReturnSessionFuncArgCaptor.getValue();
        sessionFunction.apply(session);
        assertTrue(actualResult.isPresent());
        assertEquals(trainingType, actualResult.get());
    }
}