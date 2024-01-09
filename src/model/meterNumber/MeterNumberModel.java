/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.meterNumber;

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
public class MeterNumberModel {
     private DbConfig dbConfig;
    
    public MeterNumberModel(){
        this.dbConfig = new DbConfig();
    }
    public ObservableList<MeterNumber> getMeterNumber() throws SQLException{
        ObservableList<MeterNumber> meterNumber = FXCollections.observableArrayList();
        String query = "SELECT * FROM get_meterNumbers;";
        
        try (Connection connection = dbConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            
            int no = 1;
            while(rs.next()){
                String status = rs.getInt("meterStatus") == 1?"Active":"Inactive";
                String cMStatus = getStatus(rs.getInt("cMStatus"));
                meterNumber.add(new MeterNumber(
                        no,
                        rs.getInt("cmID"),
                        rs.getInt("cID"),
                        rs.getInt("meterID"),
                        rs.getString("meterNumber"),
                        rs.getString("meterLocation"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("name"),
                        10,
                        cMStatus,
                        status
                        
                ));
                
                no++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }    
        return meterNumber;
    }
    public ObservableList<MeterNumber> getConnectionHistory() throws SQLException{
        ObservableList<MeterNumber> meterNumber = FXCollections.observableArrayList();
        String query = "SELECT * FROM get_meterNumbers ORDER BY date;";
        
        try (Connection connection = dbConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            
            int no = 1;
            while(rs.next()){
                String status = rs.getInt("meterStatus") == 1?"Active":"Inactive";
                String cMStatus = getStatus(rs.getInt("cMStatus"));
                meterNumber.add(new MeterNumber(
                        no,
                        rs.getInt("cmID"),
                        rs.getInt("cID"),
                        rs.getInt("meterID"),
                        rs.getString("meterNumber"),
                        rs.getString("meterLocation"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("name"),
                        10,
                        cMStatus,
                        status
                        
                ));
                
                no++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }    
        return meterNumber;
    }
    public ObservableList<MeterNumber> getMeterNumberByConsumerID(int id) throws SQLException{
        ObservableList<MeterNumber> meterNumber = FXCollections.observableArrayList();
        String query = "SELECT * FROM get_meterNumbers WHERE cID = '"+id+"';";
        
        try (Connection connection = dbConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            
            int no = 1;
            while(rs.next()){
                String status = getStatus(rs.getInt("meterStatus"));
                String cMStatus = getStatus(rs.getInt("cMStatus"));
                meterNumber.add(new MeterNumber(
                        no,
                        rs.getInt("cmID"),
                        rs.getInt("cID"),
                        rs.getInt("meterID"),
                        rs.getString("meterNumber"),
                        rs.getString("meterLocation"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("name"),
                        10,
                        cMStatus,
                        status
                        
                ));
                
                no++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }    
        return meterNumber;
    }
    public int checkConsumerStatus(int cID) throws SQLException{
        String query =  "SELECT\n" +
                        "	c.cStatus\n" +
                        "FROM\n" +
                        "	conscessionaries c \n" +
                        "WHERE \n" +
                        "	c.cID = ?";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            int parameterIndex = 1;
            statement.setInt(parameterIndex++, cID);
            
            ResultSet rs = statement.executeQuery();
            
            if(rs.next()){
                int cStatus = rs.getInt("cStatus");
                return cStatus;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; 
        }
        return 0;
    }
    public void updateConnectionStatus(int cmID, int status) throws SQLException {
        try (Connection connection = dbConfig.getConnection()) {
            String updateConsumerSQL = "UPDATE consumermeternumber SET cMStatus = ? WHERE consumerMeterNumberID = ?;";
            
            try (PreparedStatement updateStatement = connection.prepareStatement(updateConsumerSQL)) {
                updateStatement.setInt(1, status);
                updateStatement.setInt(2, cmID);
                updateStatement.executeUpdate();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void updateMeterStatus(int meterId, int consumerMeterId, int status) throws SQLException {
        try (Connection connection = dbConfig.getConnection()) {
            String updateMeterStatusQuery = "UPDATE meter SET meterStatus = ? WHERE meterID = ?;";

            try (PreparedStatement updateMeterStatusStatement = connection.prepareStatement(updateMeterStatusQuery)) {
                updateMeterStatusStatement.setInt(1, status);
                updateMeterStatusStatement.setInt(2, meterId);
                int rowsAffected = updateMeterStatusStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("asdasdas");
                    String updateConsumerMeterStatusQuery = "UPDATE consumermeternumber SET cMStatus = ? WHERE consumerMeterNumberID = ?;";

                    try (PreparedStatement updateConsumerMeterStatusStatement = connection.prepareStatement(updateConsumerMeterStatusQuery)) {
                        updateConsumerMeterStatusStatement.setInt(1, (status == 2) ? 4 : 1);
                        updateConsumerMeterStatusStatement.setInt(2, consumerMeterId);

                        if (updateConsumerMeterStatusStatement.executeUpdate() > 0) {
                            // Both updates were successful.
                            // You can optionally commit the transaction here if using transactions.
                        } else {
                            // Handle the case where the second update did not affect any rows.
                        }
                    }
                } else {
                    // Handle the case where the first update did not affect any rows.
                }
            }
        } catch (SQLException e) {
            // Log the exception or handle it appropriately.
            e.printStackTrace();
            throw e;
        }
    }

    public String getStatus(int value){
        switch(value){
            case 1:
                return "Active";
            case 2:
                return "Disconnected";
            case 3:
                return "Transferred";
            case 4:
                return "Inactive";
            default:
                return "";
        }
    }
    
}
