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
                String cMStatus = getStatus(rs.getInt("mchStatus"));
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
    public ObservableList<MeterNumber> filterMeterNumber(int month, int year, String searchVal,int cStatus, int mStatus, String meterLoc) throws SQLException {
        ObservableList<MeterNumber> meterNumber = FXCollections.observableArrayList();
        searchVal = (searchVal != null) ? searchVal.toUpperCase() : null;

        String query = "SELECT * FROM get_meterNumbers " +
                (month != 0 || year != 0 || cStatus != 0 || mStatus != 0 || (searchVal != null && !searchVal.isEmpty()) || (meterLoc != "All" )? "WHERE " : "") +              
                (month != 0 ? "MONTH(date) = ? " : "") +
                (year != 0 ? (month != 0 ? "AND " : "") + "YEAR(date) = ? " : "") +
                (cStatus != 0 ? (month != 0 || year != 0 ? "AND " : "") + "mchStatus = ? " : "") +
                (mStatus != 0 ? (month != 0 || year != 0 || cStatus != 0 ? "AND " : "") + "meterStatus = ? " : "") +
                (searchVal != null && !searchVal.isEmpty() ? (month != 0 || year != 0 || cStatus != 0 || mStatus != 0 ? "AND " : "") +
                        "UPPER(name) LIKE ? OR UPPER(meterNumber) LIKE ? OR UPPER(meterLocation) LIKE ? " : "") +
                (meterLoc != "All" ? (month != 0 || year != 0 || cStatus != 0 || mStatus != 0 || (searchVal != null && !searchVal.isEmpty()) ? "AND " : "") +
                        "meterLocation = ? " : "") +
                ";";
System.out.println("Generated SQL query: " + query);

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            int parameterIndex = 1;
            if (month != 0) statement.setInt(parameterIndex++, month);
            if (year != 0) statement.setInt(parameterIndex++, year);
            if (cStatus != 0) statement.setInt(parameterIndex++, cStatus); 
            if (mStatus != 0) statement.setInt(parameterIndex++, mStatus);  // Assuming status corresponds to mchStatus
            if (searchVal != null && !searchVal.isEmpty()) {
                String likePattern = "%" + searchVal + "%";
                statement.setString(parameterIndex++, likePattern);
                statement.setString(parameterIndex++, likePattern);
                statement.setString(parameterIndex++, likePattern);
            }
            if (meterLoc != "All") statement.setString(parameterIndex, meterLoc);

            try (ResultSet rs = statement.executeQuery()) {
                int no = 1;
                while (rs.next()) {
                    String mstatus = (rs.getInt("meterStatus") == 1) ? "Active" : "Inactive";
                    String cMStatus = getStatus(rs.getInt("mchStatus"));

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
                            mstatus
                    ));

                    no++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return meterNumber;
    }

    public ObservableList<MeterNumber> getConnectionHistory() throws SQLException{
        ObservableList<MeterNumber> meterNumber = FXCollections.observableArrayList();
        String query = "SELECT * FROM get_connection_history ORDER BY mchID;";
        
        try (Connection connection = dbConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            
            int no = 1;
            while(rs.next()){
                String status = rs.getInt("meterStatus") == 1?"Active":"Inactive";
                String cMStatus = getStatus(rs.getInt("mchStatus"));
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
    public ObservableList<MeterNumber> filterConnectionHistory(int month, int year, String searchVal, int status, String meterLoc) throws SQLException {
        ObservableList<MeterNumber> meterNumber = FXCollections.observableArrayList();
        searchVal = (searchVal != null) ? searchVal.toUpperCase() : null;

        String query = "SELECT * FROM get_connection_history " +
                (month != 0 || year != 0 || status != 0 || (searchVal != null && !searchVal.isEmpty()) || (meterLoc != "All" )? "WHERE " : "") +
                (month != 0 ? "MONTH(date) = ? " : "") +
                (year != 0 ? (month != 0 ? "AND " : "") + "YEAR(date) = ? " : "") +
                (status != 0 ? (month != 0 || year != 0 ? "AND " : "") + "mchStatus = ? " : "") +
                (searchVal != null && !searchVal.isEmpty() ? (month != 0 || year != 0 || status != 0 ? "AND " : "") +
                        "UPPER(name) LIKE ? OR UPPER(meterNumber) LIKE ? OR UPPER(meterLocation) LIKE ? " : "") +
                (meterLoc != "All" ? (month != 0 || year != 0 || status != 0 || (searchVal != null && !searchVal.isEmpty()) ? "AND " : "") +
                        "meterLocation = ? " : "") +
                "ORDER BY mchID;";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            int parameterIndex = 1;
            if (month != 0) statement.setInt(parameterIndex++, month);
            if (year != 0) statement.setInt(parameterIndex++, year);
            if (status != 0) statement.setInt(parameterIndex++, status);  // Assuming status corresponds to mchStatus
            if (searchVal != null && !searchVal.isEmpty()) {
                String likePattern = "%" + searchVal + "%";
                statement.setString(parameterIndex++, likePattern);
                statement.setString(parameterIndex++, likePattern);
                statement.setString(parameterIndex++, likePattern);
            }
            if (meterLoc != "All") statement.setString(parameterIndex, meterLoc);

            try (ResultSet rs = statement.executeQuery()) {
                int no = 1;
                while (rs.next()) {
                    String mstatus = (rs.getInt("meterStatus") == 1) ? "Active" : "Inactive";
                    String cMStatus = getStatus(rs.getInt("mchStatus"));

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
                            mstatus
                    ));

                    no++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return meterNumber;
    }
    
    public ObservableList<MeterNumber> getMeterConnectionHistoryByID(int meterID) throws SQLException{
        ObservableList<MeterNumber> meterNumber = FXCollections.observableArrayList();
        String query = "SELECT * FROM get_connection_history WHERE meterID = ? ORDER BY mchID;";
        
        try (Connection connection = dbConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, meterID);
            ResultSet rs = statement.executeQuery();
            
            int no = 1;
            while(rs.next()){
                String status = rs.getInt("meterStatus") == 1?"Active":"Inactive";
                String cMStatus = getStatus(rs.getInt("mchStatus"));
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
                String cMStatus = getStatus(rs.getInt("mchStatus"));
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
    public MeterNumber getTransferredMeterConsumer(int id) throws SQLException{
        MeterNumber meterNumber = null;
        String query = "SELECT * FROM get_meterNumbers\n" +
                        "WHERE cmID > '"+id+"' \n" +
                        "ORDER BY cmID DESC\n" +
                        "LIMIT 1;";

        try (Connection connection = dbConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            
            int no = 1;
            if(rs.next()){
                String status = getStatus(rs.getInt("meterStatus"));
                String cMStatus = getStatus(rs.getInt("mchStatus"));
                meterNumber = new MeterNumber(
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
                        
                );
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
            String updateConsumerSQL =  "INSERT INTO meterconnectionhistory (cmID, date, mchStatus) \n" +
                                        "VALUES (?, CURRENT_DATE, ?);";
            
            try (PreparedStatement updateStatement = connection.prepareStatement(updateConsumerSQL)) {
                updateStatement.setInt(1, cmID);
                updateStatement.setInt(2, status);
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
                    String updateConsumerMeterStatusQuery = "INSERT INTO meterconnectionhistory (cmID, date, mchStatus) \n" +
                                        "VALUES (?, CURRENT_DATE, ?);";

                    try (PreparedStatement updateConsumerMeterStatusStatement = connection.prepareStatement(updateConsumerMeterStatusQuery)) {
                        updateConsumerMeterStatusStatement.setInt(1, consumerMeterId);
                        updateConsumerMeterStatusStatement.setInt(2, (status == 2) ? 4 : 1);

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
