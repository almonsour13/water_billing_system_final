/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view.meterReading;

import controller.MeterReadingController;
import controller.accountLoggedSetter.LoggedAccountSetter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.meterReading.MeterReading;
import model.meterReading.MeterReadingModel;
import view.consumer.ModalController;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class EditReadingController {

    @FXML
    private VBox container;
    @FXML
    private TextField originalValue;
    @FXML
    private TextField newValue;
    private MeterReadingModel meterReadingModel;
    private int meterReadingID;
    @FXML
    private TextArea reasonField;
    private  LoggedAccountSetter logAccount = new LoggedAccountSetter();
    @FXML
    private Button saveBtn;
    private MeterReadingController meterCon;
    private int prevValue;
    @FXML
    private TextField prevValueField;
    
    private static BorderPane mainPanel = new BorderPane();
    private static Pane pageSetter = new Pane();
    
    public EditReadingController(){
        meterReadingModel = new MeterReadingModel();
    }
    public void setMeterReading(int meterReadingID,int prevValue, int oldValue) throws SQLException{
        this.meterReadingID = meterReadingID;
        this.prevValue = prevValue;
        prevValueField.setText(String.valueOf(prevValue));
        originalValue.setText(String.valueOf(oldValue));
    }
    public void setController(MeterReadingController meterCon){
        this.meterCon = meterCon;
    }
    public void mainPanel(BorderPane mainPanel){
        this.mainPanel = mainPanel;
    }
    public void pageSetter(Pane pageSetter){
        this.pageSetter = pageSetter;
    }
    @FXML
    private void save(ActionEvent event) throws SQLException, IOException {
        int newValue = Integer.parseInt(this.newValue.getText());
        int orginalValue = Integer.parseInt(this.originalValue.getText());
        String reason = reasonField.getText();
        if(newValue>= prevValue){
            meterReadingModel.insertNewValue(meterReadingID,logAccount.getAccount(),prevValue,orginalValue,newValue,reason);
            meterCon.loadMeterReading();
            modal("Current Reading Value Updated Successfully");
            stageClose();
        }
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
    private void newValue(KeyEvent event) {
        int newValue = this.newValue.getText().equals("")?0:Integer.parseInt(this.newValue.getText());
        if(newValue>=prevValue && !reasonField.getText().isEmpty()){
             saveBtn.setDisable(false);
        }else{
            saveBtn.setDisable(true);
        }
    }
    
    public void modal(String prompt) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Modal.fxml"));
        VBox modal = loader.load();
        ModalController modCOn = loader.getController();
        modCOn.setPromptLabel(prompt);
        // Create a new stage for the internal frame
        Stage internalFrame = new Stage();
        internalFrame.initStyle(StageStyle.UTILITY); // Customize the stage style
        internalFrame.initModality(Modality.WINDOW_MODAL);
        internalFrame.initOwner(mainPanel.getScene().getWindow()); // Set the owner stage (mainPanel's stage)

        Scene scene = new Scene(modal);
        internalFrame.setScene(scene);

        internalFrame.setTitle("Sucess");
        internalFrame.setResizable(false);

        internalFrame.show();
    }

}
