package com.example.hotel.model.dao;

import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.entity.DayPrice;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DayPriceDAO extends AbstractDAO<DayPrice> {
    void setDayPricesForApartment(int apartmentId, LocalDate dateIn, LocalDate dateOut, BigDecimal price) throws DAOException;
}
