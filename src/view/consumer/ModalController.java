/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view.consumer;

import controller.ConsumerController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class ModalController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private static ConsumerController conCon;
    @FXML
    private Label promptLabel;
    @FXML
    private Button okBtn;
    @FXML
    private VBox container;
    
    public void setController(ConsumerController conCon){
        this.conCon = conCon;
    }
    public void setPromptLabel(String label){
        promptLabel.setText(label);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void ok(ActionEvent event) {
        Stage stage = (Stage) container.getScene().getWindow();
        stage.close();
     //   conCon.clear();
    }
    
}
