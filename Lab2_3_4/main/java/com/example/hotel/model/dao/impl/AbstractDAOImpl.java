package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.AbstractDAO;
import com.example.hotel.model.dao.exception.DAOException;
import com.example.hotel.model.dao.exception.EntityAlreadyExistsDAOException;
import com.example.hotel.model.dao.exception.EntityNotFoundDAOException;
import com.example.hotel.model.dao.function.ThrowableBiConsumer;
import com.example.hotel.model.dao.function.ThrowableFunction;
import com.example.hotel.model.db.ConnectionPool;
import com.example.hotel.model.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractDAOImpl<T extends Entity> implements AbstractDAO<T> {
    private static final Logger logger = LogManager.getLogger(AbstractDAOImpl.class);
    static final ConnectionPool connectionPool = ConnectionPool.getInstance();


    abstract String getSelectByIdQuery();

    abstract String getDeleteByIdQuery();

    abstract String getCreateQuery();

    abstract String getUpdateQuery();

    abstract String getSelectAllQuery();

    abstract String getNumberOfRowsQuery();


    abstract T parseEntity(ResultSet resultSet) throws SQLException;

    abstract void fillStatement(T entity, PreparedStatement preparedStatement) throws SQLException;

    abstract void fillStatementForUpdate(T entity, PreparedStatement preparedStatement) throws SQLException;

    @Override
    public T getById(int id) throws DAOException {
        return getEntityById(id, getSelectByIdQuery(), this::parseEntity);
    }

    @Override
    public boolean deleteById(int id) throws DAOException {
        return deleteEntityById(id, getDeleteByIdQuery());
    }

    @Override
    public boolean create(T t) throws DAOException {
        return createEntity(t, getCreateQuery(), this::fillStatement);
    }

    @Override
    public boolean update(T t) throws DAOException {
        return updateEntity(t, getUpdateQuery(), this::fillStatementForUpdate);
    }

    @Override
    public List<T> getAll() throws DAOException {
        return getAllEntities(getSelectAllQuery(), this::parseEntity);
    }

    @Override
    public int countRows() throws DAOException {
        return getNumberOfRows(getNumberOfRowsQuery());
    }

    private T getEntityById(int id, String sql, ThrowableFunction<ResultSet, T> createEntity) throws DAOException {
        T entity;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new EntityNotFoundDAOException();
            }

            entity = createEntity.apply(resultSet);
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not get entity.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, preparedStatement, resultSet);
        }
        return entity;
    }

    private boolean deleteEntityById(int id, String sql) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            if (preparedStatement.executeUpdate() != 1) throw new EntityNotFoundDAOException();
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not delete entity.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, preparedStatement);
        }
        return true;
    }

    private boolean createEntity(T entity, String sql, ThrowableBiConsumer<T, PreparedStatement> fillStatement) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            fillStatement.accept(entity, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new EntityAlreadyExistsDAOException(e);
            }
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not add entity.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, preparedStatement);
        }
        return true;
    }

    private boolean updateEntity(T entity, String sql, ThrowableBiConsumer<T, PreparedStatement> fillStatement) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            fillStatement.accept(entity, preparedStatement);
            if (preparedStatement.executeUpdate() != 1) throw new EntityNotFoundDAOException();
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not update entity.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, preparedStatement);
        }
        return true;
    }

    private List<T> getAllEntities(String sql, ThrowableFunction<ResultSet, T> createEntity) throws DAOException {
        List<T> entities = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                entities.add(createEntity.apply(resultSet));
            }
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not get all entities.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, preparedStatement, resultSet);
        }
        return entities;
    }

    private int getNumberOfRows(String sql) throws DAOException {
        int numberOfRows = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                numberOfRows = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            logger.error(e);
            connectionPool.safeRollback(connection);
            throw new DAOException("Could not count all entities.", e);
        } finally {
            connectionPool.safeCommitAndClose(connection, preparedStatement, resultSet);
        }
        return numberOfRows;
    }
}
