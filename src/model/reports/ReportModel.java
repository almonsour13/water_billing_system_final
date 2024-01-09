/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DbConfig;

/**
 *
 * @author Merry Ann
 */
public class ReportModel {
    private final DbConfig dbConfig = new DbConfig();
    public Reports getBillsAnalytic(int month, int year) throws SQLException {
        Reports bill = null;
        String query = "SELECT\n" +
                        "	b.billingDate AS date,\n" +
                "    COUNT(CASE WHEN b.billingStatus IN (2, 4) THEN 1 ELSE NULL END) AS collected,\n" +
                "    COUNT(CASE WHEN b.billingStatus IN (1, 3) THEN 1 ELSE NULL END) AS not_collected\n" +
                "FROM\n" +
                "    bills b\n" +
                (month == 0 && year == 0 ? "" : "WHERE ") +
                (month == 0 ? "" : "MONTH(b.billingDate) = ? ") +
                (month == 0 || year == 0 ? "" : "AND ") +
                (year == 0 ? "" : "YEAR(b.billingDate) = ?");


        try (Connection connection = dbConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            int parameterIndex = 1;
            if (month != 0) statement.setInt(parameterIndex++, month);
            if (year != 0) statement.setInt(parameterIndex++, year);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                bill = new Reports(
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("collected"),
                        rs.getInt("not_collected")
                );
            }
        }
        return bill;  
    }

    public ObservableList<Reports> getAll() throws SQLException{
        ObservableList bill = FXCollections.observableArrayList();
        String query =  "SELECT\n" +
                        "	billingDate AS date\n" +
                        "FROM\n" +
                        "	bills b \n" +
                        "ORDER BY \n" +
                        "	date;";

        try (Connection connection = dbConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                
                 bill.add(new Reports(
                        rs.getDate("date").toLocalDate()
                 )
                );
            }
        }
        return bill;
    }
}
