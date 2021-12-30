package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.PaymentDAO;
import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.entity.Payment;
import com.example.hotel.model.entity.enums.PaymentStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PaymentDAOImpl extends AbstractDAOImpl<Payment> implements PaymentDAO {
    private static final Logger logger = LogManager.getLogger(PaymentDAOImpl.class);

    private static final String SELECT_ALL_PAYMENT = "SELECT * FROM payments ";
    private static final String SELECT_PAYMENT_BY_ID = "SELECT * FROM payments WHERE id=?;";
    private static final String UPDATE_PAYMENT_BY_ID = "UPDATE payments SET reservation_id=?," +
            "pay_date=?, expire_date=?, payment_status=?, amount=? WHERE id=?;";
    private static final String INSERT_PAYMENT = "INSERT INTO payments(reservation_id, pay_date, " +
            "expire_date, payment_status, amount) VALUES " +
            "(?, ?, ?, ?, ?);";
    private static final String DELETE_PAYMENT_BY_ID = "DELETE FROM payments WHERE id=?;";
    private static final String SELECT_ALL_PAYMENTS_BY_USER_ID = "SELECT p.id, p.reservation_id, p.pay_date, " +
            "p.expire_date, p.payment_status, p.amount FROM payments p " +
            "JOIN reservations r " +
            "ON p.reservation_id = r.id " +
            "WHERE user_id = ? ORDER BY expire_date DESC ";
    private static final String CALL_MAKE_PAYMENT = "CALL makePayment(?)";
    private static final String CANCEL_PAYMENT_BY_ID = "CALL cancel_payment(?)";
    private static final String UPDATE_FAILED_PAYMENTS = "CALL update_failed_payments()";
    private static final String COUNT_ROWS = "SELECT COUNT(ID) FROM payments";

    @Override
    protected String getSelectByIdQuery() {
        return SELECT_PAYMENT_BY_ID;
    }

    @Override
    protected String getDeleteByIdQuery() {
        return DELETE_PAYMENT_BY_ID;
    }

    @Override
    protected String getCreateQuery() {
        return INSERT_PAYMENT;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_PAYMENT_BY_ID;
    }

    @Override
    protected String getSelectAllQuery() {
        return SELECT_ALL_PAYMENT;
    }

    @Override
    protected String getNumberOfRowsQuery() {
        return COUNT_ROWS;
    }

    protected String getSelectAllPaymentsByUserId() {
        return SELECT_ALL_PAYMENTS_BY_USER_ID;
    }

    @Override
    public Payment parseEntity(ResultSet resultSet) throws SQLException {
        Payment payment = new Payment();
        payment.setId(resultSet.getInt("id"));
        payment.setReservationId(resultSet.getInt("reservation_id"));
        java.sql.Timestamp timestamp = resultSet.getTimestamp("pay_date");
        payment.setPayDate(timestamp != null ? timestamp.toInstant() : null);
        payment.setExpireDate(resultSet.getTimestamp("expire_date").toInstant());
        payment.setStatus(PaymentStatus.valueOf(resultSet.getString("payment_status")));
        payment.setAmount(resultSet.getBigDecimal("amount"));
        return payment;
    }

    @Override
    public void fillStatement(Payment payment, PreparedStatement preparedStatement) throws SQLException {
        int k = 1;
        preparedStatement.setObject(k++, payment.getReservationId(), Types.INTEGER);
        preparedStatement.setTimestamp(k++, java.sql.Timestamp.from(payment.getPayDate()));
        preparedStatement.setTimestamp(k++, java.sql.Timestamp.from(payment.getExpireDate()));
        preparedStatement.setObject(k++, payment.getStatus(), Types.OTHER);
        preparedStatement.setBigDecimal(k, payment.getAmount());
    }

    @Override
    protected void fillStatementForUpdate(Payment entity, PreparedStatement preparedStatement) throws SQLException {
        fillStatement(entity, preparedStatement);
        preparedStatement.setObject(6, entity.getId(), Types.INTEGER);
    }

    @Override
    public List<Payment> getPaymentsByUserId(int userId) throws DAOException {
        List<Payment> payments = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(getSelectAllPaymentsByUserId());

            preparedStatement.setInt(1, userId);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                payments.add(parseEntity(resultSet));
            }
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not get all apartments for reservation.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, preparedStatement, resultSet);
        }
        return payments;
    }

    @Override
    public void pay(int paymentId) throws DAOException {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = connectionPool.getConnection();
            callableStatement = connection.prepareCall(CALL_MAKE_PAYMENT);
            callableStatement.setInt(1, paymentId);
            callableStatement.execute();
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not update entity.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, callableStatement);
        }
    }

    @Override
    public boolean cancelPayment(int paymentId) throws DAOException {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = connectionPool.getConnection();
            callableStatement = connection.prepareCall(CANCEL_PAYMENT_BY_ID);
            callableStatement.setInt(1, paymentId);
            callableStatement.execute();
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not cancel payment.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, callableStatement);
        }
        return true;
    }

    @Override
    public boolean updateFailedPayments() throws DAOException {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = connectionPool.getConnection();
            callableStatement = connection.prepareCall(UPDATE_FAILED_PAYMENTS);
            callableStatement.execute();
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not update payment.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, callableStatement);
        }
        return true;
    }
}
