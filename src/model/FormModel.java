/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Merry Ann
 */
public class FormModel {
    private DbConfig dbConfig;
    
    public FormModel(){
        this.dbConfig = new DbConfig();
    }
    public Object[] matchPassAndUser(String userName, String passWord) throws SQLException{
        Object[] array = new Object[3];
        
        String query = "SELECT aID, aType FROM `accounts` WHERE aUserName = '"+userName+"' && aPassword = '"+passWord+"' ";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                array[0] = true;
                array[1] = rs.getInt("aID");
                array[2] = rs.getInt("aType");
            }else{
                array[0] = false;
            }
        }
        return array;
    }
    public int checkAccountType(int id) throws SQLException{
        String query = "SELECT aType FROM `accounts` WHERE aID = '"+id+"'; ";
        int type = 0;
        try (Connection connection = dbConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                type  = rs.getInt("aType");
            }
        }
        return type;
    }
}
