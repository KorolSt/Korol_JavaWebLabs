package com.example.hotel.model.service;

import com.example.hotel.model.entity.Payment;
import com.example.hotel.model.service.exception.ServiceException;

import java.util.List;

public interface PaymentService {
    List<Payment> getPayments(int limit, int currentPage, Sorter sorter) throws ServiceException;

    List<Payment> getUserPayments(int userId, int limit, int currentPage, Sorter sorter) throws ServiceException;

    void pay(int paymentId) throws ServiceException;

    int countRowsNumber() throws ServiceException;

    int countRowsNumberByUserId(int userId) throws ServiceException;

    boolean cancelPayment(int paymentId) throws ServiceException;

    boolean updateFailedPayments()  throws ServiceException;
}
