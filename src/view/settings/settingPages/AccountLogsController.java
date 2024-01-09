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
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import view.settings.Settings;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class AccountLogsController implements Initializable {

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
    @FXML
    private TableColumn<SystemLogs, Integer> action;
    private SystemLogsModel systemLodsModel = new SystemLogsModel();
     ObservableList<SystemLogs> data;
    @FXML
    private TableView<SystemLogs> logTable;
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
            showLogs();
        } catch (SQLException ex) {
            Logger.getLogger(AccountLogsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    public void showLogs() throws SQLException{
        logTable.getItems().clear();
        data = systemLodsModel.getSystemsLog();
        logTable.getItems().addAll(data);
    }
    
}
