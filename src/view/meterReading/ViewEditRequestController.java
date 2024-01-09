/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view.meterReading;

import controller.MeterReadingController;
import controller.accountLoggedSetter.LoggedAccountSetter;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.meterReading.MeterReading;
import model.meterReading.MeterReadingModel;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class ViewEditRequestController{

    @FXML
    private AnchorPane container;
    @FXML
    private TextField originalValue;
    @FXML
    private TextField newValue;
    @FXML
    private Button approveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private TextArea reasonField;
    @FXML
    private Button rejectBtn;
    private int meterReadingID;
    private MeterReadingController readingController;
    @FXML
    private Label collectorName;
    @FXML
    private Label requestDate;
    private  LoggedAccountSetter logAccount = new LoggedAccountSetter();
    private MeterReadingModel meterReadingModel;
    
    public ViewEditRequestController(){
        meterReadingModel = new MeterReadingModel();
    }
    public void setController(MeterReadingController readingController){
        this.readingController = readingController;
    }
    public void setMeterReading(int meterReadingID) throws SQLException{
        MeterReading reading = meterReadingModel.viewRequest(meterReadingID);
        this.meterReadingID = meterReadingID;
        if(reading != null){
            originalValue.setText(String.valueOf(reading.getOriginalValue()));
            newValue.setText(String.valueOf(reading.getNewValue()));
            reasonField.setText(String.valueOf(reading.getReason()));
            collectorName.setText("Collector Name: "+reading.getName());
            requestDate.setText(String.valueOf(reading.getRequestDate()));
            if(!reading.getStatus().equals("Pending")){
                approveBtn.setDisable(true);
                cancelBtn.setDisable(false);
                rejectBtn.setDisable(true);
            }
        }
    }
    @FXML
    private void enableButton(KeyEvent event) {
    }
    @FXML
    private void approve(ActionEvent event) throws SQLException {
        meterReadingModel.approvedRequest(meterReadingID);
        stageClose();
    }
    @FXML
    private void cancel(ActionEvent event) throws SQLException {
        stageClose();
    }

    @FXML
    private void reject(ActionEvent event) throws SQLException {
        meterReadingModel.rejectRequest(meterReadingID);
        stageClose();
    }
    public void stageClose() throws SQLException{
        readingController.loadMeterReading();
        Stage stage = (Stage) container.getScene().getWindow();
        stage.close();
    }
    
}
