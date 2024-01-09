/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.bills;

import java.time.LocalDate;

/**
 *
 * @author Merry Ann
 */
public class Bills {
    private int no;
    private int id;
    private int billID;
    private int meterReadingID;
    private String meterNo;
    private String meterLocation;
    private String name;
    private LocalDate billingDate;
    private LocalDate dueDate;
    private Double totalAmount;
    private int consumption;
    private String Status;
    private LocalDate readingDate;
    private int prevReading;
    private int curReading;
    private int paymentStatus;
    private Double paymentAmount;
    private int year;
    private int billingStatus;
    private LocalDate penaltyDate;
    private String penaltyType;
    private String penaltyDescription;
    private Double penaltyAmount;
    private int rate;

    public Bills(int billID,String name, String meterNo,String meterLocation,  LocalDate billingDate, LocalDate dueDate, Double totalAmount, LocalDate penaltyDate, String penaltyType, String penaltyDescription, Double penaltyAmount) {
        this.billID = billID;
        this.meterNo = meterNo;
        this.name = name;
        this.billingDate = billingDate;
        this.dueDate = dueDate;
        this.totalAmount = totalAmount;
        this.Status = Status;
        this.meterLocation = meterLocation;
        this.penaltyDate = penaltyDate;
        this.penaltyType = penaltyType;
        this.penaltyDescription = penaltyDescription;
        this.penaltyAmount = penaltyAmount;
    }
    public Bills(int id,int billID, int meterReadingID, String meterNo, String name, LocalDate billingDate, 
            LocalDate dueDate, double totalAmount, int consumption, String status, LocalDate readingDate, int prevReading, int curReading){
        this.id = id;
        this.billID = billID;
        this.meterReadingID = meterReadingID;
        this.meterNo = meterNo;
        this.name = name;
        this.billingDate = billingDate;
        this.dueDate = dueDate;
        this.totalAmount = totalAmount;
        this.consumption = consumption;
        this.Status = status;
        this.readingDate = readingDate;
        this.prevReading = prevReading;
        this.curReading = curReading;
    }

    public Bills(int no, int id, int billID, String meterNo, String meterLocation, String name, LocalDate billingDate, LocalDate dueDate, Double totalAmount, int consumption,int rate, Double penaltyAmount, String Status) {
        this.no = no;
        this.id = id;
        this.billID = billID;
        this.meterNo = meterNo;
        this.meterLocation = meterLocation;
        this.name = name;
        this.billingDate = billingDate;
        this.dueDate = dueDate;
        this.totalAmount = totalAmount;
        this.consumption = consumption;
        this.rate = rate;
        this.penaltyAmount = penaltyAmount;
        this.Status = Status;
    }
    
    public Bills(int id, String name){
        this.id = id;
        this.name = name;
    }
    public Bills(String name,String meterNo, Double totalAmount, Double penaltyAmount,int billingStatus) {
        this.meterNo = meterNo;
        this.name = name;
        this.totalAmount = totalAmount;
        this.penaltyAmount = penaltyAmount;
        this.billingStatus = billingStatus;
    }

    public LocalDate getPenaltyDate() {
        return penaltyDate;
    }

    public void setPenaltyDate(LocalDate penaltyDate) {
        this.penaltyDate = penaltyDate;
    }

    public String getPenaltyType() {
        return penaltyType;
    }

    public void setPenaltyType(String penaltyType) {
        this.penaltyType = penaltyType;
    }

    public String getPenaltyDescription() {
        return penaltyDescription;
    }

    public void setPenaltyDescription(String penaltyDescription) {
        this.penaltyDescription = penaltyDescription;
    }

    public Double getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(Double penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }
    
    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
    
    public String getMeterLocation() {
        return meterLocation;
    }

    public void setMeterLocation(String meterLocation) {
        this.meterLocation = meterLocation;
    }
    
    public int getBillingStatus() {
        return billingStatus;
    }

    public void setBillingStatus(int billingStatus) {
        this.billingStatus = billingStatus;
    }

    public Bills(int year) {
        this.year = year;
    }
    
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    
    public Double getPaymentAmount() {
        return paymentAmount;
    }
    
    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
    
    
    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public LocalDate getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(LocalDate readingDate) {
        this.readingDate = readingDate;
    }

    public int getPrevReading() {
        return prevReading;
    }

    public void setPrevReading(int prevReading) {
        this.prevReading = prevReading;
    }

    public int getCurReading() {
        return curReading;
    }

    public void setCurReading(int curReading) {
        this.curReading = curReading;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Bills(String name){
        this.name = name;
    }
    public int getBillID(){
        return billID;
    }
    public void setBillID(int billID){
        this.billID = billID;
    }

    public int getMeterReadingID() {
        return meterReadingID;
    }

    public void setMeterReadingID(int meterReadingID) {
        this.meterReadingID = meterReadingID;
    }
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public LocalDate getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(LocalDate billingDate) {
        this.billingDate = billingDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getConsumption() {
        return consumption;
    }

    public void setConsumption(int consumption) {
        this.consumption = consumption;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }
}
