/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller.consumer;

import controller.ConsumerController;
import controller.accountLoggedSetter.LoggedAccountSetter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.consumer.Consumer;
import model.consumer.ConsumerModel;
import view.consumer.ModalController;
import view.consumer.ProceedAddingController;
import view.settings.settingPages.SystemLogsModel;


/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class AddMeterNoController implements Initializable {
    private ConsumerModel consumerModel;
    private ConsumerController consumerController;
    @FXML
    private TableView<Consumer> meterTable;
    
    ObservableList<Consumer> data;
    @FXML
    private TableColumn<Consumer, Integer> meterNo;
    @FXML
    private TableColumn<Consumer, LocalDate> installationDate;
    @FXML
    private TableColumn<Consumer, Integer> status;
    @FXML
    private Label consumerName;
    @FXML
    private Label purok;
    @FXML
    private Label postalCode;
    @FXML
    private TextField setMeterNumber;
    @FXML
    private Button submit;
    @FXML
    private Button cancel;
    @FXML
    private DatePicker setInstallationDate;
    private int cID;
    @FXML
    private TextField setMeterLcation;
    @FXML
    private TableColumn<Consumer, String> meterLocation;
    @FXML
    private VBox modalContainer;
    private  LoggedAccountSetter logAccount = new LoggedAccountSetter();
    private SystemLogsModel systemLogsModel = new SystemLogsModel();
    
    
    public AddMeterNoController(){
        consumerModel = new ConsumerModel();
    }
    public void setController(ConsumerController consumerController) throws SQLException{
        this.consumerController = consumerController;
    }
    public void setConsumer(int id) throws SQLException{
       this.cID = id;
       Consumer consumer = consumerModel.getConsumerAddress(id);
       consumerName.setText(consumer.getName());
       purok.setText(consumer.getPurok());
       postalCode.setText(consumer.getPostalCode());
       showMeterNoTable();
    }
    public void showMeterNoTable() throws SQLException{
       meterTable.getItems().clear();     
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        meterNo.setCellValueFactory(new PropertyValueFactory<>("MeterNo"));
        meterLocation.setCellValueFactory(new PropertyValueFactory<>("MeterLocation"));
        installationDate.setCellValueFactory(new PropertyValueFactory<>("InstallationDate"));
        status.setCellFactory(new Callback<TableColumn<Consumer, Integer>, TableCell<Consumer, Integer>>() {
            @Override
            public TableCell<Consumer, Integer> call(TableColumn<Consumer, Integer> param) {
                return new TableCell<Consumer, Integer>() {
                    final ChoiceBox<String> status = new ChoiceBox();
                   
                    {
                        status.setItems(FXCollections.observableArrayList("Active","Disconnected","Inactive"));
                        status.getSelectionModel().selectedItemProperty().addListener((observable, oldItem, newItem) -> {
                            if (newItem != null) {
                                Consumer selectedMeterNo = getTableView().getItems().get(getIndex());

                                try {
                                    String statusValue = newItem;
                                    if (!statusValue.equals(selectedMeterNo.getStatus())) {
                                        int status = 0;
                                        if(statusValue.equals("Active")){
                                            status = 1;
                                        }else if(statusValue.equals("Disconnected")){
                                            status = 2;    
                                        }else if(statusValue.equals("Transferred")){
                                            status = 3;
                                        }else if(statusValue.equals("inactive")){
                                            status = 4;
                                        }
                                        consumerModel.updateMeterNoStatus(selectedMeterNo.getMeterID(), status);
                                        showMeterNoTable();
                                    }
                                } catch (SQLException ex) {
                                    Logger.getLogger(ConsumerController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });

                    }

                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Consumer consumer = getTableView().getItems().get(getIndex());
                            String statusValue = consumer.getStatus();
                            
                            if(statusValue.equals("Active")){
                                
                            }else if(statusValue.equals("Disconnected")){
                                
                            }else if(statusValue.equals("Transferred")){
                                
                            }else if(statusValue.equals("inactive")){
                                
                            }
                            status.setValue(statusValue);
                            status.getStyleClass().clear();
                            status.getStyleClass().add("statusChoiceBox");
                            status.getStyleClass().add(statusValue.equals("Active")?"active":"inactive");
                            status.setStyle("-fx-pref-width: 60;-fx-pref-height: 30;");

                            
                            HBox buttonsBox = new HBox(new HBox(status));
                            buttonsBox.setAlignment(Pos.CENTER);
                       
                            setGraphic(buttonsBox);
                           // setText(statusValue);
                        }
                    }
                };
            }
        });        
        
       
        setInstallationDate.setValue(LocalDate.now());
    }    

    @FXML
    private void submit(ActionEvent event) throws SQLException, IOException {
        if(setMeterNumber.getText().isEmpty() || setMeterLcation.getText().isEmpty()){
            modal("Fill All Fields.");
            return;
        }
        int meterChecker = consumerModel.checkAddMeterNo(setMeterNumber.getText());
        if(meterChecker == 1){
            modal("Meter number is already exist and active. Cannot add.");
            return;
        }else if(meterChecker == 2){
            conModal("Meter number is already exist but its inactive and owner is active.");
            return;
        }else if(meterChecker == 3){
            conModal("Meter number is already exist but its inactive and the user also inactive.");
            return;
        }else if(meterChecker == 4){
            modal("Meter number is exist but its inactive or broken. Cannot Add.");
            return;
        }else if(meterChecker == 5){
            conModal("Meter number is exist and active but disconnected.");
            return;
        }else{
            consumerModel.insertMeterNo(cID,setMeterNumber.getText(),setMeterLcation.getText(),setInstallationDate.getValue());
            modal("Meter number added successfully.");
            
            systemLogsModel.insertLog(logAccount.getAccount(), "Successfully Added meter number "+setMeterNumber.getText()+" to consumer("+consumerName.getText()+")");
        
            Stage stage = (Stage) modalContainer.getScene().getWindow();
            stage.close();
            return;
        }  
    }
    
    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) modalContainer.getScene().getWindow();
        stage.close();
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
    public void conModal(String promptLabel) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/consumer/proceedAdding.fxml"));
        Parent rootNode = loader.load();
        ProceedAddingController procAddCon = loader.getController();
        procAddCon.setContainer(modalContainer);
        procAddCon.setPromptLabel(promptLabel);
        procAddCon.setValue(cID,setMeterNumber.getText(),setMeterLcation.getText(),setInstallationDate.getValue());
        Stage modalWindow = new Stage();
        modalWindow.setResizable(false);
        modalWindow.setScene(new Scene(rootNode));
        modalWindow.initModality(Modality.APPLICATION_MODAL);
        modalWindow.centerOnScreen();
        modalWindow.setOpacity(1);
        modalWindow.show();
    }
}
