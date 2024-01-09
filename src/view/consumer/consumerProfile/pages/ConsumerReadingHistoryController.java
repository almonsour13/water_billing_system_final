/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view.consumer.consumerProfile.pages;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class ConsumerReadingHistoryController implements Initializable {

    @FXML
    private TableView<?> meterReadingTable;
    @FXML
    private TableColumn<?, ?> noCol;
    @FXML
    private TableColumn<?, ?> meterNoCol;
    @FXML
    private TableColumn<?, ?> meterLocationCol;
    @FXML
    private TableColumn<?, ?> prevReadingCol;
    @FXML
    private TableColumn<?, ?> curReadingCol;
    @FXML
    private TableColumn<?, ?> dateAddedCol;
    @FXML
    private TableColumn<?, ?> eneteredByCol;
    @FXML
    private TableColumn<?, ?> statusCol;
    private static BorderPane mainPanel = new BorderPane();
    private static Pane pageSetter = new Pane();
    private int id;

    /**
     * Initializes the controller class.
     */
    public void setConsumer(int id) throws SQLException{
        this.id = id;
    }
    public void mainPanel(BorderPane mainPanel){
        this.mainPanel = mainPanel;
    }
    public void pageSetter(Pane pageSetter){
        this.pageSetter = pageSetter;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    
    
}
