/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Merry Ann
 */
public class dateFormatter {
    public String dateFormat(LocalDate date){
        String splitDate[] = String.valueOf(date).split("-");
        ObservableList<String> month = FXCollections.observableArrayList();
        month.addAll("Jan","Feb","Mar","Apr","May","Jun",
                     "Jul","Aug","Sep","Oct","Nov","Dec");
        return String.valueOf(month.get(Integer.parseInt(splitDate[1])-1)+" "+splitDate[2]+", "+splitDate[0]);
    }
}
