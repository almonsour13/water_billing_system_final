/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view.meterReading;

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
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import model.meterReading.MeterReading;
import model.meterReading.MeterReadingModel;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class MeterReadingEditHistoryController implements Initializable {
    
    ObservableList<String> months = 
                FXCollections.observableArrayList(
                        "Jan","Feb","Mar","Apr","May","Jun",
                        "Jul","Aug","Sep","Oct","Nov","Dec");
    @FXML
    private TextField searchVal;
    @FXML
    private ChoiceBox<String> monthChoiceBox;
    @FXML
    private ChoiceBox<String> yearChoiceBox;
    @FXML
    private TableView<MeterReading> editReadingTable1;
    @FXML
    private TableColumn<MeterReading, ?> noCol;
    @FXML
    private TableColumn<MeterReading, ?> meterNoCol;
    @FXML
    private TableColumn<MeterReading, ?> nameCol;
    @FXML
    private TableColumn<MeterReading, ?> prevReadingCol;
    @FXML
    private TableView<MeterReading> editReadingTable2;
    @FXML
    private TableColumn<MeterReading, ?> newValueCol;
    @FXML
    private TableColumn<MeterReading, ?> oldValueCol;
    @FXML
    private TableView<MeterReading> editReadingTable3;
    @FXML
    private TableColumn<MeterReading, ?> editReadingDateCol;
    @FXML
    private TableColumn<MeterReading, ?> accountNameCol;
    private MeterReadingModel meterReadingModel = new MeterReadingModel();
    @FXML
    private TableColumn<MeterReading, ?> reasonCol;
    private static BorderPane mainPanel = new BorderPane();
    private static Pane pageSetter = new Pane();
    /**
     * Initializes the controller class.
     */
    public void mainPanel(BorderPane mainPanel){
        this.mainPanel = mainPanel;
    }
    public void pageSetter(Pane pageSetter){
        this.pageSetter = pageSetter;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //table 1
        noCol.setCellValueFactory(new PropertyValueFactory<>("no"));
        meterNoCol.setCellValueFactory(new PropertyValueFactory<>("MeterNo"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        prevReadingCol.setCellValueFactory(new PropertyValueFactory<>("PrevReading"));
        //table 2
        oldValueCol.setCellValueFactory(new PropertyValueFactory<>("OriginalValue"));
        newValueCol.setCellValueFactory(new PropertyValueFactory<>("NewValue"));
        //table 3
        editReadingDateCol.setCellValueFactory(new PropertyValueFactory<>("RequestDate"));
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("Reason"));
        accountNameCol.setCellValueFactory(new PropertyValueFactory<>("CollectorName"));
        try {
            setChoices();
            showEditReadingHistory();
        } catch (SQLException ex) {
            Logger.getLogger(MeterReadingEditHistoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        monthChoiceBox.setOnAction(this::monthChoice);
        yearChoiceBox.setOnAction(this::yearChoice);
    }    
    public void choiceBox(ActionEvent event){
        try {
            showEditReadingHistory();
        } catch (SQLException ex) {
        }
    }
    public void setChoices() throws SQLException {      
        yearChoiceBox.getItems().clear();
        yearChoiceBox.getItems().add("All");
        for(MeterReading meterReading: meterReadingModel.getEditReadingHistory()){
            String splitDate[] = String.valueOf(meterReading.getReadingDate()).split("-");
            if(!yearChoiceBox.getItems().contains(splitDate[0])){
                yearChoiceBox.getItems().add(splitDate[0]);
            }
        }
        yearChoiceBox.setValue(String.valueOf(LocalDate.now().getYear()));
     
        monthChoiceBox.getItems().clear();
        monthChoiceBox.setValue(months.get(LocalDate.now().getMonthValue() - 1));
        monthChoiceBox.getItems().add("All");
        for (MeterReading meterReading: meterReadingModel.getEditReadingHistory()) {
            String splitDate[] = String.valueOf(meterReading.getRequestDate()).split("-");
            if (!monthChoiceBox.getItems().contains(months.get(Integer.parseInt(splitDate[1]) - 1))
              && yearChoiceBox.getValue().equals(splitDate[0])) {
                monthChoiceBox.getItems().add(months.get(Integer.parseInt(splitDate[1]) - 1));
            }
        }
    }
    public void monthChoice(ActionEvent event){
        try {
            showEditReadingHistory();
        } catch (SQLException ex) {
        }
    }
    public void yearChoice(ActionEvent event){
        monthChoiceBox.getItems().clear();
        monthChoiceBox.setValue("All");
        monthChoiceBox.getItems().add("All");
        try {
            for (MeterReading meterReading: meterReadingModel.getEditReadingHistory()) {
                String splitDate[] = String.valueOf(meterReading.getRequestDate()).split("-");
                if (!monthChoiceBox.getItems().contains(months.get(Integer.parseInt(splitDate[1]) - 1))
                && yearChoiceBox.getValue().equals(splitDate[0])) {
                    monthChoiceBox.getItems().add(months.get(Integer.parseInt(splitDate[1]) - 1));
                }
            }
            showEditReadingHistory();
        } catch (SQLException ex) {
          
        }
    }
    public void showEditReadingHistory() throws SQLException{
        ObservableList<MeterReading> data = FXCollections.observableArrayList();
        data = meterReadingModel.getEditReadingHistory();
        editReadingTable1.getItems().addAll(data);
        editReadingTable2.getItems().addAll(data);
        editReadingTable3.getItems().addAll(data);
    }
    @FXML
    private void search(ActionEvent event) {
        try {
            showEditReadingHistory();
        } catch (SQLException ex) {
        }
    }
    
}
