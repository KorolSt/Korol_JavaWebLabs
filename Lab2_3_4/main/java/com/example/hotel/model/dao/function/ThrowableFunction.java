package com.example.hotel.model.dao.function;

import java.sql.SQLException;

@FunctionalInterface
public interface ThrowableFunction<T, R>{
    R apply(T t) throws SQLException;
}
