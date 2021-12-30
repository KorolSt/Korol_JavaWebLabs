package com.example.hotel.model.service.impl;

import com.example.hotel.model.dao.DAOFactory;
import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.dao.impl.DAOFactoryImpl;
import com.example.hotel.model.service.DayPriceService;
import com.example.hotel.model.service.exception.ServiceException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DayPriceServiceImpl implements DayPriceService {
    @Override
    public void setDayPricesForApartment(int apartmentId, LocalDate dateIn, LocalDate dateOut, BigDecimal price) throws ServiceException {
        DAOFactory daoFactory = DAOFactoryImpl.getInstance();
        try {
            daoFactory.getDayPriceDAO().setDayPricesForApartment(apartmentId, dateIn, dateOut, price);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
