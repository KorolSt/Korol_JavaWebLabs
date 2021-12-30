package com.example.hotel.model.dao;

import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.dto.ApartmentDTO;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.enums.ApartmentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ApartmentDAO extends AbstractDAO<Apartment> {
    List<Apartment> getFreeApartmentsForOrder(int orderId) throws DAOException;

    List<Apartment> getByType(ApartmentType apartmentType) throws DAOException;

    boolean checkIsApartmentFree(int apartmentId, LocalDate dateIn, LocalDate dateOut) throws DAOException;

    List<ApartmentDTO> getAllForRequest(LocalDate dateIn, LocalDate dateOut, int placeCount) throws DAOException;

    BigDecimal getApartmentPrice(int apartmentId, LocalDate dateIn, LocalDate dateOut) throws DAOException;
}
