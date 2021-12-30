package com.example.hotel.model.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionPool {
    private static final Logger log = LogManager.getLogger(ConnectionPool.class);

    private static ConnectionPool instance;

    private ConnectionPool() {
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        Connection con = null;
        try {
            Context envContext = (Context) new InitialContext().lookup("java:/comp/env");

            DataSource ds = (DataSource) envContext.lookup("jdbc/hoteldb");
            con = ds.getConnection();
        } catch (NamingException e) {
            log.error("Error with context.", e);
        }
        return con;
    }

    public void safeCommitAndClose(Connection con, PreparedStatement pst, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                log.error("ResultSet error.", ex);
            }
        }
        safeCommitAndClose(con, pst);
    }

    public void safeCommitAndClose(Connection con, PreparedStatement pst) {
        if (con != null) {
            try {
                con.commit();
            } catch (SQLException ex) {
                log.error("Commit error.", ex);
            }
            try {
                con.close();
            } catch (SQLException ex) {
                log.error("Closing connection error.", ex);
            }
        }
        if (pst != null) {
            try {
                pst.close();
            } catch (SQLException ex) {
                log.error("PreparedStatement error.", ex);
            }
        }
    }

    public void safeRollback(Connection con) {
        if (con == null) return;
        try {
            con.rollback();
        } catch (SQLException ex) {
            log.error("Rollback error.", ex);
        }
    }
}
