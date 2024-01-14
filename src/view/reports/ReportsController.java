/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view.reports;

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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import model.dashboard.Dashboard;
import model.reports.ReportModel;
import model.reports.Reports;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class ReportsController implements Initializable {
    private ReportModel reportModel = new ReportModel();
    private ObservableList<String> months = 
                FXCollections.observableArrayList(
                        "Jan","Feb","Mar","Apr","May","Jun",
                        "Jul","Aug","Sep","Oct","Nov","Dec");
    @FXML
    private StackedBarChart<String, Double> paymentCollectionChart;
    @FXML
    private PieChart billsChart;
    @FXML
    private PieChart billsChart1;
    @FXML
    private Label totalBillsLabel1;
    @FXML
    private Label paidLabel1;
    @FXML
    private Label unpaidLabel1;
    @FXML
    private Label notCollectedLabel;
    @FXML
    private Label collectedLabel;
    @FXML
    private ChoiceBox<String> monthChoiceBox;
    @FXML
    private ChoiceBox<String> yearChoiceBox;
    @FXML
    private LineChart<String, Integer> waterConsumptionChart;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            setChoices();
            billChart();
            paymentCollectionChart();
            waterConsumptionChart();
        } catch (SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        monthChoiceBox.setOnAction(this::monthChoice);
        yearChoiceBox.setOnAction(this::yearChoice);
    }    
    public void billChart() throws SQLException{
        int month = monthChoiceBox.getValue()== "All"?0: months.indexOf(monthChoiceBox.getValue())+1;
        int year = yearChoiceBox.getValue()== "All"?0: Integer.parseInt(yearChoiceBox.getValue());
        
        Reports bills = reportModel.getBillsAnalytic(month,year);
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Paid", bills.getCollected()),
                new PieChart.Data("Unpaid", bills.getNotCollected()));
        billsChart.setData(pieChartData);
        int collected =  bills.getCollected();
        int notCollected =  bills.getNotCollected();

        int totalBills = collected + notCollected;

        // Calculate percentages
        double collectedPercentage = (totalBills == 0) ? 0.0 : ((double) collected / totalBills) * 100.0;
        double notCollectedPercentage = (totalBills == 0) ? 0.0 : ((double) notCollected / totalBills) * 100.0;

        // Set percentages as text in labels
        collectedLabel.setText(String.format("%.1f%%", collectedPercentage));
        notCollectedLabel.setText(String.format("%.1f%%", notCollectedPercentage));
    }
    public void waterConsumptionChart() throws SQLException{
        waterConsumptionChart.getData().clear();
        LocalDate date = LocalDate.now();
        int year = yearChoiceBox.getValue()== "All"?0: Integer.parseInt(yearChoiceBox.getValue());
        if(year == 0){
            year = date.getYear();
        }
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        for(int a = 0; a<months.size(); a++){
            String monthIndex = months.get(a);
            
            for (Reports reports : reportModel.getWaterConsumption(year)) {
                if(reports.getWaterConsumptionPerMonth() != 0 && a == (reports.getMonth()-1)){
                    series.getData().add(new XYChart.Data<>(monthIndex, reports.getWaterConsumptionPerMonth()));
                }else{
                    series.getData().add(new XYChart.Data<>(monthIndex, 0));
                }
            }
        }
        waterConsumptionChart.getData().add(series);
    }
     public void paymentCollectionChart() throws SQLException{
        LocalDate date = LocalDate.now();
        paymentCollectionChart.getData().clear();
        int year = yearChoiceBox.getValue()== "All"?0: Integer.parseInt(yearChoiceBox.getValue());
        if(year == 0){
            year = date.getYear();
        }
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        for(int a = 0; a<months.size(); a++){
            String monthIndex = months.get(a);
            
            for (Reports reports : reportModel.getPaymentIncomes(year)) {
                if(reports.getPaymentCollectionPerMonth() != 0.0 && a == (reports.getMonth()-1)){
                    series.getData().add(new XYChart.Data<>(monthIndex, reports.getPaymentCollectionPerMonth()));
                }else{
                    series.getData().add(new XYChart.Data<>(monthIndex, 0.0));
                }
            }
        }

        paymentCollectionChart.getData().add(series); 
    }
    public void setChoices() throws SQLException{
        yearChoiceBox.setValue("All");
        yearChoiceBox.getItems().add("All");
        
        for(Reports reports:reportModel.getAll()){
            String splitDate[] = String.valueOf(reports.getBillingDate()).split("-");
            if(!yearChoiceBox.getItems().contains(splitDate[0])){
                yearChoiceBox.getItems().add(splitDate[0]);
            }
        }
        LocalDate date = LocalDate.now();
        monthChoiceBox.setValue(months.get(date.getMonthValue()-1));
        yearChoiceBox.setValue(String.valueOf(date.getYear()));
        
        monthChoiceBox.getItems().add("All");
        for (Reports reports : reportModel.getAll()) {
            String splitDate[] = String.valueOf(reports.getBillingDate()).split("-");
            if (!monthChoiceBox.getItems().contains(months.get(Integer.parseInt(splitDate[1]) - 1))
              && yearChoiceBox.getValue().equals(splitDate[0])) {
                monthChoiceBox.getItems().add(months.get(Integer.parseInt(splitDate[1]) - 1));
            }
        }
        
    }
    public void monthChoice(ActionEvent event){
        try {
            billChart();
        } catch (SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void yearChoice(ActionEvent event){
        monthChoiceBox.getItems().clear();
        monthChoiceBox.setValue("All");
        monthChoiceBox.getItems().add("All");
        try {
            for (Reports reports : reportModel.getAll()) {
                String splitDate[] = String.valueOf(reports.getBillingDate()).split("-");
                if (!monthChoiceBox.getItems().contains(months.get(Integer.parseInt(splitDate[1]) - 1))
                && yearChoiceBox.getValue().equals(splitDate[0])) {
                    monthChoiceBox.getItems().add(months.get(Integer.parseInt(splitDate[1]) - 1));
                }
            }
            billChart();
            waterConsumptionChart();
            paymentCollectionChart();
        } catch (SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
