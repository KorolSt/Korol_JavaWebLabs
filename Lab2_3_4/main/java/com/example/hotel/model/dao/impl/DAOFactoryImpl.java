package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.*;

public class DAOFactoryImpl implements DAOFactory {
    private static final DAOFactoryImpl instance = new DAOFactoryImpl();

    private final UserDAO userDAO = new UserDAOImpl();
    private final ApartmentDAO apartmentDAO = new ApartmentDAOImpl();
    private final DayPriceDAO dayPriceDAO = new DayPriceDAOImpl();
    private final PaymentDAO paymentDAO = new PaymentDAOImpl();
    private final ReservationDAO reservationDAO = new ReservationDAOImpl();
    private final OrderDAO orderDAO = new OrderDAOImpl();

    private DAOFactoryImpl() {

    }

    public static DAOFactoryImpl getInstance() {
        return instance;
    }

    @Override
    public UserDAO getUserDAO() {
        return userDAO;
    }

    @Override
    public ApartmentDAO getApartmentDAO() {
        return apartmentDAO;
    }

    @Override
    public DayPriceDAO getDayPriceDAO() {
        return dayPriceDAO;
    }

    @Override
    public PaymentDAO getPaymentDAO() {
        return paymentDAO;
    }

    @Override
    public ReservationDAO getReservationDAO() {
        return reservationDAO;
    }

    @Override
    public OrderDAO getOrderDAO() {
        return orderDAO;
    }
}
