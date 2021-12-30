package com.example.hotel.model.service;

import com.example.hotel.model.entity.Order;
import com.example.hotel.model.service.exception.ServiceException;

import java.util.List;

public interface OrderService {
    List<Order> getUserOrders(int userId, int currentPage, int records, Sorter sorter) throws ServiceException;

    int countRowsNumberByUserId(int userId) throws ServiceException;

    List<Order> getOrders(int limit, int currentPage, Sorter sorter) throws ServiceException;

    int countRowsNumber() throws ServiceException;

    void cancelOrder(int orderId) throws ServiceException;

    void connectApartmentAndOrder(int orderId, int apartmentId) throws ServiceException;

    void makeOrderByClient(Order order) throws ServiceException;
}
