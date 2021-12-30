package com.example.hotel.model.service.impl;

import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.dao.exception.EntityNotFoundDAOException;
import com.example.hotel.model.dao.impl.DAOFactoryImpl;
import com.example.hotel.model.dto.ApartmentDTO;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.enums.ApartmentType;
import com.example.hotel.model.service.ApartmentService;
import com.example.hotel.model.service.Sorter;
import com.example.hotel.model.service.exception.EntityNotFoundServiceException;
import com.example.hotel.model.service.exception.ServiceException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApartmentServiceImpl implements ApartmentService {

    private static final Map<Sorter, Comparator<ApartmentDTO>> ascComparators = new EnumMap<>(Sorter.class);
    private static final Map<Sorter, Comparator<ApartmentDTO>> descComparators = new EnumMap<>(Sorter.class);

    static {
        Comparator<ApartmentDTO> amountComparator = Comparator.comparing(ApartmentDTO::getAmount);
        Comparator<ApartmentDTO> placeCountComparator = Comparator.comparing(ApartmentDTO::getPlaceCount);
        Comparator<ApartmentDTO> statusComparator = Comparator.comparing(ApartmentDTO::getStatus);
        Comparator<ApartmentDTO> apartmentTypeComparator = Comparator.comparing(ApartmentDTO::getType);

        ascComparators.put(Sorter.APARTMENTS_BY_PRICE_ASC, amountComparator);
        ascComparators.put(Sorter.APARTMENTS_BY_PLACE_NUMBER_ASC, placeCountComparator);
        ascComparators.put(Sorter.APARTMENTS_BY_STATUS_ASC, statusComparator);
        ascComparators.put(Sorter.APARTMENTS_BY_APARTMENT_TYPE_ASC, apartmentTypeComparator);

        descComparators.put(Sorter.APARTMENTS_BY_PRICE_DESC, amountComparator.reversed());
        descComparators.put(Sorter.APARTMENTS_BY_PLACE_NUMBER_DESC, placeCountComparator.reversed());
        descComparators.put(Sorter.APARTMENTS_BY_STATUS_DESC, statusComparator.reversed());
        descComparators.put(Sorter.APARTMENTS_BY_APARTMENT_TYPE_DESC, apartmentTypeComparator.reversed());
    }

    @Override
    public BigDecimal getApartmentPrice(int apartmentId, LocalDate dateIn, LocalDate dateOut) throws ServiceException {
        try {
            return DAOFactoryImpl.getInstance().getApartmentDAO().getApartmentPrice(apartmentId, dateIn, dateOut);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Apartment getApartmentById(int id) throws ServiceException {
        try {
            return DAOFactoryImpl.getInstance().getApartmentDAO().getById(id);
        } catch (EntityNotFoundDAOException e) {
            throw new EntityNotFoundServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Apartment> getApartments() throws ServiceException {
        try {
            return DAOFactoryImpl.getInstance().getApartmentDAO().getAll();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Apartment> getFreeApartments(int orderId) throws ServiceException {
        try {
            return DAOFactoryImpl.getInstance().getApartmentDAO().getFreeApartmentsForOrder(orderId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<ApartmentDTO> getApartments(LocalDate[] dates, int placeCount) throws ServiceException {
        List<ApartmentDTO> apartments;
        try {
            apartments = DAOFactoryImpl.getInstance().getApartmentDAO().getAllForRequest(dates[0], dates[1], placeCount);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return apartments;
    }

    @Override
    public List<Apartment> getApartments(ApartmentType apartmentType) throws ServiceException {
        try {
            return DAOFactoryImpl.getInstance().getApartmentDAO().getByType(apartmentType);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean isApartmentFree(int apartmentId, LocalDate dateIn, LocalDate dateOut) throws ServiceException {
        try {
            return DAOFactoryImpl.getInstance().getApartmentDAO()
                    .checkIsApartmentFree(apartmentId, dateIn, dateOut);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    public List<ApartmentDTO> pagination(int limit, int currentPage, List<ApartmentDTO> apartments) {
        int offset = limit * currentPage - limit;
        return apartments.stream().skip(offset).limit(limit).collect(Collectors.toList());
    }

    public void sort(List<ApartmentDTO> apartments, List<Sorter> sorters) {
        descComparators.forEach((sorter, comparator) -> apartments.sort(comparator));
        sorters.stream().filter(ascComparators::containsKey)
                .forEach(s -> apartments.sort(ascComparators.get(s)));
    }

    public List<ApartmentDTO> filterByStatus(List<ApartmentDTO> apartments,
                                             List<ApartmentDTO.Status> statuses) {
        if (statuses.isEmpty()) return apartments;

        return apartments.stream()
                .filter(a -> statuses.contains(a.getStatus()))
                .collect(Collectors.toList());
    }
}
