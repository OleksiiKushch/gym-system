package org.example.dao.impl.hibernate;

import lombok.Getter;
import org.example.dao.UserDao;
import org.example.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

import static org.example.constants.PersistenceLayerConstants.FIND_ALL_USERS_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.FIND_USER_BY_USERNAME_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.USERNAME_PARAM;

@Getter
@Repository
public class HibernateUserDao implements UserDao {

    private final SessionFactory sessionFactory;

    public HibernateUserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return getSessionFactory().fromSession(session ->
                session.createNamedQuery(FIND_USER_BY_USERNAME_QUERY_NAME, User.class)
                        .setParameter(USERNAME_PARAM, username)
                        .uniqueResultOptional()
        );
    }

    @Override
    public Collection<User> findAll() {
        return getSessionFactory().fromSession(session ->
                session.createNamedQuery(FIND_ALL_USERS_QUERY_NAME, User.class).getResultList()
        );
    }

    @Override
    public void update(User user) {
        getSessionFactory().inTransaction(session -> session.merge(user));
    }
}
