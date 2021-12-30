package com.example.hotel.model.service;

import com.example.hotel.model.dto.ApartmentDTO;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.enums.ApartmentType;
import com.example.hotel.model.service.exception.ServiceException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ApartmentService {
    Apartment getApartmentById(int id) throws ServiceException;

    BigDecimal getApartmentPrice(int apartmentId, LocalDate dateIn, LocalDate dateOut) throws ServiceException;

    List<Apartment> getApartments() throws ServiceException;

    List<Apartment> getFreeApartments(int orderId) throws ServiceException;

    List<ApartmentDTO> getApartments(LocalDate[] dates, int placeCount) throws ServiceException;

    List<Apartment> getApartments(ApartmentType apartmentType) throws ServiceException;

    boolean isApartmentFree(int apartmentId, LocalDate dateIn, LocalDate dateOut) throws ServiceException;

    List<ApartmentDTO> pagination(int records, int currentPage, List<ApartmentDTO> apartments);

    List<ApartmentDTO> filterByStatus(List<ApartmentDTO> apartments, List<ApartmentDTO.Status> apartmentStatuses);

    void sort(List<ApartmentDTO> apartments, List<Sorter> sorters);
}
