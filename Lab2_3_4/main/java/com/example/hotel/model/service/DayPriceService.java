package com.example.hotel.model.service;

import com.example.hotel.model.service.exception.ServiceException;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DayPriceService {
    void setDayPricesForApartment(int apartmentId, LocalDate dateIn, LocalDate dateOut, BigDecimal price) throws ServiceException;
}
