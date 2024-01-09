/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DbConfig;
import model.account.Account;
import model.bills.Bills;

/**
 *
 * @author Merry Ann
 */
public class AccountModel {
    private final DbConfig dbConfig;
    
    public AccountModel(){
        this.dbConfig = new DbConfig();
    }
    public ObservableList<Account> getAccounts() throws SQLException{
        ObservableList<Account> accounts = FXCollections.observableArrayList();
        try (Connection connection = dbConfig.getConnection();
             Statement statement = connection.createStatement()) {
            String query = "SELECT aID, aFName, aMName, aLName, aSuffix, aUserName, aPassWord, aType, aStatus FROM accounts;";
            ResultSet rs = statement.executeQuery(query);
            int no = 1;
            while (rs.next()) {
                String status = "";
                if(rs.getInt("aStatus")== 1){
                    status = "Active";
                }else if(rs.getInt("aStatus")== 2){
                    status = "Inactive";
                }
                String accountType = "";
                if(rs.getInt("aType") == 1){
                    accountType = "Admin";
                }else if(rs.getInt("aType") == 2){
                    accountType = "Collector";
                }
                if(rs.getInt("aStatus")!= 3){
                    accounts.add(
                     new Account(no,
                            rs.getInt("aID"), rs.getString("aFName"), 
                            rs.getString("aMName"), rs.getString("aLName"), 
                            rs.getString("aUserName"), rs.getString("aPassWord"),
                            accountType, status
                                )
                            );
                    
                    no++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return accounts;
        
    }
    public void addAccount(String fName, String mName, String lName, String uName, String pWord, int role, int status) throws SQLException {      
        try (Connection connection = dbConfig.getConnection()) {
            connection.setAutoCommit(false);

            // Insert data into the Account table
            String insertAccountSQL = "INSERT INTO accounts (aFName, aMName, aLName, aUserName, aPassWord, aType, aStatus, aSuffix) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement accountStatement = connection.prepareStatement(insertAccountSQL, Statement.RETURN_GENERATED_KEYS)) {
               
                accountStatement.setString(1, fName);
                accountStatement.setString(2, mName);
                accountStatement.setString(3, lName);
                accountStatement.setString(4, uName);
                accountStatement.setString(5, pWord);
                accountStatement.setInt(6, role);
                accountStatement.setInt(7, status);
                accountStatement.setString(8, "");

                accountStatement.executeUpdate();                
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void updateAccount(int id, String fName, String mName, String lName, String uName, String pWord, int role, int status) throws SQLException{       
        try (Connection connection = dbConfig.getConnection()) {
            connection.setAutoCommit(false);

            // Update data in the Account table
            String updateAccountSQL = "UPDATE accounts "
                    + "SET aFName = ?, "
                    + "aMName = ?, "
                    + "aLName = ?, "
                    + "aUserName = ?, "
                    + "aPassWord = ?, "
                    + "aType = ?, "
                    + "aStatus = ? "
                    + "WHERE aID = ?";
            try (PreparedStatement accountStatement = connection.prepareStatement(updateAccountSQL)) {
                accountStatement.setString(1, fName);
                accountStatement.setString(2, mName);
                accountStatement.setString(3, lName);
                accountStatement.setString(4, uName);
                accountStatement.setString(5, pWord);
                accountStatement.setInt(6, role);
                accountStatement.setInt(7, status);
                accountStatement.setInt(8, id);

                accountStatement.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }
    public void archiveAccount(int id) throws SQLException{
        try (Connection connection = dbConfig.getConnection();
            Statement statement = connection.createStatement()) {

            String query = "UPDATE accounts c SET aStatus = " + 3 + " WHERE aID = " + id;
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public Account getAccountByID(int id) throws SQLException {
        Account account = null;
        String query = "SELECT CONCAT(a.aFName,' ',a.aLName) AS name " +
                       "FROM accounts a " +
                       "WHERE a.aStatus != 0 AND a.aID = ?";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                account = new Account(rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }

        return account;
    }

}
