package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.DayPriceDAO;
import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.entity.DayPrice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public class DayPriceDAOImpl extends AbstractDAOImpl<DayPrice> implements DayPriceDAO {
    private static final Logger logger = LogManager.getLogger(DayPriceDAOImpl.class);

    private static final String SELECT_ALL_DAY_PRICE = "SELECT * FROM day_prices ";
    private static final String SELECT_DAY_PRICE_BY_ID = "SELECT * FROM day_prices WHERE id=?";

    private static final String UPDATE_DAY_PRICE_BY_ID = "UPDATE day_prices SET apartment_id=?," +
            "price=?, date=? WHERE id=?";

    private static final String INSERT_DAY_PRICE = "INSERT INTO day_prices(apartment_id, price, " +
            "date) VALUES " + "(?, ?, ?);";

    private static final String DELETE_DAY_PRICE_BY_ID = "DELETE FROM day_prices WHERE id=?";
    private static final String SET_DAY_PRICE_BY_FOR_APARTMENT = "SELECT setDayPrice(?, ?, ?, ?);";

    private static final String COUNT_ROWS = "SELECT COUNT(ID) FROM day_prices";


    @Override
    protected String getSelectByIdQuery() {
        return SELECT_DAY_PRICE_BY_ID;
    }

    @Override
    protected String getDeleteByIdQuery() {
        return DELETE_DAY_PRICE_BY_ID;
    }

    @Override
    protected String getCreateQuery() {
        return INSERT_DAY_PRICE;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_DAY_PRICE_BY_ID;
    }

    @Override
    protected String getSelectAllQuery() {
        return SELECT_ALL_DAY_PRICE;
    }

    @Override
    protected String getNumberOfRowsQuery() {
        return COUNT_ROWS;
    }

    protected String getSetDatePriceForApartment() {
        return SET_DAY_PRICE_BY_FOR_APARTMENT;
    }

    @Override
    protected DayPrice parseEntity(ResultSet resultSet) throws SQLException {
        DayPrice dayPrice = new DayPrice();
        dayPrice.setId(resultSet.getInt("id"));
        dayPrice.setPrice(resultSet.getBigDecimal("price"));
        dayPrice.setApartmentId(resultSet.getInt("apartment_id"));
        dayPrice.setDate(resultSet.getDate("date").toLocalDate());
        return dayPrice;
    }

    @Override
    protected void fillStatement(DayPrice dayPrice, PreparedStatement preparedStatement) throws SQLException {
        int k = 1;
        preparedStatement.setObject(k++, dayPrice.getApartmentId(), Types.INTEGER);
        preparedStatement.setBigDecimal(k++, dayPrice.getPrice());
        preparedStatement.setDate(k, java.sql.Date.valueOf(dayPrice.getDate()));
    }

    @Override
    protected void fillStatementForUpdate(DayPrice entity, PreparedStatement preparedStatement) throws SQLException {
        fillStatement(entity, preparedStatement);
        preparedStatement.setObject(4, entity.getId(), Types.INTEGER);
    }

    @Override
    public void setDayPricesForApartment(int apartmentId, LocalDate dateIn, LocalDate dateOut, BigDecimal price) throws DAOException {
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            connection = connectionPool.getConnection();
            callableStatement = connection.prepareCall(getSetDatePriceForApartment());
            callableStatement.setInt(1, apartmentId);
            callableStatement.setDate(2, java.sql.Date.valueOf(dateIn));
            callableStatement.setDate(3, java.sql.Date.valueOf(dateOut));
            callableStatement.setBigDecimal(4, price);
            callableStatement.execute();
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not set DayPrices for apartment.", e);
        }
        finally {
            connectionPool.safeCommitAndClose(connection, callableStatement);
        }
    }
}
