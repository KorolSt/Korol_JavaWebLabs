package com.example.hotel.model.service.impl;

import com.example.hotel.model.service.*;

public class ServiceFactoryImpl implements ServiceFactory {

    private static final ServiceFactoryImpl instance = new ServiceFactoryImpl();

    private final ApartmentService apartmentService = new ApartmentServiceImpl();
    private final PaymentService paymentService = new PaymentServiceImpl();
    private final ReservationService reservationService = new ReservationServiceImpl();
    private final UserService userService = new UserServiceImpl();
    private final OrderService orderService = new OrderServiceImpl();
    private final DayPriceService dayPriceService = new DayPriceServiceImpl();

    private ServiceFactoryImpl() {

    }

    public static ServiceFactoryImpl getInstance() {
        return instance;
    }

    @Override
    public ApartmentService getApartmentService(){
        return apartmentService;
    }

    @Override
    public PaymentService getPaymentService() {
        return paymentService;
    }

    @Override
    public ReservationService getReservationService() {
        return reservationService;
    }

    @Override
    public UserService getUserService() {
        return userService;
    }

    @Override
    public OrderService getOrderService() {
        return orderService;
    }

    @Override
    public DayPriceService getDayPriceService() {
        return dayPriceService;
    }
}
