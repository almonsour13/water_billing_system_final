/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view.consumer.consumerProfile;

import controller.ConsumerController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import model.consumer.Consumer;
import model.consumer.ConsumerModel;
import view.consumer.consumerProfile.pages.ConsumerBillHistoryController;
import view.consumer.consumerProfile.pages.ConsumerPaymentHistoryController;
import view.consumer.consumerProfile.pages.ConsumerPenaltyHistoryController;
import view.consumer.consumerProfile.pages.ConsumerReadingHistoryController;
import view.consumer.consumerProfile.pages.MeterNoController;
import view.settings.SettingsController;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class ConsumerProfileController implements Initializable {
    private static BorderPane mainPanel = new BorderPane();
    private static Pane pageSetter = new Pane();
    @FXML
    private Button back;
    @FXML
    private Label consumerProfle;
    @FXML
    private Label fName;
    @FXML
    private Label mName;
    @FXML
    private Label lName;
    @FXML
    private Label contactNo;
    @FXML
    private Label emailAd;
    @FXML
    private Label purok;
    @FXML
    private Label postalCode;
    private static int  cID;
    public ConsumerModel ConsumerModel = new ConsumerModel();
    @FXML
    private Label dateAdded;
    @FXML
    private HBox pageContainer;
    @FXML
    private HBox navBar;
    @FXML
    private Button meterNoBtn;
    @FXML
    private Button billHistoryBtn;
    @FXML
    private Button paymentHistroyBtn;
    @FXML
    private Button penaltyHistroyBtn;
    @FXML
    private Button readingHistroyBtn;
    /**
     * Initializes the controller class.
     */
    public void mainPanel(BorderPane mainPanel){
        this.mainPanel = mainPanel;
    }
    public void pageSetter(Pane pageSetter){
        this.pageSetter = pageSetter;
    }
    public void setConsumer(int cID) throws SQLException, IOException{
        this.cID = cID;
        Consumer consumer = ConsumerModel.getSelectedConsumerById(cID);
        if(consumer != null){
            consumerProfle.setText(consumer.getFirstName()+" PROFILE:");
            fName.setText(consumer.getFirstName());
            mName.setText(consumer.getMidName());
            lName.setText(consumer.getLastName());
            contactNo.setText(consumer.getContactNo());
            emailAd.setText(consumer.getEmailAd());
            purok.setText(consumer.getPurok());
            postalCode.setText(consumer.getPostalCode());
            dateAdded.setText(String.valueOf(consumer.getDateAdded()));
            loadPage("meterNo");
            updateButtonStyles(meterNoBtn);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    private void updateButtonStyles(Button clickedButton) {
        for (Node node : navBar.getChildren()) {
            if (node instanceof Button button) {
                if (button == clickedButton) {
                    button.getStyleClass().setAll("menu-btn-clicked");
                } else {
                    button.getStyleClass().setAll("menu-btn");
                }
            }
        }
    }
    public void loadPage(String page) throws IOException, SQLException {
        pageContainer.getChildren().clear();
        FXMLLoader loader = null;
        Pane root = null;
        if(page.equals("meterNo")){
            loader = new FXMLLoader(getClass().getResource("pages/meterNo.fxml"));
            root = loader.load();
            MeterNoController conPro = loader.getController();
            conPro.setConsumer(cID);
            conPro.mainPanel(mainPanel);
            conPro.pageSetter(pageSetter);
            conPro.initialize(null, null);
        }else if(page.equals("billHistory")){
            loader = new FXMLLoader(getClass().getResource("pages/consumerBillHistory.fxml"));
            root = loader.load();
            ConsumerBillHistoryController conBill = loader.getController();
            conBill.setConsumer(cID);
            conBill.initialize(null, null);
            conBill.mainPanel(mainPanel);
            conBill.pageSetter(pageSetter);
        }else if(page.equals("paymentHistory")){
            loader = new FXMLLoader(getClass().getResource("pages/consumerPaymentHistory.fxml"));
            root = loader.load();
            ConsumerPaymentHistoryController conPay = loader.getController();
            conPay.setConsumer(cID);
            conPay.initialize(null, null);
            conPay.mainPanel(mainPanel);
            conPay.pageSetter(pageSetter);
        }else if(page.equals("penaltytHistory")){
            loader = new FXMLLoader(getClass().getResource("pages/consumerPenaltyHistory.fxml"));
            root = loader.load();
            ConsumerPenaltyHistoryController conPen = loader.getController();
            conPen.setConsumer(cID);
            conPen.initialize(null, null);
            conPen.mainPanel(mainPanel);
            conPen.pageSetter(pageSetter);
        }else if(page.equals("readingHistory")){
            loader = new FXMLLoader(getClass().getResource("pages/consumerReadingHistory.fxml"));
            root = loader.load();
            ConsumerReadingHistoryController conRead = loader.getController();
            conRead.setConsumer(cID);
            conRead.initialize(null, null);
            conRead.mainPanel(mainPanel);
            conRead.pageSetter(pageSetter);
        }
        pageContainer.getChildren().setAll(root);
    }
    @FXML
    private void back(ActionEvent event) throws SQLException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/consumer/consumer.fxml"));
        ConsumerController conCon = new ConsumerController();
        conCon.mainPanel(mainPanel);
        conCon.pageSetter(pageSetter);
        Pane root = loader.load();
        pageSetter.getChildren().setAll(root);
    }

    @FXML
    private void meterNo(ActionEvent event) throws IOException, SQLException {
        loadPage("meterNo");
        updateButtonStyles(meterNoBtn);
    }

    @FXML
    private void billHistory(ActionEvent event) throws IOException, SQLException {
        loadPage("billHistory");
        updateButtonStyles(billHistoryBtn);
    }

    @FXML
    private void paymentHistroy(ActionEvent event) throws IOException, SQLException {
        loadPage("paymentHistory");
        updateButtonStyles(paymentHistroyBtn);
    }

    @FXML
    private void penaltyHistroy(ActionEvent event) throws IOException, SQLException {
        loadPage("penaltytHistory");
        updateButtonStyles(penaltyHistroyBtn);
    }

    @FXML
    private void readingHistory(ActionEvent event) throws IOException, SQLException {
        loadPage("readingHistory");
        updateButtonStyles(readingHistroyBtn);
    }
    
}
