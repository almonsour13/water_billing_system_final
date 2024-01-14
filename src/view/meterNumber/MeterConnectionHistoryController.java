/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view.meterNumber;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.meterNumber.MeterNumber;
import model.meterNumber.MeterNumberModel;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class MeterConnectionHistoryController implements Initializable {
    private MeterNumberModel meterNumberModel = new MeterNumberModel();
    ObservableList<String> months = 
                FXCollections.observableArrayList(
                        "Jan","Feb","Mar","Apr","May","Jun",
                        "Jul","Aug","Sep","Oct","Nov","Dec");

    @FXML
    private ChoiceBox<String> monthChoiceBox;
    @FXML
    private ChoiceBox<String> yearChoiceBox;
    @FXML
    private ChoiceBox<String> statusChoiceBox;
    @FXML
    private ChoiceBox<String> meterLocChoiceBox;
    @FXML
    private TableView<MeterNumber> meterNumberTable;
    @FXML
    private TableColumn<MeterNumber, ?> noCol;
    @FXML
    private TableColumn<MeterNumber, ?> meterLocationCol;
    @FXML
    private TableColumn<MeterNumber, ?> installationDateCol;
    @FXML
    private TableColumn<MeterNumber, ?> nameCol;
    @FXML
    private TableColumn<MeterNumber, Integer> statusCol;
    @FXML
    private TableColumn<MeterNumber, String> transferredToCol;
    private int meterID;
    private static BorderPane mainPanel = new BorderPane();
    private static Pane pageSetter = new Pane();
    @FXML
    private Button backBtn;
    @FXML
    private Label meterNumberLabel;
    
    /**
     * Initializes the controller class.
     */
    public void setMeterNumber(int meterID) throws SQLException{
        this.meterID = meterID;
        setChoices();
        meterConnectionHistory();
    }
    public void mainPanel(BorderPane mainPanel){
        this.mainPanel = mainPanel;
    }
    public void pageSetter(Pane pageSetter){
        this.pageSetter = pageSetter;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        noCol.setCellValueFactory(new PropertyValueFactory<>("No"));
        meterLocationCol.setCellValueFactory(new PropertyValueFactory<>("MeterLoc"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        installationDateCol.setCellValueFactory(new PropertyValueFactory<>("InstallationDate"));
        statusCol.setCellFactory(new Callback<TableColumn<MeterNumber, Integer>, TableCell<MeterNumber, Integer>>() {
            @Override
            public TableCell<MeterNumber, Integer> call(TableColumn<MeterNumber, Integer> param) {
                return new TableCell<MeterNumber, Integer>() {
                    final ChoiceBox<String> status = new ChoiceBox();
                   
                    {
                        status.setItems(FXCollections.observableArrayList("Active","Inactive","Disconnected"));
                        status.getSelectionModel().selectedItemProperty().addListener((observable, oldItem, newItem) -> {
                            if (newItem != null) {
                                MeterNumber meterNo = getTableView().getItems().get(getIndex());

                                    String statusValue = newItem;
                                    if (!statusValue.equals(meterNo.getcMStatus())) {
                                        try {
                                            int cmStatus = 0;
                                            if(statusValue.equals("Active")){
                                                cmStatus = 1;
                                            }else if(statusValue.equals("Inactive")){
                                                cmStatus = 4;
                                            }else if(statusValue.equals("Disconnected")){
                                                cmStatus = 2;
                                            }
                                            meterNumberModel.updateConnectionStatus(meterNo.getCmID() , cmStatus);
                                             meterConnectionHistory();
                                         //     systemLogsModel.insertLog(logAccount.getAccount(), "Successfully Changed status of meter number "+meterNo.getMeterNumber()+" to inactive for consumer ("+meterNo.getName()+") ");
                                        } catch (SQLException ex) {
                                            Logger.getLogger(ConnectionHistoryController.class.getName()).log(Level.SEVERE, null, ex);
                                        }
        
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
                            }else if(statusValue.equals("Disconnected")){
                                status.getStyleClass().add("orange");
                            }else if(statusValue.equals("Transferred")){
                                status.getStyleClass().add("transferred");
                            }
                            status.setDisable(true);
//                            try {
//                                if(meterNo.getcMStatus().equals("Transferred")
//                                        || meterNo.getcMStatus().equals("Inactive") || meterNumberModel.checkConsumerStatus(meterNo.getcID()) == 2)
//                                {
//                                    status.setDisable(true);
//                                }else{
//                                    status.setDisable(false);
//                                }
//                            } catch (SQLException ex) {
//                                Logger.getLogger(MeterNumberController.class.getName()).log(Level.SEVERE, null, ex);
//                            }
                            
                            HBox buttonsBox = new HBox(new HBox(status));
                            buttonsBox.setAlignment(Pos.CENTER);
                       
                            setGraphic(buttonsBox);
                        }
                    }
                };
            }
        });        
        transferredToCol.setCellFactory(new Callback<TableColumn<MeterNumber, String>, TableCell<MeterNumber, String>>() {
            @Override
            public TableCell<MeterNumber, String> call(TableColumn<MeterNumber, String> param) {
                return new TableCell<MeterNumber, String>() {
                    final Text text = new Text();
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            MeterNumber meterNumber = getTableView().getItems().get(getIndex());
                            try {
                                MeterNumber consumer = meterNumberModel.getTransferredMeterConsumer(meterNumber.getCmID());
                                if (consumer != null && meterNumber.getcMStatus().equals("Transferred")) {
                                    text.setText(consumer.getName());
                                } else {
                                    text.setText("N/A");
                                    text.setFill(Color.web("gray"));
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(ConnectionHistoryController.class.getName()).log(Level.SEVERE, null, ex);
                                setText("Error"); // or any appropriate default value
                            }
                            setGraphic(text);
                        }
                    }
                };
            }
        });

        monthChoiceBox.setOnAction(this::monthChoice);
        yearChoiceBox.setOnAction(this::yearChoice);
        statusChoiceBox.setOnAction(this::choiceBox);
        meterLocChoiceBox.setOnAction(this::choiceBox);
    }    
    public void monthChoice(ActionEvent event){
        try {
            meterConnectionHistory();
        } catch (SQLException ex) {
           
        }
    }
    public void setChoices() throws SQLException {      
        yearChoiceBox.getItems().clear();
        yearChoiceBox.getItems().add("All");
        for(MeterNumber meterNumber:meterNumberModel.getMeterConnectionHistoryByID(meterID)){
            String splitDate[] = String.valueOf(meterNumber.getInstallationDate()).split("-");
            if(!yearChoiceBox.getItems().contains(splitDate[0])){
                yearChoiceBox.getItems().add(splitDate[0]);
                meterNumberLabel.setText("Meter Number "+meterNumber.getMeterNumber()+" Connection History:");
            }
        }
        
        yearChoiceBox.setValue("All");
     
        monthChoiceBox.getItems().clear();
        monthChoiceBox.setValue("All");
        monthChoiceBox.getItems().add("All");
        for (MeterNumber meterNumber:meterNumberModel.getMeterConnectionHistoryByID(meterID)) {
            String splitDate[] = String.valueOf(meterNumber.getInstallationDate()).split("-");
            if (!monthChoiceBox.getItems().contains(months.get(Integer.parseInt(splitDate[1]) - 1))
              && yearChoiceBox.getValue().equals(splitDate[0])) {
                monthChoiceBox.getItems().add(months.get(Integer.parseInt(splitDate[1]) - 1));
            }
        }
        statusChoiceBox.getItems().clear();
        statusChoiceBox.setValue("All");
        statusChoiceBox.getItems().addAll("All","Disconnected","Inactive");
        
        meterLocChoiceBox.getItems().clear();
        meterLocChoiceBox.setValue("All");
        meterLocChoiceBox.getItems().add("All");
        for (MeterNumber meterNumber:meterNumberModel.getMeterNumberByConsumerID(meterID)) {
            if (!meterLocChoiceBox.getItems().contains(meterNumber.getMeterLoc())
                ) {
                meterLocChoiceBox.getItems().add(meterNumber.getMeterLoc());
            }
        };
        
    }
    public void yearChoice(ActionEvent event){
        monthChoiceBox.getItems().clear();
        monthChoiceBox.setValue("All");
        monthChoiceBox.getItems().add("All");
        try {
            for (MeterNumber meterNumber:meterNumberModel.getMeterConnectionHistoryByID(meterID)) {
                String splitDate[] = String.valueOf(meterNumber.getInstallationDate()).split("-");
                if (!monthChoiceBox.getItems().contains(months.get(Integer.parseInt(splitDate[1]) - 1))
                && yearChoiceBox.getValue().equals(splitDate[0])) {
                    monthChoiceBox.getItems().add(months.get(Integer.parseInt(splitDate[1]) - 1));
                }
            }
            meterConnectionHistory();
        } catch (SQLException ex) {
          
        }
    }
    public void choiceBox(ActionEvent event){
        try {
            meterConnectionHistory();
        } catch (SQLException ex) {
        }
    }
    public void meterConnectionHistory() throws SQLException {
        meterNumberTable.getItems().clear();

        int month = "All".equals(monthChoiceBox.getValue()) ? 0 : months.indexOf(monthChoiceBox.getValue()) + 1;
        int year = "All".equals(yearChoiceBox.getValue()) ? 0 : Integer.parseInt(yearChoiceBox.getValue());

        String selectedStatus = statusChoiceBox.getValue();
        int status;

        if (selectedStatus != null) {
            switch (selectedStatus) {
                case "Active":
                    status = 1;
                    break;
                case "Disconnected":
                    status = 2;
                    break;
                case "Transferred":
                    status = 3;
                    break;
                case "Inactive":
                    status = 4;
                    break;
                default:
                    status = 0;
            }
        } else {
            status = 0; // Default value if selectedStatus is null
        }

        //meterNumberTable.getItems().addAll(meterNumberModel.getMeterConnectionHistoryByID(meterID));
         meterNumberTable.getItems().addAll(meterNumberModel.filterMeterConnectionHistoryByI(meterID, month, year, status, meterLocChoiceBox.getValue()));
    }
    private void search(KeyEvent event) throws SQLException {
        meterConnectionHistory();
    }

    @FXML
    private void back(ActionEvent event) throws IOException {
        pageSetter.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/meterNumber/meterNumber.fxml"));
        Pane root = loader.load();
        MeterNumberController conPro = loader.getController();
        conPro.mainPanel(mainPanel);
        conPro.pageSetter(pageSetter);
        pageSetter.getChildren().setAll(root);
    }
    
}
