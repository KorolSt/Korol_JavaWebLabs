package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.ApartmentDAO;
import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.dto.ApartmentDTO;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.enums.ApartmentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApartmentDAOImpl extends AbstractDAOImpl<Apartment> implements ApartmentDAO {
    private static final Logger logger = LogManager.getLogger(ApartmentDAOImpl.class);

    private static final String SELECT_APARTMENTS = "SELECT * FROM apartments ";
    private static final String SELECT_APARTMENT_BY_ID = "SELECT * FROM apartments WHERE id=?";

    private static final String UPDATE_APARTMENT_BY_ID = "UPDATE apartments SET name=?," +
            "place_count=?, available=?, apartment_type=? WHERE id=?";

    private static final String INSERT_APARTMENT = "INSERT INTO apartments(name, place_count, " +
            "available, apartment_type) VALUES (?, ?, ?, ?);";

    private static final String DELETE_APARTMENT_BY_ID = "DELETE FROM apartments WHERE id=?";

    private static final String SELECT_ALL_AVAILABLE_BY_TYPE = "SELECT * FROM apartments" +
            " WHERE apartment_type=? AND available=true";

    private static final String SELECT_EXISTS_FREE_APARTMENT_BY_ID = "select exists (" +
            "select 1 from apartments a " +
            "WHERE a.id NOT IN (" +
            "    SELECT DISTINCT apartment_id" +
            "        FROM reservations r" +
            "        WHERE (?, ?) OVERLAPS (r.date_in, r.date_out)" +
            "        AND (r.reservation_status = 'CONFIRMED' OR r.reservation_status='PROCESSING')" +
            ") AND a.id = ?);";
    private static final String ERROR_MESSAGE = "Could not get all apartments for reservation.";
    private static final String COUNT_ROWS = "SELECT COUNT(ID) FROM apartments";
    private static final String SELECT_ALL_APARTMENTS_BY_DATES = "SELECT * FROM get_all_apartments(?, ?, ?)";
    private static final String SELECT_NOT_RESERVED = "SELECT * FROM get_not_reserved_with_type(?)";
    private static final String GET_APARTMENT_PRICE = "SELECT sum(dp.price) " +
            "FROM day_prices dp " +
            "         JOIN apartments a on a.id = dp.apartment_id " +
            "WHERE a.id = ? " +
            "  AND dp.date >= ? " +
            "  AND dp.date < ? ";

    @Override
    protected String getSelectByIdQuery() {
        return SELECT_APARTMENT_BY_ID;
    }

    @Override
    protected String getDeleteByIdQuery() {
        return DELETE_APARTMENT_BY_ID;
    }

    @Override
    protected String getCreateQuery() {
        return INSERT_APARTMENT;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_APARTMENT_BY_ID;
    }

    @Override
    protected String getSelectAllQuery() {
        return SELECT_APARTMENTS;
    }

    @Override
    protected String getNumberOfRowsQuery() {
        return COUNT_ROWS;
    }


    protected String getSelectAllByType() {
        return SELECT_ALL_AVAILABLE_BY_TYPE;
    }

    protected String getSelectExistsFreeApartmentById() {
        return SELECT_EXISTS_FREE_APARTMENT_BY_ID;
    }

    @Override
    protected Apartment parseEntity(ResultSet resultSet) throws SQLException {
        Apartment apartment = new Apartment();
        apartment.setId(resultSet.getInt("id"));
        apartment.setName(resultSet.getString("name"));
        apartment.setPlaceCount(resultSet.getInt("place_count"));
        apartment.setAvailable(resultSet.getBoolean("available"));
        apartment.setType(ApartmentType.valueOf(resultSet.getString("apartment_type")));
        return apartment;
    }

    @Override
    protected void fillStatement(Apartment apartment, PreparedStatement preparedStatement) throws SQLException {
        int k = 1;
        preparedStatement.setString(k++, apartment.getName());
        preparedStatement.setObject(k++, apartment.getPlaceCount(), Types.INTEGER);
        preparedStatement.setBoolean(k++, apartment.isAvailable());
        preparedStatement.setObject(k, apartment.getType(), Types.OTHER);
    }

    protected ApartmentDTO parseFreeApartmentDTO(ResultSet resultSet) throws SQLException {
        ApartmentDTO apartmentDTO = new ApartmentDTO();
        apartmentDTO.setApartmentId(resultSet.getInt("apartment_id"));
        apartmentDTO.setName(resultSet.getString("name"));
        apartmentDTO.setStatus(ApartmentDTO.Status.valueOf(resultSet.getString("status")));

        apartmentDTO.setDateIn(resultSet.getDate("date_in").toLocalDate());
        apartmentDTO.setDateOut(resultSet.getDate("date_out").toLocalDate());
        apartmentDTO.setPlaceCount(resultSet.getInt("place_count"));

        apartmentDTO.setAmount(resultSet.getBigDecimal("amount"));
        apartmentDTO.setAvailable(resultSet.getBoolean("available"));
        apartmentDTO.setType(ApartmentType.valueOf(resultSet.getString("apartment_type")));
        return apartmentDTO;
    }

    @Override
    protected void fillStatementForUpdate(Apartment entity, PreparedStatement preparedStatement) throws SQLException {
        fillStatement(entity, preparedStatement);
        preparedStatement.setObject(5, entity.getId(), Types.INTEGER);
    }

    @Override
    public BigDecimal getApartmentPrice(int apartmentId, LocalDate dateIn, LocalDate dateOut) throws DAOException {
        BigDecimal res = new BigDecimal("0");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(GET_APARTMENT_PRICE);

            preparedStatement.setInt(1, apartmentId);
            preparedStatement.setDate(2, java.sql.Date.valueOf(dateIn));
            preparedStatement.setDate(3, java.sql.Date.valueOf(dateOut));

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                res = resultSet.getBigDecimal("sum");
            }
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not get all apartments by date.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, preparedStatement, resultSet);
        }
        return res;
    }

    @Override
    public List<ApartmentDTO> getAllForRequest(LocalDate dateIn, LocalDate dateOut, int placeCount) throws DAOException {
        List<ApartmentDTO> apartments = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SELECT_ALL_APARTMENTS_BY_DATES);

            preparedStatement.setDate(1, java.sql.Date.valueOf(dateIn));
            preparedStatement.setDate(2, java.sql.Date.valueOf(dateOut));
            preparedStatement.setInt(3, placeCount);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                apartments.add(parseFreeApartmentDTO(resultSet));
            }
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not get all apartments by date.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, preparedStatement, resultSet);
        }
        return apartments;
    }

    @Override
    public List<Apartment> getFreeApartmentsForOrder(int orderId) throws DAOException {
        List<Apartment> apartments = new ArrayList<>();

        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.getConnection();
            callableStatement = connection.prepareCall(SELECT_NOT_RESERVED);
            callableStatement.setInt(1, orderId);

            resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                apartments.add(parseEntity(resultSet));
            }
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException(ERROR_MESSAGE, e);
        } finally {
            connectionPool.safeCommitAndClose(connection, callableStatement, resultSet);
        }
        return apartments;
    }

    @Override
    public List<Apartment> getByType(ApartmentType apartmentType) throws DAOException {
        List<Apartment> apartments = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = getSelectAllByType();
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setObject(1, apartmentType, Types.OTHER);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                apartments.add(parseEntity(resultSet));
            }
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not get all apartments by date.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, preparedStatement, resultSet);
        }
        return apartments;
    }

    @Override
    public boolean checkIsApartmentFree(int apartmentId, LocalDate dateIn, LocalDate dateOut)
            throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(getSelectExistsFreeApartmentById());

            preparedStatement.setDate(1, java.sql.Date.valueOf(dateIn));
            preparedStatement.setDate(2, java.sql.Date.valueOf(dateOut));
            preparedStatement.setInt(3, apartmentId);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean("exists");
            }
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException(ERROR_MESSAGE, e);
        } finally {
            connectionPool.safeCommitAndClose(connection, preparedStatement, resultSet);
        }
        return false;
    }
}
