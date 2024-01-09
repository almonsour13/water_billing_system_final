/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DbConfig;
import model.consumer.Consumer;

/**
 *
 * @author Merry Ann
 */
public class SettingsModel {
    public DbConfig dbConfig = new DbConfig();
    public SettingsModel(){
        this.dbConfig = new DbConfig();
    }
    public Settings getComInfo() throws SQLException{
        Settings settings = null;
        try (Connection connection = dbConfig.getConnection();
            Statement statement = connection.createStatement()) {
            String query =  "SELECT\n" +
                            "	c.companyID,\n" +
                            "	c.companyName,\n" +
                            "    c.companyCountry,\n" +
                            "    c.companyRegion,\n" +
                            "    c.companyProvince,\n" +
                            "    c.companyMunicipality,\n" +
                            "    c.companyBaranggay,\n" +
                            "    c.companyPurok\n" +
                            "FROM\n" +
                            "	companyinformationsetting c\n" +
                            "WHERE companyID = 1;";
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()){
                   settings = new Settings(
                           rs.getInt("companyID"),
                           rs.getString("companyName"),
                           rs.getString("companyCountry"),
                           rs.getString("companyRegion"),
                           rs.getString("companyProvince"),
                           rs.getString("companyMunicipality"),
                           rs.getString("companyBaranggay"),
                           rs.getString("companyPurok")
                   ); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return settings;
    }
    public void insertConInfo(Settings settings){
        
    }
    public void updateConInfo(Settings settings) throws SQLException{
        try (Connection connection = dbConfig.getConnection()) {
            String updateConsumerSQL =  "UPDATE companyinformationsetting\n" +
                                        "SET\n" +
                                        "    companyName = ?,\n" +
                                        "    companyCountry = ?,\n" +
                                        "    companyRegion = ?,\n" +
                                        "    companyProvince = ?,\n" +
                                        "    companyMunicipality = ?,\n" +
                                        "    companyBaranggay = ?,\n" +
                                        "    companyPurok = ?\n" +
                                        "WHERE companyID = ?;";

            try (PreparedStatement updateStatement = connection.prepareStatement(updateConsumerSQL)) {
                updateStatement.setString(1, settings.getCompanyName());
                updateStatement.setString(2, settings.getCompanyCountry());
                updateStatement.setString(3, settings.getCompanyRegion());
                updateStatement.setString(4, settings.getCompanyProvince());
                updateStatement.setString(5, settings.getCompanyMunicipality());
                updateStatement.setString(6, settings.getCompanyBaranggay());
                updateStatement.setString(7, settings.getCompanyPurok());
                updateStatement.setInt(8, settings.getCompanyID());

                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public ObservableList<Settings> getBillingConfiguration() throws SQLException{
        ObservableList<Settings> settings =  FXCollections.observableArrayList();
        try (Connection connection = dbConfig.getConnection();
            Statement statement = connection.createStatement()) {
            String query =  "SELECT\n" +
                            "	b.settingID,\n" +
                            "    b.settingName,\n" +
                            "    b.settingValue\n" +
                            "FROM\n" +
                            "	billingconfiguration b;";
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                   settings.add(new Settings(
                           rs.getInt("settingID"),
                           rs.getString("settingName"),
                           rs.getString("settingValue")
                    )
                   ); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return settings;
    }
    public void updateBillCon(ObservableList<Settings> newValue) throws SQLException{
        try (Connection connection = dbConfig.getConnection()) {
            for(Settings settings:newValue){
                String updateConsumerSQL =  "UPDATE billingconfiguration\n" +
                                            "SET\n" +
                                            "    settingValue = ?\n" +
                                            "WHERE settingID = ?;";

                try (PreparedStatement updateStatement = connection.prepareStatement(updateConsumerSQL)) {
                    updateStatement.setString(1, settings.getSettingValue());
                    updateStatement.setInt(2, settings.getSettingID());

                    updateStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
                throw e;
        }
    }
    public void insertBillCon(ObservableList<Settings> settings){
        System.out.println("asdasdad");
    }
    public void getLogs(){
        
    }
}
