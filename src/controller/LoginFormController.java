/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import controller.accountLoggedSetter.LoggedAccountSetter;
import controller.consumer.AddMeterNoController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import model.FormModel;
import view.settings.settingPages.SystemLogsModel;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class LoginFormController implements Initializable {

    @FXML
    private AnchorPane loginFormContainer;
    private FormModel formModel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox checkBox;
    @FXML
    private TextField userNameField;
    private  LoggedAccountSetter logAccount;
    private SystemLogsModel systemLogsModel;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.formModel = new FormModel();
         this.systemLogsModel = new SystemLogsModel();
        this.logAccount = new LoggedAccountSetter();
        
    }    

    @FXML
    private void loginBtn(ActionEvent event) throws IOException, SQLException {
        
        String uName = userNameField.getText();
        String pWord = passwordField.getText();
        if(!uName.isEmpty() && !pWord.isEmpty()){
            Object returnData[]  = formModel.matchPassAndUser(uName, pWord);
            System.out.println(returnData[0]);
            if((boolean)returnData[0] == true){
                if((Integer)returnData[2] == 1){
                    logAccount.setAccount((Integer)returnData[1]);
                    logAdmin();
                }
                systemLogsModel.insertLog((Integer)returnData[1], "Successfully logged in");
            }else{
                JOptionPane.showMessageDialog(null, "Invalid Credentials");
            }
        }else{
            if(!uName.isEmpty() && pWord.isEmpty()){
                JOptionPane.showMessageDialog(null, "Password Required.");
            }else if(uName.isEmpty() && !pWord.isEmpty()){
                JOptionPane.showMessageDialog(null, "UserName Required.");
            }else{
                JOptionPane.showMessageDialog(null, "Empty Field is not accepted");
            }
        }
    }
    public void logAdmin() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminPanel.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);  
        Stage primaryStage = new Stage();
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Water Billing System");
        primaryStage.setOpacity(1);
        primaryStage.show();
        Stage stage = (Stage) loginFormContainer.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void showPassword(ActionEvent event) {
        if(checkBox.isSelected()){
            passwordField.setPromptText(passwordField.getText());
            passwordField.setText("");
        }else{
            passwordField.setText(passwordField.getPromptText());
            passwordField.setPromptText("Password");
        }
    }
}
