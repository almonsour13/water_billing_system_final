/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view.consumer.consumerProfile.pages;

import controller.ConsumerController;
import controller.accountLoggedSetter.LoggedAccountSetter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import model.meterNumber.MeterNumber;
import model.meterNumber.MeterNumberModel;
import view.meterNumber.MeterNumberController;
import view.settings.settingPages.SystemLogsModel;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class MeterNoController implements Initializable {

    private MeterNumberModel meterNumberModel = new MeterNumberModel();
    @FXML
    private TableColumn<MeterNumber, Integer> noCol;
    @FXML
    private TableColumn<MeterNumber, String> meterNumberCol;
    @FXML
    private TableColumn<MeterNumber, String> meterLocationCol;
    @FXML
    private TableColumn<MeterNumber, String> nameCol;
    @FXML
    private TableColumn<MeterNumber, LocalDate> installationDateCol;
    @FXML
    private TableColumn<MeterNumber, Integer> totalConsumptionCol;
    @FXML
    private TableColumn<MeterNumber, Integer> statusCol;
    @FXML
    private TableColumn<MeterNumber, Integer> actionCol;
    @FXML
    private TableView<MeterNumber> meterNumberTable;
    private  LoggedAccountSetter logAccount = new LoggedAccountSetter();
    private SystemLogsModel systemLogsModel = new SystemLogsModel();
    private static BorderPane mainPanel = new BorderPane();
    private static Pane pageSetter = new Pane();
    private int id;

    /**
     * Initializes the controller class.
     */
    public void setConsumer(int id) throws SQLException{
        this.id = id;
        showMeterNumber();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        noCol.setCellValueFactory(new PropertyValueFactory<>("No"));
       meterNumberCol.setCellValueFactory(new PropertyValueFactory<>("MeterNumber"));
       meterLocationCol.setCellValueFactory(new PropertyValueFactory<>("MeterLoc"));
       installationDateCol.setCellValueFactory(new PropertyValueFactory<>("InstallationDate"));
       totalConsumptionCol.setCellValueFactory(new PropertyValueFactory<>("TotalConsumption"));
       statusCol.setCellFactory(new Callback<TableColumn<MeterNumber, Integer>, TableCell<MeterNumber, Integer>>() {
            @Override
            public TableCell<MeterNumber, Integer> call(TableColumn<MeterNumber, Integer> param) {
                return new TableCell<MeterNumber, Integer>() {
                    final ChoiceBox<String> status = new ChoiceBox();
                   
                    {
                        status.setItems(FXCollections.observableArrayList("Active","Inactive"));
                        status.getSelectionModel().selectedItemProperty().addListener((observable, oldItem, newItem) -> {
                            if (newItem != null) {
                                MeterNumber meterNo = getTableView().getItems().get(getIndex());

                                try {
                                    String statusValue = newItem;
                                    if (!statusValue.equals(meterNo.getcMStatus())) {
                                       // meterNumberModel.updateMeterNoStatus(meterNo.getcID(),meterNo.getMeterID(),    meterNo.getcMStatus().equals("Active") ? 2 : 1);
                                        showMeterNumber();
                                        systemLogsModel.insertLog(logAccount.getAccount(), "Successfully Changed status of meter number "+meterNo.getMeterNumber()+" to inactive for consumer ("+meterNo.getName()+") ");
        
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
                            MeterNumber meterNo = getTableView().getItems().get(getIndex());
                            String statusValue = meterNo.getcMStatus();
                            status.setValue(statusValue);
                            status.getStyleClass().clear();
                            status.getStyleClass().add("statusChoiceBox");
                            if(statusValue.equals("Active")){
                                status.getStyleClass().add("green");
                            }else if(statusValue.equals("Inactive")){
                                status.getStyleClass().add("red");
                            }if(statusValue.equals("Disconnected")){
                                status.getStyleClass().add("orange");
                            }
                            status.setStyle("-fx-pref-width: 100;-fx-pref-height: 30;");
                            try {
                                if(meterNo.getcMStatus().equals("Disconnected")
                                        || meterNo.getcMStatus().equals("Inactive") && meterNumberModel.checkConsumerStatus(meterNo.getcID()) == 2){
                                    status.setDisable(true);
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(MeterNumberController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            HBox buttonsBox = new HBox(new HBox(status));
                            buttonsBox.setAlignment(Pos.CENTER);
                       
                            setGraphic(buttonsBox);
                           // setText(statusValue);
                        }
                    }
                };
            }
        });        
       actionCol.setCellFactory(new Callback<TableColumn<MeterNumber, Integer>, TableCell<MeterNumber, Integer>>() {
            @Override
            public TableCell<MeterNumber, Integer> call(TableColumn<MeterNumber, Integer> param) {
                return new TableCell<MeterNumber, Integer>() {
                    final Button viewBillHistBtn = new Button("VBH");
                    final Button viewPaymentHistBtn = new Button("BPH");

                        {
                            viewBillHistBtn.getStyleClass().add("actionBtn");
                            viewPaymentHistBtn.getStyleClass().add("actionBtn-arc");
                            
                            viewBillHistBtn.setOnAction(event -> {
                            });
                            viewPaymentHistBtn.setOnAction(event -> {
                                MeterNumber selectedBill = getTableView().getItems().get(getIndex());
                            });
                        }

                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            HBox buttonsBox = new HBox(5, viewBillHistBtn, viewPaymentHistBtn);
                            buttonsBox.setAlignment(Pos.CENTER);

                            setGraphic(buttonsBox);
                            setText(null);
                        }
                    }
                };
            }
        });
        
    }  
      public void showMeterNumber() throws SQLException{
        meterNumberTable.getItems().clear();
        meterNumberTable.getItems().addAll(meterNumberModel.getMeterNumberByConsumerID(id));
    }

    public void mainPanel(BorderPane mainPanel){
        this.mainPanel = mainPanel;
    }
    public void pageSetter(Pane pageSetter){
        this.pageSetter = pageSetter;
    }
    
}
