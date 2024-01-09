/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view.settings;

import controller.ConsumerController;
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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class SettingsController implements Initializable {

    @FXML
    private Button companyBtn;
    @FXML
    private Button billingBtn;
    @FXML
    private VBox settingSidebar;
    @FXML
    private VBox settingPageContainer;
    @FXML
    private Button accountLogBtn;
    @FXML
    private Button backUpBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadPage("company");
        } catch (IOException ex) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        updateButtonStyles(companyBtn);
    }    
    private void updateButtonStyles(Button clickedButton) {
        for (Node node : settingSidebar.getChildren()) {
            if (node instanceof Button button) {
                if (button == clickedButton) {
                    button.getStyleClass().setAll("menu-btn-clicked");
                } else {
                    button.getStyleClass().setAll("menu-btn");
                }
            }
        }
    }
     public void loadPage(String page) throws IOException, SQLException {
        FXMLLoader loader = null;
        settingPageContainer.getChildren().clear();
        
        if(page.equals("company")){
             loader = new FXMLLoader(getClass().getResource("settingPages/companyInformation.fxml"));
        }else if(page.equals("billing")){
             loader = new FXMLLoader(getClass().getResource("settingPages/billingConfiguration.fxml"));
        }else if(page.equals("accountLogs")){
             loader = new FXMLLoader(getClass().getResource("settingPages/accountLogs.fxml"));
        }
        
        Pane root = loader.load();
        settingPageContainer.getChildren().setAll(root);
    }
    @FXML
    private void company(ActionEvent event) throws IOException, SQLException {
        loadPage("company");
        updateButtonStyles(companyBtn);
    }
    @FXML
    private void billing(ActionEvent event) throws IOException, SQLException {
        loadPage("billing");
        updateButtonStyles(billingBtn);
    }

    @FXML
    private void accountLog(ActionEvent event) throws IOException, SQLException {
        loadPage("accountLogs");
        updateButtonStyles(accountLogBtn);
    }

    @FXML
    private void backUp(ActionEvent event) throws IOException, SQLException {
        loadPage("company");
        updateButtonStyles(companyBtn);
    }
    
}
