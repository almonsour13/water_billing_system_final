/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.penalty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DbConfig;
import model.payment.Payment;

/**
 *
 * @author Merry Ann
 */
public class PenaltyModel {
     private final DbConfig dbConfig;   
     public PenaltyModel(){
     dbConfig = new DbConfig();
     }
     
   public ObservableList<Penalty> getPenalty() throws SQLException{
       ObservableList<Penalty> penalty = FXCollections.observableArrayList();
        String query =  "SELECT * FROM get_penalty;";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet rs = statement.executeQuery();
            int no = 1;
            while (rs.next()) {
                String status = "";

                if (rs.getInt("penaltyStatus") == 1) {
                    status = "Unpaid";
                } else {
                    status = "Paid";
                }
                String penaltyType = "";
                if(rs.getInt("penaltyType") == 1){
                    penaltyType = "Overdue Penalty";
                }

                penalty.add( new Penalty(
                        no,
                        rs.getInt("penaltyID"),
                        rs.getInt("billID"),
                        rs.getString("name"),
                        rs.getString("meterNumber"),
                        rs.getString("meterLocation"),
                        rs.getDate("billingDate").toLocalDate(),
                        rs.getInt("billAmount"),
                        rs.getDate("penaltyDate").toLocalDate(),
                        penaltyType,
                        rs.getInt("penaltyAmount"),
                        rs.getDouble("totalAmount"),
                        status
                        
                 
                        
                )
                 ); 
                no++;
            }
        }
        return penalty;
   }
   public ObservableList<Penalty> getPenaltyById(int id) throws SQLException{
       ObservableList<Penalty> penalty = FXCollections.observableArrayList();
        String query =  "SELECT * FROM get_penalty WHERE cID = '"+id+"';";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet rs = statement.executeQuery();
            int no = 1;
            while (rs.next()) {
                String status = "";

                if (rs.getInt("penaltyStatus") == 1) {
                    status = "Unpaid";
                } else {
                    status = "Paid";
                }
                String penaltyType = "";
                if(rs.getInt("penaltyType") == 1){
                    penaltyType = "Overdue Penalty";
                }

                penalty.add( new Penalty(
                        no,
                        rs.getInt("penaltyID"),
                        rs.getInt("billID"),
                        rs.getString("name"),
                        rs.getString("meterNumber"),
                        rs.getString("meterLocation"),
                        rs.getDate("billingDate").toLocalDate(),
                        rs.getInt("billAmount"),
                        rs.getDate("penaltyDate").toLocalDate(),
                        penaltyType,
                        rs.getInt("penaltyAmount"),
                        rs.getDouble("totalAmount"),
                        status
                )
                 ); 
                no++;
            }
        }
        return penalty;
   }
   public Penalty getPenaltyDetailsById(int id) throws SQLException{
       Penalty penalty = null;
        String query =  "SELECT * FROM get_penalty WHERE billID = '"+id+"';";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet rs = statement.executeQuery();
            int no = 1;
            while (rs.next()) {
                String status = "";

                if (rs.getInt("penaltyStatus") == 1) {
                    status = "Unpaid";
                } else {
                    status = "Paid";
                }
                String penaltyType = "";
                if(rs.getInt("penaltyType") == 1){
                    penaltyType = "Overdue Penalty";
                }

                penalty =  new Penalty(
                        no,
                        rs.getInt("penaltyID"),
                        rs.getInt("billID"),
                        rs.getString("name"),
                        rs.getString("meterNumber"),
                        rs.getString("meterLocation"),
                        rs.getDate("billingDate").toLocalDate(),
                        rs.getInt("billAmount"),
                        rs.getDate("penaltyDate").toLocalDate(),
                        penaltyType,
                        rs.getInt("penaltyAmount"),
                        rs.getDouble("totalAmount"),
                        status
                 ); 
                no++;
            }
        }
        return penalty;
   }
   public ObservableList<Penalty> filterPenalty(int month, int year, String meterLoc,int status, String searchVal) throws SQLException {
        ObservableList<Penalty> penalty = FXCollections.observableArrayList();

        // Convert searchVal to uppercase for case-insensitive search
        searchVal = (searchVal != null) ? searchVal.toUpperCase() : null;

        String query = "SELECT * FROM get_penalty " +
            (month != 0 || year != 0 || !meterLoc.equals("All") || status != 0 || (searchVal != null && !searchVal.isEmpty()) ? "WHERE " : "") +
            (month != 0 ? "MONTH(penaltyDate) = ? " : "") +
            (year != 0 ? (month != 0 ? "AND " : "") + "YEAR(penaltyDate) = ? " : "") +
            (!meterLoc.equals("All") ? (month != 0 || year != 0 ? "AND " : "") + "meterLocation = ? " : "") +
            (status != 0 ? (month != 0 || year != 0 || !meterLoc.equals("All") ? "AND " : "") + "paymentStatus = ? " : "") +
            (searchVal != null && !searchVal.isEmpty() ? (month != 0 || year != 0 || !meterLoc.equals("All") || status != 0 ? "AND " : "") +
                    "(UPPER(name) LIKE ? OR UPPER(meterNumber) LIKE ? OR UPPER(meterLocation) LIKE ?) " : "") +
            "ORDER BY penaltyDate;";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            int parameterIndex = 1;
            if (month != 0) preparedStatement.setInt(parameterIndex++, month);
            if (year != 0) preparedStatement.setInt(parameterIndex++, year);
            if (!meterLoc.equals("All")) preparedStatement.setString(parameterIndex++, meterLoc);
            if (status != 0) preparedStatement.setInt(parameterIndex++, status);
            if (searchVal != null && !searchVal.isEmpty()) {
                String likePattern = "%" + searchVal + "%";
                preparedStatement.setString(parameterIndex++, likePattern);
                preparedStatement.setString(parameterIndex++, likePattern);
                preparedStatement.setString(parameterIndex++, likePattern);
            }

            ResultSet rs = preparedStatement.executeQuery();

            int no = 1;
            while (rs.next()) {
                String penaltyStatus = "";

                if (rs.getInt("penaltyStatus") == 1) {
                    penaltyStatus = "Unpaid";
                } else {
                    penaltyStatus = "Paid";
                }
                String penaltyType = "";
                if(rs.getInt("penaltyType") == 1){
                    penaltyType = "Overdue Penalty";
                }

                penalty.add( new Penalty(
                        no,
                        rs.getInt("penaltyID"),
                        rs.getInt("billID"),
                        rs.getString("name"),
                        rs.getString("meterNumber"),
                        rs.getString("meterLocation"),
                        rs.getDate("billingDate").toLocalDate(),
                        rs.getInt("billAmount"),
                        rs.getDate("penaltyDate").toLocalDate(),
                        penaltyType,
                        rs.getInt("penaltyAmount"),
                        rs.getDouble("totalAmount"),
                        penaltyStatus
                        
                 
                        
                )
                    );
                    no++;
                }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return penalty;
    }

         
}
