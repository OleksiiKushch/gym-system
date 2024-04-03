package org.example.mapper;

/**
 * @param <S> source
 * @param <T> target
 */
public interface Mapper<S, T> {

    T map(S source);
}
