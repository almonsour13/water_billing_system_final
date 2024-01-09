/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.consumer;

import java.time.LocalDate;

/**
 *
 * @author Merry Ann
 */
public class Consumer {
    private int no;
    private int id;
    private String fName; 
    private String mName;
    private String lName;
    private String suffix;
    private String contactNo;
    private String emailAd;
    private int meterID;
    private String meterNo;
    private String meterLocation;
    private LocalDate installationDate; // Use LocalDate for dates
    private String purok;
    private String postalCode;
    private String status;
    private String name;
    private LocalDate dateAdded;
    private int yearAdded;
    
    public Consumer(){
        
    }
    public Consumer(int no, int id, String fName, String mName, String lName, String suffix,
                    String contactNo, String emailAd, 
                    String purok, String postalCode, LocalDate dateAdded,String status) {
        this.no = no;
        this.id = id;
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.suffix = suffix;
        this.contactNo = contactNo;
        this.emailAd = emailAd;
        this.meterNo = meterNo;
        this.installationDate = installationDate;
        this.purok = purok;
        this.postalCode = postalCode;
        this.dateAdded = dateAdded;
        this.status = status;
       
    }
    public Consumer(int id, String fName, String mName, String lName, String suffix,
                    String contactNo, String emailAd, String meterNo, LocalDate installationDate,
                    String purok, String postalCode, String status) {
        this.id = id;
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.suffix = suffix;
        this.contactNo = contactNo;
        this.emailAd = emailAd;
        this.meterNo = meterNo;
        this.installationDate = installationDate;
        this.purok = purok;
        this.postalCode = postalCode;
        this.status = status;
    }
    public Consumer(int id, String fName, String mName, String lName, String suffix,
                    String contactNo, String emailAd, 
                    String purok, String postalCode, String status) {
        this.id = id;
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.suffix = suffix;
        this.contactNo = contactNo;
        this.emailAd = emailAd;
        this.meterNo = meterNo;
        this.purok = purok;
        this.postalCode = postalCode;
        this.status = status;
    }

    public Consumer(int yearAdded) {
        this.yearAdded = yearAdded;
    }
    
    public int getYearAdded() {
        return yearAdded;
    }

    public void setYearAdded(int yearAdded) {
        this.yearAdded = yearAdded;
    }
    
    public String getMeterLocation() {
        return meterLocation;
    }

    public void setMeterLocation(String meterLocation) {
        this.meterLocation = meterLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }
    
    
    public int getMeterID() {
        return meterID;
    }
    public Consumer(String purok){
        this.purok = purok;
    }
    public void setMeterID(int meterID) {
        this.meterID = meterID;
    }
    public Consumer(int meterID, String meterNo,String meterLocation, LocalDate installationDate, String status) {
        this.meterID = meterID;
        this.meterNo = meterNo;
        this.meterLocation = meterLocation;
        this.installationDate = installationDate;
        this.status = status;
    }

    public Consumer(String name, String purok, String postalCode) {
        this.purok = purok;
        this.postalCode = postalCode;
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getMidName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getLastName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmailAd() {
        return emailAd;
    }

    public void setEmailAd(String emailAd) {
        this.emailAd = emailAd;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public LocalDate getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(LocalDate installationDate) {
        this.installationDate = installationDate;
    }

    public String getPurok() {
        return purok;
    }

    public void setPurok(String purok) {
        this.purok = purok;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
