package org.example.dao.impl.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.example.config.storage.inmemory.InMemoryStorageWithIntId;
import org.example.entity.User;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public abstract class InMemoryAbstractUserDao<T extends User> {

    private static final String USER_ALREADY_EXISTS_EXCEPTION_MESSAGE = "User (%s) with id '%d' already exists";
    private static final String USER_ALREADY_EXISTS_LOG_MESSAGE = "User ({}) with id '{}' already exists";
    private static final String USER_DOES_NOT_EXIST_EXCEPTION_MESSAGE = "User (%s) with id '%d' does not exist";
    private static final String USER_DOES_NOT_EXIST_LOG_MESSAGE = "User ({}) with id '{}' does not exist";

    public void insert(T user) {
        if (Objects.isNull(user.getUserId())) {
            user.setUserId(getUserStorage().findNextId());
        } else if (getUserStorage().containsKey(user.getUserId())) {
            log.error(USER_ALREADY_EXISTS_LOG_MESSAGE, getTypeOfUser(user), user.getUserId());
            throw new IllegalArgumentException(formExceptionMessage(USER_ALREADY_EXISTS_EXCEPTION_MESSAGE,
                    getTypeOfUser(user), user.getUserId()));
        }
        getUserStorage().put(user.getUserId(), user);
    }

    public void update(T user) {
        if (getUserStorage().containsKey(user.getUserId())) {
            getUserStorage().put(user.getUserId(), user);
        } else {
            log.error(USER_DOES_NOT_EXIST_LOG_MESSAGE, getTypeOfUser(user), user.getUserId());
            throw new IllegalArgumentException(formExceptionMessage(USER_DOES_NOT_EXIST_EXCEPTION_MESSAGE,
                    getTypeOfUser(user), user.getUserId()));
        }
    }

    public Optional<T> findById(Integer id) {
        return Optional.ofNullable(getUserStorage().get(id));
    }

    public Optional<T> findByUsername(String username) {
        throw new UnsupportedOperationException();
    }

    public Collection<T> findAll() {
        return getUserStorage().values();
    }

    protected abstract InMemoryStorageWithIntId<T> getUserStorage();

    private String formExceptionMessage(String message, Object... args) {
        return String.format(message, args);
    }

    private String getTypeOfUser(User user) {
        return user.getClass().getSimpleName();
    }
}
