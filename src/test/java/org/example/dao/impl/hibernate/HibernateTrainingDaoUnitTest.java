package org.example.dao.impl.hibernate;

import jakarta.persistence.criteria.Predicate;
import org.example.entity.Training;
import org.example.entity.search.TraineeTrainingsCriteria;
import org.example.entity.search.TrainerTrainingsCriteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;
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
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HibernateTrainingDaoUnitTest {

    @InjectMocks
    HibernateTrainingDao testInstance;

    @Mock
    SessionFactory sessionFactory;
    @Mock
    Session session;
    @Mock
    HibernateCriteriaBuilder hibernateCriteriaBuilder;

    @Captor
    ArgumentCaptor<Consumer<Session>> sessionConsumerArgCaptor;
    @Captor
    ArgumentCaptor<Function<Session, Collection<Training>>> collectionReturnSessionFuncArgCaptor;

    @Mock
    Training training;
    @Mock
    Query<Training> trainingQuery;
    @Mock
    TraineeTrainingsCriteria traineeTrainingsCriteria;
    @Mock
    TrainerTrainingsCriteria trainerTrainingsCriteria;
    @Mock
    JpaCriteriaQuery<Training> criteriaQuery;
    @Mock
    JpaRoot<Training> root;
    @Spy
    List<Training> trainings = new ArrayList<>();
    @Spy
    List<Predicate> predicates = new ArrayList<>();

    @Test
    void shouldInsertTraining() {
        doNothing().when(sessionFactory).inTransaction(sessionConsumerArgCaptor.capture());

        testInstance.insert(training);

        Consumer<Session> sessionConsumer = sessionConsumerArgCaptor.getValue();
        sessionConsumer.accept(session);
        verify(session).persist(training);
    }

    @Test
    void shouldUpdateTraining() {
        doNothing().when(sessionFactory).inTransaction(sessionConsumerArgCaptor.capture());

        testInstance.update(training);

        Consumer<Session> sessionConsumer = sessionConsumerArgCaptor.getValue();
        sessionConsumer.accept(session);
        verify(session).merge(training);
    }

    @Test
    void shouldFindTraineeTrainingsByCriteria() {
        when(sessionFactory.fromSession(collectionReturnSessionFuncArgCaptor.capture())).thenReturn(trainings);
        when(session.getCriteriaBuilder()).thenReturn(hibernateCriteriaBuilder);
        when(hibernateCriteriaBuilder.createQuery(Training.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Training.class)).thenReturn(root);
        when(traineeTrainingsCriteria.formPredicatesForHibernateCriteria(hibernateCriteriaBuilder, root)).thenReturn(predicates);
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(session.createQuery(criteriaQuery)).thenReturn(trainingQuery);
        when(trainingQuery.list()).thenReturn(trainings);

        var actualResult = testInstance.findTraineeTrainingsByCriteria(traineeTrainingsCriteria);

        Function<Session, Collection<Training>> sessionFunction = collectionReturnSessionFuncArgCaptor.getValue();
        sessionFunction.apply(session);
        assertEquals(trainings, actualResult);
    }

    @Test
    void shouldFindTrainerTrainingsByCriteria() {
        when(sessionFactory.fromSession(collectionReturnSessionFuncArgCaptor.capture())).thenReturn(trainings);
        when(session.getCriteriaBuilder()).thenReturn(hibernateCriteriaBuilder);
        when(hibernateCriteriaBuilder.createQuery(Training.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Training.class)).thenReturn(root);
        when(trainerTrainingsCriteria.formPredicatesForHibernateCriteria(hibernateCriteriaBuilder, root)).thenReturn(predicates);
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(session.createQuery(criteriaQuery)).thenReturn(trainingQuery);
        when(trainingQuery.list()).thenReturn(trainings);

        var actualResult = testInstance.findTrainerTrainingsByCriteria(trainerTrainingsCriteria);

        Function<Session, Collection<Training>> sessionFunction = collectionReturnSessionFuncArgCaptor.getValue();
        sessionFunction.apply(session);
        assertEquals(trainings, actualResult);
    }

}