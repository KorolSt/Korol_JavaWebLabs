package com.example.hotel.model.service.impl;

import com.example.hotel.model.dao.DAOFactory;
import com.example.hotel.model.dao.OrderDAO;
import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.dao.exception.EntityAlreadyExistsDAOException;
import com.example.hotel.model.dao.impl.DAOFactoryImpl;
import com.example.hotel.model.entity.Order;
import com.example.hotel.model.service.OrderService;
import com.example.hotel.model.service.Sorter;
import com.example.hotel.model.service.exception.EntityAlreadyExistsServiceException;
import com.example.hotel.model.service.exception.ServiceException;

import java.util.*;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    private static final Map<Sorter, Comparator<Order>> comparators = new EnumMap<>(Sorter.class);

    static {
        comparators.put(Sorter.ORDERS_BY_STATUS_DESC, Comparator.comparing(Order::getStatus));
        comparators.put(Sorter.ORDERS_BY_STATUS_ASC, (o1, o2) -> o2.getStatus().compareTo(o1.getStatus()));
        comparators.put(Sorter.ORDERS_BY_DATE_IN_ASC, Comparator.comparing(Order::getDateIn));
        comparators.put(Sorter.ORDERS_BY_DATE_IN_DESC, (o1, o2) -> o2.getDateIn().compareTo(o1.getDateIn()));
        comparators.put(Sorter.ORDERS_BY_ORDER_DATE_ASC, Comparator.comparing(Order::getOrderDate));
        comparators.put(Sorter.ORDERS_BY_ORDER_DATE_DESC, (o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()));
    }

    @Override
    public void makeOrderByClient(Order order) throws ServiceException {
        DAOFactory daoFactory = DAOFactoryImpl.getInstance();
        OrderDAO orderDAO = daoFactory.getOrderDAO();

        try {
            orderDAO.create(order);
        } catch (EntityAlreadyExistsDAOException e) {
            throw new EntityAlreadyExistsServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Order> getUserOrders(int userId, int currentPage, int limit, Sorter sorter) throws ServiceException {
        List<Order> orders;
        try {
            orders = DAOFactoryImpl.getInstance().getOrderDAO().getOrdersByUserId(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        int offset = limit * currentPage - limit;
        orders = orders.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
        return sort(orders, sorter);
    }

    @Override
    public List<Order> getOrders(int limit, int currentPage, Sorter sorter) throws ServiceException {
        List<Order> orders;
        try {
            orders = DAOFactoryImpl.getInstance().getOrderDAO().getAll();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        int offset = limit * currentPage - limit;
        orders = orders.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
        return sort(orders, sorter);
    }

    @Override
    public void cancelOrder(int orderId) throws ServiceException {
        try {
            DAOFactoryImpl.getInstance().getOrderDAO().cancelOrder(orderId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int countRowsNumber() throws ServiceException {
        try {
            return DAOFactoryImpl.getInstance().getOrderDAO().countRows();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void connectApartmentAndOrder(int orderId, int apartmentId) throws ServiceException {
        try {
            DAOFactoryImpl.getInstance().getOrderDAO().processOrder(orderId, apartmentId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int countRowsNumberByUserId(int userId) throws ServiceException {
        try {
            return DAOFactoryImpl.getInstance().getOrderDAO().getOrdersByUserId(userId).size();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    private List<Order> sort(List<Order> orders, Sorter sorter) {
        if (orders == null) return new ArrayList<>();
        orders.sort(comparators.get(Objects.requireNonNullElse(sorter, Sorter.ORDERS_BY_ORDER_DATE_DESC)));
        return orders;
    }
}
