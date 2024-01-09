/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view.settings.settingPages;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import view.settings.Settings;
import view.settings.SettingsModel;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class billingConfigurationController implements Initializable {

    @FXML
    private Spinner<Integer> readingDat;
    @FXML
    private Spinner<Integer> billingOffset;
    @FXML
    private Spinner<Integer> penaltyOffset;
    @FXML
    private Spinner<Integer> disconOffset;
    @FXML
    private Button edit;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private TextField penaltyAmount;
    @FXML
    private TextField rateValue;
    private SettingsModel settingsModel;
    private ObservableList<Settings> settings;

    public billingConfigurationController(){
        settingsModel = new SettingsModel();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            spinner(readingDat);
            spinner(billingOffset);
            spinner(penaltyOffset);
            spinner(disconOffset);
            setValue();
        } catch (SQLException ex) {
            Logger.getLogger(billingConfigurationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void spinner(Spinner<Integer> spinner) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 31);
        valueFactory.setValue(1);
        spinner.setValueFactory(valueFactory);
    }  
    public void setValue() throws SQLException {
        settings = settingsModel.getBillingConfiguration();
        if (settings != null) {
            for (Settings data : settings) {
                if (data.getSettingID() == 1) {
                    setSpinnerValue(readingDat,StringToInt(data.getSettingValue()));
                } else if (data.getSettingID() == 2) {
                    setSpinnerValue(billingOffset,StringToInt(data.getSettingValue()));
                } else if (data.getSettingID() == 3) {
                    setSpinnerValue(penaltyOffset,StringToInt(data.getSettingValue()));
                } else if (data.getSettingID() == 4) {
                    penaltyAmount.setText(data.getSettingValue());
                } else if (data.getSettingID() == 5) {
                    setSpinnerValue(disconOffset,StringToInt(data.getSettingValue()));
                } else if (data.getSettingID() == 6) {
                    rateValue.setText(data.getSettingValue());
                }
            }
        }
    }
    public void setSpinnerValue(Spinner<Integer> spinner,int value){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 31);
        valueFactory.setValue(value);
        spinner.setValueFactory(valueFactory);
    }
    
    public int StringToInt(String value){
        int convertedValue = 0;
        try{
            convertedValue = Integer.parseInt(value);
        }catch(Exception e){
            
        }
        return convertedValue;
    }
    public String intToString(int value){
        String convertedValue = String.valueOf(value);
        
        return convertedValue;
    }
    public void disable(boolean con){
        readingDat.setDisable(con);
        billingOffset.setDisable(con);
        penaltyOffset.setDisable(con);
        penaltyAmount.setDisable(con);
        rateValue.setDisable(con);
        disconOffset.setDisable(con);
        edit.setDisable(!con);
        cancelBtn.setDisable(con);
        saveBtn.setDisable(con);
    }
    private boolean areAddressFieldsNotEmpty() {
        return !rateValue.getText().isEmpty() &&
               !penaltyAmount.getText().isEmpty();
    }
    @FXML
    private void edit(ActionEvent event) {
        disable(false);
    }

    @FXML
    private void cancel(ActionEvent event) throws SQLException {
        setValue();
        disable(true);
    }

    @FXML
    private void save(ActionEvent event) throws SQLException {
        
        
        if (areAddressFieldsNotEmpty()) {
            ObservableList<Settings> newValue =  FXCollections.observableArrayList();
            newValue.add(new Settings(1,intToString(readingDat.getValue())));
            newValue.add(new Settings(2,intToString(billingOffset.getValue())));
            newValue.add(new Settings(3,intToString(penaltyOffset.getValue())));
            newValue.add(new Settings(4,penaltyAmount.getText()));
            newValue.add(new Settings(5,intToString(disconOffset.getValue())));
            newValue.add(new Settings(6,rateValue.getText()));
            if(settings !=null && newValue.size() == 6){
                settingsModel.updateBillCon(newValue);
            }else{
                settingsModel.insertBillCon(newValue);
            }
            setValue(); // Refresh the values after saving
            disable(true);
        }else{
        }
    }
    @FXML
    private void typeEvent(KeyEvent event) {
        Spinner<Integer> spinner = (Spinner<Integer>) event.getSource();

        int currentValue = spinner.getValue();

        String typedCharacter = event.getText();

        try {
            String updatedValue = String.valueOf(currentValue) + typedCharacter;

            int updatedIntValue = Integer.parseInt(updatedValue);


            if (updatedIntValue <= 31) {
                spinner.getValueFactory().setValue(updatedIntValue);
                System.out.println("Setting Value: " + updatedIntValue);
            } else {
                spinner.getValueFactory().setValue(currentValue);
                event.consume();
            }


            System.out.println(spinner.getValue());
        } catch (NumberFormatException e) {
            event.consume();
        }
    }


    
}
