/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import controller.accountLoggedSetter.LoggedAccountSetter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.bills.Bills;
import model.bills.BillsModel;
import view.consumer.ConsumerBillHistoryController;
import view.consumer.ModalController;
import view.settings.settingPages.SystemLogsModel;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class PaybillController implements Initializable {
    private BillingController billingController;
    private ConsumerBillHistoryController ConsumerBillHistoryController;
    private BillsModel billModel  = new BillsModel();
    @FXML
    private Label name;
    @FXML
    private Label meterNumber;
    @FXML
    private Label meterLocation;
    @FXML
    private Label paymentStatus;
    @FXML
    private Label totalAmount;
    @FXML
    private TextField inputPaymentAmount;
    private int billID;
    @FXML
    private VBox parentContainer;
    @FXML
    private Label billAmount;
    @FXML
    private Label penaltyAmount;
    private static BorderPane mainPanel = new BorderPane();
    private  LoggedAccountSetter logAccount = new LoggedAccountSetter();
    private SystemLogsModel systemLogsModel = new SystemLogsModel();
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        parentContainer.setOpacity(0);

        // Create a FadeTransition for the modal
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), parentContainer);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
        inputPaymentAmount.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            try {
            // Get the character that the user is attempting to input
                String input = event.getCharacter();
                // Try to parse the input as an integer
                Integer.parseInt(input);
            } catch (NumberFormatException e) {
                event.consume();
            }
        });
    }    
    
    public void setController(ConsumerBillHistoryController ConsumerBillHistoryController){
        this.ConsumerBillHistoryController = ConsumerBillHistoryController;
    }
    public void setController(BillingController billingController){
        this.billingController = billingController;
    }
    public void mainPanel(BorderPane mainPanel){
        this.mainPanel = mainPanel;
    }
    public void setBillID(int billID) throws SQLException{
        Bills bills = billModel.getBillsDetails(billID);
        name.setText(bills.getName());
        meterNumber.setText(String.valueOf(bills.getMeterNo()));
        if(bills.getBillingStatus() == 1){
            paymentStatus.setText("Unpaid");
        }else if(bills.getPaymentStatus() == 3){
            paymentStatus.setText("Over Due");
        }
        billAmount.setText(String.valueOf(bills.getTotalAmount()));
        penaltyAmount.setText(String.valueOf(bills.getPenaltyAmount()));
        totalAmount.setText(String.valueOf(bills.getPenaltyAmount()+bills.getTotalAmount()));
        
        this.billID = billID;
    }
    public void modal(String promptLabel) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/consumer/Modal.fxml"));
        VBox modal = loader.load();
        ModalController modCOn = loader.getController();
        modCOn.setPromptLabel(promptLabel);
        Stage internalFrame = new Stage();
        internalFrame.initStyle(StageStyle.UTILITY); // Customize the stage style
        internalFrame.initModality(Modality.WINDOW_MODAL);
        internalFrame.initOwner(mainPanel.getScene().getWindow()); // Set the owner stage (mainPanel's stage)
       // internalFrame.getIcons().add(getIcon());
        Scene scene = new Scene(modal);
        internalFrame.setScene(scene);

        internalFrame.setTitle("");
        internalFrame.setResizable(false);

        internalFrame.show();
    }
    @FXML
    private void submitPayment(ActionEvent event) throws SQLException, IOException {
        String paymentAmount = inputPaymentAmount.getText();
        if(!paymentAmount.isEmpty()){
            
            Double paymentAmountValue = Double.parseDouble(paymentAmount);
            Double totalAmountValue = Double.parseDouble(totalAmount.getText());
            if(paymentAmountValue >= totalAmountValue){
                double parseAmount = paymentAmountValue*0.0040;
                billModel.insertPayment(this.billID,parseAmount);
                Stage stage = (Stage) parentContainer.getScene().getWindow();
                stage.close();
                systemLogsModel.insertLog(logAccount.getAccount(), "Payment received: $" + paymentAmountValue + " from " + name.getText() + " for bill #" + billID + ". Total: $" + totalAmountValue + ", Change: $" + (paymentAmountValue - totalAmountValue));
                if(billingController != null){
                    billingController.showBilling();
                }else{
                    ConsumerBillHistoryController.showConsumerBills();
                }
            }else{
                modal("Entered amount is less than the total amount. Please enter the correct amount.");
            }
        }else{
            modal("Enter Amount!");
        }
    }
    @FXML
    private void cancelPayment(ActionEvent event) {
        Stage stage = (Stage) parentContainer.getScene().getWindow();
        stage.close();
    }
}
