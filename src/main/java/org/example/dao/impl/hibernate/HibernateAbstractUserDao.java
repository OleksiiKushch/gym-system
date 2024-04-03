package org.example.dao.impl.hibernate;

import org.example.entity.User;
import org.hibernate.SessionFactory;

import java.util.Collection;
import java.util.Optional;

import static org.example.constants.PersistenceLayerConstants.USERNAME_PARAM;

public abstract class HibernateAbstractUserDao<T extends User> {

    public void insert(T user) {
        getSessionFactory().inTransaction(session -> session.persist(user));
    }

    public void update(T user) {
        getSessionFactory().inTransaction(session -> session.merge(user));
    }

    protected Optional<T> findById(Class<T> type, Integer id) {
        return Optional.ofNullable(getSessionFactory().fromSession(session -> session.get(type, id)));
    }

    protected Optional<T> findByUsername(String queryName, Class<T> type, String username) {
        return getSessionFactory().fromSession(session ->
                session.createNamedQuery(queryName, type)
                        .setParameter(USERNAME_PARAM, username)
                        .uniqueResultOptional()
        );
    }

    protected Collection<T> findAll(String queryName, Class<T> type) {
        return getSessionFactory().fromSession(session ->
                session.createNamedQuery(queryName, type)
                        .getResultList()
        );
    }

    protected abstract SessionFactory getSessionFactory();
}
