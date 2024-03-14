package org.example.dao.impl.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import org.example.dao.impl.TrainingSearchDaoImpl;
import org.example.entity.Training;
import org.example.entity.search.TraineeTrainingsCriteria;
import org.example.entity.search.TrainerTrainingsCriteria;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HibernateTrainingDaoUnitTest {

    @InjectMocks
    TrainingSearchDaoImpl testInstance;

    @Mock
    EntityManager entityManager;
    @Mock
    CriteriaBuilder criteriaBuilder;

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
    void shouldFindTraineeTrainingsByCriteria() {
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Training.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Training.class)).thenReturn(root);
        when(traineeTrainingsCriteria.formPredicatesForJpaCriteria(criteriaBuilder, root)).thenReturn(predicates);
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(trainingQuery);
        when(trainingQuery.getResultList()).thenReturn(trainings);

        var actualResult = testInstance.findTraineeTrainingsByCriteria(traineeTrainingsCriteria);

        assertEquals(trainings, actualResult);
    }

    @Test
    void shouldFindTrainerTrainingsByCriteria() {
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Training.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Training.class)).thenReturn(root);
        when(trainerTrainingsCriteria.formPredicatesForJpaCriteria(criteriaBuilder, root)).thenReturn(predicates);
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(trainingQuery);
        when(trainingQuery.getResultList()).thenReturn(trainings);

        var actualResult = testInstance.findTrainerTrainingsByCriteria(trainerTrainingsCriteria);

        assertEquals(trainings, actualResult);
    }

}