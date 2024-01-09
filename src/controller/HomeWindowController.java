/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import controller.accountLoggedSetter.LoggedAccountSetter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.account.Account;
import model.account.AccountModel;
import view.settings.settingPages.SystemLogsModel;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class HomeWindowController implements Initializable {

    @FXML
    private Pane parentContainer;
    @FXML
    private VBox sidebar;
    @FXML
    private Button dashboardBtn;
    @FXML
    private Button consumerBtn;
    @FXML
    private Button meterReadingBtn;
    @FXML
    private Button billingBtn;
    @FXML
    private Button paymentBtn;
    @FXML
    private Button accountBtn;
    private int accountID;
    @FXML
    private Label accountName;
    @FXML
    private Button logoutBtn;
    private AccountModel accountModel;
    @FXML
    private BorderPane panel;
    private  LoggedAccountSetter logAccount = new LoggedAccountSetter();
    @FXML
    private Button meterNumberBtn;
    @FXML
    private Button settings;
    @FXML
    private Label pageName;
    @FXML
    private Button penaltyBtn;
    @FXML
    private VBox consumerDropdown;
    @FXML
    private VBox billingDropdown;
    @FXML
    private TitledPane consumerTitledPane;
    @FXML
    private TitledPane billingTitledPane;
    @FXML
    private TitledPane meterReadingTitledPane;
    private TitledPane reportsTitledPane;
    @FXML
    private VBox meterReadingDropdown;
    private VBox reportsDropdown;
    @FXML
    private Button editRequestBtn;
    private SystemLogsModel systemLogsModel = new SystemLogsModel();
    @FXML
    private Button reporBtn;
    @FXML
    private Button connectionHistoryBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {      
        try {
            loadPage("penalty");
            updateButtonStyles(penaltyBtn);
            setAccount();
        } catch (IOException ex) {
            Logger.getLogger(HomeWindowController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(HomeWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        logoutBtn.setGraphic(loadSvgIcon("/assets/icons/logout.png"));
    } 
    public HomeWindowController(){
        parentContainer = new Pane();
        this.accountModel = new AccountModel();
    }
    public void setAccount() throws SQLException{
        Account account = accountModel.getAccountByID(logAccount.getAccount());
        if(account != null){
            accountName.setText(account.getName());
        }
        
    }
    private ImageView loadSvgIcon(String path) {
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20); // Set the width as needed
        imageView.setFitHeight(20); // Set the height as needed
        return imageView;
    }
    public void loadPage(String page) throws IOException, SQLException {
        FXMLLoader loader = null;
        parentContainer.getChildren().clear();
        
        if(page.equals("dashboard")){
             loader = new FXMLLoader(getClass().getResource("/view/dashboard/dashboard.fxml"));
             pageName.setText("Dashboard");
        }else if(page.equals("consumer")){
            loader = new FXMLLoader(getClass().getResource("/view/consumer/consumer.fxml"));
            ConsumerController conCon = new ConsumerController();
            conCon.mainPanel(panel);
            conCon.pageSetter(parentContainer);
            pageName.setText("Consumers");
        }else if(page.equals("meterReading")){
            loader = new FXMLLoader(getClass().getResource("/view/meterReading/meterReading.fxml"));
            pageName.setText("Meter Reading");
            MeterReadingController meReadCon = new MeterReadingController();
            meReadCon.mainPanel(panel);
            meReadCon.pageSetter(parentContainer);
        }else if(page.equals("billing")){
            loader = new FXMLLoader(getClass().getResource("/view/billing/billing.fxml"));
            pageName.setText("Bills");
            BillingController con = new BillingController();
            con.mainPanel(panel);
            con.pageSetter(parentContainer);
        }else if(page.equals("accounts")){
            loader = new FXMLLoader(getClass().getResource("/view/accounts/accounts.fxml"));
            pageName.setText("Accounts");
        }else if(page.equals("payments")){
            loader = new FXMLLoader(getClass().getResource("/view/payment/payments.fxml"));
            pageName.setText("Payments");
        }else if(page.equals("penalty")){
            loader = new FXMLLoader(getClass().getResource("/view/penalty/penalty.fxml"));
            pageName.setText("Penalty");
        }else if(page.equals("settings")){
            loader = new FXMLLoader(getClass().getResource("/view/settings/settings.fxml"));
            pageName.setText("Settings");
        }else if(page.equals("meterNumber")){
            loader = new FXMLLoader(getClass().getResource("/view/meterNumber/meterNumber.fxml"));
            pageName.setText("Meter");
        }
        else if(page.equals("reports")){
            loader = new FXMLLoader(getClass().getResource("/view/reports/reports.fxml"));
            pageName.setText("Reports");
        }else if(page.equals("connectionHistory")){
            loader = new FXMLLoader(getClass().getResource("/view/meterNumber/connectionHistory.fxml"));
            pageName.setText("Meter Connection History");
        }
        
        Pane root = loader.load();
        parentContainer.getChildren().setAll(root);
    }
    private void updateButtonStyles(Button clickedButton) {
        updateButtonStylesInContainer(sidebar, clickedButton, null);
        updateButtonStylesInContainer(consumerDropdown, clickedButton, consumerTitledPane);
        updateButtonStylesInContainer(billingDropdown, clickedButton, billingTitledPane);
        updateButtonStylesInContainer(meterReadingDropdown, clickedButton, meterReadingTitledPane);
    }
    private void updateButtonStylesInContainer(Pane container, Button clickedButton, TitledPane titlePane) {
        for (Node node : container.getChildren()) {
            updateButtonStyle(node, clickedButton, titlePane);
        }
    }
    private void updateButtonStyle(Node node, Button clickedButton, TitledPane titlePane) {
        if (node instanceof Button button) {
            boolean isClicked = button == clickedButton;
            button.getStyleClass().setAll(isClicked ? "menu-btn-clicked" : "menu-btn");

            if (isClicked && titlePane != null) {
                // Collapse other titled panes
                for (Node child : sidebar.getChildren()) {
                    if (child instanceof TitledPane) {
                        TitledPane otherPane = (TitledPane) child;
                        if (otherPane != titlePane && otherPane.isExpanded()) {
                            animateTitledPane(otherPane);
                        }
                    }
                }
            } else if(isClicked && titlePane == null){
                for (Node child : sidebar.getChildren()) {
                    if (child instanceof TitledPane) {
                        TitledPane otherPane = (TitledPane) child;
                        if (otherPane.isExpanded()) {
                            animateTitledPane(otherPane);
                        }
                    }
                }
            }
        }
    }
    private void animateTitledPane(TitledPane titlePane) {
        if (titlePane != null && titlePane.isExpanded()) {
            Timeline timeline = new Timeline();
            KeyValue keyValue = new KeyValue(titlePane.expandedProperty(), false);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(300), keyValue);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        }
    }
    @FXML
    private void dashboard(ActionEvent event) throws IOException, SQLException {
        loadPage("dashboard");
        updateButtonStyles(dashboardBtn);
    }

    @FXML
    private void consumer(ActionEvent event) throws IOException, SQLException {
        loadPage("consumer");
        updateButtonStyles(consumerBtn);
    }

    @FXML
    private void meterReading(ActionEvent event) throws IOException, SQLException {
        loadPage("meterReading");
        updateButtonStyles(meterReadingBtn);
    }

    @FXML
    private void billing(ActionEvent event) throws IOException, SQLException {
        loadPage("billing");
        updateButtonStyles(billingBtn);
    }

    @FXML
    private void payment(ActionEvent event) throws IOException, SQLException {
        loadPage("payments");
        updateButtonStyles(paymentBtn);
    }

    @FXML
    private void account(ActionEvent event) throws IOException, SQLException {
        loadPage("accounts");
        updateButtonStyles(accountBtn);
    } 

    @FXML
    private void logout(ActionEvent event) throws IOException, SQLException {
        
        systemLogsModel.insertLog(accountID, "Successfully logged out");
        logAccount.removeAccount();
        Stage stage = (Stage) panel.getScene().getWindow();
        stage.close();
        
        Stage primaryStage = new Stage();
        Image icon = new Image(getClass().getResourceAsStream("/assets/logo.png"));
        primaryStage.getIcons().add(icon);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/form/loginForm.fxml"));
        Parent root = loader.load();
                   
        Scene scene = new Scene(root);   
        
        primaryStage.setResizable(false);
        primaryStage.setTitle("Water Billing System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    private void meterNumber(ActionEvent event) throws IOException, SQLException {
        loadPage("meterNumber");
        updateButtonStyles(meterNumberBtn);
    }


    @FXML
    private void settings(ActionEvent event) throws IOException, SQLException {
        loadPage("settings");
        updateButtonStyles(settings);
    }

    @FXML
    private void penalty(ActionEvent event) throws IOException, SQLException {
        loadPage("penalty");
        updateButtonStyles(penaltyBtn);
    }

    @FXML
    private void repor(ActionEvent event) throws IOException, SQLException {
        loadPage("reports");
        updateButtonStyles(reporBtn);
    }

    @FXML
    private void connectionHistory(ActionEvent event) throws SQLException, IOException {
        loadPage("connectionHistory");
        updateButtonStyles(connectionHistoryBtn);
    }
    
}
