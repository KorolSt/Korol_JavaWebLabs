package com.example.hotel.model.dao;

import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.entity.Reservation;

import java.util.List;

public interface ReservationDAO extends AbstractDAO<Reservation> {
    List<Reservation> getReservationsByUserId(int userId) throws DAOException;

    boolean reserve(Reservation reservation) throws DAOException;

    void reserveByOrderId(int orderId) throws DAOException;

    void cancelReservation(int reservationId) throws DAOException;
}
