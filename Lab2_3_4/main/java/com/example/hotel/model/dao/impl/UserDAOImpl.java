package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.UserDAO;
import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.dao.exception.EntityNotFoundDAOException;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.entity.enums.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class UserDAOImpl extends AbstractDAOImpl<User> implements UserDAO {
    private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);

    private static final String SELECT_ALL_USERS = "SELECT * FROM users ";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id=?";
    private static final String SELECT_USER_BY_LOGIN = "SELECT * FROM users WHERE login=?";
    private static final String UPDATE_USER_BY_ID = "UPDATE users SET login=?," +
            "password=?, user_role=?, name=?, surname=?, phone=?, email=? WHERE id=?";
    private static final String INSERT_USER = "INSERT INTO users(login, password, " +
            "user_role, name, surname, phone, email) VALUES " +
            "(?, ?, ?, ?, ?, ?, ?);";
    private static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE id=?";
    private static final String SELECT_USER_BY_PAYMENT = "SELECT u.* " +
            "FROM payments p " +
            "JOIN reservations r " +
            "ON p.reservation_id = r.id " +
            "JOIN users u " +
            "ON u.id = r.user_id " +
            "WHERE p.id=?";


    private static final String COUNT_ROWS = "SELECT COUNT(ID) FROM users";

    @Override
    protected String getSelectByIdQuery() {
        return SELECT_USER_BY_ID;
    }

    @Override
    protected String getDeleteByIdQuery() {
        return DELETE_USER_BY_ID;
    }

    @Override
    protected String getCreateQuery() {
        return INSERT_USER;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_USER_BY_ID;
    }

    @Override
    protected String getSelectAllQuery() {
        return SELECT_ALL_USERS;
    }

    @Override
    protected String getNumberOfRowsQuery() {
        return COUNT_ROWS;
    }

    protected String getSelectByLoginQuery() {
        return SELECT_USER_BY_LOGIN;
    }

    @Override
    public User parseEntity(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(resultSet.getString("user_role") == null ? null : UserRole.valueOf(resultSet.getString("user_role")));
        user.setName(resultSet.getString("name"));
        user.setSurname(resultSet.getString("surname"));
        user.setPhone(resultSet.getString("phone"));
        user.setEmail(resultSet.getString("email"));
        return user;
    }

    @Override
    public void fillStatement(User user, PreparedStatement preparedStatement) throws SQLException {
        int k = 1;
        preparedStatement.setString(k++, user.getLogin());
        preparedStatement.setString(k++, user.getPassword());
        preparedStatement.setObject(k++, user.getRole(), Types.OTHER);
        preparedStatement.setString(k++, user.getName());
        preparedStatement.setString(k++, user.getSurname());
        preparedStatement.setString(k++, user.getPhone());
        preparedStatement.setString(k, user.getEmail());
    }

    @Override
    protected void fillStatementForUpdate(User entity, PreparedStatement preparedStatement) throws SQLException {
        fillStatement(entity, preparedStatement);
        preparedStatement.setObject(8, entity.getId(), Types.INTEGER);
    }

    @Override
    public User getByLogin(String login) throws DAOException {
        User user;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(getSelectByLoginQuery());
            preparedStatement.setString(1, login);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new EntityNotFoundDAOException("User not found.");
            }

            user = parseEntity(resultSet);
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not get user by login.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, preparedStatement, resultSet);
        }
        return user;
    }

    @Override
    public User getByPaymentId(int paymentId) throws DAOException {
        User user;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_PAYMENT);
            preparedStatement.setInt(1, paymentId);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new EntityNotFoundDAOException("User not found.");
            }

            user = parseEntity(resultSet);
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not get user by payment.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, preparedStatement, resultSet);
        }
        return user;
    }
}
