/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view.consumer.consumerProfile.pages;

import com.itextpdf.text.DocumentException;
import controller.BillingController;
import controller.PaybillController;
import controller.billPDFGenenerator.billPdfGenerator;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import model.bills.Bills;
import model.bills.BillsModel;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class ConsumerBillHistoryController implements Initializable {

    @FXML
    private TableView<Bills> billTable;
    @FXML
    private TableColumn<Bills, Integer> noCol;
    @FXML
     private TableColumn<Bills, Integer> meterNoCol;
    private TableColumn<Bills, String> meterLocCol;
    @FXML
    private TableColumn<Bills, LocalDate> billingDateCol;
    @FXML
    private TableColumn<Bills, LocalDate> dueDateCol;
    @FXML
    private TableColumn<Bills, Integer> consumptionCol;
    @FXML
    private TableColumn<Bills, Integer> rateCol;
    @FXML
    private TableColumn<Bills, Integer> totalAmountCol;
    @FXML
    private TableColumn<Bills, String> statusCol;
    @FXML
    private TableColumn<Bills, Integer> actionCol;
    @FXML
    private TableColumn<Bills, String> meterLoc;
    @FXML
    private TableColumn<Bills, Integer> billAmountCol;
    private int id;
    private BillsModel billsModel  = new BillsModel();
    private static BorderPane mainPanel = new BorderPane();
    private static Pane pageSetter = new Pane();
    private billPdfGenerator printPdfBill;
    

    /**
     * Initializes the controller class.
     */
    public void setConsumer(int id) throws SQLException{
        this.id = id;
        showConsumerBillHistory();
    }
    public void mainPanel(BorderPane mainPanel){
        this.mainPanel = mainPanel;
    }
    public void pageSetter(Pane pageSetter){
        this.pageSetter = pageSetter;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        noCol.setCellValueFactory(new PropertyValueFactory<>("No"));
        meterNoCol.setCellValueFactory(new PropertyValueFactory<>("MeterNo"));
        meterLoc.setCellValueFactory(new PropertyValueFactory<>("MeterLocation"));
        billingDateCol.setCellValueFactory(new PropertyValueFactory<>("BillingDate"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("DueDate"));
        consumptionCol.setCellValueFactory(new PropertyValueFactory<>("Consumption"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("Status"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("Rate"));
        billAmountCol.setCellFactory(new Callback<TableColumn<Bills, Integer>, TableCell<Bills, Integer>>() {
            @Override
            public TableCell<Bills, Integer> call(TableColumn<Bills, Integer> param) {
                return new TableCell<Bills, Integer>() {
                    final Text billAmountText = new Text();
                    final Text penaltyAmountText = new Text();
                    final Text plusSignText = new Text(" + ");

                    {
                        penaltyAmountText.setFill(Color.web("red"));
                    }

                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Bills bill = getTableView().getItems().get(getIndex());
                            billAmountText.setText(String.valueOf(bill.getTotalAmount()));
                            HBox textContainer = null;
                            if (bill.getPenaltyAmount() != 0) {
                                penaltyAmountText.setText(String.valueOf(bill.getPenaltyAmount()));
                                textContainer = new HBox(billAmountText, plusSignText, penaltyAmountText);
                            } else {
                                textContainer = new HBox(billAmountText);
                            }

                            textContainer.setAlignment(Pos.CENTER_LEFT);
                            setGraphic(textContainer);
                            setText(null);
                        }
                    }
                };
            }
});
        totalAmountCol.setCellFactory(new Callback<TableColumn<Bills, Integer>, TableCell<Bills, Integer>>() {
            @Override
            public TableCell<Bills, Integer> call(TableColumn<Bills, Integer> param) {
                return new TableCell<Bills, Integer>() {
                    final Text totalAmount = new Text();

                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Bills bill = getTableView().getItems().get(getIndex());
                            totalAmount.setText(String.valueOf(bill.getTotalAmount()+bill.getPenaltyAmount()));
                            HBox textContainer = textContainer = new HBox(totalAmount);

                            textContainer.setAlignment(Pos.CENTER_LEFT);
                            setGraphic(textContainer);
                            setText(null);
                        }
                    }
                };
            }
        });
        statusCol.setCellFactory(column -> new TableCell<Bills, String>() {
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
                        case "over due":
                            getStyleClass().setAll("cut-cell");
                            break;
                        default:
                            getStyleClass().setAll("default-cell");
                            break;
                    }
                }
            }
        });   
        actionCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Bills, Integer> call(TableColumn<Bills, Integer> param) {
                return new TableCell<>() {
                    final Button actionButton = new Button();
                    MenuItem sendNoticeItem = new MenuItem();
                    MenuItem payBillItem = new MenuItem();
                    MenuItem viewBillDetailsItem = new MenuItem();
                    MenuItem viewPenaltyItem = new MenuItem();
                    MenuItem printBillItem = new MenuItem();
                    {
                        actionButton.getStyleClass().add("actionBtn");
                        sendNoticeItem = createMenuItem("Send Notice", "/assets/icons/speaker.png");
                        payBillItem = createMenuItem("Pay Bill", "/assets/icons/payment.png");
                        viewBillDetailsItem = createMenuItem("View Bill Details", "/assets/icons/bills.png");
                        viewPenaltyItem = createMenuItem("View Penalty", "/assets/icons/warning.png");
                        printBillItem = createMenuItem("Print Bill", "/assets/icons/print.png");


                        ContextMenu contextMenu = new ContextMenu(sendNoticeItem, payBillItem, viewBillDetailsItem, viewPenaltyItem, printBillItem);

                        // Change the icon of the Button
                        actionButton.setGraphic(loadSvgIcon("/assets/icons/action.png")); // Replace with the path to your new icon

                        actionButton.setOnAction(event -> {
                            if (contextMenu.isShowing()) {
                               contextMenu.hide();
                            } else {
                               contextMenu.show(actionButton, Side.LEFT, 0, 0);
                            }
                        });
                        sendNoticeItem.setOnAction(event -> handleAction(getIndex(), "Send Notice"));
                        payBillItem.setOnAction(event -> handleAction(getIndex(), "Pay Bill"));
                        viewBillDetailsItem.setOnAction(event -> handleAction(getIndex(), "View Bill Details"));
                        viewPenaltyItem.setOnAction(event -> handleAction(getIndex(), "View Penalty"));
                        printBillItem.setOnAction(event -> handleAction(getIndex(), "Print Bill"));
                    }

                    private void handleAction(int index, String selectedAction) {
                        Bills selectedBill = getTableView().getItems().get(index);
                        switch (selectedAction) {
                            case "Send Notice":
                                // Handle Send Notice action
                                break;
                            case "Pay Bill":
                                try {
                                    payConsumerBill(selectedBill.getBillID());
                                } catch (IOException | SQLException ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case "View Bill Details":
                                try {
                                    viewBillingDetails(selectedBill.getBillID());
                                } catch (IOException | SQLException ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case "View Penalty":
                                try {
                                    viewPenaltyDetails(selectedBill.getBillID());
                                } catch (IOException | SQLException ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case "Print Bill":
                                try {
                                    printPdfBill = new billPdfGenerator(selectedBill.getBillID());
                                    showConsumerBillHistory();
                                } catch (DocumentException | IOException | SQLException ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            default:
                                // Handle default case
                                break;
                        }
                    }

                    private MenuItem createMenuItem(String text, String iconPath) {
                        Image icon = new Image(getClass().getResourceAsStream(iconPath));
                        ImageView iconView = new ImageView(icon);
                        iconView.setFitWidth(16);
                        iconView.setFitHeight(16);

                        MenuItem menuItem = new MenuItem(text, iconView);
                        menuItem.setStyle("-fx-padding: 5 0 5 5;"); // Adjust padding as needed

                        return menuItem;
                    }

                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Bills selectedBill = getTableView().getItems().get(getIndex());
                            try {
                                if(billsModel.checkPaymentStatus(selectedBill.getBillID())){
                                    payBillItem.setDisable(true);
                                }else{
                                    payBillItem.setDisable(false);

                                }
    //                            if(billsModel.viewPenaltyDetails(selectedBill.getBillID()) == null){
    //                                viewPenaltyBtn.setDisable(true);
    //                            }
                                if(billsModel.checkInvoice(selectedBill.getBillID())){
                                    printBillItem.setDisable(true);
                                }else{
                                    printBillItem.setDisable(false);

                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            setGraphic(actionButton);
                        }
                    }
                };
            }
        });
    }   
    public void showConsumerBillHistory() throws SQLException{
        billTable.getItems().clear();
        billTable.getItems().addAll(billsModel.getConsumerBillsById(id));
    }
    private ImageView loadSvgIcon(String path) {
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(18); // Set the width as needed
        imageView.setFitHeight(18); // Set the height as needed
        return imageView;
    }
    public void payConsumerBill(int billID) throws IOException, SQLException{
                                
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/paybill.fxml"));
        VBox modal = loader.load();
        PaybillController payBillCon = loader.getController();
    //    payBillCon.setController(this);
        payBillCon.setBillID(billID);
        payBillCon.mainPanel(mainPanel);
        Stage internalFrame = new Stage();
        internalFrame.initStyle(StageStyle.UTILITY); // Customize the stage style
        internalFrame.initModality(Modality.WINDOW_MODAL);
        internalFrame.initOwner(mainPanel.getScene().getWindow()); // Set the owner stage (mainPanel's stage)

        Scene scene = new Scene(modal);
        internalFrame.setScene(scene);

        internalFrame.setTitle("Pay Bill");
        internalFrame.setResizable(false);

        internalFrame.show();
        
    }
    public void viewPenaltyDetails(int billID) throws IOException, SQLException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewPenaltyDetails.fxml"));
        VBox modal = loader.load();
        
        Stage internalFrame = new Stage();
        internalFrame.initStyle(StageStyle.UTILITY); // Customize the stage style
        internalFrame.initModality(Modality.WINDOW_MODAL);
        internalFrame.initOwner(mainPanel.getScene().getWindow()); // Set the owner stage (mainPanel's stage)

        Scene scene = new Scene(modal);
        internalFrame.setScene(scene);

        internalFrame.setTitle("Add consumer");
        internalFrame.setResizable(false);

        internalFrame.show();
    }
    public void viewBillingDetails(int billID) throws IOException, SQLException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewBillDetails.fxml"));
        VBox modal = loader.load();
        Stage internalFrame = new Stage();
//        ViewBillDetailsController viewBillController = loader.getController();
//        viewBillController.setBill(billID);
        internalFrame.initStyle(StageStyle.UTILITY); 
        internalFrame.initModality(Modality.WINDOW_MODAL);
        internalFrame.initOwner(mainPanel.getScene().getWindow()); 
        Scene scene = new Scene(modal);
        internalFrame.setScene(scene);
        internalFrame.setTitle("Add consumer");
        internalFrame.setResizable(false);
        internalFrame.show();
    }
    
}
