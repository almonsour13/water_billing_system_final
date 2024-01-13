/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.consumer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import model.DbConfig;

/**
 *
 * @author Merry Ann
 */
public class ConsumerModel {

    /**
     *
     */
    public DbConfig dbConfig = new DbConfig();
    
    public ObservableList<Consumer> getConsumers() throws SQLException {
        ObservableList<Consumer> consumers = FXCollections.observableArrayList();
        try (Connection connection = dbConfig.getConnection();
             Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM get_consumers ORDER BY cFName;";
            ResultSet rs = statement.executeQuery(query);
            int no = 1;
            while (rs.next()) {
                 String status = getStatusLabel(rs.getInt("cStatus"));
                if(rs.getInt("cStatus")!= 4){
                    consumers.add(
                     new Consumer(no,
                            rs.getInt("cID"), rs.getString("cFName"), 
                            rs.getString("cMName"), rs.getString("cLName"), 
                            rs.getString("cSuffix"), rs.getString("cContactNo"),
                            rs.getString("cEmailAd"), 
                             rs.getString("purok"), 
                            rs.getString("postalCode"),
                            rs.getDate("dateAdded").toLocalDate(),status)
                    );
                    
                    no++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return consumers;
    }
    public ObservableList<Consumer> filterConsumer(String purok, int cStatus, int month, int year, String searchVal) throws SQLException {
        ObservableList<Consumer> consumers = FXCollections.observableArrayList();

        // Convert searchVal to uppercase for case-insensitive search
        searchVal = (searchVal != null) ? searchVal.toUpperCase() : null;

        String query = "SELECT * FROM get_consumers " +
                (month != 0 || year != 0 || cStatus != 0 || !purok.equals("All") || (searchVal != null && !searchVal.isEmpty()) ? "WHERE " : "") +
                (!purok.equals("All") ? "purok = ? " : "") +
                (month != 0 ? (purok.equals("All") ? "" : "AND ") + "MONTH(dateAdded) = ? " : "") +
                (year != 0 ? ((purok.equals("All") && month == 0) ? "" : "AND ") + "YEAR(dateAdded) = ? " : "") +
                (cStatus != 0 ? ((purok.equals("All") && month == 0 && year == 0) ? "" : "AND ") + "cStatus = ? " : "") +
                (searchVal != null && !searchVal.isEmpty() ? ((purok.equals("All") && month == 0 && year == 0 && cStatus == 0) ? "" : "AND ") +
                        "(UPPER(cFName) LIKE ? OR UPPER(cMName) LIKE ? OR UPPER(cLName) LIKE ?) " : "") +
                "ORDER BY cFName;";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            int parameterIndex = 1;
            if (!purok.equals("All")) preparedStatement.setString(parameterIndex++, purok);
            if (month != 0) preparedStatement.setInt(parameterIndex++, month);
            if (year != 0) preparedStatement.setInt(parameterIndex++, year);
            if (cStatus != 0) preparedStatement.setInt(parameterIndex++, cStatus);
            if (searchVal != null && !searchVal.isEmpty()) {
                String likePattern = "%" + searchVal + "%";
                preparedStatement.setString(parameterIndex++, likePattern);
                preparedStatement.setString(parameterIndex++, likePattern);
                preparedStatement.setString(parameterIndex++, likePattern);
            }

            ResultSet rs = preparedStatement.executeQuery();

            int no = 1;
            while (rs.next()) {
                String status = getStatusLabel(rs.getInt("cStatus"));
                if (rs.getInt("cStatus") != 4) {
                    consumers.add(
                            new Consumer(no,
                                    rs.getInt("cID"), rs.getString("cFName"),
                                    rs.getString("cMName"), rs.getString("cLName"),
                                    rs.getString("cSuffix"), rs.getString("cContactNo"),
                                    rs.getString("cEmailAd"),
                                    rs.getString("purok"),
                                    rs.getString("postalCode"),
                                    rs.getDate("dateAdded").toLocalDate(), status)
                    );

                    no++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return consumers;
    }
    private String getStatusLabel(int cStatus) {
        switch (cStatus) {
            case 1:
                return "Active";
            case 2:
                return "Inactive";
            default:
                return "";
        }
    }
    public ObservableList<Consumer> getPuroks() throws SQLException{
        ObservableList<Consumer> puroks = FXCollections.observableArrayList();
        try (Connection connection = dbConfig.getConnection();
            Statement statement = connection.createStatement()) {
            String query =  "SELECT \n" +
                            "	a.purok\n" +
                            "FROM\n" +
                            "	address a\n" +
                            "GROUP BY\n" +
                            "	a.purok\n" +
                            "ORDER BY\n" +
                            "	a.purok;";
            ResultSet rs = statement.executeQuery(query);
            
            while (rs.next()) {
                    puroks.add(
                     new Consumer(rs.getString("purok"))
                    );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return puroks;
    }
    public ObservableList<Consumer> getYear() throws SQLException{
        ObservableList<Consumer> consumers = FXCollections.observableArrayList();
        try (Connection connection = dbConfig.getConnection();
            Statement statement = connection.createStatement()) {
            String query =  "SELECT\n" +
                            "	YEAR(c.dateAdded) AS year	\n" +
                            "FROM\n" +
                            "	conscessionaries c\n" +
                            "GROUP BY\n" +
                            "	year \n" +
                            "ORDER BY\n" +
                            "	year;;";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                    consumers.add(
                     new Consumer(
                             rs.getInt("year"))
                    );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return consumers;
    } 
    public boolean checkAddConsumer(String fName, String mName, String lName, String suffix) throws SQLException {
        String query = "SELECT * FROM get_consumers WHERE cFName = ? " +
                       (!mName.isEmpty() ? "AND cMName = ? " : "") +
                       "AND cLName = ? " +
                       (!suffix.isEmpty() ? "AND cSuffix = ?" : "");

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            int parameterIndex = 1;
            statement.setString(parameterIndex++, fName);
            if (!mName.isEmpty()) statement.setString(parameterIndex++, mName);
            statement.setString(parameterIndex++, lName);
            if (!suffix.isEmpty()) statement.setString(parameterIndex++, suffix);

            ResultSet rs = statement.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; 
        }
    }
    public void insertAddConsumer(
        String fName, String mName, String lName,String suffix, String contactNo, String emailAd,String purok, String postalCode,
        int status
    ) throws SQLException{ 
        try (Connection connection = dbConfig.getConnection()) {
            connection.setAutoCommit(false);
            
            String insertConscessionariesSQL = "INSERT INTO conscessionaries (aID, cFName, cMName, cLName, cSuffix, cContactNo, cEmailAd, dateAdded, cStatus) VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, ?)";
            try (PreparedStatement conscessionariesStatement = connection.prepareStatement(insertConscessionariesSQL, Statement.RETURN_GENERATED_KEYS)) {
                conscessionariesStatement.setInt(1, 0);
                conscessionariesStatement.setString(2, fName);
                conscessionariesStatement.setString(3, mName);
                conscessionariesStatement.setString(4, lName);
                conscessionariesStatement.setString(5, suffix);
                conscessionariesStatement.setString(6, contactNo);
                conscessionariesStatement.setString(7, emailAd);
                conscessionariesStatement.setInt(8, status);    

                int rowsAffected = conscessionariesStatement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Inserting consumer failed, no rows affected.");
                }
                
                // Retrieve the generated cID
                ResultSet generatedKeys = conscessionariesStatement.getGeneratedKeys();
                int cID = 0;
                if (generatedKeys.next()) {
                    cID = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Inserting consumer failed, no generated key obtained.");
                }
                // Insert data into the address table
                String insertAddressSQL = "INSERT INTO address (cID, purok, postalCode) VALUES (?, ?, ?)";
                try (PreparedStatement addressStatement = connection.prepareStatement(insertAddressSQL)) {
                    addressStatement.setInt(1, cID);
                    addressStatement.setString(2, purok);
                    addressStatement.setString(3, postalCode);

                    addressStatement.executeUpdate();
                }
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public Consumer getSelectedConsumerById(int id) throws SQLException {
        Consumer consumer = null;
        String query = "SELECT * FROM get_consumers WHERE cID = '"+id+"' ";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            //statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String status = "";

                if (rs.getInt("cStatus") == 1) {
                    status = "Active";
                } else {
                    status = "Inactive";
                }

                consumer = new Consumer(1,
                    rs.getInt("cID"), rs.getString("cFName"),
                                    rs.getString("cMName"), rs.getString("cLName"),
                                    rs.getString("cSuffix"), rs.getString("cContactNo"),
                                    rs.getString("cEmailAd"),
                                    rs.getString("purok"),
                                    rs.getString("postalCode"),
                                    rs.getDate("dateAdded").toLocalDate(), status
                 );
            }
        }
        return consumer;
        
    }
    public void updateConsumerStatus(int cID, int status) throws SQLException {
        try (Connection connection = dbConfig.getConnection()) {
            // Update consumer status
            String updateConsumerSQL = "UPDATE conscessionaries c SET c.cStatus = ? WHERE c.cID = ?";

            try (PreparedStatement updateConsumerStatement = connection.prepareStatement(updateConsumerSQL)) {
                updateConsumerStatement.setInt(1, status);
                updateConsumerStatement.setInt(2, cID);

                int rowsAffected = updateConsumerStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // The update on conscessionaries was successful
                    // Now check if the consumer has meters with cMStatus = 1
                    String checkMeterSQL = "SELECT cm.meterID FROM consumermeternumber cm WHERE cm.cID = ? AND cm.cMStatus = 1";

                    try (PreparedStatement checkMeterStatement = connection.prepareStatement(checkMeterSQL)) {
                        checkMeterStatement.setInt(1, cID);
                        ResultSet resultSet = checkMeterStatement.executeQuery();

                        while (resultSet.next()) {
                            // Update each meter status
                            int meterID = resultSet.getInt("meterID");

                            String updateCMStatus = "UPDATE consumermeternumber cm SET cMStatus = 2 WHERE cMStatus = 1 AND cm.cID = ? AND cm.meterID = ?";
                            try (PreparedStatement updateCMStatusStatement = connection.prepareStatement(updateCMStatus)) {
                                updateCMStatusStatement.setInt(1, cID);
                                updateCMStatusStatement.setInt(2, meterID);
                                updateCMStatusStatement.executeUpdate();
                            }

                            // Update meter status
                            String updateMeterStatus = "UPDATE meter SET meterStatus = 2 WHERE meterID = ?";
                            try (PreparedStatement updateMeterStatement = connection.prepareStatement(updateMeterStatus)) {
                                updateMeterStatement.setInt(1, meterID);
                                updateMeterStatement.executeUpdate();
                            }
                        }
                    }
                } else {
                    // No rows were updated in conscessionaries, handle accordingly
                    System.out.println("No rows were updated in conscessionaries");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void updateConsumer(int id,
        String fName, String mName, String lName, 
        String suffix, String contactNo, String emailAd, String purok, String postalCode, int status
    ) throws SQLException {
        try (Connection connection = dbConfig.getConnection()) {
            
            String updateConsumerSQL = "UPDATE conscessionaries c SET "
                    + "c.cFName = ?, "
                    + "c.cMName = ?, "
                    + "c.cLName = ?, "
                    + "c.cSuffix = ?, "
                    + "c.cContactNo = ?, "
                    + "c.cEmailAd = ?, "
                    + "c.cStatus = ? "
                    + "WHERE c.cID = ?";
            
            try (PreparedStatement updateStatement = connection.prepareStatement(updateConsumerSQL)) {
                updateStatement.setString(1, fName);
                updateStatement.setString(2, mName);
                updateStatement.setString(3, lName);
                updateStatement.setString(4, suffix);
                updateStatement.setString(5, contactNo);
                updateStatement.setString(6, emailAd);
                updateStatement.setInt(7, status);
                updateStatement.setInt(8, id);
                updateStatement.executeUpdate();
            }
            
            String updateConsumerAddressSQL = "UPDATE address a SET "
                    + "a.purok = ?, "
                    + "a.postalCode = ? "
                    + "WHERE a.cID = ?";
            
            try (PreparedStatement updateAddressStatement = connection.prepareStatement(updateConsumerAddressSQL)) {
                updateAddressStatement.setString(1, purok);
                updateAddressStatement.setString(2, postalCode);
                updateAddressStatement.setInt(3, id);
                updateAddressStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void archiveConsumer(int id) throws SQLException {
        String query = "UPDATE conscessionaries SET cStatus = ? WHERE cID = ? AND cStatus != ?";
        try (Connection connection = dbConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, 4); 
            preparedStatement.setInt(2, id); 
            preparedStatement.setInt(3, 1); 

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Consumer move to archives.");
            }else{
                System.out.print("Updating consumer status failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public ObservableList<Consumer> getConsumerMeterNo(int id) throws SQLException {
        ObservableList<Consumer> meterNoList = FXCollections.observableArrayList();

        String query = "SELECT * FROM get_consumermeternumber " +
                "WHERE cID = ?;";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String status= getStatus(rs.getInt("cMStatus"));
                
                Consumer consumer = new Consumer(
                        rs.getInt("meterID"),
                        rs.getString("meterNumber"),
                        rs.getString("meterLocation"),
                        rs.getDate("installationDate").toLocalDate(),status
                );

                meterNoList.add(consumer);
            }
        }
        return meterNoList;
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
    public Consumer getConsumerAddress(int id) throws SQLException{
        Consumer Consumer = null;
        String query =  "SELECT \n" +
                        "    CONCAT(c.cFName,' ',c.cLName) AS name,\n" +
                        "    a.purok,\n" +
                        "    a.postalCode\n" +
                        "FROM \n" +
                        "	conscessionaries c\n" +
                        "JOIN \n" +
                        "	address a ON c.cID = a.cID\n" +
                        "WHERE c.cID = '"+id+"';";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            //statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {

                Consumer = new Consumer(
                        rs.getString("name"),
                        rs.getString("purok"),
                        rs.getString("postalCode")
                 );
            }
        }
        return Consumer;
    }
    public int checkAddMeterNo(String meterNumber ) throws SQLException {
        String query =  "SELECT\n" +
                        "    mchStatus,\n" +
                        "    meterStatus\n" +
                        "FROM get_meterNumbers\n" +
                        "WHERE \n" +
                        "	meterNumber = ?\n" +
                        "ORDER BY\n" +
                        "	date DESC LIMIT 1;";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            int parameterIndex = 1;
            statement.setString(parameterIndex++, meterNumber);
            
            ResultSet rs = statement.executeQuery();
            
            if(rs.next()){
                int meterStatus = rs.getInt("meterStatus");
                int cMStatus = rs.getInt("mchStatus");
                
                if(cMStatus == 1 && meterStatus == 1){//exist and active
                    return 1;
                }else if(cMStatus == 2  && meterStatus == 1) {// exist but its active and connection status is disconnected
                    return 2;
                }else if(meterStatus == 2) {//broken meter number 
                    return 4;
                }
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; 
        }
        return 0;
    }
    public void insertMeterNo(int cID, String meterNo, String meterLocation, LocalDate installationDate) throws SQLException {
        try (Connection connection = dbConfig.getConnection()) {
            connection.setAutoCommit(false);

            String insertMeterNoSQL = "INSERT INTO meter (meterNumber, meterStatus) VALUES (?, ?);";
            try (PreparedStatement meterNoStatement = connection.prepareStatement(insertMeterNoSQL, Statement.RETURN_GENERATED_KEYS)) {
                meterNoStatement.setString(1, meterNo);
                meterNoStatement.setInt(2, 1);

                int affectedRows = meterNoStatement.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = meterNoStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int meterID = generatedKeys.getInt(1);
                            String addRelationshipQuery = "INSERT INTO consumermeternumber (cID, meterID, meterLocation) VALUES (?, ?, ?);";

                            try (PreparedStatement addRelationshipStatement = connection.prepareStatement(addRelationshipQuery, Statement.RETURN_GENERATED_KEYS)) {
                                addRelationshipStatement.setInt(1, cID);
                                addRelationshipStatement.setInt(2, meterID);
                                addRelationshipStatement.setString(3, meterLocation);

                                int cmnInserted = addRelationshipStatement.executeUpdate();

                                if (cmnInserted > 0) {
                                    try (ResultSet generatedCmID = addRelationshipStatement.getGeneratedKeys()) {
                                        if (generatedCmID.next()) {
                                            int cmID = generatedCmID.getInt(1);
                                            String insertMCH = "INSERT INTO meterconnectionhistory (cmID, date, mchStatus) VALUES (?, CURRENT_DATE, 1);";

                                            try (PreparedStatement mchStatement = connection.prepareStatement(insertMCH)) {
                                                mchStatement.setInt(1, cmID);

                                                int mchInserted = mchStatement.executeUpdate();

                                                if (mchInserted > 0) {
                                                    System.out.println("Meter number added successfully.");
                                                } else {
                                                    System.out.println("Failed to add meter connection history.");
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    System.out.println("Failed to add consumer meter number relationship.");
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Failed to add meter number.");
                }
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                throw e;
            }

            try {
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }
    public void updateMeterNoStatus(int id, int status) throws SQLException {
        try (Connection connection = dbConfig.getConnection()) {
            String updateConsumerSQL = "UPDATE meter SET meterStatus = ? WHERE meterID = ?";

            try (PreparedStatement updateStatement = connection.prepareStatement(updateConsumerSQL)) {
                updateStatement.setInt(1, status);
                updateStatement.setInt(2, id);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public boolean proceedAdding(int cID, String meterNo, String meterLocation, LocalDate installationDate) throws SQLException {
        String getLatestMeterQuery = "SELECT cmID, meterID FROM get_meternumbers WHERE meterNumber = ? ORDER BY date DESC LIMIT 1;";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement getLatestMeterStatement = connection.prepareStatement(getLatestMeterQuery)) {

            getLatestMeterStatement.setString(1, meterNo);
            ResultSet rs = getLatestMeterStatement.executeQuery();

            int cmID = 0;
            int meterID = 0;

            if (rs.next()) {
                cmID = rs.getInt("cmID");
                meterID = rs.getInt("meterID");
            }

            String insertMchStatusSQL = "INSERT INTO meterconnectionhistory (cmID, date, mchStatus) VALUES (?, CURRENT_DATE, 3)";
            try (PreparedStatement insertMchStatusStatement = connection.prepareStatement(insertMchStatusSQL)) {
                insertMchStatusStatement.setInt(1, cmID);
                int insertMchStatusInsert = insertMchStatusStatement.executeUpdate();
                if (insertMchStatusInsert > 0) {
                    System.out.println("Meter connection history status updated successfully.");
                }
            }

            String insertCMNSql = "INSERT INTO consumermeternumber(cID, meterID, meterLocation) VALUES (?, ?, ?);";

            try {
                connection.setAutoCommit(false);

                try (PreparedStatement insertStatement = connection.prepareStatement(insertCMNSql, Statement.RETURN_GENERATED_KEYS)) {
                    insertStatement.setInt(1, cID);
                    insertStatement.setInt(2, meterID);
                    insertStatement.setString(3, meterLocation);

                    int rowsAffected = insertStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        try (ResultSet newCMN = insertStatement.getGeneratedKeys()) {
                            if (newCMN.next()) {
                                int newCMNID = newCMN.getInt(1);
                                String insertNewMCHStatusSQL = "INSERT INTO meterconnectionhistory (cmID, date, mchStatus) VALUES (?, CURRENT_DATE, 1)";
                                try (PreparedStatement insertNewMchStatusStatement = connection.prepareStatement(insertNewMCHStatusSQL)) {
                                    insertNewMchStatusStatement.setInt(1, newCMNID);
                                    int insertNewMchStatusInsert = insertNewMchStatusStatement.executeUpdate();
                                    if (insertNewMchStatusInsert > 0) {
                                        System.out.println("New meter connection history status updated successfully.");
                                    }
                                }
                                System.out.println("Meter number transferred successfully.");
                                connection.commit();
                                return true;
                            }
                        }
                    } else {
                        System.out.println("Failed to update consumer meter status.");
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return false;
    }

}
