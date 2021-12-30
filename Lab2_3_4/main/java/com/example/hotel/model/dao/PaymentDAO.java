package com.example.hotel.model.dao;

import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.entity.Payment;

import java.util.List;

public interface PaymentDAO extends AbstractDAO<Payment> {

    List<Payment> getPaymentsByUserId(int userId) throws DAOException;

    void pay(int paymentId) throws DAOException;

    boolean cancelPayment(int paymentId) throws DAOException;

    boolean updateFailedPayments() throws DAOException;
}
