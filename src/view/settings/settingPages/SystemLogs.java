/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.settings.settingPages;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author Merry Ann
 */
public class SystemLogs {
    private int logID;
    private int aID;
    private String name;
    private String role;
    private LocalDate date;
    private LocalTime time;
    private String description;
    private int status;

    public SystemLogs(int logID, int aID, String name, String role, LocalDate date, LocalTime time, String description, int status) {
        this.logID = logID;
        this.aID = aID;
        this.name = name;
        this.role = role;
        this.date = date;
        this.time = time;
        this.description = description;
        this.status = status;
    }

    
    
    
    
    public int getLogID() {
        return logID;
    }

    public void setLogID(int logID) {
        this.logID = logID;
    }

    public int getaID() {
        return aID;
    }

    public void setaID(int aID) {
        this.aID = aID;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    
}
