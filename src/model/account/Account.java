/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.account;

import java.time.LocalDate;

/**
 *
 * @author Merry Ann
 */
public class Account {
    private int no;
    private int id;
    private String fName; 
    private String mName;
    private String lName;
    private String uName;
    private String pWord;
    private String role;
    private String status;
    private String name;
    private LocalDate dateAdded;

    public Account(int no, int id, String fName, String mName, String lName, String uName, String pWord, String role, String status,LocalDate dateAdded) {
        this.no = no;
        this.id = id;
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.uName = uName;
        this.pWord = pWord;
        this.role = role;
        this.status = status;
        this.dateAdded = dateAdded;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }
    
    
    public Account(String name){
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getMName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getLName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getUName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getPWord() {
        return pWord;
    }

    public void setpWord(String pWord) {
        this.pWord = pWord;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
