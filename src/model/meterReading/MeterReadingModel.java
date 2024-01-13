/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.meterReading;

import controller.accountLoggedSetter.LoggedAccountSetter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DbConfig;

/**
 *
 * @author Administrator
 */
public class MeterReadingModel {
    private final DbConfig dbConfig = new DbConfig();    
    private  LoggedAccountSetter logAccount = new LoggedAccountSetter();
    
    public ObservableList<MeterReading> getConsumerMeterNo(int id) throws SQLException{
        ObservableList<MeterReading> MeterNumber = FXCollections.observableArrayList();
        String query =  "SELECT \n" +
                        "    m.meterID,\n" +
                        "    m.meterNumber,\n" +
                        "    m.installationDate,\n" +
                        "    m.meterStatus\n" +
                        "FROM \n" +
                        "	conscessionaries c\n" +
                        "JOIN\n" +
                        "	meter m ON c.cID = m.cID\n" +
                        "WHERE c.cID = '"+id+"';";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            //statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String status = "";

                if (rs.getInt("meterStatus") == 1) {
                    status = "Active";
                } else {
                    status = "Disconnected";
                }

                MeterNumber.add( new MeterReading(
                        rs.getInt("meterID"),
                        rs.getString("meterNumber"),
                        rs.getDate("installationDate").toLocalDate(),
                        status
                )
                 );
            }
        }
        return MeterNumber;
    }
    public ObservableList<MeterReading> getMeterReading() throws SQLException{
        ObservableList<MeterReading> MeterReading = FXCollections.observableArrayList();
        String query =  "SELECT * FROM get_meterReading;";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            //statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            int no = 1;
            while (rs.next()) {
                String date = rs.getDate("date") != null ? String.valueOf(rs.getDate("date").toLocalDate()) : "";
                String name = "";
                if(logAccount.getAccount() == rs.getInt("aID")){
                    name = "You";
                }else{
                    name = rs.getString("collectorName");
                }
                MeterReading.add(new MeterReading(
                        no,
                        rs.getInt("cID"),
                        rs.getInt("consumerMeterNumberID"),
                        rs.getInt("meterReadingID"),
                        rs.getString("meterNumber"),
                        rs.getString("meterLocation"),
                        rs.getString("name"),
                        rs.getDate("readingDate").toLocalDate(),
                        rs.getInt("previousReading"),
                        rs.getInt("currentReading"),
                        date,
                       name,
                        rs.getInt("meterReadingStatus")==1?"":"Edited"
                ));
                no++;
            }
        }
        return MeterReading;
    } 
    public ObservableList<MeterReading> getMeterReadingByID() throws SQLException{
        ObservableList<MeterReading> MeterReading = FXCollections.observableArrayList();
        String query =  "SELECT * FROM get_meterReading;";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            //statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            int no = 1;
            while (rs.next()) {
                String date = rs.getDate("date") != null ? String.valueOf(rs.getDate("date").toLocalDate()) : "";
                String name = "";
                if(logAccount.getAccount() == rs.getInt("aID")){
                    name = "You";
                }else{
                    name = rs.getString("collectorName");
                }
                MeterReading.add(new MeterReading(
                        no,
                        rs.getInt("cID"),
                        rs.getInt("consumerMeterNumberID"),
                        rs.getInt("meterReadingID"),
                        rs.getString("meterNumber"),
                        rs.getString("meterLocation"),
                        rs.getString("name"),
                        rs.getDate("readingDate").toLocalDate(),
                        rs.getInt("previousReading"),
                        rs.getInt("currentReading"),
                        date,
                       name,
                        rs.getInt("meterReadingStatus")==1?"":"Edited"
                ));
                no++;
            }
        }
        return MeterReading;
    } 
    public ObservableList<MeterReading> filterMeterReading(int month, int year, String searchVal) throws SQLException {
        ObservableList<MeterReading> meterReadings = FXCollections.observableArrayList();
        String query = "SELECT * FROM get_meterReading " +
            (month != 0 || year != 0 ||  (searchVal != null && !searchVal.isEmpty()) ? "WHERE " : "") +
            (month != 0 ? "MONTH(readingDate) = ? " : "") +
            (year != 0 ? (month != 0 ? "AND " : "") + "YEAR(readingDate) = ? " : "") +
            (searchVal != null && !searchVal.isEmpty() ? (month != 0 || year != 0 ? "AND " : "") +
                    "UPPER(name) LIKE ? " : "") +
            "ORDER BY name;";
        try (Connection connection = dbConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            int parameterIndex = 1;
            if (month != 0) statement.setInt(parameterIndex++, month);
            if (year != 0) statement.setInt(parameterIndex++, year);
            if (searchVal != null && !searchVal.isEmpty()) {
                String likePattern = "%" + searchVal + "%";
                statement.setString(parameterIndex++, likePattern);
            }

            ResultSet rs = statement.executeQuery();
            int no = 1;
            while (rs.next()) {
                String date = rs.getDate("date") != null ? String.valueOf(rs.getDate("date").toLocalDate()) : "";
                String name = "";
                if(logAccount.getAccount() == rs.getInt("aID")){
                    name = "You";
                }else{
                    name = rs.getString("collectorName");
                }
                meterReadings.add(new MeterReading(
                        no,
                        rs.getInt("cID"),
                        rs.getInt("consumerMeterNumberID"),
                        rs.getInt("meterReadingID"),
                        rs.getString("meterNumber"),
                        rs.getString("meterLocation"),
                        rs.getString("name"),
                        rs.getDate("readingDate").toLocalDate(),
                        rs.getInt("previousReading"),
                        rs.getInt("currentReading"),
                        date,
                       name,
                        rs.getInt("meterReadingStatus")==1?"":"Edited"
                ));
                no++;
            }
        }
        return meterReadings;
    }   
    public ObservableList<MeterReading> getEditReadingHistory() throws SQLException{
        ObservableList<MeterReading> MeterReading = FXCollections.observableArrayList();
        String query =  "SELECT\n" +
                        "    mr.meterReadingID,\n" +
                        "    er.editReadingID,\n" +
                        "    a.aID,\n" +
                        "    m.meterNumber,\n" +
                        "    cm.meterLocation,\n" +
                        "    CONCAT(c.cFName, ' ', c.cLName) AS name,\n" +
                        "    mr.readingDate,\n" +
                        "    mr.previousReading,\n" +
                        "    er.orignalValue,\n" +
                        "    er.newValue,\n" +
                        "    er.reason,\n" +
                        "    er.editReadingDate,\n" +
                        "    CONCAT(a.aFName, ' ', a.aLName) AS accountName\n" +
                        "FROM\n" +
                        "	conscessionaries c \n" +
                        "JOIN\n" +
                        "	consumermeternumber cm ON c.cID = cm.cID\n" +
                        "JOIN\n" +
                        "	meter m ON cm.meterID = m.meterID\n" +
                        "JOIN\n" +
                        "	meterreading mr ON cm.consumerMeterNumberID = mr.consumerMeterNumberID\n" +
                        "JOIN\n" +
                        "	editreading er ON mr.meterReadingID = er.meterReadingID\n" +
                        "JOIN\n" +
                        "	accounts a ON er.aID = a.aID;";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            //statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            int no = 1;
            while (rs.next()) {
                String name = "";
                if(logAccount.getAccount() == rs.getInt("aID")){
                    name = "You";
                }else{
                    name = rs.getString("collectorName");
                }
                MeterReading.add(new MeterReading(
                        no,
                        rs.getInt("meterReadingID"),
                        rs.getInt("editReadingID"),
                        rs.getString("meterNumber"),
                        rs.getString("meterLocation"),
                        rs.getString("name"),
                        rs.getDate("readingDate").toLocalDate(),
                        rs.getInt("previousReading"),
                        rs.getInt("orignalValue"),
                        rs.getInt("newValue"),
                        rs.getDate("editReadingDate").toLocalDate(),
                        rs.getString("reason"),
                        rs.getString("accountName")
                ));
                no++;
            }
        }
        return MeterReading;
    } 
    
    public boolean checkCurrentReading(int meterID, int meterReadingID) throws SQLException{
        try (Connection connection = dbConfig.getConnection()) {
            connection.setAutoCommit(false);

            // Check if the bill already exists
            try (PreparedStatement checkBillStatement = connection.prepareStatement("SELECT currentReading FROM meterreading WHERE consumerMeterNumberID = ? AND meterReadingID = ?")) {
                checkBillStatement.setInt(1, meterID);
                checkBillStatement.setInt(2, meterReadingID);
               // checkBillStatement.setDate(3, java.sql.Date.valueOf(billDate));

                ResultSet checkBillResults = checkBillStatement.executeQuery();

                if (checkBillResults.next()) {
                    if(checkBillResults.getInt("currentReading")!=0){
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }            
    }
    public void addNewMeterReading() throws SQLException {
        String insertMeterReading = 
                "INSERT INTO meterreading (consumerMeterNumberID, readingDate, previousReading, currentReading, meterReadingStatus)\n" +
                "SELECT\n" +
                "    cm.consumerMeterNumberID,\n" +
                "    CURRENT_TIMESTAMP AS readingDate,\n" +
                "    COALESCE(MAX(mr.currentReading), 0) AS previousReading,\n" +
                "    0 AS currentReading,\n" +
                "    1 AS meterReadingStatus\n" +
                "FROM\n" +
                "    consumermeternumber cm\n" +
                "JOIN\n" +
                "    meter m ON cm.meterID = m.meterID\n" +
                "JOIN\n" +
                "    meterconnectionhistory mch ON cm.consumerMeterNumberID = mch.cmID\n" +
                "LEFT JOIN\n" +
                "    meterreading mr ON cm.consumerMeterNumberID = mr.consumerMeterNumberID\n" +
                "WHERE\n" +
                "    mch.mchStatus = 1 AND m.meterStatus = 1 AND \n" +
                "    mch.mchID = (SELECT MAX(mchID) FROM meterconnectionhistory WHERE cmID = cm.consumerMeterNumberID)\n" +
                "GROUP BY\n" +
                "    consumerMeterNumberID;";

        try (Connection connection = dbConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(insertMeterReading)) {

            statement.executeUpdate(); 
        }
    }
    public void saveCurrentReading(int meterReadingId, int curReading, int accountID) throws SQLException {
        String updateCurrentReadingQuery = "UPDATE meterreading mr SET mr.currentReading = ? WHERE mr.meterReadingID = ?";

        String insertCurrentReadingDateQuery = "INSERT INTO curreadingdateadded(date, meterReadingID, aID) VALUES (CURRENT_DATE, ?, ?)";

        try (Connection connection = dbConfig.getConnection();
            PreparedStatement updateStatement = connection.prepareStatement(updateCurrentReadingQuery);
            PreparedStatement insertStatement = connection.prepareStatement(insertCurrentReadingDateQuery)) {

            connection.setAutoCommit(false);

            updateStatement.setInt(1, curReading);
            updateStatement.setInt(2, meterReadingId);

            int updateAffectedRows = updateStatement.executeUpdate();

            insertStatement.setInt(1, meterReadingId);
            insertStatement.setInt(2, accountID);

            int insertAffectedRows = insertStatement.executeUpdate();

            if (updateAffectedRows > 0 && insertAffectedRows > 0) {
                System.out.println("Current reading updated successfully.");
                connection.commit(); // Commit the transaction
            } else {
                System.out.println("Failed to update current reading.");
                connection.rollback(); // Rollback the transaction in case of failure
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately, e.g., log the error
        }
    }
    public int getRate() throws SQLException{
        int rate = 0;
        try (Connection connection = dbConfig.getConnection()) {
            connection.setAutoCommit(false);

            String query =  "SELECT\n" +
                            "	settingValue\n" +
                            "FROM\n" +
                            "	billingconfiguration\n" +
                            "WHERE\n" +
                            "	settingID = 6;";
            try (PreparedStatement getRate = connection.prepareStatement(query)) {

                ResultSet rs = getRate.executeQuery();

                if (rs.next()) {
                    rate = rs.getInt("settingValue");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }       
        return rate;
    }
    public int getBillDaysOffset() throws SQLException{
        int rate = 0;
        try (Connection connection = dbConfig.getConnection()) {
            connection.setAutoCommit(false);

            String query =  "SELECT\n" +
                            "	settingValue\n" +
                            "FROM\n" +
                            "	billingconfiguration\n" +
                            "WHERE\n" +
                            "	settingID = 2;";
            try (PreparedStatement getRate = connection.prepareStatement(query)) {

                ResultSet rs = getRate.executeQuery();

                if (rs.next()) {
                    rate = rs.getInt("settingValue");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }       
        return rate;
    }
    public int getDueDateDays() throws SQLException{
        int rate = 0;
        try (Connection connection = dbConfig.getConnection()) {
            connection.setAutoCommit(false);

            String query =  "SELECT\n" +
                            "	settingValue\n" +
                            "FROM\n" +
                            "	billingconfiguration\n" +
                            "WHERE\n" +
                            "	settingID = 7;";
            try (PreparedStatement getRate = connection.prepareStatement(query)) {

                ResultSet rs = getRate.executeQuery();

                if (rs.next()) {
                    rate = rs.getInt("settingValue");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }       
        return rate;
    }
    public void addBillToConsumer(int meterID,int cID, int meterReadingID, LocalDate billDate, LocalDate dueDate,
                             int consumption, int rate, double billAmount) throws SQLException {
    try (Connection connection = dbConfig.getConnection()) {
        connection.setAutoCommit(false);
        // Insert into bills table
        String billSQL = "INSERT INTO bills (cID, meterReadingID, billingDate, dueDate, waterConsumption, rate, totalAmount, billingStatus) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement billStatement = connection.prepareStatement(billSQL, Statement.RETURN_GENERATED_KEYS)) {
            int parameterIndex = 1;
            billStatement.setInt(parameterIndex++, cID);
            billStatement.setInt(parameterIndex++, meterReadingID);
            billStatement.setDate(parameterIndex++, java.sql.Date.valueOf(billDate));
            billStatement.setDate(parameterIndex++, java.sql.Date.valueOf(dueDate));
            billStatement.setInt(parameterIndex++, consumption);
            billStatement.setInt(parameterIndex++, rate);
            billStatement.setDouble(parameterIndex++, billAmount*0.0040);
            billStatement.setInt(parameterIndex++, 1);
            billStatement.executeUpdate();
        }

        connection.commit();
    } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    }
}
    public boolean checkCurrentReadingMonth() throws SQLException {
    try (Connection connection = dbConfig.getConnection()) {
        connection.setAutoCommit(false);
        LocalDate date = LocalDate.now();
        String query =  "SELECT \n" +
                        "	readingDate AS date\n" +
                        "FROM \n" +
                        "	`meterreading`\n" +
                        "WHERE \n" +
                        "	MONTH(readingDate) = ? AND YEAR(readingDate) = ?\n" +
                        "GROUP BY\n" +
                        "	YEAR(readingDate);";
        try (PreparedStatement checkBillStatement = connection.prepareStatement(query)) {
            checkBillStatement.setInt(1, date.getMonthValue());
            checkBillStatement.setInt(2, date.getYear());

            try (ResultSet checkBillResults = checkBillStatement.executeQuery()) {
                if (checkBillResults.next()) {
                    // Move the cursor to the first row before accessing data
                    if (checkBillResults.getString("date") != null) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    // No results
                    return false;
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    }
}
    public void insertNewValue(int meterReadingID, int logAccount,int prevValue, int orginalValue, int newValue, String reason) {
        String insertCurrentReadingDateQuery = "INSERT INTO editReading(meterReadingID, aID, editReadingDate, orignalValue, newValue, reason, editReadingStatus) "
                + "VALUES (?, ?, CURRENT_DATE, ?, ?, ?, 1)";

        
        try (Connection connection = dbConfig.getConnection();
            PreparedStatement insertStatement = connection.prepareStatement(insertCurrentReadingDateQuery))
             {
            

            connection.setAutoCommit(false);  // Disable auto-commit for transaction

            // Insert new value into editReading table
            insertStatement.setInt(1, meterReadingID);
            insertStatement.setInt(2, logAccount);
            insertStatement.setInt(3, orginalValue);
            insertStatement.setInt(4, newValue);
            insertStatement.setString(5, reason);

            int insertAffectedRows = insertStatement.executeUpdate();

            if (insertAffectedRows > 0) {
                String updateReadingValueQuery = "UPDATE meterReading SET currentReading = ?, meterReadingStatus = 2 WHERE meterReadingID = ?";

                try (PreparedStatement updateStatement = connection.prepareStatement(updateReadingValueQuery)){
                    updateStatement.setInt(1, newValue);
                    updateStatement.setInt(2, meterReadingID);

                    int updateStatementAffected = updateStatement.executeUpdate();

                    if (updateStatementAffected > 0) {
                        System.out.println("Request sent successfully. Current Reading updated successfully for meterReadingID: " + meterReadingID);
                        String updateBillQuery = "UPDATE bills SET totalAmount = ?, waterConsumption = ? WHERE meterReadingID = ?;";

                        try (PreparedStatement updateBills = connection.prepareStatement(updateBillQuery)){
                            int waterConsumption = newValue - prevValue;
                            int totalAmount =  waterConsumption*getRate();
                            Double parseAmount = totalAmount*0.004;
                            updateBills.setDouble(1, parseAmount);
                            updateBills.setInt(2, waterConsumption);
                            updateBills.setInt(3, meterReadingID);

                            int billsRowAffected = updateBills.executeUpdate();

                            if (billsRowAffected > 0) {
                                System.out.println("Request sent successfully. Current Reading updated successfully for meterReadingID: " + meterReadingID);

                                connection.commit();  // Commit the transaction if everything is successful
                            } else {
                                System.out.println("Failed to update current reading.");
                                connection.rollback();  // Rollback the transaction if updating current reading fails
                            }
                        }
                        connection.commit();  // Commit the transaction if everything is successful
                    } else {
                        System.out.println("Failed to update current reading.");
                        connection.rollback();  // Rollback the transaction if updating current reading fails
                    }
                }
            } else {
                System.out.println("Failed to insert");
                connection.rollback();  // Rollback the transaction if inserting into editReading fails
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately, e.g., log the error
        }
    }
    public boolean checkInvoice(int meterReadingID) throws SQLException{
        try (Connection connection = dbConfig.getConnection()) {
            connection.setAutoCommit(false);
                String query =  "SELECT\n" +
                                "	mr.meterReadingID,\n" +
                                "    b.billingStatus,\n" +
                                "    i.invoiceDate\n" +
                                "FROM\n" +
                                "	meterreading mr \n" +
                                "JOIN\n" +
                                "	bills b ON mr.meterReadingID = b.meterReadingID\n" +
                                "JOIN\n" +
                                "	invoice i ON b.billID = i.billID\n" +
                                "WHERE\n" +
                                "	mr.meterReadingID = ?;";
                
            // Check if the bill already exists
            try (PreparedStatement checkBillStatement = connection.prepareStatement(query)) {
                checkBillStatement.setInt(1, meterReadingID);

                ResultSet checkBillResults = checkBillStatement.executeQuery();

                if (checkBillResults.next()) {
                    return true;
                }else{
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }        
    }
    public boolean checkBillingStatusByMrID(int meterReadingID) throws SQLException{
        try (Connection connection = dbConfig.getConnection()) {
            connection.setAutoCommit(false);
                String query =  "SELECT \n" +
                                "	billingStatus \n" +
                                "FROM \n" +
                                "	meterreading mr \n" +
                                "JOIN\n" +
                                "	bills b ON mr.meterReadingID = b.meterReadingID\n" +
                                "WHERE\n" +
                                "	mr.meterReadingID = ?;";
                
            // Check if the bill already exists
            try (PreparedStatement checkBillStatement = connection.prepareStatement(query)) {
                checkBillStatement.setInt(1, meterReadingID);

                ResultSet rs = checkBillStatement.executeQuery();

                if (rs.next()) {
                    if(rs.getInt("billingStatus")==2){
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return false;
    }
    
}