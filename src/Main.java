/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */

import controller.LoginFormController;
import controller.accountLoggedSetter.LoggedAccountSetter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.FormModel;

/**
 *
 * @author Administrator
 */
public class Main extends Application {
    
    private  LoggedAccountSetter logAccount = new LoggedAccountSetter();
    private FormModel formModel = new FormModel();
    @Override
    public void start(Stage primaryStage) throws IOException {
        Image icon = new Image(getClass().getResourceAsStream("assets/logo.png"));
        primaryStage.getIcons().add(icon);
        FXMLLoader loader = null;
        if(logAccount.checkAccount()){
            try {
                int accountType = formModel.checkAccountType(logAccount.getAccount());
                if(accountType == 1){
                   loader = new FXMLLoader(getClass().getResource("view/adminPanel.fxml"));
                }else if(accountType == 2){
                     loader = new FXMLLoader(getClass().getResource("view/CollectorPanel.fxml"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(LoginFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            loader = new FXMLLoader(getClass().getResource("view/form/loginForm.fxml"));           
        }

        Parent root = loader.load();
                   
        Scene scene = new Scene(root);   
        
        primaryStage.setResizable(false);
        primaryStage.setTitle("Water Billing System");
        primaryStage.setScene(scene);
       //    primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
