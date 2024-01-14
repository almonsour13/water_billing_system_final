/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.settings;

/**
 *
 * @author Merry Ann
 */
public class Settings {
    private int companyID;
    private String companyName;
    private String companyCountry;
    private String companyRegion;
    private String companyProvince;
    private String companyMunicipality;
    private String companyBaranggay;
    private String companyPurok;
    private String contactNo;
    private String emailAd;
    private int settingID;
    private String settingName;
    private String settingValue;

    public Settings(int companyID, String companyName, String companyCountry, String companyRegion, String companyProvince, String companyMunicipality, String companyBaranggay, String companyPurok,String contactNo,String emailAd) {
        this.companyID = companyID;
        this.companyName = companyName;
        this.companyCountry = companyCountry;
        this.companyRegion = companyRegion;
        this.companyProvince = companyProvince;
        this.companyMunicipality = companyMunicipality;
        this.companyBaranggay = companyBaranggay;
        this.companyPurok = companyPurok;
        this.contactNo = contactNo;
        this.emailAd = emailAd;
    }

    public Settings(int settingID, String settingName, String settingValue) {
        this.settingID = settingID;
        this.settingName = settingName;
        this.settingValue = settingValue;
    }

    public Settings(int settingID, String settingValue) {
        this.settingID = settingID;
        this.settingValue = settingValue;
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
    
    
    public int getSettingID() {
        return settingID;
    }

    public void setSettingID(int settingID) {
        this.settingID = settingID;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }
    
    
    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCountry() {
        return companyCountry;
    }

    public void setCompanyCountry(String companyCountry) {
        this.companyCountry = companyCountry;
    }

    public String getCompanyRegion() {
        return companyRegion;
    }

    public void setCompanyRegion(String companyRegion) {
        this.companyRegion = companyRegion;
    }

    public String getCompanyProvince() {
        return companyProvince;
    }

    public void setCompanyProvince(String companyProvince) {
        this.companyProvince = companyProvince;
    }

    public String getCompanyMunicipality() {
        return companyMunicipality;
    }

    public void setCompanyMunicipality(String companyMunicipality) {
        this.companyMunicipality = companyMunicipality;
    }

    public String getCompanyBaranggay() {
        return companyBaranggay;
    }

    public void setCompanyBaranggay(String companyBaranggay) {
        this.companyBaranggay = companyBaranggay;
    }

    public String getCompanyPurok() {
        return companyPurok;
    }

    public void setCompanyPurok(String companyPurok) {
        this.companyPurok = companyPurok;
    }
    
}
