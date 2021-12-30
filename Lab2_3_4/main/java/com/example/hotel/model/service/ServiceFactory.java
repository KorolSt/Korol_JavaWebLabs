package com.example.hotel.model.service;

public interface ServiceFactory {
    ApartmentService getApartmentService();

    PaymentService getPaymentService();

    ReservationService getReservationService();

    UserService getUserService();

    OrderService getOrderService();

    DayPriceService getDayPriceService();
}
