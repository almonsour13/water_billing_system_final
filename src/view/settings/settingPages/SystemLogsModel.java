/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.settings.settingPages;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DbConfig;

/**
 *
 * @author Merry Ann
 */
public class SystemLogsModel {
    public DbConfig dbConfig = new DbConfig();
    
    public SystemLogsModel(){
        this.dbConfig = new DbConfig();
    }
    public ObservableList<SystemLogs> getSystemsLog() throws SQLException{
        ObservableList<SystemLogs> systemsLogs = FXCollections.observableArrayList();
        String query =  "SELECT\n" +
                        "    al.actvityLogID,\n" +
                        "    al.aID,\n" +
                        "    a.aType,\n" +
                        "    CONCAT(a.aFName,' ',a.aLName) as name,\n" +
                        "    al.activityDate,\n" +
                        "    al.activityTime,\n" +
                        "    al.activityDescription,\n" +
                        "    al.activityStatus\n" +
                        "FROM\n" +
                        "	activitylog al\n" +
                        "JOIN\n" +
                        "	accounts a ON al.aID = a.aID\n" +
                        "ORDER BY\n" +
                        "	al.activityDate, al.activityTime;";
        
        try (Connection connection = dbConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                String role = rs.getInt("aType") == 1?"Admin":"Collector";
                
                systemsLogs.add(new SystemLogs(
                        rs.getInt("actvityLogID"),
                        rs.getInt("aID"),
                        rs.getString("name"),
                        role,
                        rs.getDate("activityDate").toLocalDate(),
                        rs.getTime("activityTime").toLocalTime(),
                        rs.getString("activityDescription"),
                        rs.getInt("activityStatus")
                )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return systemsLogs;
    }
    public void insertLog(int id, String type) throws SQLException {
        try (Connection connection = dbConfig.getConnection()) {
            connection.setAutoCommit(false);

            String insertConscessionariesSQL = "INSERT INTO activitylog (aID, activityDate, activityTime, activityDescription, activityStatus) VALUES (?, CURRENT_DATE, CURRENT_TIME, ?, 1);";
            try (PreparedStatement conscessionariesStatement = connection.prepareStatement(insertConscessionariesSQL, Statement.RETURN_GENERATED_KEYS)) {
                conscessionariesStatement.setInt(1, id);
                conscessionariesStatement.setString(2, type);

                int rowsAffected = conscessionariesStatement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Inserting log entry failed, no rows affected.");
                }
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
