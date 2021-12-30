package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.ReservationDAO;
import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.entity.Reservation;
import com.example.hotel.model.entity.enums.ReservationStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl extends AbstractDAOImpl<Reservation> implements ReservationDAO {
    private static final Logger logger = LogManager.getLogger(ReservationDAOImpl.class);

    private static final String SELECT_ALL_RESERVATIONS = "SELECT * FROM reservations ";
    private static final String SELECT_RESERVATION_BY_ID = "SELECT * FROM reservations WHERE id=?;";
    private static final String UPDATE_RESERVATION_BY_ID = "UPDATE reservations SET user_id=?," +
            "reservation_status=?, apartment_id=?, reservation_date=?, date_in=?, date_out=?, " +
            "person_count=? WHERE id=?;";
    private static final String INSERT_RESERVATION = "INSERT INTO reservations(user_id, " +
            "reservation_status, apartment_id, reservation_date, date_in, date_out, person_count) VALUES " +
            "(?, ?, ?, ?, ?, ?, ?);";
    private static final String DELETE_RESERVATION_BY_ID = "DELETE FROM reservations WHERE id=?;";
    private static final String SELECT_ALL_RESERVATIONS_BY_USER_ID = "SELECT * FROM reservations" +
            " WHERE user_id=? ORDER BY reservation_date DESC";
    private static final String CALL_RESERVE_BY_ORDER = "CALL reserve_by_order(?)";
    private static final String CALL_RESERVE = "CALL reserve(?, ?, ?, ?, ?)";

    private static final String CANCEL_RESERVATION_BY_ID = "CALL cancel_reservation(?)";

    private static final String COUNT_ROWS = "SELECT COUNT(ID) FROM reservations";

    @Override
    protected String getSelectByIdQuery() {
        return SELECT_RESERVATION_BY_ID;
    }

    @Override
    protected String getDeleteByIdQuery() {
        return DELETE_RESERVATION_BY_ID;
    }

    @Override
    protected String getCreateQuery() {
        return INSERT_RESERVATION;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_RESERVATION_BY_ID;
    }

    @Override
    protected String getSelectAllQuery() {
        return SELECT_ALL_RESERVATIONS;
    }

    @Override
    protected String getNumberOfRowsQuery() {
        return COUNT_ROWS;
    }

    protected String getSelectAllReservationsByUserId() {
        return SELECT_ALL_RESERVATIONS_BY_USER_ID;
    }

    @Override
    protected Reservation parseEntity(ResultSet resultSet) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(resultSet.getInt("id"));
        reservation.setUserId(resultSet.getInt("user_id"));
        reservation.setStatus(ReservationStatus.valueOf(resultSet.getString("reservation_status")));
        reservation.setApartmentId(resultSet.getInt("apartment_id"));
        reservation.setReservationDate(resultSet.getTimestamp("reservation_date").toInstant());
        reservation.setDateIn(resultSet.getDate("date_in").toLocalDate());
        reservation.setDateOut(resultSet.getDate("date_out").toLocalDate());
        reservation.setPersonCount(resultSet.getInt("person_count"));
        return reservation;
    }

    @Override
    protected void fillStatement(Reservation reservation, PreparedStatement preparedStatement) throws SQLException {
        int k = 1;
        preparedStatement.setObject(k++, reservation.getUserId(), Types.INTEGER);
        preparedStatement.setObject(k++, reservation.getStatus(), Types.OTHER);
        preparedStatement.setObject(k++, reservation.getApartmentId(), Types.INTEGER);
        preparedStatement.setTimestamp(k++, java.sql.Timestamp.from(reservation.getReservationDate()));
        preparedStatement.setDate(k++, java.sql.Date.valueOf(reservation.getDateIn()));
        preparedStatement.setDate(k++, java.sql.Date.valueOf(reservation.getDateOut()));
        preparedStatement.setObject(k, reservation.getPersonCount(), Types.INTEGER);
    }

    @Override
    protected void fillStatementForUpdate(Reservation entity, PreparedStatement preparedStatement) throws SQLException {
        fillStatement(entity, preparedStatement);
        preparedStatement.setObject(8, entity.getId(), Types.INTEGER);
    }

    @Override
    public List<Reservation> getReservationsByUserId(int userId) throws DAOException {
        List<Reservation> reservations = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(getSelectAllReservationsByUserId());

            preparedStatement.setInt(1, userId);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                reservations.add(parseEntity(resultSet));
            }
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not get all apartments for reservation.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, preparedStatement, resultSet);
        }
        return reservations;
    }

    @Override
    public boolean reserve(Reservation reservation) throws DAOException {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = connectionPool.getConnection();
            callableStatement = connection.prepareCall(CALL_RESERVE);
            callableStatement.setInt(1, reservation.getUserId());
            callableStatement.setInt(2, reservation.getApartmentId());
            callableStatement.setDate(3, java.sql.Date.valueOf(reservation.getDateIn()));
            callableStatement.setDate(4, java.sql.Date.valueOf(reservation.getDateOut()));
            callableStatement.setInt(5, reservation.getPersonCount());
            callableStatement.execute();
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not make reservation.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, callableStatement);
        }
        return true;
    }

    @Override
    public void reserveByOrderId(int orderId) throws DAOException {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = connectionPool.getConnection();
            callableStatement = connection.prepareCall(CALL_RESERVE_BY_ORDER);
            callableStatement.setInt(1, orderId);
            callableStatement.execute();
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not make reservation by order id.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, callableStatement);
        }
    }

    @Override
    public void cancelReservation(int reservationId) throws DAOException {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = connectionPool.getConnection();
            callableStatement = connection.prepareCall(CANCEL_RESERVATION_BY_ID);
            callableStatement.setInt(1, reservationId);
            callableStatement.execute();
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not make reservation.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, callableStatement);
        }
    }
}


