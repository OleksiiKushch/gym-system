package org.example.dao;

import org.example.entity.RevokedToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface RevokedTokenDao extends CrudRepository<RevokedToken, Integer> {

    boolean existsByToken(String token);
    void removeAllByExpirationDateTimeBefore(Date dateTime);
}
