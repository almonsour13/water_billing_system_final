/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view.penalty;

import controller.MeterReadingController;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import model.payment.Payment;
import model.penalty.Penalty;
import model.penalty.PenaltyModel;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class PenaltyController implements Initializable {
    private PenaltyModel penaltyModel = new PenaltyModel();
    @FXML
    private ChoiceBox<String> monthChoiceBox;
    @FXML
    private ChoiceBox<String> yearChoiceBox;
    @FXML
    private ChoiceBox<String> statusChoiceBox;
    @FXML
    private ChoiceBox<String> meterLocChoiceBox;
    @FXML
    private TableColumn<Penalty, Integer> noCol;
    @FXML
    private TableColumn<Penalty, String> nameCol;
    @FXML
    private TableColumn<Penalty, String> meterNumCol;
    @FXML
    private TableColumn<Penalty, String> meterLocCol;
    @FXML
    private TableColumn<Penalty, LocalDate> bDateCol;
    @FXML
    private TableColumn<Penalty, Integer> bAmountCol;
    @FXML
    private TableColumn<Penalty, LocalDate> pDateCol;
    @FXML
    private TableColumn<Penalty, String> pTypeCol;
    @FXML
    private TableColumn<Penalty, String> pAmountCol;
    @FXML
    private TableColumn<Penalty, String> statusCol;
    @FXML
    private TableColumn<Penalty, Integer> actionCol;
    @FXML
    private TableView<Penalty> penaltyTable;
    @FXML
    private TableColumn<Penalty, Double> totalAmountCol;
    ObservableList<String> months = 
                FXCollections.observableArrayList(
                        "Jan","Feb","Mar","Apr","May","Jun",
                        "Jul","Aug","Sep","Oct","Nov","Dec");
    @FXML
    private TextField search;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            setChoices();
            showPenalty();
        } catch (SQLException ex) {
            Logger.getLogger(PenaltyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        noCol.setCellValueFactory(new PropertyValueFactory<>("no"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Cname"));
        meterNumCol.setCellValueFactory(new PropertyValueFactory<>("Meterno"));
        meterLocCol.setCellValueFactory(new PropertyValueFactory<>("Meterloc"));
        bDateCol.setCellValueFactory(new PropertyValueFactory<>("Billdate"));
        bAmountCol.setCellValueFactory(new PropertyValueFactory<>("Billamount"));
        pDateCol.setCellValueFactory(new PropertyValueFactory<>("Pdate"));
        pTypeCol.setCellValueFactory(new PropertyValueFactory<>("Ptype"));
        pAmountCol.setCellValueFactory(new PropertyValueFactory<>("Pamount"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("Status"));
        statusCol.setCellFactory(column -> new TableCell<Penalty, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                
                if (empty || status == null) {
                    setText(null);
                    getStyleClass().clear(); 
                } else {
                    setText(status);

                    switch (status.toLowerCase()) {
                        case "paid":
                            getStyleClass().setAll("active-cell");
                            break;
                        case "unpaid":
                            getStyleClass().setAll("disconnected-cell");
                            break;
                        default:
                            getStyleClass().setAll("default-cell");
                            break;
                    }
                }
            }
        }); 
        totalAmountCol.setCellValueFactory(new PropertyValueFactory<>("TotalAmount"));
        monthChoiceBox.setOnAction(this::monthChoice);
        yearChoiceBox.setOnAction(this::yearChoice); 
        meterLocChoiceBox.setOnAction(this::choiceBox); 
        statusChoiceBox.setOnAction(this::choiceBox); 
    }    
    public void monthChoice(ActionEvent event){
        try {
            showPenalty();
        } catch (SQLException ex) {
            Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void choiceBox(ActionEvent event){
        try {
            showPenalty();
        } catch (SQLException ex) {
        }
    }
    public void showPenalty(ActionEvent event){
        monthChoiceBox.getItems().clear();
        monthChoiceBox.setValue("All");
        monthChoiceBox.getItems().add("All");
        try {
            for (Penalty penalty:penaltyModel.getPenalty()) {
                String splitDate[] = String.valueOf(penalty.getPdate()).split("-");
                if (!monthChoiceBox.getItems().contains(months.get(Integer.parseInt(splitDate[1]) - 1))
                && yearChoiceBox.getValue().equals(splitDate[0])) {
                    monthChoiceBox.getItems().add(months.get(Integer.parseInt(splitDate[1]) - 1));
                }
            }
            showPenalty();
        } catch (SQLException ex) {
          
        }
    }
    public void setChoices() throws SQLException{
        yearChoiceBox.getItems().clear();
        yearChoiceBox.getItems().add("All");
        for(Penalty penalty:penaltyModel.getPenalty()){
            String splitDate[] = String.valueOf(penalty.getPdate()).split("-");
            if(!yearChoiceBox.getItems().contains(splitDate[0])){
                yearChoiceBox.getItems().add(splitDate[0]);
            }
        }
        yearChoiceBox.setValue(String.valueOf(LocalDate.now().getYear()));
     
        monthChoiceBox.getItems().clear();
        monthChoiceBox.setValue(months.get(LocalDate.now().getMonthValue() - 1));
        monthChoiceBox.getItems().add("All");
        for (Penalty penalty:penaltyModel.getPenalty()) {
            String splitDate[] = String.valueOf(penalty.getPdate()).split("-");
            if (!monthChoiceBox.getItems().contains(months.get(Integer.parseInt(splitDate[1]) - 1))
              && yearChoiceBox.getValue().equals(splitDate[0])) {
                monthChoiceBox.getItems().add(months.get(Integer.parseInt(splitDate[1]) - 1));
            }
        }
        meterLocChoiceBox.setValue("All");
        meterLocChoiceBox.getItems().add("All");
        for (Penalty penalty:penaltyModel.getPenalty()) {
            if (!meterLocChoiceBox.getItems().contains(penalty.getMeterloc())) {
                meterLocChoiceBox.getItems().add(penalty.getMeterloc());
            }
        }
        statusChoiceBox.setValue("All");
        statusChoiceBox.getItems().add("All");
        for (Penalty penalty:penaltyModel.getPenalty()) {
            if (!statusChoiceBox.getItems().contains(penalty.getStatus())) {
                statusChoiceBox.getItems().add(penalty.getStatus());
            }
        }
        
    }
    public void yearChoice(ActionEvent event){
        monthChoiceBox.getItems().clear();
        monthChoiceBox.setValue("All");
        monthChoiceBox.getItems().add("All");
        try {
            for (Penalty penalty:penaltyModel.getPenalty()) {
                String splitDate[] = String.valueOf(penalty.getPdate()).split("-");
                if (!monthChoiceBox.getItems().contains(months.get(Integer.parseInt(splitDate[1]) - 1))
                && yearChoiceBox.getValue().equals(splitDate[0])) {
                    monthChoiceBox.getItems().add(months.get(Integer.parseInt(splitDate[1]) - 1));
                }
            }
            showPenalty();
        } catch (SQLException ex) {
          
        }
    }
    public void showPenalty() throws SQLException{
        penaltyTable.getItems().clear();
        int month = monthChoiceBox.getValue()== "All"?0: months.indexOf(monthChoiceBox.getValue())+1;
        int year = yearChoiceBox.getValue()== "All"?0: Integer.parseInt(yearChoiceBox.getValue());
        
        int status;
            switch (statusChoiceBox.getValue()) {
                case "Paid":
                    status = 1;
                    break;
                case "Unpaid":
                    status = 2;
                    break;
                default:
                    status = 0;
            }
        ObservableList<Penalty> data = penaltyModel.filterPenalty(month, year, meterLocChoiceBox.getValue(),status,search.getText());
        penaltyTable.getItems().addAll(data);
    }

    @FXML
    private void search(KeyEvent event) throws SQLException {
        showPenalty();
    }
}
