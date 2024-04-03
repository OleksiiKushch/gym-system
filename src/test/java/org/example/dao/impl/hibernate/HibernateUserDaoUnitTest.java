package org.example.dao.impl.hibernate;

import org.example.entity.User;
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

import static org.example.constants.PersistenceLayerConstants.FIND_ALL_USERS_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_USER_BY_USERNAME_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.USERNAME_PARAM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HibernateUserDaoUnitTest {

    private static final String TEST_USERNAME = "John.Doe";

    @InjectMocks
    HibernateUserDao testInstance;

    @Mock
    SessionFactory sessionFactory;
    @Mock
    Session session;

    @Captor
    ArgumentCaptor<Function<Session, Optional<User>>> singleReturnSessionFuncArgCaptor;
    @Captor
    ArgumentCaptor<Function<Session, Collection<User>>> collectionReturnSessionFuncArgCaptor;
    @Captor
    ArgumentCaptor<Consumer<Session>> sessionConsumerArgCaptor;

    @Mock
    User user;
    @Mock
    Query<User> query;
    @Spy
    List<User> users = new ArrayList<>();

    @Test
    void shouldFindByUsername() {
        when(sessionFactory.fromSession(singleReturnSessionFuncArgCaptor.capture())).thenReturn(Optional.of(user));
        when(session.createNamedQuery(FIND_USER_BY_USERNAME_QUERY_NAME, User.class)).thenReturn(query);
        when(query.setParameter(USERNAME_PARAM, TEST_USERNAME)).thenReturn(query);
        when(query.uniqueResultOptional()).thenReturn(Optional.of(user));

        var actualResult = testInstance.findByUsername(TEST_USERNAME);

        Function<Session, Optional<User>> sessionFunction = singleReturnSessionFuncArgCaptor.getValue();
        sessionFunction.apply(session);
        assertTrue(actualResult.isPresent());
        assertEquals(user, actualResult.get());
    }

    @Test
    void shouldFindAll() {
        when(sessionFactory.fromSession(collectionReturnSessionFuncArgCaptor.capture())).thenReturn(users);
        when(session.createNamedQuery(FIND_ALL_USERS_QUERY_NAME, User.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(users);

        var actualResult = testInstance.findAll();

        Function<Session, Collection<User>> sessionFunction = collectionReturnSessionFuncArgCaptor.getValue();
        sessionFunction.apply(session);
        assertEquals(users, actualResult);
    }

    @Test
    void shouldUpdateUser() {
        doNothing().when(sessionFactory).inTransaction(sessionConsumerArgCaptor.capture());

        testInstance.update(user);

        Consumer<Session> sessionConsumer = sessionConsumerArgCaptor.getValue();
        sessionConsumer.accept(session);
        verify(session).merge(user);
    }
}