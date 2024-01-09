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
import javafx.scene.control.ChoiceBox;
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
public class EditReadingController {

    @FXML
    private AnchorPane container;
    @FXML
    private TextField originalValue;
    @FXML
    private TextField newValue;
    @FXML
    private Button sendRequest;
    private MeterReadingModel meterReadingModel;
    private int meterReadingID;
    @FXML
    private TextArea reasonField;
    private  LoggedAccountSetter logAccount = new LoggedAccountSetter();
    @FXML
    private Button sendRequest1;
    
    public EditReadingController(){
        meterReadingModel = new MeterReadingModel();
    }
    public void setMeterReading(int meterReadingID) throws SQLException{
        this.meterReadingID = meterReadingID;
        int meterReading = meterReadingModel.getMeterReading(meterReadingID);
        originalValue.setText(String.valueOf(meterReading));
    }

    @FXML
    private void sendRequest(ActionEvent event) throws SQLException {
        int newValue = Integer.parseInt(this.newValue.getText());
        int orginalValue = Integer.parseInt(this.originalValue.getText());
        String reason = reasonField.getText();
        meterReadingModel.insertNewValue(meterReadingID,logAccount.getAccount(),orginalValue,newValue,reason);
        stageClose();
    }

    @FXML
    private void cancel(ActionEvent event) {
        stageClose();
    }
    public void stageClose(){
        Stage stage = (Stage) container.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void enableButton(){
        if(!newValue.getText().isEmpty() && !reasonField.getText().isEmpty()){
            sendRequest.setDisable(false);
        }else{
            sendRequest.setDisable(true);
        }
    }

}
