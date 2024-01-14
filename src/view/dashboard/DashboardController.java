/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view.dashboard;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.dashboard.Dashboard;
import model.dashboard.DashboardModel;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class DashboardController implements Initializable {
    private DashboardModel dashboardModel;
    @FXML
    private LineChart<String, Integer> waterConsumerTrends;

    /**
     * Initializes the controller class.
     */
    private ObservableList<String> months = 
                FXCollections.observableArrayList(
                        "Jan","Feb","Mar","Apr","May","Jun",
                        "Jul","Aug","Sep","Oct","Nov","Dec");
    @FXML
    private PieChart billsPieChart;
    @FXML
    private Label paidLabel;
    @FXML
    private Label unpaidLabel;
    @FXML
    private Label overdueLabel;
    @FXML
    private Label totalBillsLabel;
    @FXML
    private BarChart<String, Double> incomeTrends;
    @FXML
    private Label totalConsumer;
    @FXML
    private VBox meterNum;
    @FXML
    private Label totalMeterNumber;

    
    
    public DashboardController(){
        dashboardModel = new DashboardModel();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            incomeBarChart();
            waterLineChart();
            billPieChart();
            totalConsumer.setText(String.valueOf(dashboardModel.getTotalConsumer()));
            totalMeterNumber.setText(String.valueOf(dashboardModel.getTotalMeterNumber()));
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    

    public void waterLineChart() throws SQLException{
        ObservableList<Dashboard> waterConsumption = dashboardModel.getWaterConsumption();

        // Create a series for the LineChart
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Water Consumption"); // Set the series name

        // Add data points to the series
        for (Dashboard dashboard : waterConsumption) {
            String monthIndex = months.get(dashboard.getMonth()-1);
            series.getData().add(new XYChart.Data<>(monthIndex, dashboard.getMonthlyConsumption()));
        }

        // Add the series to the LineChart
        waterConsumerTrends.getData().add(series);
    }
    public void billPieChart() throws SQLException {
        Dashboard dashboard = dashboardModel.getBillAnalytics();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Paid", dashboard.getPaid()),
                new PieChart.Data("Unpaid", dashboard.getUnpaid()),
                new PieChart.Data("Overdue", dashboard.getOverDue())); 

        billsPieChart.setData(pieChartData); 
        paidLabel.setText(inToString(dashboard.getPaid()));
        unpaidLabel.setText(inToString(dashboard.getUnpaid()));
        overdueLabel.setText(inToString(dashboard.getOverDue()));
        totalBillsLabel.setText(inToString(dashboard.getTotalBills()));
    }
    public void incomeBarChart() throws SQLException{
        ObservableList<Dashboard> waterConsumption = dashboardModel.getIncomes();

        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName("Water Consumption"); 
        
        for (Dashboard dashboard : waterConsumption) {
            String monthIndex = months.get(dashboard.getMonth()-1);
            series.getData().add(new XYChart.Data<>(monthIndex, dashboard.getPaymentAmount()));
            
        }

        incomeTrends.getData().add(series); 
    }
    public String inToString(int value){
        return String.valueOf(value);
    }

    
}
