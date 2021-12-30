package com.example.hotel.model.service.impl;

import com.example.hotel.model.dao.DAOFactory;
import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.dao.exception.EntityNotFoundDAOException;
import com.example.hotel.model.dao.impl.DAOFactoryImpl;
import com.example.hotel.model.entity.Payment;
import com.example.hotel.model.service.PaymentService;
import com.example.hotel.model.service.Sorter;
import com.example.hotel.model.service.exception.EntityNotFoundServiceException;
import com.example.hotel.model.service.exception.NotFoundPeriodServiceException;
import com.example.hotel.model.service.exception.ServiceException;

import java.util.*;
import java.util.stream.Collectors;

public class PaymentServiceImpl implements PaymentService {
    private static final Map<Sorter, Comparator<Payment>> comparators = new EnumMap<>(Sorter.class);

    static {
        comparators.put(Sorter.PAYMENTS_BY_STATUS_ASC, Comparator.comparing(Payment::getStatus));
        comparators.put(Sorter.PAYMENTS_BY_STATUS_DESC, (o1, o2) -> o2.getStatus().compareTo(o1.getStatus()));
        comparators.put(Sorter.PAYMENTS_BY_EXPIRE_DATE_ASC, Comparator.comparing(Payment::getExpireDate));
        comparators.put(Sorter.PAYMENTS_BY_EXPIRE_DATE_DESC, (o1, o2) -> o2.getExpireDate().compareTo(o1.getExpireDate()));
    }

    @Override
    public List<Payment> getPayments(int limit, int currentPage, Sorter sorter) throws ServiceException {
        List<Payment> payments;
        try {
            payments = DAOFactoryImpl.getInstance().getPaymentDAO().getAll();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        int offset = limit * currentPage - limit;
        payments = payments.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
        return safeSort(payments, sorter);
    }

    @Override
    public List<Payment> getUserPayments(int userId, int limit, int currentPage, Sorter sorter) throws ServiceException {
        DAOFactory daoFactory = DAOFactoryImpl.getInstance();
        List<Payment> payments;
        try {
            payments = daoFactory.getPaymentDAO().getPaymentsByUserId(userId);
        } catch (EntityNotFoundDAOException e) {
            throw new EntityNotFoundServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        int offset = limit * currentPage - limit;
        payments = payments.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
        return safeSort(payments, sorter);
    }

    @Override
    public void pay(int paymentId) throws ServiceException {
        DAOFactory daoFactory = DAOFactoryImpl.getInstance();
        try {
            daoFactory.getPaymentDAO().pay(paymentId);
        } catch (EntityNotFoundDAOException e) {
            throw new NotFoundPeriodServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int countRowsNumber() throws ServiceException {
        try {
            return DAOFactoryImpl.getInstance().getPaymentDAO().countRows();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int countRowsNumberByUserId(int userId) throws ServiceException {
        try {
            return DAOFactoryImpl.getInstance().getPaymentDAO().getPaymentsByUserId(userId).size();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean cancelPayment(int paymentId) throws ServiceException {
        try {
            return DAOFactoryImpl.getInstance().getPaymentDAO().cancelPayment(paymentId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean updateFailedPayments() throws ServiceException {
        try {
            return DAOFactoryImpl.getInstance().getPaymentDAO().updateFailedPayments();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    private List<Payment> safeSort(List<Payment> payments, Sorter sorter) {
        if (payments == null) return new ArrayList<>();
        payments.sort(comparators.get(Objects.requireNonNullElse(sorter, Sorter.PAYMENTS_BY_EXPIRE_DATE_DESC)));
        return payments;
    }
}
