/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.payment;

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
public class PaymentModel {
    private final DbConfig dbConfig;
    
    public PaymentModel(){
        this.dbConfig = new DbConfig();
    }
    public ObservableList<Payment> getPayments() throws SQLException {
        ObservableList<Payment> payments = FXCollections.observableArrayList();
        try (Connection connection = dbConfig.getConnection();
             Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM get_payment " +
                    "WHERE paymentStatus != 0 " +
                    "ORDER BY billID;";
            ResultSet rs = statement.executeQuery(query);
            int no = 1;
            while (rs.next()) {
                String status = "";
                if (rs.getInt("paymentStatus") == 1) {
                    status = "paid";
                } else if (rs.getInt("paymentStatus") == 2) {
                    status = "Paid with penalty";
                }
                if (rs.getInt("paymentStatus") != 0) {
                    payments.add(new Payment(
                            no,
                            rs.getInt("billID"),
                            rs.getString("name"),
                            rs.getDate("dueDate").toLocalDate(),
                            rs.getDate("paymentDate").toLocalDate(),
                            rs.getDouble("totalAmount"),
                            rs.getDouble("penaltyAmount"),
                            rs.getDouble("paymentAmount"),
                            status,
                            rs.getString("meterNumber"),
                            rs.getString("meterLocation")
                    ));

                    no++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return payments;
    }
    public ObservableList<Payment> filterPayments(int month, int year, String meterLoc,int status, String searchVal) throws SQLException {
        ObservableList<Payment> payments = FXCollections.observableArrayList();

        // Convert searchVal to uppercase for case-insensitive search
        searchVal = (searchVal != null) ? searchVal.toUpperCase() : null;

        String query = "SELECT * FROM get_payment " +
            (month != 0 || year != 0 || !meterLoc.equals("All") || status != 0 || (searchVal != null && !searchVal.isEmpty()) ? "WHERE " : "") +
            (month != 0 ? "MONTH(paymentDate) = ? " : "") +
            (year != 0 ? (month != 0 ? "AND " : "") + "YEAR(paymentDate) = ? " : "") +
            (!meterLoc.equals("All") ? (month != 0 || year != 0 ? "AND " : "") + "meterLocation = ? " : "") +
            (status != 0 ? (month != 0 || year != 0 || !meterLoc.equals("All") ? "AND " : "") + "paymentStatus = ? " : "") +
            (searchVal != null && !searchVal.isEmpty() ? (month != 0 || year != 0 || !meterLoc.equals("All") || status != 0 ? "AND " : "") +
                    "(UPPER(name) LIKE ? OR UPPER(meterNumber) LIKE ? OR UPPER(meterLocation) LIKE ?) " : "") +
            "ORDER BY paymentDate;";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            int parameterIndex = 1;
            if (month != 0) preparedStatement.setInt(parameterIndex++, month);
            if (year != 0) preparedStatement.setInt(parameterIndex++, year);
            if (!meterLoc.equals("All")) preparedStatement.setString(parameterIndex++, meterLoc);
            if (status != 0) preparedStatement.setInt(parameterIndex++, status);
            if (searchVal != null && !searchVal.isEmpty()) {
                String likePattern = "%" + searchVal + "%";
                preparedStatement.setString(parameterIndex++, likePattern);
                preparedStatement.setString(parameterIndex++, likePattern);
                preparedStatement.setString(parameterIndex++, likePattern);
            }

            ResultSet rs = preparedStatement.executeQuery();

            int no = 1;
            while (rs.next()) {
                String paymentStatus = "";
                if (rs.getInt("paymentStatus") == 1) {
                    paymentStatus = "paid";
                } else if (rs.getInt("paymentStatus") == 2) {
                    paymentStatus = "Paid with penalty";
                }
                if (rs.getInt("paymentStatus") != 0) {
                    payments.add(new Payment(
                            no,
                            rs.getInt("billID"),
                            rs.getString("name"),
                            rs.getDate("dueDate").toLocalDate(),
                            rs.getDate("paymentDate").toLocalDate(),
                            rs.getDouble("totalAmount"),
                            rs.getDouble("penaltyAmount"),
                            rs.getDouble("paymentAmount"),
                            paymentStatus,
                            rs.getString("meterNumber"),
                            rs.getString("meterLocation")
                    ));
                    no++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return payments;
    }

    public ObservableList<Payment> getLoc() throws SQLException {
        ObservableList<Payment> payments = FXCollections.observableArrayList();
        try (Connection connection = dbConfig.getConnection();
             Statement statement = connection.createStatement()) {
            String query =  "SELECT \n" +
                            "	meterLocation \n" +
                            "FROM \n" +
                            "	get_payment\n" +
                            "GROUP BY\n" +
                            "	meterLocation\n" +
                            "ORDER BY\n" +
                            "	meterLocation;";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                payments.add(new Payment(
                   rs.getString("meterLocation")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return payments;
    }
    public ObservableList<Payment> getYear() throws SQLException {
        ObservableList<Payment> payments = FXCollections.observableArrayList();
        try (Connection connection = dbConfig.getConnection();
             Statement statement = connection.createStatement()) {
            String query =  "SELECT\n" +
                            "	YEAR(p.paymentDate) AS year\n" +
                            "FROM\n" +
                            "	payment p\n" +
                            "GROUP BY\n" +
                            "	year\n" +
                            "ORDER BY\n" +
                            "	year;";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                    payments.add(new Payment(
                            rs.getInt("year")
                    ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return payments;
    }
    public ObservableList<Payment> getMonth() throws SQLException {
        ObservableList<Payment> payments = FXCollections.observableArrayList();
        try (Connection connection = dbConfig.getConnection();
             Statement statement = connection.createStatement()) {
            String query =  "SELECT\n" +
                            "	MONTH(p.paymentDate) AS month\n" +
                            "FROM\n" +
                            "	payment p\n" +
                            "GROUP BY\n" +
                            "	month\n" +
                            "ORDER BY\n" +
                            "	month;";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                payments.add(new Payment(
                   rs.getInt("month")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return payments;
    }
    public ObservableList<Payment> getPaymentsById(int id) throws SQLException {
        ObservableList<Payment> payments = FXCollections.observableArrayList();
        try (Connection connection = dbConfig.getConnection();
             Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM get_payment " +
                    "WHERE paymentStatus != 0 AND cID = '"+id+"'" +
                    "ORDER BY billID;";
            ResultSet rs = statement.executeQuery(query);
            int no = 1;
            while (rs.next()) {
                String status = "";
                if (rs.getInt("paymentStatus") == 1) {
                    status = "paid";
                } else if (rs.getInt("paymentStatus") == 2) {
                    status = "Paid with penalty";
                }
                if (rs.getInt("paymentStatus") != 0) {
                    payments.add(new Payment(
                            no,
                            rs.getInt("billID"),
                            rs.getString("name"),
                            rs.getDate("dueDate").toLocalDate(),
                            rs.getDate("paymentDate").toLocalDate(),
                            rs.getDouble("totalAmount"),
                            rs.getDouble("penaltyAmount"),
                            rs.getDouble("paymentAmount"),
                            status,
                            rs.getString("meterNumber"),
                            rs.getString("meterLocation")
                    ));

                    no++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception
        }
        return payments;
    }
}
