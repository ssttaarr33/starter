package com.mycompany.starterproject;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DbUtils {

    private static final DbUtils INSTANCE = new DbUtils();

    private DataSource dm = null;

    private DbUtils() {
        init();
    }

    public static DbUtils getInstance() {
        return INSTANCE;
    }

    public Connection getConnection(DB db) throws SQLException {

        return getDsByDb(db).getConnection();

    }

    public void releaseConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                Logger.getLogger(DbUtils.class.getName()).log(Level.WARNING, e.getMessage(), e);
            }
            rs = null;
        }
    }

    public void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                Logger.getLogger(DbUtils.class.getName()).log(Level.WARNING, e.getMessage(), e);
            }
            stmt = null;
        }
    }

    private void init() {
        dm = createDs(DB.DEVICEMANAGEMENT);
    }

    private DataSource createDs(DB db) throws RuntimeException {
        try {
            Context envCtx = (Context) new InitialContext().lookup("java:comp/env");
            return (DataSource) envCtx.lookup(db.jndiName);
        } catch (NamingException ex) {
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, null, ex);

            throw new RuntimeException(ex);
        }
    }

    private DataSource getDsByDb(DB db) {
        switch (db) {
            case DEVICEMANAGEMENT:
                return dm;
            default:
                return null;
        }
    }

    public static interface ExecCallback<T> {

        T execute(Connection conn);
    }

    public static enum DB {
        DEVICEMANAGEMENT("jdbc/dmDB");

        private String jndiName;

        DB(String jndiName) {
            this.jndiName = jndiName;
        }

    }

}
