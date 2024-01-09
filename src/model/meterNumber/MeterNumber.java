/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.meterNumber;

import java.time.LocalDate;

/**
 *
 * @author Merry Ann
 */
public class MeterNumber {
    private int no;
    private int cID;
    private int cmID;
    private int meterID;
    private String meterNumber;
    private String meterLoc;
    private LocalDate installationDate;
    private String name;
    private int totalConsumption;
    private String cMStatus;
    private String meterStatus;

    public MeterNumber(int no,int cmID, int cID, int meterID, String meterNumber, String meterLoc, LocalDate installationDate, String name, int totalConsumption,String cMStatus, String meterStatus) {
        this.no = no;
        this.cmID = cmID;
        this.cID = cID;
        this.meterID = meterID;
        this.meterNumber = meterNumber;
        this.meterLoc = meterLoc;
        this.installationDate = installationDate;
        this.name = name;
        this.totalConsumption = totalConsumption;
        this.cMStatus = cMStatus;
        this.meterStatus = meterStatus;
    }
    
    
    public int getCmID() {
        return cmID;
    }

    public void setCmID(int cmID) {
        this.cmID = cmID;
    }
    
    public String getcMStatus() {
        return cMStatus;
    }

    public void setcMStatus(String cMStatus) {
        this.cMStatus = cMStatus;
    }

    public int getcID() {
        return cID;
    }

    public void setcID(int cID) {
        this.cID = cID;
    }

    
    
    
    
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getMeterID() {
        return meterID;
    }

    public void setMeterID(int meterID) {
        this.meterID = meterID;
    }
    
    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getMeterLoc() {
        return meterLoc;
    }

    public void setMeterLoc(String meterLoc) {
        this.meterLoc = meterLoc;
    }

    public LocalDate getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(LocalDate installationDate) {
        this.installationDate = installationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalConsumption() {
        return totalConsumption;
    }

    public void setTotalConsumption(int totalConsumption) {
        this.totalConsumption = totalConsumption;
    }

    public String getMeterStatus() {
        return meterStatus;
    }

    public void setMeterStatus(String meterStatus) {
        this.meterStatus = meterStatus;
    }
    
    
    
}
