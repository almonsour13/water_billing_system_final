/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dashboard;

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
public class DashboardModel {
    
    private final DbConfig dbConfig;
    
    public DashboardModel(){
        this.dbConfig = new DbConfig();
    }
    public ObservableList<Dashboard> getIncomes() throws SQLException{
        ObservableList<Dashboard> income = FXCollections.observableArrayList();
        String query =  "SELECT\n" +
                        "	MONTH(b.billingDate) AS month,\n" +
                        "    SUM(p.paymentAmount) AS incomePerMonth\n" +
                        "FROM\n" +
                        "	meterreading mr \n" +
                        "JOIN\n" +
                        "	bills b ON mr.meterReadingID = b.meterReadingID\n" +
                        "JOIN\n" +
                        "	payment p ON b.billID = p.billID\n" +
                        "GROUP BY	\n" +
                        "	month\n" +
                        "ORDER BY \n" +
                        "	month\n" +
                        "    ; ";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            //statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            int no = 1;
            while(rs.next()) {
                
                income.add( new Dashboard(
                        rs.getInt("month"),
                        rs.getDouble("incomePerMonth")
                )
              );
                no++;
            }
        }
        return income;
    } 
    public Dashboard getBillAnalytics() throws SQLException{
      Dashboard bills = null;
        String query =  "SELECT\n" +
                        "  SUM(CASE WHEN billingStatus = 1 THEN 1 ELSE 0 END) AS unpaid,\n" +
                        "  SUM(CASE WHEN billingStatus = 2 THEN 1 ELSE 0 END) AS paid,\n" +
                        "  SUM(CASE WHEN billingStatus = 3 THEN 1 ELSE 0 END) AS overdue,\n" +
                        "  COUNT(*) AS totalBills\n" +
                        "FROM\n" +
                        "  bills;";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            //statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                bills = new Dashboard(
                        rs.getInt("unpaid"),
                        rs.getInt("paid"),
                        rs.getInt("overdue"),
                        rs.getInt("totalBills")
                );
            }
            
        }
        
        return bills;   
    }
    public Dashboard getConsumerAnalytics() throws SQLException{
      Dashboard bills = null;
        String query =  "SELECT\n" +
                        "    SUM(CASE WHEN cStatus = 1 THEN 1 ELSE 0 END) AS Active,\n" +
                        "    SUM(CASE WHEN cStatus = 2 THEN 1 ELSE 0 END) AS Disconnected,\n" +
                        "    SUM(CASE WHEN cStatus = 3 THEN 1 ELSE 0 END) AS Cut,\n" +
                        "    COUNT(*) AS totalCon\n" +
                        "FROM\n" +
                        "	conscessionaries;";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            //statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                bills = new Dashboard(
                        rs.getInt("Active"),
                        rs.getInt("Disconnected"),
                        rs.getInt("Cut"),
                        rs.getInt("totalCon")
                );
            }
            
        }
        return bills;   
    }
    public ObservableList<Dashboard> getWaterConsumption() throws SQLException{
        ObservableList<Dashboard> usage = FXCollections.observableArrayList();
        String query =  "SELECT\n" +
                        "    MONTH(mr.readingDate) AS month,\n" +
                        "    SUM(mr.currentReading-mr.previousReading) as waterConsumption\n" +
                        "FROM \n" +
                        "	meterreading mr\n" +
                        "WHERE\n" +
                        "	mr.currentReading != 0\n" +
                        "GROUP BY\n" +
                        "	month\n" +
                        "ORDER BY\n" +
                        "	month;";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            //statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            int no = 1;
            while(rs.next()) {
                
                usage.add( new Dashboard(
                        rs.getInt("month"),
                        rs.getInt("waterConsumption")
                )
              );
                no++;
            }
        }
        return usage;
    }
    public ObservableList<Dashboard> getRecentBills() throws SQLException{
        ObservableList<Dashboard> bills = FXCollections.observableArrayList();
        String query =  "SELECT\n" +
                        "	m.meterNumber,\n" +
                        "	CONCAT(c.cFName,' ', c.cLName) AS name,\n" +
                        "	b.billingDate AS billingDate,\n" +
                        "	b.totalAmount,\n" +
                        "	p.paymentStatus\n" +
                        "FROM\n" +
                        "	conscessionaries c\n" +
                        "JOIN\n" +
                        "	meter m ON c.cID = m.cID\n" +
                        "JOIN\n" +
                        "	bills b ON m.meterID = b.meterID\n" +
                        "JOIN\n" +
                        "	payment p ON b.billID = p.billID\n" +
                        "GROUP BY\n" +
                        "	m.meterID,\n" +
                        "	b.billingDate,\n" +
                        "	b.totalAmount,\n" +
                        "	p.paymentStatus\n" +
                        "ORDER BY\n" +
                        "	b.billingDate DESC\n" +
                        "LIMIT 5;";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            //statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            int no = 1;
            while(rs.next()) {
                String status = "";
                if(rs.getInt("paymentStatus") == 0){
                    status = "Unpaid";
                }else if(rs.getInt("paymentStatus") == 1){
                    status = "Partial";
                }else{
                    status = "Paid";
                }
                bills.add( new Dashboard(
                                no,
                        rs.getString("name"),
                        rs.getDate("billingDate").toLocalDate(),
                        rs.getDouble("totalAmount"),
                        status
                )
              );
                no++;
            }
        }
        return bills;
    }
     
}
