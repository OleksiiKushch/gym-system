package org.example.dao.impl.hibernate;

import org.example.entity.Trainer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.example.constants.PersistenceLayerConstants.FIND_ALL_TRAINERS_NOT_ASSIGNED_TO_TRAINEE_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_ALL_TRAINERS_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_TRAINER_BY_USERNAME_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_TRAINER_WITH_TRAININGS_BY_USERNAME_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.USERNAME_PARAM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HibernateTrainerDaoUnitTest {

    private static final Integer TEST_TRAINEE_ID = 1;
    private static final String TEST_TRAINEE_USERNAME = "John.Doe";

    @InjectMocks
    HibernateTrainerDao testInstance;

    @Mock
    SessionFactory sessionFactory;
    @Mock
    Session session;

    @Captor
    ArgumentCaptor<Consumer<Session>> sessionConsumerArgCaptor;
    @Captor
    ArgumentCaptor<Function<Session, Trainer>> trainerReturnSessionFuncArgCaptor;
    @Captor
    ArgumentCaptor<Function<Session, Optional<Trainer>>> optionalReturnSessionFuncArgCaptor;
    @Captor
    ArgumentCaptor<Function<Session, Collection<Trainer>>> collectionReturnSessionFuncArgCaptor;

    @Mock
    Trainer trainer;
    @Mock
    Query<Trainer> query;
    @Spy
    List<Trainer> trainers = new ArrayList<>();

    @Test
    void shouldInsertTrainer() {
        doNothing().when(sessionFactory).inTransaction(sessionConsumerArgCaptor.capture());

        testInstance.insert(trainer);

        Consumer<Session> sessionConsumer = sessionConsumerArgCaptor.getValue();
        sessionConsumer.accept(session);
        verify(session).persist(trainer);
    }

    @Test
    void shouldUpdateTrainer() {
        doNothing().when(sessionFactory).inTransaction(sessionConsumerArgCaptor.capture());

        testInstance.update(trainer);

        Consumer<Session> sessionConsumer = sessionConsumerArgCaptor.getValue();
        sessionConsumer.accept(session);
        verify(session).merge(trainer);
    }

    @Test
    void shouldFindById() {
        when(sessionFactory.fromSession(trainerReturnSessionFuncArgCaptor.capture())).thenReturn(trainer);
        when(session.get(Trainer.class, TEST_TRAINEE_ID)).thenReturn(trainer);

        var actualResult = testInstance.findById(TEST_TRAINEE_ID);

        Function<Session, Trainer> sessionFunction = trainerReturnSessionFuncArgCaptor.getValue();
        sessionFunction.apply(session);
        assertTrue(actualResult.isPresent());
        assertEquals(trainer, actualResult.get());
    }

    @Test
    void shouldFindByUsername() {
        when(sessionFactory.fromSession(optionalReturnSessionFuncArgCaptor.capture())).thenReturn(Optional.of(trainer));
        when(session.createNamedQuery(FIND_TRAINER_BY_USERNAME_QUERY_NAME, Trainer.class)).thenReturn(query);
        when(query.setParameter(USERNAME_PARAM, TEST_TRAINEE_USERNAME)).thenReturn(query);
        when(query.uniqueResultOptional()).thenReturn(Optional.of(trainer));

        var actualResult = testInstance.findByUsername(TEST_TRAINEE_USERNAME);

        Function<Session, Optional<Trainer>> sessionFunction = optionalReturnSessionFuncArgCaptor.getValue();
        sessionFunction.apply(session);
        assertTrue(actualResult.isPresent());
        assertEquals(trainer, actualResult.get());
    }

    @Test
    void shouldFindWithTrainingsByUsername() {
        when(sessionFactory.fromSession(optionalReturnSessionFuncArgCaptor.capture())).thenReturn(Optional.of(trainer));
        when(session.createNamedQuery(FIND_TRAINER_WITH_TRAININGS_BY_USERNAME_QUERY_NAME, Trainer.class)).thenReturn(query);
        when(query.setParameter(USERNAME_PARAM, TEST_TRAINEE_USERNAME)).thenReturn(query);
        when(query.uniqueResultOptional()).thenReturn(Optional.of(trainer));

        var actualResult = testInstance.findWithTrainingsByUsername(TEST_TRAINEE_USERNAME);

        Function<Session, Optional<Trainer>> sessionFunction = optionalReturnSessionFuncArgCaptor.getValue();
        sessionFunction.apply(session);
        assertTrue(actualResult.isPresent());
        assertEquals(trainer, actualResult.get());
    }

    @Test
    void shouldFindAll() {
        when(sessionFactory.fromSession(collectionReturnSessionFuncArgCaptor.capture())).thenReturn(trainers);
        when(session.createNamedQuery(FIND_ALL_TRAINERS_QUERY_NAME, Trainer.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(trainers);

        var actualResult = testInstance.findAll();

        Function<Session, Collection<Trainer>> sessionFunction = collectionReturnSessionFuncArgCaptor.getValue();
        sessionFunction.apply(session);
        assertEquals(trainers, actualResult);
    }

    @Test
    void shouldFindAllThatNotAssignedOnTrainee() {
        when(sessionFactory.fromSession(collectionReturnSessionFuncArgCaptor.capture())).thenReturn(trainers);
        when(session.createNamedQuery(FIND_ALL_TRAINERS_NOT_ASSIGNED_TO_TRAINEE_QUERY_NAME, Trainer.class)).thenReturn(query);
        when(query.setParameter(USERNAME_PARAM, TEST_TRAINEE_USERNAME)).thenReturn(query);
        when(query.getResultList()).thenReturn(trainers);

        var actualResult = testInstance.findAllThatNotAssignedOnTrainee(TEST_TRAINEE_USERNAME);

        Function<Session, Collection<Trainer>> sessionFunction = collectionReturnSessionFuncArgCaptor.getValue();
        sessionFunction.apply(session);
        assertEquals(trainers, actualResult);
    }
}