/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import controller.accountLoggedSetter.LoggedAccountSetter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import model.consumer.Consumer;
import model.consumer.ConsumerModel;
import view.consumer.ModalController;
import view.settings.settingPages.SystemLogsModel;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class AddConsumerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField fName;
    @FXML
    private TextField mName;
    @FXML
    private TextField lName;
    @FXML
    private ChoiceBox<String> suffix;
    @FXML
    private TextField contactNo;
    @FXML
    private TextField emailAd;
    @FXML
    private TextField postalCode;
    @FXML
    private ComboBox<String> purok;
    private static ConsumerModel ConsumerModel;
    ObservableList<Consumer> data = FXCollections.observableArrayList();
    private static ConsumerController consumerController;
    @FXML
    private Button addPurok;
    @FXML
    private VBox parentContainer;
    private  LoggedAccountSetter logAccount = new LoggedAccountSetter();
    private SystemLogsModel systemLogsModel = new SystemLogsModel();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.ConsumerModel = new ConsumerModel();
        try {
            this.data = ConsumerModel.getConsumers();
        } catch (SQLException ex) {
            Logger.getLogger(AddConsumerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        setChoices();
        purok.setOnAction(this::enable);
    }    
    public void AddConsumerController() throws SQLException{
        this.ConsumerModel = new ConsumerModel();
        this.data = ConsumerModel.getConsumers();
        setChoices();
    }
    public void setController(ConsumerController consumerController){
        this.consumerController = consumerController;
    }
    public void setChoices() {
        suffix.setItems(FXCollections.observableArrayList("--Select Suffix--", "Jr", "Sr", "I", "II", "III", "IV"));
        suffix.setValue("--Select Suffix--");

        ObservableList<String> puroks = FXCollections.observableArrayList("--Select Purok--");
        puroks.addAll(data.stream().map(Consumer::getPurok).filter(p -> !p.equals("--Select Purok--")).distinct().collect(Collectors.toList()));
        purok.setItems(puroks);
        purok.setValue("--Select Purok--");
    }
    private boolean isValidMeterNo(String meterNo){
        try {
            Integer.parseInt(meterNo);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean isValidContactNo(String contactNo) {
        return Pattern.compile("^(?:09)[0-9]{9}$").matcher(contactNo).matches();
    }
    private boolean isValidEmailAddress(String emailAddress) {
        return Pattern.compile("^(.+)@(.+)$").matcher(emailAddress).matches();
    }
    public void enable(ActionEvent event){
        addPurok.setDisable(false);
    }
    @FXML
    private void addConsumer(ActionEvent event) throws SQLException, IOException {
        String suffixes = suffix.getValue().isEmpty() || suffix.getValue().equals("--Select Suffix--") ? "" : suffix.getValue();
        String puroks = purok.getValue().isEmpty() || purok.getValue().equals("--Select Purok--") ? "" : purok.getValue();
        
        if (fName.getText().isEmpty() || lName.getText().isEmpty() || contactNo.getText().isEmpty() 
            || purok.getValue().equals("--Select Purok--")) {
            modal("Please fill in all required fields");
            return;
        }
        
        if( ConsumerModel.checkAddConsumer(upper(fName.getText()),upper(mName.getText()),upper(lName.getText()),upper(suffixes))){
            modal("Consumer Information Already Exist");
        }    
        if ((!emailAd.getText().isEmpty() && !isValidEmailAddress(emailAd.getText()))) {
            modal("Please enter valid email address. ");
            return;
        }
        if(!isValidContactNo(contactNo.getText()) ){
            modal("Please enter valid contact number.");
            return;
        }

        
        
        ConsumerModel.insertAddConsumer(
            upper(fName.getText()),
            mName.getText().isEmpty() ? "" : upper(mName.getText()),
            upper(lName.getText()),
            upper(suffixes),
            contactNo.getText(),
            emailAd.getText().isEmpty() ? "" : emailAd.getText(),
            upper(puroks),
            postalCode.getText().isEmpty() ? "" : postalCode.getText(),
            1
        );

        consumerController.showConsumerTable();
        Stage stage = (Stage) parentContainer.getScene().getWindow();
        stage.close();

        modal( "Consumer Information Added Successfully"); 
        systemLogsModel.insertLog(logAccount.getAccount(), "Successfully added a new consumer ("+fName.getText()+" "+lName.getText()+")");
                                
    }
    public String upper(String value){
        return value.toUpperCase();
    }
    public void modal(String promptLabel) throws IOException{
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/consumer/Modal.fxml"));
            Parent rootNode = loader.load();
            ModalController modCOn = loader.getController();
            modCOn.setController(consumerController);
            modCOn.setPromptLabel(promptLabel);
            Stage modalWindow = new Stage();
            modalWindow.setResizable(false);
            modalWindow.setScene(new Scene(rootNode));
            modalWindow.initModality(Modality.APPLICATION_MODAL);
            modalWindow.centerOnScreen();
            modalWindow.setOpacity(1);
            modalWindow.show();
    }
    @FXML
    private void cancelAdd(ActionEvent event) {
        Stage stage = (Stage) parentContainer.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void addPurok(ActionEvent event) {
        purok.setEditable(true);
        purok.setValue("");
        addPurok.setDisable(true);
    }
    
}
