package com.example.hotel.model.service.impl;

import com.example.hotel.model.dao.DAOFactory;
import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.dao.exception.EntityNotFoundDAOException;
import com.example.hotel.model.dao.impl.DAOFactoryImpl;
import com.example.hotel.model.entity.Reservation;
import com.example.hotel.model.service.ReservationService;
import com.example.hotel.model.service.Sorter;
import com.example.hotel.model.service.exception.EntityNotFoundServiceException;
import com.example.hotel.model.service.exception.ServiceException;

import java.util.*;
import java.util.stream.Collectors;

public class ReservationServiceImpl implements ReservationService {

    private static final Map<Sorter, Comparator<Reservation>> comparators = new EnumMap<>(Sorter.class);

    static {
        comparators.put(Sorter.RESERVATIONS_BY_STATUS_ASC, Comparator.comparing(Reservation::getStatus));
        comparators.put(Sorter.RESERVATIONS_BY_STATUS_DESC, (o1, o2) -> o2.getStatus().compareTo(o1.getStatus()));
        comparators.put(Sorter.RESERVATIONS_BY_DATE_IN_ASC, Comparator.comparing(Reservation::getDateIn));
        comparators.put(Sorter.RESERVATIONS_BY_DATE_IN_DESC, (o1, o2) -> o2.getDateIn().compareTo(o1.getDateIn()));
        comparators.put(Sorter.RESERVATIONS_BY_RESERVATION_DATE_ASC, Comparator.comparing(Reservation::getReservationDate));
        comparators.put(Sorter.RESERVATIONS_BY_RESERVATION_DATE_DESC, (o1, o2) -> o2.getReservationDate().compareTo(o1.getReservationDate()));
    }

    @Override
    public Reservation getReservationById(int id) throws ServiceException {
        DAOFactory daoFactory = DAOFactoryImpl.getInstance();
        Reservation reservation;
        try {
            reservation = daoFactory.getReservationDAO().getById(id);
        } catch (EntityNotFoundDAOException e) {
            throw new EntityNotFoundServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return reservation;
    }

    @Override
    public List<Reservation> getReservations(int limit, int currentPage, Sorter sorter) throws ServiceException {
        DAOFactory daoFactory = DAOFactoryImpl.getInstance();
        List<Reservation> reservations;
        try {
            reservations = daoFactory.getReservationDAO().getAll();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        int offset = limit * currentPage - limit;
        reservations = reservations.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
        return safeSort(reservations, sorter);
    }

    @Override
    public void reserve(Reservation reservation) throws ServiceException {
        DAOFactory daoFactory = DAOFactoryImpl.getInstance();
        try {
            daoFactory.getReservationDAO().reserve(reservation);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void reserveByOrderId(int orderId) throws ServiceException {
        DAOFactory daoFactory = DAOFactoryImpl.getInstance();
        try {
            daoFactory.getReservationDAO().reserveByOrderId(orderId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Reservation> getUserReservations(int userId, int currentPage, int limit, Sorter sorter) throws ServiceException {
        DAOFactory daoFactory = DAOFactoryImpl.getInstance();
        List<Reservation> reservations;
        try {
            reservations = daoFactory.getReservationDAO().getReservationsByUserId(userId);
        } catch (EntityNotFoundDAOException e) {
            throw new EntityNotFoundServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        int offset = limit * currentPage - limit;
        reservations = reservations.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
        return safeSort(reservations, sorter);
    }

    @Override
    public void cancelReservation(int reservationId) throws ServiceException {
        DAOFactory daoFactory = DAOFactoryImpl.getInstance();
        try {
            daoFactory.getReservationDAO().cancelReservation(reservationId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int countRowsNumber() throws ServiceException {
        try {
            return DAOFactoryImpl.getInstance().getReservationDAO().countRows();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int countRowsNumberByUserId(int userId) throws ServiceException {
        try {
            return DAOFactoryImpl.getInstance().getReservationDAO().getReservationsByUserId(userId).size();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    private List<Reservation> safeSort(List<Reservation> reservations, Sorter sorter) {
        if (reservations == null) return new ArrayList<>();
        reservations.sort(comparators.get(Objects.requireNonNullElse(sorter, Sorter.RESERVATIONS_BY_RESERVATION_DATE_DESC)));
        return reservations;
    }
}
