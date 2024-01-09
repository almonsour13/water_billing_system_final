/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view.consumer.consumerProfile.pages;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import model.penalty.Penalty;
import model.penalty.PenaltyModel;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class ConsumerPenaltyHistoryController implements Initializable {
    private PenaltyModel penaltyModel = new PenaltyModel();

    @FXML
    private TableView<Penalty> penaltyTable;
    @FXML
    private TableColumn<Penalty, Integer> noCol;
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
    private TableColumn<Penalty, Double> totalAmountCol;
    private static BorderPane mainPanel = new BorderPane();
    private static Pane pageSetter = new Pane();
    private int id;

    /**
     * Initializes the controller class.
     */
    public void setConsumer(int id) throws SQLException{
        this.id = id;
        showPenaltyHistory();
    }
    public void mainPanel(BorderPane mainPanel){
        this.mainPanel = mainPanel;
    }
    public void pageSetter(Pane pageSetter){
        this.pageSetter = pageSetter;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       noCol.setCellValueFactory(new PropertyValueFactory<>("no"));
        meterNumCol.setCellValueFactory(new PropertyValueFactory<>("Meterno"));
        meterLocCol.setCellValueFactory(new PropertyValueFactory<>("Meterloc"));
        bDateCol.setCellValueFactory(new PropertyValueFactory<>("Billdate"));
        bAmountCol.setCellValueFactory(new PropertyValueFactory<>("Billamount"));
        pDateCol.setCellValueFactory(new PropertyValueFactory<>("Pdate"));
        pTypeCol.setCellValueFactory(new PropertyValueFactory<>("Ptype"));
        pAmountCol.setCellValueFactory(new PropertyValueFactory<>("Pamount"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("Status"));
        totalAmountCol.setCellValueFactory(new PropertyValueFactory<>("TotalAmount"));
    }   
    public void showPenaltyHistory() throws SQLException{
        penaltyTable.getItems().clear();
        penaltyTable.getItems().addAll(penaltyModel.getPenaltyById(id));
    }
    
}
