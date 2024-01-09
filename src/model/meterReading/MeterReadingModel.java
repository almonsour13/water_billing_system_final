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
        String query =  "SELECT\n" +
                            "	cm.consumerMeterNumberID,\n" +
                            "	mr.meterReadingID,\n" +
                            "    m.meterNumber,\n" +
                            "    cm.meterLocation,\n" +
                            "    CONCAT(c.cFName,' ',c.cLName) AS name,\n" +
                            "    mr.readingDate,\n" +
                            "    mr.previousReading,\n" +
                            "    mr.currentReading,\n" +
                            "    mr.meterReadingStatus,\n" +
                            "    COALESCE(cd.date, NULL) AS date,\n" +
                            "    COALESCE(CONCAT(a.aFName,' ',a.aLName), '') AS collectorName,\n" +
                            "    a.aID\n" +
                            "FROM\n" +
                            "    conscessionaries c\n" +
                            "JOIN\n" +
                            "    consumermeternumber cm ON c.cID = cm.cID\n" +
                            "JOIN\n" +
                            "    meter m ON cm.meterID = m.meterID\n" +
                            "JOIN\n" +
                            "    meterreading mr ON cm.consumerMeterNumberID = mr.consumerMeterNumberID\n" +
                            "LEFT JOIN\n" +
                            "	curreadingdateadded cd ON mr.meterReadingID = cd.meterReadingID\n" +
                            "LEFT JOIN\n" +
                            "    accounts a ON cd.aID = a.aID ORDER BY c.cFName;";

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
                        rs.getInt("consumerMeterNumberID"),
                        rs.getInt("meterReadingID"),
                        rs.getString("meterNumber"),
                        rs.getString("meterLocation"),
                        rs.getString("name"),
                        rs.getDate("readingDate").toLocalDate(),
                        rs.getInt("previousReading"),
                        rs.getInt("currentReading"),
                        date,
                       name
                ));
                no++;
            }
        }
        return MeterReading;
    } 
    public ObservableList<MeterReading> filterMeterReading(int month, int year) throws SQLException {
        ObservableList<MeterReading> meterReadings = FXCollections.observableArrayList();
           String query =   "SELECT\n" +
                            "	cm.consumerMeterNumberID,\n" +
                            "	mr.meterReadingID,\n" +
                            "    m.meterNumber,\n" +
                            "    cm.meterLocation,\n" +
                            "    CONCAT(c.cFName,' ',c.cLName) AS name,\n" +
                            "    mr.readingDate,\n" +
                            "    mr.previousReading,\n" +
                            "    mr.currentReading,\n" +
                            "    mr.meterReadingStatus,\n" +
                            "    COALESCE(cd.date, NULL) AS date,\n" +
                            "    COALESCE(CONCAT(a.aFName,' ',a.aLName), '') AS collectorName,\n" +
                            "    a.aID\n" +
                            "FROM\n" +
                            "    conscessionaries c\n" +
                            "JOIN\n" +
                            "    consumermeternumber cm ON c.cID = cm.cID\n" +
                            "JOIN\n" +
                            "    meter m ON cm.meterID = m.meterID\n" +
                            "JOIN\n" +
                            "    meterreading mr ON cm.consumerMeterNumberID = mr.consumerMeterNumberID\n" +
                            "LEFT JOIN\n" +
                            "	curreadingdateadded cd ON mr.meterReadingID = cd.meterReadingID\n" +
                            "LEFT JOIN\n" +
                            "    accounts a ON cd.aID = a.aID WHERE MONTH(readingDate) = ? AND YEAR(readingDate) = ? ORDER BY c.cFName;";


        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, month);
            statement.setInt(2, year);

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
                        rs.getInt("consumerMeterNumberID"),
                        rs.getInt("meterReadingID"),
                        rs.getString("meterNumber"),
                        rs.getString("meterLocation"),
                        rs.getString("name"),
                        rs.getDate("readingDate").toLocalDate(),
                        rs.getInt("previousReading"),
                        rs.getInt("currentReading"),
                        date,
                       name
                ));
                no++;
            }
        }
        return meterReadings;
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
"    conscessionaries c\n" +
"JOIN\n" +
"    consumermeternumber cm ON c.cID = cm.cID\n" +
"JOIN\n" +
"    meter m ON cm.meterID = m.meterID\n" +
"LEFT JOIN\n" +
"    meterreading mr ON cm.consumerMeterNumberID = mr.consumerMeterNumberID\n" +
"WHERE\n" +
"    c.cStatus = 1 AND cm.cMStatus = 1 AND m.meterStatus = 1\n" +
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
    public void addBillToConsumer(int meterID, int meterReadingID, LocalDate billDate, LocalDate dueDate,
                             int consumption, int rate, double billAmount) throws SQLException {
    try (Connection connection = dbConfig.getConnection()) {
        connection.setAutoCommit(false);

        String query = "SELECT c.cID FROM conscessionaries c " +
                       "JOIN consumermeternumber cm ON c.cID = cm.cID " +
                       "JOIN meter m ON cm.meterID = m.meterID " +
                       "WHERE cm.cMStatus = 1 AND cm.consumerMeterNumberID = ?;";
        int cID = 0;
        try (PreparedStatement checkBillStatement = connection.prepareStatement(query)) {
            checkBillStatement.setInt(1, meterID);
            ResultSet rs = checkBillStatement.executeQuery();

            if (rs.next()) {
                cID = rs.getInt("cID");
            }
        }

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
            billStatement.setDouble(parameterIndex++, billAmount);
            billStatement.setInt(parameterIndex++, 1);
            billStatement.executeUpdate();
        }

        connection.commit();
    } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    }
}

    public boolean checkBillExist(int meterID, int meterReadingID) throws SQLException{
        try (Connection connection = dbConfig.getConnection()) {
            connection.setAutoCommit(false);

           
            try (PreparedStatement checkBillStatement = connection.prepareStatement("SELECT * FROM bills WHERE meterID = ? AND meterReadingID = ?")) {
                checkBillStatement.setInt(1, meterID);
                checkBillStatement.setInt(2, meterReadingID);
               // checkBillStatement.setDate(3, java.sql.Date.valueOf(billDate));

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
    public int getMeterReading(int meterReadingID) throws SQLException{
        try (Connection connection = dbConfig.getConnection()) {
            connection.setAutoCommit(false);

            // Check if the bill already exists
            try (PreparedStatement checkBillStatement = connection.prepareStatement("SELECT currentReading FROM meterreading WHERE meterReadingID = ?")) {
                checkBillStatement.setInt(1, meterReadingID);
               // checkBillStatement.setDate(3, java.sql.Date.valueOf(billDate));

                ResultSet checkBillResults = checkBillStatement.executeQuery();

                if (checkBillResults.next()) {
                    if(checkBillResults.getInt("currentReading")!=0){
                        return checkBillResults.getInt("currentReading");
                    }else{
                        return 0;
                    }
                }else{
                    return 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }    
    }
    public void insertNewValue(int meterReadingID, int logAccount, int orginalValue, int newValue, String reason) {
        String insertCurrentReadingDateQuery = "INSERT INTO editrequest(meterReadingID, aID, editRequestDate, orignalValue, newValue, reason, editRequestStatus) "
                + "VALUES (?, ?, CURRENT_DATE, ?, ?, ?, 1)";

        try (Connection connection = dbConfig.getConnection();
            PreparedStatement insertStatement = connection.prepareStatement(insertCurrentReadingDateQuery)) {
            insertStatement.setInt(1, meterReadingID);
            insertStatement.setInt(2, logAccount);
            insertStatement.setInt(3, orginalValue);
            insertStatement.setInt(4, newValue);
            insertStatement.setString(5, reason);

            int insertAffectedRows = insertStatement.executeUpdate();
             if (insertAffectedRows > 0) {
                System.out.println("request sent successfully.");
            } else {
                System.out.println("Failed to update current reading.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately, e.g., log the error
        }
    }
    public MeterReading viewRequest(int meterReadingID) throws SQLException{
        MeterReading request = null;
        try (Connection connection = dbConfig.getConnection()) {
            connection.setAutoCommit(false);
            String query =  "SELECT\n" +
                            "	 ed.editRequestID,\n" +
                            "    ed.meterReadingID,\n" +
                            "    CONCAT(a.aFName,' ',a.aLName) AS name,\n" +
                            "    ed.editRequestDate,\n" +
                            "    ed.orignalValue,\n" +
                            "    ed.newValue,\n" +
                            "    ed.reason,\n" +
                            "    ed.editRequestStatus as status\n" +
                            "FROM\n" +
                            "	accounts a \n" +
                            "JOIN\n" +
                            "	editrequest ed ON a.aID = ed.aID\n" +
                            "WHERE\n" +
                            "	ed.meterReadingID = ?";
            // Check if the bill already exists
            try (PreparedStatement checkBillStatement = connection.prepareStatement(query)) {
                checkBillStatement.setInt(1, meterReadingID);
                ResultSet rs = checkBillStatement.executeQuery();

                if (rs.next()) {
                    String status = "";
                    int stat = rs.getInt("status");
                    if(stat == 1){
                        status = "Pending";
                    }else if(stat == 2){
                        status = "Approved";
                    }else if(stat == 3){
                        status = "Rejected";
                    }
                    request = new MeterReading(
                        rs.getInt("editRequestID"),
                        rs.getInt("meterReadingID"),
                        rs.getString("name"),
                        rs.getDate("editRequestDate").toLocalDate(),
                        rs.getInt("orignalValue"),
                        rs.getInt("newValue"),
                        rs.getString("reason"),
                                status
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }         
        return request;
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
    public void approvedRequest(int meterReadingID) {
        String query = "UPDATE meterreading\n" +
                       "SET currentReading = (\n" +
                       "    SELECT newValue\n" +
                       "    FROM editrequest\n" +
                       "    WHERE meterReadingID = ?\n" +
                       ")\n" +
                       "WHERE meterReadingID = ?;";

        String update = "UPDATE editrequest\n" +
                        "SET editRequestStatus = ?\n" +
                        "WHERE meterReadingID = ?;";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(query);
             PreparedStatement updateStatement2 = connection.prepareStatement(update)) {

            updateStatement.setInt(1, meterReadingID);
            updateStatement.setInt(2, meterReadingID);

            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Current Reading updated successfully for meterReadingID: " + meterReadingID);
            } else {
                System.out.println("No rows affected. Current Reading not updated for meterReadingID: " + meterReadingID);
            }

            // Assuming you want to update the editRequestStatus as well
            updateStatement2.setInt(1, 2);  // Assuming 1 is the status for approval
            updateStatement2.setInt(2, meterReadingID);

            int rowsAffected2 = updateStatement2.executeUpdate();

            if (rowsAffected2 > 0) {
                System.out.println("Edit Request Status updated successfully for meterReadingID: " + meterReadingID);
            } else {
                System.out.println("No rows affected. Edit Request Status not updated for meterReadingID: " + meterReadingID);
            }

        } catch (SQLException e) {
            System.err.println("Error updating current reading for meterReadingID: " + meterReadingID);
            e.printStackTrace();
            // Handle the exception appropriately, e.g., log the error
        }
    }
    public void rejectRequest(int meterReadingID){
        String update = "UPDATE editrequest\n" +
                        "SET editRequestStatus = ?\n" +
                        "WHERE meterReadingID = ?;";
        try (Connection connection = dbConfig.getConnection();
            PreparedStatement updateStatement = connection.prepareStatement(update)){
            updateStatement.setInt(1, 3);
            updateStatement.setInt(2, meterReadingID);

            int rowsAffected2 = updateStatement.executeUpdate();

            if (rowsAffected2 > 0) {
                System.out.println("Reject Request  successfully for meterReadingID: " + meterReadingID);
            } else {
                System.out.println("No rows affected. Reject Request  not updated for meterReadingID: " + meterReadingID);
            }

        } catch (SQLException e) {
            System.err.println("Error updating current reading for meterReadingID: " + meterReadingID);
            e.printStackTrace();
            // Handle the exception appropriately, e.g., log the error
        }
    }
}