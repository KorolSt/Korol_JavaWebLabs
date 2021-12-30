package com.example.hotel.model.service;

import com.example.hotel.model.entity.User;
import com.example.hotel.model.service.exception.ServiceException;

public interface UserService {
    User login(String login, String password) throws ServiceException;

    void register(User user) throws ServiceException;

    User getByPayment(int paymentId) throws ServiceException;
}
