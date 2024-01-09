package model;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Merry Ann
 */
public class DbConfig {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/water_billing_system_1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = (Connection) DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(DbConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }
    public void executeQuery(String query) {
        try (Connection con = getConnection();
            Statement st = con.createStatement()) {
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
