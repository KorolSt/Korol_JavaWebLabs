package com.example.hotel.model.dao;

import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.entity.User;

public interface UserDAO extends AbstractDAO<User> {
    User getByLogin(String login) throws DAOException;

    User getByPaymentId(int paymentId) throws DAOException;
}
