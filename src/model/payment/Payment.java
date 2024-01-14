/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.payment;

import java.time.LocalDate;

/**
 *
 * @author Merry Ann
 */
public class Payment {
    private int no;
    private int paymentID;
    private int billID;
    private String name;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private Double totalAmount;
    private Double paymentAmount;
    private Double Balance;
    private String status;
    private String meterNumber;
    private String meterLocation;
    private int year;
    private int month;
    private Double penaltyAmount;

    public Payment(int no, int paymentID,int billID, String name, LocalDate dueDate, LocalDate paymentDate, Double totalAmount, Double paymentAmount, String status) {
        this.no = no;
        this.billID = billID;
        this.name = name;
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
        this.totalAmount = totalAmount;
        this.paymentAmount = paymentAmount;
        this.status = status;
    }

    public Payment(int no,int paymentID, int billID, String name, LocalDate dueDate, LocalDate paymentDate, Double totalAmount, Double penaltyAmount, Double paymentAmount, String status, String meterNumber, String meterLocation) {
        this.no = no;
        this.paymentID = paymentID;
        this.billID = billID;
        this.name = name;
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
        this.totalAmount = totalAmount;
        this.paymentAmount = paymentAmount;
        this.status = status;
        this.penaltyAmount = penaltyAmount;
        this.meterNumber = meterNumber;
        this.meterLocation = meterLocation;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }
    
    public double getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(Double penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }
    
    public Payment(int year) {
        this.year = year;
    }
    
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
    
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    
    public Payment(String meterLocation) {
        this.meterLocation = meterLocation;
    }
    
    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getMeterLocation() {
        return meterLocation;
    }

    public void setMeterLocation(String meterLocation) {
        this.meterLocation = meterLocation;
    }
    
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getBillID() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Double getBalance() {
        return Balance;
    }

    public void setBalance(Double Balance) {
        this.Balance = Balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
