package com.example.hotel.model.dao.function;

import java.sql.SQLException;

@FunctionalInterface
public interface ThrowableBiConsumer<T, U> {
    void accept(T t, U u) throws SQLException;
}
