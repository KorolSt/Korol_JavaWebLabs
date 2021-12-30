package com.example.hotel.model.service;

import com.example.hotel.model.entity.Reservation;
import com.example.hotel.model.service.exception.ServiceException;

import java.util.List;

public interface ReservationService {
    Reservation getReservationById(int id) throws ServiceException;

    List<Reservation> getReservations(int limit, int offset, Sorter sorter) throws ServiceException;

    void reserve (Reservation reservation) throws ServiceException;

    void reserveByOrderId(int orderId) throws ServiceException;

    List<Reservation> getUserReservations(int userId, int currentPage, int limit, Sorter sorter) throws ServiceException;

    void cancelReservation(int reservationId) throws ServiceException;

    int countRowsNumber() throws  ServiceException;

    int countRowsNumberByUserId(int userId) throws ServiceException;
}
