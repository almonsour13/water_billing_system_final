/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.penalty;

import java.time.LocalDate;

/**
 *
 * @author Merry Ann
 */
public class Penalty {
    private int no;
    private int penaltyID;
    private int billID;
    private String cname;
    private String meterno;
    private String meterloc;
    private LocalDate billdate;
    private int billamount;
    private LocalDate pdate;
    private String ptype;
    private int pamount;
    private double totalAmount; 
    private String status;

    public Penalty(int no, int penaltyID, int billID, String cname, String meterno, String meterloc, LocalDate billdate, int billamount, LocalDate pdate, String ptype, int pamount,double totalAmount, String status) {
        this.no = no;
        this.penaltyID = penaltyID;
        this.billID = billID;
        this.cname = cname;
        this.meterno = meterno;
        this.meterloc = meterloc;
        this.billdate = billdate;
        this.billamount = billamount;
        this.pdate = pdate;
        this.ptype = ptype;
        this.pamount = pamount;
        this.totalAmount =totalAmount;
        this.status = status;
    }
    
    
    public int getNo() {
        return no;
    }

    public int getPenaltyID() {
        return penaltyID;
    }

    public void setPenaltyID(int penaltyID) {
        this.penaltyID = penaltyID;
    }

    public int getBillID() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }
    
    public void setNo(int no) {
        this.no = no;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getMeterno() {
        return meterno;
    }

    public void setMeterno(String meterno) {
        this.meterno = meterno;
    }

    public String getMeterloc() {
        return meterloc;
    }

    public void setMeterloc(String meterloc) {
        this.meterloc = meterloc;
    }

    public LocalDate getBilldate() {
        return billdate;
    }

    public void setBilldate(LocalDate billdate) {
        this.billdate = billdate;
    }

    public int getBillamount() {
        return billamount;
    }

    public void setBillamount(int billamount) {
        this.billamount = billamount;
    }

    public LocalDate getPdate() {
        return pdate;
    }

    public void setPdate(LocalDate pdate) {
        this.pdate = pdate;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public int getPamount() {
        return pamount;
    }

    public void setPamount(int pamount) {
        this.pamount = pamount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    
    
    
}
