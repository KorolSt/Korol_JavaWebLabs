package com.example.hotel.model.dao;

import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.entity.Order;

import java.util.List;

public interface OrderDAO extends AbstractDAO<Order> {
    List<Order> getOrdersByUserId(int userId) throws DAOException;

    void cancelOrder(int orderId) throws DAOException;

    void processOrder(int orderId, int apartmentId) throws DAOException;
}
