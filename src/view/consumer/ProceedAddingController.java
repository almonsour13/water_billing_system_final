/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view.consumer;

import controller.ConsumerController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.consumer.ConsumerModel;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class ProceedAddingController implements Initializable {

    @FXML
    private VBox container;
    @FXML
    private Label promptLabel;
    private static VBox conCon;
    private int cID;
    private String meterNumber;
    private String meterLocation;
    private LocalDate installationDate;
    private ConsumerModel conMod = new ConsumerModel();
    
    public void setContainer(VBox conCon){
        this.conCon = conCon;
    }
    public void setPromptLabel(String label){
        promptLabel.setText(label);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void setValue(int cID, String meterNumber, String meterLcation,LocalDate installationDate){
        this.cID = cID;
        this.meterNumber = meterNumber;
        this.meterLocation = meterLcation;
        this.installationDate = installationDate;
    }
    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) container.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void proceedAdding(ActionEvent event) throws IOException, SQLException {
        boolean checker = conMod.proceedAdding(cID, meterNumber, meterLocation, installationDate);
        if(checker == true){
            Stage stage = (Stage) container.getScene().getWindow();
            stage.close();
            stage = (Stage) conCon.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/consumer/Modal.fxml"));
            Parent rootNode = loader.load();
            ModalController modCOn = loader.getController();
            modCOn.setPromptLabel("Meter number added successfully.");
            Stage modalWindow = new Stage();
            modalWindow.setResizable(false);
            modalWindow.setScene(new Scene(rootNode));
            modalWindow.initModality(Modality.APPLICATION_MODAL);
            modalWindow.centerOnScreen();
            modalWindow.setOpacity(1);
            modalWindow.show();
            
        }
    }
    
}
