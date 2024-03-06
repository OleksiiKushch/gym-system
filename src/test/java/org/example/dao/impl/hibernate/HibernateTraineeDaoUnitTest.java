package org.example.dao.impl.hibernate;

import org.example.entity.Trainee;
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

import static org.example.constants.PersistenceLayerConstants.DELETE_TRAINEE_BY_ID_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.DELETE_TRAINEE_BY_USERNAME_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_ALL_TRAINEES_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_TRAINEE_BY_USERNAME_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_TRAINEE_WITH_TRAININGS_BY_USERNAME_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.ID_PARAM;
import static org.example.constants.PersistenceLayerConstants.USERNAME_PARAM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HibernateTraineeDaoUnitTest {

    private static final Integer TEST_TRAINEE_ID = 1;
    private static final String TEST_TRAINEE_USERNAME = "John.Doe";

    @InjectMocks
    HibernateTraineeDao testInstance;

    @Mock
    SessionFactory sessionFactory;
    @Mock
    Session session;

    @Captor
    ArgumentCaptor<Consumer<Session>> sessionConsumerArgCaptor;
    @Captor
    ArgumentCaptor<Function<Session, Trainee>> traineeReturnSessionFuncArgCaptor;
    @Captor
    ArgumentCaptor<Function<Session, Optional<Trainee>>> optionalReturnSessionFuncArgCaptor;
    @Captor
    ArgumentCaptor<Function<Session, Collection<Trainee>>> collectionReturnSessionFuncArgCaptor;

    @Mock
    Trainee trainee;
    @Mock
    Query<Trainee> query;
    @Mock
    Query<Object> queryWithoutType;
    @Spy
    List<Trainee> trainees = new ArrayList<>();
    @Spy
    List<Trainer> currentTrainers = new ArrayList<>();
    @Spy
    Collection<Trainer> newTrainers = new ArrayList<>();

    @Test
    void shouldInsertTrainee() {
        doNothing().when(sessionFactory).inTransaction(sessionConsumerArgCaptor.capture());

        testInstance.insert(trainee);

        Consumer<Session> sessionConsumer = sessionConsumerArgCaptor.getValue();
        sessionConsumer.accept(session);
        verify(session).persist(trainee);
    }

    @Test
    void shouldUpdateTrainee() {
        doNothing().when(sessionFactory).inTransaction(sessionConsumerArgCaptor.capture());

        testInstance.update(trainee);

        Consumer<Session> sessionConsumer = sessionConsumerArgCaptor.getValue();
        sessionConsumer.accept(session);
        verify(session).merge(trainee);
    }

    @Test
    void shouldFindById() {
        when(sessionFactory.fromSession(traineeReturnSessionFuncArgCaptor.capture())).thenReturn(trainee);
        when(session.get(Trainee.class, TEST_TRAINEE_ID)).thenReturn(trainee);

        var actualResult = testInstance.findById(TEST_TRAINEE_ID);

        Function<Session, Trainee> sessionFunction = traineeReturnSessionFuncArgCaptor.getValue();
        sessionFunction.apply(session);
        assertTrue(actualResult.isPresent());
        assertEquals(trainee, actualResult.get());
    }

    @Test
    void shouldFindByUsername() {
        when(sessionFactory.fromSession(optionalReturnSessionFuncArgCaptor.capture())).thenReturn(Optional.of(trainee));
        when(session.createNamedQuery(FIND_TRAINEE_BY_USERNAME_QUERY_NAME, Trainee.class)).thenReturn(query);
        when(query.setParameter(USERNAME_PARAM, TEST_TRAINEE_USERNAME)).thenReturn(query);
        when(query.uniqueResultOptional()).thenReturn(Optional.of(trainee));

        var actualResult = testInstance.findByUsername(TEST_TRAINEE_USERNAME);

        Function<Session, Optional<Trainee>> sessionFunction = optionalReturnSessionFuncArgCaptor.getValue();
        sessionFunction.apply(session);
        assertTrue(actualResult.isPresent());
        assertEquals(trainee, actualResult.get());
    }

    @Test
    void shouldFindWithTrainingsByUsername() {
        when(sessionFactory.fromSession(optionalReturnSessionFuncArgCaptor.capture())).thenReturn(Optional.of(trainee));
        when(session.createNamedQuery(FIND_TRAINEE_WITH_TRAININGS_BY_USERNAME_QUERY_NAME, Trainee.class)).thenReturn(query);
        when(query.setParameter(USERNAME_PARAM, TEST_TRAINEE_USERNAME)).thenReturn(query);
        when(query.uniqueResultOptional()).thenReturn(Optional.of(trainee));

        var actualResult = testInstance.findWithTrainingsByUsername(TEST_TRAINEE_USERNAME);

        Function<Session, Optional<Trainee>> sessionFunction = optionalReturnSessionFuncArgCaptor.getValue();
        sessionFunction.apply(session);
        assertTrue(actualResult.isPresent());
        assertEquals(trainee, actualResult.get());
    }

    @Test
    void shouldFindAll() {
        when(sessionFactory.fromSession(collectionReturnSessionFuncArgCaptor.capture())).thenReturn(trainees);
        when(session.createNamedQuery(FIND_ALL_TRAINEES_QUERY_NAME, Trainee.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(trainees);

        var actualResult = testInstance.findAll();

        Function<Session, Collection<Trainee>> sessionFunction = collectionReturnSessionFuncArgCaptor.getValue();
        sessionFunction.apply(session);
        assertEquals(trainees, actualResult);
    }

    @Test
    void shouldUpdateTrainersList() {
        doNothing().when(sessionFactory).inTransaction(sessionConsumerArgCaptor.capture());
        when(session.createNamedQuery(FIND_TRAINEE_BY_USERNAME_QUERY_NAME, Trainee.class)).thenReturn(query);
        when(query.setParameter(USERNAME_PARAM, TEST_TRAINEE_USERNAME)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(trainee);
        when(trainee.getTrainers()).thenReturn(currentTrainers);
        when(session.merge(trainee)).thenReturn(trainee);

        testInstance.updateTrainersList(TEST_TRAINEE_USERNAME, newTrainers);

        Consumer<Session> sessionConsumer = sessionConsumerArgCaptor.getValue();
        sessionConsumer.accept(session);
        verify(currentTrainers).clear();
        verify(currentTrainers).addAll(newTrainers);
        verify(session).merge(trainee);
    }

    @Test
    void shouldRemoveById() {
        doNothing().when(sessionFactory).inTransaction(sessionConsumerArgCaptor.capture());
        when(session.createNamedQuery(DELETE_TRAINEE_BY_ID_QUERY_NAME, null)).thenReturn(queryWithoutType);
        when(queryWithoutType.setParameter(ID_PARAM, TEST_TRAINEE_ID)).thenReturn(queryWithoutType);

        testInstance.remove(TEST_TRAINEE_ID);

        Consumer<Session> sessionConsumer = sessionConsumerArgCaptor.getValue();
        sessionConsumer.accept(session);
        verify(queryWithoutType).executeUpdate();
    }

    @Test
    void shouldRemoveByUsername() {
        doNothing().when(sessionFactory).inTransaction(sessionConsumerArgCaptor.capture());
        when(session.createNamedQuery(DELETE_TRAINEE_BY_USERNAME_QUERY_NAME, null)).thenReturn(queryWithoutType);
        when(queryWithoutType.setParameter(USERNAME_PARAM, TEST_TRAINEE_USERNAME)).thenReturn(queryWithoutType);

        testInstance.removeByUsername(TEST_TRAINEE_USERNAME);

        Consumer<Session> sessionConsumer = sessionConsumerArgCaptor.getValue();
        sessionConsumer.accept(session);
        verify(queryWithoutType).executeUpdate();
    }
}