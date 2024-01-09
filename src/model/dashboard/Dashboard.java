/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dashboard;

import java.time.LocalDate;

/**
 *
 * @author Merry Ann
 */
public class Dashboard {
    private int month;
    private int monthlyConsumption;
    private Double paymentAmount;
    private int unpaid;
    private int overdue;
    private int paid;
    private int totalBills;
    private int no;
    private String name;
    private LocalDate billDate;
    private Double totalAmount;
    private String status;
    
    public Dashboard(int month, Double paymentAmount) {
        this.month = month;
        this.paymentAmount = paymentAmount;
    }

    public Dashboard(int unpaid, int paid, int overdue, int totalBills) {
        this.unpaid = unpaid;
        this.overdue = overdue;
        this.paid = paid;
        this.totalBills = totalBills;
    }

    public Dashboard(int no, String name, LocalDate billDate, Double totalAmount, String status) {
        this.no = no;
        this.name = name;
        this.billDate = billDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Dashboard(int month, int monthlyConsumption) {
        this.month = month;
        this.monthlyConsumption = monthlyConsumption;
    }
    
    public int getMonthlyConsumption() {
        return monthlyConsumption;
    }

    public void setMonthlyConsumption(int monthlyConsumption) {
        this.monthlyConsumption = monthlyConsumption;
    }
    
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public int getUnpaid() {
        return unpaid;
    }

    public void setUnpaid(int unpaid) {
        this.unpaid = unpaid;
    }

    public int getOverDue() {
        return overdue;
    }

    public void setOverDue(int overdue) {
        this.overdue = overdue;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public int getTotalBills() {
        return totalBills;
    }

    public void setTotalBills(int totalBills) {
        this.totalBills = totalBills;
    }
    
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
    
}
