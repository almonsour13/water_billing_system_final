/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.bills.Bills;
import model.bills.BillsModel;
import model.dateFormatter;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class ViewBillDetailsController implements Initializable {

    @FXML
    private Label billingDate;
    @FXML
    private Label dueDate;
    @FXML
    private Label readingDate;
    @FXML
    private Label previousReading;
    @FXML
    private Label currentReading;
    @FXML
    private Label consumption;
    @FXML
    private Label amount;
    @FXML
    private Label balance;
    @FXML
    private Label sttatus;
    private BillsModel billModel;
    @FXML
    private Label consumerName;
    private dateFormatter dateFormatter;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        billModel = new BillsModel();
        dateFormatter = new dateFormatter();
    }   
    public void setBill(int id) throws SQLException{
//        Bills bill = billModel.getConsumerBillDetails(id);
//        
//        consumerName.setText(bill.getName()+" Billing Details:");
//        billingDate.setText(dateFormatter.dateFormat(bill.getBillingDate()));
//        dueDate.setText(dateFormatter.dateFormat(bill.getDueDate()));
//        readingDate.setText(dateFormatter.dateFormat(bill.getReadingDate()));
//        previousReading.setText(String.valueOf(bill.getPrevReading()));
//        currentReading.setText(String.valueOf(bill.getCurReading()));
//        consumption.setText(String.valueOf(bill.getConsumption()));
//        amount.setText(String.valueOf(bill.getTotalAmount()));
//        sttatus.setText(bill.getStatus());
    
    }
    
}
