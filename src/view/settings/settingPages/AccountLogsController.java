/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view.settings.settingPages;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
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
import javafx.scene.input.KeyEvent;
import view.settings.Settings;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class AccountLogsController implements Initializable {
    
    ObservableList<String> months = 
                FXCollections.observableArrayList(
                        "Jan","Feb","Mar","Apr","May","Jun",
                        "Jul","Aug","Sep","Oct","Nov","Dec");
    @FXML
    private TableColumn<SystemLogs, String> name;
    @FXML
    private TableColumn<SystemLogs, String> role;
    @FXML
    private TableColumn<SystemLogs, LocalDate> date;
    @FXML
    private TableColumn<SystemLogs, LocalTime> time;
    @FXML
    private TableColumn<SystemLogs, String> activityDescription;
    private SystemLogsModel systemLodsModel = new SystemLogsModel();
     ObservableList<SystemLogs> data;
    @FXML
    private TableView<SystemLogs> logTable;
    @FXML
    private TextField searchValue;
    @FXML
    private ChoiceBox<String> monthChoices;
    @FXML
    private ChoiceBox<String> yearChoices;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       name.setCellValueFactory(new PropertyValueFactory<>("Name"));
       role.setCellValueFactory(new PropertyValueFactory<>("Role"));
       date.setCellValueFactory(new PropertyValueFactory<>("Date"));
       time.setCellValueFactory(new PropertyValueFactory<>("Time"));
       activityDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        try {
            setChoices();
            showLogs();
        } catch (SQLException ex) {
            Logger.getLogger(AccountLogsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        monthChoices.setOnAction(this::monthChoicesEvent);
        yearChoices.setOnAction(this::yearChoicesEvent);
    }  
    public void showLogs() throws SQLException{
        int month = monthChoices.getValue()== "All"?0: months.indexOf(monthChoices.getValue())+1;
        int year = yearChoices.getValue()== "All"?0: Integer.parseInt(yearChoices.getValue());
        logTable.getItems().clear();
        data = systemLodsModel.filterSystemLogs(month, year, searchValue.getText());
        logTable.getItems().addAll(data);
    }
    public void setChoices() throws SQLException{
        yearChoices.getItems().clear();
        yearChoices.getItems().add("All");
        for(SystemLogs systemsLogs:systemLodsModel.getSystemsLog()){
            String splitDate[] = String.valueOf(systemsLogs.getDate()).split("-");
            if(!yearChoices.getItems().contains(splitDate[0])){
                yearChoices.getItems().add(splitDate[0]);
            }
        }
        yearChoices.setValue(String.valueOf(LocalDate.now().getYear()));
        monthChoices.getItems().clear();
        monthChoices.setValue(months.get(LocalDate.now().getMonthValue() - 1));
        monthChoices.getItems().add("All");
        for (SystemLogs systemsLogs:systemLodsModel.getSystemsLog()) {
            String splitDate[] = String.valueOf(systemsLogs.getDate()).split("-");
            if (!monthChoices.getItems().contains(months.get(Integer.parseInt(splitDate[1]) - 1))
              && yearChoices.getValue().equals(splitDate[0])) {
                monthChoices.getItems().add(months.get(Integer.parseInt(splitDate[1]) - 1));
            }
        }
    }
    public void monthChoicesEvent(ActionEvent event){
        try {
            showLogs();
        } catch (SQLException ex) {
            Logger.getLogger(AccountLogsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void yearChoicesEvent(ActionEvent event){
        monthChoices.getItems().clear();
        monthChoices.setValue("All");
        monthChoices.getItems().add("All");
        try {
            for (SystemLogs systemsLogs:systemLodsModel.getSystemsLog()) {
                String splitDate[] = String.valueOf(systemsLogs.getDate()).split("-");
                if (!monthChoices.getItems().contains(months.get(Integer.parseInt(splitDate[1]) - 1))
                && yearChoices.getValue().equals(splitDate[0])) {
                    monthChoices.getItems().add(months.get(Integer.parseInt(splitDate[1]) - 1));
                }
            }
            showLogs();
        } catch (SQLException ex) {
          
        }
    }
    @FXML
    private void searchConsumer(KeyEvent event) throws SQLException {
        showLogs();
    }
    
}
