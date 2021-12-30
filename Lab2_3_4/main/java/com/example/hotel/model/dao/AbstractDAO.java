package com.example.hotel.model.dao;

import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.entity.Entity;

import java.util.List;

public interface AbstractDAO<T extends Entity> {
    T getById(final int id) throws DAOException;

    boolean deleteById(final int id) throws DAOException;

    boolean create(final T t) throws DAOException;

    boolean update(final T t) throws DAOException;

    List<T> getAll() throws DAOException;


    int countRows() throws DAOException;
}
