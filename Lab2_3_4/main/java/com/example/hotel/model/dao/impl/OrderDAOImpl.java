package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.OrderDAO;
import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.dao.exception.EntityNotFoundDAOException;
import com.example.hotel.model.entity.Order;
import com.example.hotel.model.entity.enums.ApartmentType;
import com.example.hotel.model.entity.enums.OrderStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl extends AbstractDAOImpl<Order> implements OrderDAO {
    private static final Logger logger = LogManager.getLogger(OrderDAOImpl.class);

    private static final String SELECT_ALL_ORDERS = "SELECT * FROM orders ";
    private static final String SELECT_ORDERS_BY_ID = "SELECT * FROM orders WHERE id=?;";

    private static final String UPDATE_ORDER_BY_ID = "UPDATE orders SET user_id=?, " +
            "order_status=?, apartment_type=?, apartment_id=?, order_date=?, date_in=?, date_out=?, " +
            "person_count=? WHERE id=?;";

    private static final String INSERT_ORDER = "INSERT INTO orders(user_id, order_status, " +
            "apartment_type, apartment_id, order_date, date_in, date_out, person_count) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String DELETE_ORDER_BY_ID = "DELETE FROM orders WHERE id=?;";
    private static final String SELECT_ALL_ORDERS_BY_USER_ID = "SELECT * FROM orders" +
            " WHERE user_id= ? ORDER BY order_date DESC";
    private static final String COUNT_ROWS = "SELECT COUNT(ID) FROM orders";
    private static final String CANCEL_ORDER_BY_ID = "UPDATE orders " +
            "SET order_status='CANCELED' WHERE id = ?";
    private static final String PROCESS_ORDER = "UPDATE orders SET " +
            "order_status='CONFIRMED_BY_MANAGER', apartment_id=? " +
            "WHERE id=?";

    @Override
    protected String getSelectByIdQuery() {
        return SELECT_ORDERS_BY_ID;
    }

    @Override
    protected String getDeleteByIdQuery() {
        return DELETE_ORDER_BY_ID;
    }

    @Override
    protected String getCreateQuery() {
        return INSERT_ORDER;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_ORDER_BY_ID;
    }

    @Override
    protected String getSelectAllQuery() {
        return SELECT_ALL_ORDERS;
    }

    @Override
    protected String getNumberOfRowsQuery() {
        return COUNT_ROWS;
    }

    protected String getSelectAllOrdersByUserId() {
        return SELECT_ALL_ORDERS_BY_USER_ID;
    }

    @Override
    protected Order parseEntity(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getInt("id"));
        order.setUserId(resultSet.getInt("user_id"));
        order.setApartmentId((Integer) resultSet.getObject("apartment_id"));
        order.setStatus(OrderStatus.valueOf(resultSet.getString("order_status")));
        order.setApartmentType(ApartmentType.valueOf(resultSet.getString("apartment_type")));
        order.setOrderDate(resultSet.getTimestamp("order_date").toInstant());
        order.setDateIn(resultSet.getDate("date_in").toLocalDate());
        order.setDateOut(resultSet.getDate("date_out").toLocalDate());
        order.setPersonCount(resultSet.getInt("person_count"));
        return order;
    }

    @Override
    protected void fillStatement(Order entity, PreparedStatement preparedStatement) throws SQLException {
        int k = 1;
        preparedStatement.setObject(k++, entity.getUserId(), Types.INTEGER);
        preparedStatement.setObject(k++, entity.getStatus(), Types.OTHER);
        preparedStatement.setObject(k++, entity.getApartmentType(), Types.OTHER);
        preparedStatement.setObject(k++, entity.getApartmentId(), Types.INTEGER);
        preparedStatement.setTimestamp(k++, java.sql.Timestamp.valueOf(LocalDateTime.ofInstant(entity.getOrderDate(), ZoneId.systemDefault())));
        preparedStatement.setDate(k++, java.sql.Date.valueOf(entity.getDateIn()));
        preparedStatement.setDate(k++, java.sql.Date.valueOf(entity.getDateOut()));
        preparedStatement.setObject(k, entity.getPersonCount(), Types.INTEGER);
    }

    @Override
    protected void fillStatementForUpdate(Order entity, PreparedStatement preparedStatement) throws SQLException {
        fillStatement(entity, preparedStatement);
        preparedStatement.setObject(9, entity.getId(), Types.INTEGER);
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) throws DAOException {
        List<Order> orders = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(getSelectAllOrdersByUserId());

            preparedStatement.setInt(1, userId);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orders.add(parseEntity(resultSet));
            }
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not get all apartments for reservation.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, preparedStatement, resultSet);
        }
        return orders;
    }

    @Override
    public void cancelOrder(int orderId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(CANCEL_ORDER_BY_ID);
            preparedStatement.setInt(1, orderId);
            if (preparedStatement.executeUpdate() != 1) throw new EntityNotFoundDAOException("Order not found");
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not make reservation.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, preparedStatement);
        }
    }

    @Override
    public void processOrder(int orderId, int apartmentId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(PROCESS_ORDER);
            preparedStatement.setInt(1, apartmentId);
            preparedStatement.setInt(2, orderId);
            if (preparedStatement.executeUpdate() != 1) throw new EntityNotFoundDAOException();
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not make reservation.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, preparedStatement);
        }
    }
}
