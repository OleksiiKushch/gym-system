package org.example.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
@Service
public class DefaultUserService implements UserService {

    private static final String USERNAME_SEPARATOR  = ".";
    private static final int NEW_USERNAME = -1;
    private static final int USERNAME_WITHOUT_SERIAL_NUMBER = 0;

    @Value("${generate.random.password.length}")
    private int randomPasswordLength;

    @Value("${password.characters.allow}")
    private String passwordAllowedCharacters;

    private final UserDao userDao;

    @Override
    public String calculateUsername(User user) {
        String newUsername = user.getFirstName() + USERNAME_SEPARATOR + user.getLastName();
        int newSerialNumber = getAllUsers().stream()
                .map(User::getUsername)
                .filter(username -> username.startsWith(newUsername))
                .map(username -> processSerialNumber(username, newUsername))
                .max(Integer::compareTo)
                .orElse(NEW_USERNAME);
        return newSerialNumber == NEW_USERNAME ? newUsername : newUsername + ++newSerialNumber;
    }

    @Override
    public String generateRandomPassword() {
        StringBuilder password = new StringBuilder(getRandomPasswordLength());
        Stream.generate(() -> getRandomCharacter(new SecureRandom()))
                .limit(getRandomPasswordLength())
                .forEach(password::append);
        return password.toString();
    }

    @Override
    public Optional<User> getUserForUsername(String username) {
        return getUserDao().findByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return IterableUtils.toList(getUserDao().findAll());
    }

    /**
     * Default user authentication by username and password. Retrieve user by username and match passwords.
     */
    @Override
    public Optional<User> authenticateUser(String username, String password) {
        Optional<User> user = userDao.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }

    @Override
    public void updateUser(User user) {
        userDao.save(user);
    }

    private int processSerialNumber(String username, String newUsername) {
        if (username.equals(newUsername)) {
            return USERNAME_WITHOUT_SERIAL_NUMBER;
        } else {
            return Integer.parseInt(username.substring(newUsername.length()));
        }
    }

    private char getRandomCharacter(SecureRandom random) {
        return getPasswordAllowedCharacters().charAt(random.nextInt(getPasswordAllowedCharacters().length()));
    }
}
