/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.bills;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DbConfig;

/**
 *
 * @author Merry Ann
 */
public class BillsModel {
    private DbConfig dbConfig;
    
    public BillsModel(){
        this.dbConfig = new DbConfig();
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
    public ObservableList<Bills> getBills() throws SQLException {
        ObservableList<Bills> bills = FXCollections.observableArrayList();

            String query =  "SELECT * FROM get_Bills" +
                        " ORDER BY billingDate ;";

        try (Connection connection = dbConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();

            int no = 1;
            while (rs.next()) {
                String cStatus = getBillingStatusLabel(rs.getInt("billingStatus"));
                    bills.add(new Bills(
                                no,
                                rs.getInt("cID"),
                                rs.getInt("billID"),
                                rs.getString("meterNumber"),
                                rs.getString("meterLocation"),
                                rs.getString("name"),
                                rs.getDate("billingDate").toLocalDate(),
                                rs.getDate("dueDate").toLocalDate(),
                                rs.getDouble("totalAmount"),
                                rs.getInt("waterConsumption"),
                                rs.getInt("rate"),
                                rs.getDouble("penaltyAmount"),
                                cStatus
                        ));

                no++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return bills;
    }
    public ObservableList<Bills> getYear() throws SQLException{
        ObservableList<Bills> year = FXCollections.observableArrayList();
        String query =  "SELECT \n" +
                        "	YEAR(billingDate) AS year\n" +
                        "FROM \n" +
                        "	bills\n" +
                        "GROUP BY\n" +
                        "	year \n" +
                        "ORDER BY\n" +
                        "	year;";
        
        try (Connection connection = dbConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                year.add(new Bills(rs.getInt("year")));
            }
        
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return year;
    }
    public ObservableList<Bills> filterBills(int month, int year, int status, String searchVal) throws SQLException {
        ObservableList<Bills> bills = FXCollections.observableArrayList();

        // Convert searchVal to uppercase for case-insensitive search
        searchVal = (searchVal != null) ? searchVal.toUpperCase() : null;

        String query = "SELECT * FROM get_Bills " +
            (month != 0 || year != 0 || status != 0 || (searchVal != null && !searchVal.isEmpty()) ? "WHERE " : "") +
            (month != 0 ? "MONTH(billingDate) = ? " : "") +
            (year != 0 ? (month != 0 ? "AND " : "") + "YEAR(billingDate) = ? " : "") +
            (status != 0 ? (month != 0 || year != 0 ? "AND " : "") + "billingStatus = ? " : "") +
            (searchVal != null && !searchVal.isEmpty() ? (month != 0 || year != 0 || status != 0 ? "AND " : "") +
                    "UPPER(name) LIKE ? " : "") +
            "ORDER BY name;";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            int parameterIndex = 1;
            if (month != 0) statement.setInt(parameterIndex++, month);
            if (year != 0) statement.setInt(parameterIndex++, year);
            if (status != 0) statement.setInt(parameterIndex++, status);
            if (searchVal != null && !searchVal.isEmpty()) {
                String likePattern = "%" + searchVal + "%";
                statement.setString(parameterIndex++, likePattern);
            }

            try (ResultSet rs = statement.executeQuery()) {
                int no = 1;
                while (rs.next()) {
                    String cStatus = getBillingStatusLabel(rs.getInt("billingStatus"));

                    bills.add(new Bills(
                            no,
                            rs.getInt("cID"),
                            rs.getInt("billID"),
                            rs.getString("meterNumber"),
                            rs.getString("meterLocation"),
                            rs.getString("name"),
                            rs.getDate("billingDate").toLocalDate(),
                            rs.getDate("dueDate").toLocalDate(),
                            rs.getDouble("totalAmount"),
                            rs.getInt("waterConsumption"),
                            rs.getInt("rate"),
                            rs.getDouble("penaltyAmount"),
                            cStatus
                    ));
                    no++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return bills;
    }

    public ObservableList<Bills> getConsumerBillsById(int customerId) throws SQLException {
        ObservableList<Bills> bills = FXCollections.observableArrayList();

        String query = "SELECT * FROM get_Bills " +
                       "WHERE cID = ? " +
                       "ORDER BY billingDate;";

        try (Connection connection = dbConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                int no = 1;
                while (rs.next()) {
                    String status = getBillingStatusLabel(rs.getInt("billingStatus"));

                    bills.add(new Bills(
                                no,
                                rs.getInt("cID"),
                                rs.getInt("billID"),
                                rs.getString("meterNumber"),
                                rs.getString("meterLocation"),
                                rs.getString("name"),
                                rs.getDate("billingDate").toLocalDate(),
                                rs.getDate("dueDate").toLocalDate(),
                                rs.getDouble("totalAmount"),
                                rs.getInt("waterConsumption"),
                                rs.getInt("rate"),
                                rs.getDouble("penaltyAmount"),
                                status
                        ));
                        no++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return bills;
    }
    public ObservableList<Bills> filterConsumerBillsById(int customerId, int month, int year, int status) throws SQLException {
        ObservableList<Bills> bills = FXCollections.observableArrayList();

        String query = "SELECT * FROM get_Bills " +
                       "WHERE cID = ? " +
                       (month != 0 ? "AND MONTH(billingDate) = ? " : "") +
                       (year != 0 ? "AND YEAR(billingDate) = ? " : "") +
                       (status != 0 ? "AND billingStatus = ? " : "") +
                       "ORDER BY billingDate;";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            int parameterIndex = 1;
            statement.setInt(parameterIndex++, customerId);

            if (month != 0) statement.setObject(parameterIndex++, month);
            if (year != 0) statement.setObject(parameterIndex++, year);
            if (status != 0) statement.setObject(parameterIndex++, status);

            try (ResultSet rs = statement.executeQuery()) {
                int no = 1;
                while (rs.next()) {
                    String billStatus = getBillingStatusLabel(rs.getInt("billingStatus"));

                    bills.add(new Bills(
                            no,
                            rs.getInt("cID"),
                            rs.getInt("billID"),
                            rs.getString("meterNumber"),
                            rs.getString("meterLocation"),
                            rs.getString("name"),
                            rs.getDate("billingDate").toLocalDate(),
                            rs.getDate("dueDate").toLocalDate(),
                            rs.getDouble("totalAmount"),
                            rs.getInt("waterConsumption"),
                                rs.getInt("rate"),
                                rs.getDouble("penaltyAmount"),
                            billStatus
                    ));
                    no++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return bills;
    }
    private String getBillingStatusLabel(int billingStatus) {
        switch (billingStatus) {
            case 1:
                return "Unpaid";
            case 2:
                return "Paid";
            case 3:
                return "Over Due";
            case 4:
                return "Paid withh penalty";
            default:
                return "";
        }
    }
    public Bills getConsumerBillDetails(int billID) throws SQLException {
        Bills bill = null;
        String query = "{call get_consumerbilldetails(?)}";

        try (Connection connection = dbConfig.getConnection();
             CallableStatement statement = connection.prepareCall(query)) {
            statement.setInt(1, billID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String status = getBillingStatusLabel(rs.getInt("billingStatus"));
                if (rs.getInt("billingStatus") != 4) {
                    bill = new Bills(
                            rs.getInt("cID"),
                            rs.getInt("billID"),
                            rs.getInt("meterReadingID"),
                            rs.getString("meterNumber"),
                            rs.getString("name"),
                            rs.getDate("billingDate").toLocalDate(),
                            rs.getDate("dueDate").toLocalDate(),
                            rs.getDouble("totalAmount"),
                            rs.getInt("waterConsumption"),
                            status,
                            rs.getDate("readingDate").toLocalDate(),
                            rs.getInt("previousReading"),
                            rs.getInt("currentReading")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return bill;
    }
    public Bills getSelectedConsumer(int id) throws SQLException{
        Bills bill = null;
        try (Connection connection = dbConfig.getConnection();
            Statement statement = connection.createStatement()) {
            String query =  "SELECT\n" +
                            "    c.cID,\n" +
                            "    CONCAT(c.cFName, ' ', c.cLName) AS name\n" +
                            "FROM\n" +
                            "    conscessionaries c\n"+
                            "WHERE\n" +
                            "	c.cID = '"+id+"';";
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()){    
                bill = new Bills(rs.getInt("cID"),rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return bill;
    }
    public Bills getBillsDetails(int billID) throws SQLException {
        Bills billDetails = null;
        String query =  "SELECT\n" +
                        "    CONCAT(c.cFName, ' ', c.cLName) AS name,\n" +
                        "    m.meterNumber,\n" +
                        "    b.totalAmount,\n" +
                        "    COALESCE(p.penaltyAmount, 0) AS penaltyAmount, -- Use COALESCE to handle cases where there is no penalty\n" +
                        "    b.billingStatus\n" +
                        "FROM\n" +
                        "    conscessionaries c\n" +
                        "JOIN\n" +
                        "    consumermeternumber cm ON c.cID = cm.cID\n" +
                        "JOIN\n" +
                        "    meter m ON cm.meterID = m.meterID\n" +
                        "JOIN\n" +
                        "    meterreading mr ON cm.consumerMeterNumberID = mr.consumerMeterNumberID\n" +
                        "JOIN\n" +
                        "    bills b ON mr.meterReadingID = b.meterReadingID\n" +
                        "LEFT JOIN\n" +
                        "    penalty p ON b.billID = p.billID " +
                       "WHERE b.billID = ?";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, billID);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                billDetails = new Bills(
                    rs.getString("name"),
                    rs.getString("meterNumber"),
                    rs.getDouble("totalAmount"),
                    rs.getDouble("penaltyAmount"),
                    rs.getInt("billingStatus")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }

        return billDetails;
    }
    public void insertPayment(int billID, Double paymentAmount) throws SQLException {
        String checkBillStatus = "SELECT billingStatus FROM bills WHERE billID = ?;";
        int status;

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkBillStatus)) {

            checkStatement.setInt(1, billID);

            try (ResultSet resultSet = checkStatement.executeQuery()) {
                status = resultSet.next() ? resultSet.getInt("billingStatus") : 0;
            }

            status = (status == 1) ? 2 : (status == 3) ? 4 : 0;

            String insertPayment = "INSERT INTO payment (billID, paymentDate, paymentAmount, paymentStatus, paymentMethod) " +
                                   "VALUES (?, CURRENT_DATE, ?, ?, 1);";

            try (PreparedStatement paymentStatement = connection.prepareStatement(insertPayment)) {
                paymentStatement.setInt(1, billID);
                paymentStatement.setDouble(2, paymentAmount);
                paymentStatement.setInt(3, (status == 4) ? 2 : 1);

                int affectedRows = paymentStatement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Payment record inserted successfully.");
                } else {
                    System.out.println("Failed to insert payment record.");
                }
                
                String updatePayment = "UPDATE bills SET billingStatus = ? WHERE billID = ?;";

                try (PreparedStatement updateBillsStatement = connection.prepareStatement(updatePayment)) {
                    updateBillsStatement.setInt(1, status);
                    updateBillsStatement.setInt(2, billID);

                    affectedRows = updateBillsStatement.executeUpdate();

                    if (affectedRows > 0) {
                        System.out.println("Bills status updated successfully.");
                    } else {
                        System.out.println("Bills payments were not updated.");
                    }
                }
                if(status == 4){
                    String updatePenalty = "UPDATE penalty SET penaltyStatus = ? WHERE billID = ?;";

                    try (PreparedStatement updatePenaltyStatement = connection.prepareStatement(updatePenalty)) {
                        updatePenaltyStatement.setInt(1, 2);
                        updatePenaltyStatement.setInt(2, billID);

                        affectedRows = updatePenaltyStatement.executeUpdate();

                        if (affectedRows > 0) {
                            System.out.println("Penalty status updated successfully.");
                        } else {
                            System.out.println("Penalty payments were not updated.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately, e.g., log the error
        }
    }
    public boolean checkPaymentStatus(int billID) throws SQLException {
        boolean checker = false;
        try (Connection connection = dbConfig.getConnection()) {
            connection.setAutoCommit(false);
            String query =
                    "SELECT " +
                    "   b.billingStatus " +
                    "FROM " +
                    "   bills b " +
                    "WHERE " +
                    "   b.billID = ?";

            // Check if the bill already exists
            try (PreparedStatement checkBillStatement = connection.prepareStatement(query)) {
                checkBillStatement.setInt(1, billID);

                ResultSet rs = checkBillStatement.executeQuery();

                if (rs.next()) {
                    checker = rs.getInt("billingStatus") == 2 || rs.getInt("billingStatus") == 4;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return checker;
    }
    public void checkBillingDueDate() throws SQLException {

        String updatePayment =  "UPDATE\n" +
                                "	bills b \n" +
                                "SET\n" +
                                "	b.billingStatus = 3\n" +
                                "WHERE\n" +
                                "	b.billingStatus = 1 AND\n" +
                                "	b.dueDate < CURRENT_DATE; ";

        try (Connection connection = dbConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(updatePayment)) {
            

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Payment status updated successfully.");
            } else {
                System.out.println("No payments were updated.");
            }
        }
     }
    public Bills viewPenaltyDetails(int billID) throws SQLException{
         Bills bill = null;
        String query = "SELECT * FROM get_penaltyDetails " +
                       "WHERE billID = ?;";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, billID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                    bill = new Bills(
                            rs.getInt("billID"),
                            rs.getString("name"),
                            rs.getString("meterNumber"),
                            rs.getString("meterLocation"),
                            rs.getDate("billingDate").toLocalDate(),
                            rs.getDate("dueDate").toLocalDate(),
                            rs.getDouble("totalAmount"),
                            rs.getDate("penaltyDate").toLocalDate(),
                            rs.getString("penaltyType"),
                            rs.getString("penaltyDescription"),
                            rs.getDouble("penaltyAmount")
                    );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return bill;
    }
    public int insertInvoice(int billID) throws SQLException {
        int invoiceID = 0;
        String insertInvoice = "INSERT INTO invoice(billID, invoiceDate, invoiceStatus)\n" +
                "VALUES(?, CURRENT_DATE, 1);";

        try (Connection connection = dbConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(insertInvoice, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, billID);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    invoiceID = resultSet.getInt(1);
                    System.out.println("Invoice record inserted successfully. InvoiceID: " + invoiceID);
                } else {
                    // Handle no generated key scenario
                    throw new SQLException("No invoice ID generated");
                }
            } else {
                System.out.println("Failed to insert invoice record.");
            }

        }

        return invoiceID;
    }
    public boolean checkInvoice(int billID) throws SQLException{
        try (Connection connection = dbConfig.getConnection()) {
            connection.setAutoCommit(false);
                String query =  "SELECT\n" +
                                "    b.billingStatus,\n" +
                                "    i.invoiceDate\n" +
                                "FROM\n" +
                                "	meterreading mr \n" +
                                "JOIN\n" +
                                "	bills b ON mr.meterReadingID = b.meterReadingID\n" +
                                "JOIN\n" +
                                "	invoice i ON b.billID = i.billID\n" +
                                "WHERE\n" +
                                "	b.billID = ?;";
                
            // Check if the bill already exists
            try (PreparedStatement checkBillStatement = connection.prepareStatement(query)) {
                checkBillStatement.setInt(1, billID);

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
    
}
