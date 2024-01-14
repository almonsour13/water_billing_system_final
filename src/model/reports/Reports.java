/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.reports;

import java.time.LocalDate;

/**
 *
 * @author Merry Ann
 */
public class Reports {
    private LocalDate billingDate;
    private int month;
    private int collected;
    private int notCollected;
    private double paymentCollectionPerMonth;
    private int year;
    private int waterConsumptionPerMonth;
    
    public Reports(LocalDate billingDate, int collected, int notCollected) {
        this.billingDate = billingDate;
        this.collected = collected;
        this.notCollected = notCollected;
    }

    public Reports(int waterConsumtionPerMonth, int month, int year) {
        this.month = month;
         this.waterConsumptionPerMonth = waterConsumtionPerMonth;
        this.year = year;
       
    }

    public Reports( double paymentCollectionPerMonth, int month, int year) {
        this.month = month;
        this.paymentCollectionPerMonth = paymentCollectionPerMonth;
        this.year = year;
    }

    public int getWaterConsumptionPerMonth() {
        return waterConsumptionPerMonth;
    }

    public void setWaterConsumptionPerMonth(int waterConsumptionPerMonth) {
        this.waterConsumptionPerMonth = waterConsumptionPerMonth;
    }
     

    public double getPaymentCollectionPerMonth() {
        return paymentCollectionPerMonth;
    }

    public void setPaymentCollectionPerMonth(double paymentCollectionPerMonth) {
        this.paymentCollectionPerMonth = paymentCollectionPerMonth;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    

    public Reports(LocalDate billingDate) {
        this.billingDate = billingDate;
    }

    public LocalDate getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(LocalDate billingDate) {
        this.billingDate = billingDate;
    }
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getCollected() {
        return collected;
    }

    public void setCollected(int collected) {
        this.collected = collected;
    }

    public int getNotCollected() {
        return notCollected;
    }

    public void setNotCollected(int notCollected) {
        this.notCollected = notCollected;
    }
    
    
    
    
    
    
}
