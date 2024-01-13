/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.meterReading;

import java.time.LocalDate;

/**
 *
 * @author Administrator
 */
public class MeterReading {
    private int no;
    private int meterId;
    private String MeterNo;
    private int meterReadingId;
    private String name;
    private LocalDate installationDate;
    private LocalDate readingDate;
    private int prevReading;
    private int curReading;
    private String status;
    private int accountID;
    private String curReadingDate;
    private String collectorName;
    private String meterLocation;
    private int editRequestID;
    private int newValue;
    private LocalDate requestDate;
    private String reason;
    private int originalValue;
    private int cID;
        
    
    
    public MeterReading(int accountID){
        this.accountID = accountID;
    }
    public MeterReading(int meterId, String MeterNo, LocalDate installationDate, String status) {
        this.meterId = meterId;
        this.MeterNo = MeterNo;
        this.installationDate = installationDate;
        this.status = status;
    }
    public MeterReading(int no,int cID, int meterID,int meterReadingId, String MeterNo,String meterLocation, String name, LocalDate readingDate, int prevReading, int curReading,String curReadingDate, String accountName, String status) {
        this.no = no;
        this.cID = cID;
        this.meterId = meterID;
        this.meterReadingId = meterReadingId;
        this.MeterNo = MeterNo;
        this.meterLocation = meterLocation;
        this.name = name;
        this.readingDate = readingDate;
        this.prevReading = prevReading;
        this.curReading = curReading;
        this.curReadingDate = curReadingDate;
        this.collectorName = accountName;
        this.status = status;
    }

    public MeterReading(int editRequestID, int meterReadingId, String name, LocalDate requestDate, int originalValue, int newValue, String reason, String status) {
        this.meterReadingId = meterReadingId;
        this.name = name;
        this.originalValue = originalValue;
        this.status = status;
        this.editRequestID = editRequestID;
        this.newValue = newValue;
        this.requestDate = requestDate;
        this.reason = reason;
    }

    public MeterReading(int no, int meterReadingId, int editRequestID,String MeterNo,String meterLocation, String name, LocalDate readingDate, int prevReading, int originalValue,  int newValue, LocalDate requestDate, String reason, String accountName ) {       
        this.no = no;
        this.MeterNo = MeterNo;
        this.meterReadingId = meterReadingId;
        this.name = name;
        this.readingDate = readingDate;
        this.prevReading = prevReading;
        this.collectorName = accountName;
        this.meterLocation = meterLocation;
        this.editRequestID = editRequestID;
        this.newValue = newValue;
        this.reason = reason;
        this.originalValue = originalValue;
        this.requestDate = requestDate;
    }
    
    public int getcID() {
        return cID;
    }

    public void setcID(int cID) {
        this.cID = cID;
    }
    
    public int getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(int originalValue) {
        this.originalValue = originalValue;
    }
    
    public int getEditRequestID() {
        return editRequestID;
    }

    public void setEditRequestID(int editRequestID) {
        this.editRequestID = editRequestID;
    }

    public int getNewValue() {
        return newValue;
    }

    public void setNewValue(int newValue) {
        this.newValue = newValue;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public String getMeterLocation() {
        return meterLocation;
    }

    public void setMeterLocation(String meterLocation) {
        this.meterLocation = meterLocation;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }
    
    public String getCurReadingDate() {
        return curReadingDate;
    }

    public void setCurReadingDate(String curReadingDate) {
        this.curReadingDate = curReadingDate;
    }
    
    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }
    
    public int getMeterReadingId() {
        return meterReadingId;
    }

    public void setMeterReadingId(int meterReadingId) {
        this.meterReadingId = meterReadingId;
    }
    
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }
    
    public int getMeterId() {
        return meterId;
    }

    public void setMeterId(int meterId) {
        this.meterId = meterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    

    public String getMeterNo() {
        return MeterNo;
    }

    public void setMeterNo(String MeterNo) {
        this.MeterNo = MeterNo;
    }

    public LocalDate getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(LocalDate installationDate) {
        this.installationDate = installationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
