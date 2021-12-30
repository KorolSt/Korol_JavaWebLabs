package com.example.hotel.model.dao;

public interface DAOFactory {
    ApartmentDAO getApartmentDAO();

    DayPriceDAO getDayPriceDAO();

    PaymentDAO getPaymentDAO();

    ReservationDAO getReservationDAO();

    UserDAO getUserDAO();

    OrderDAO getOrderDAO();
}
