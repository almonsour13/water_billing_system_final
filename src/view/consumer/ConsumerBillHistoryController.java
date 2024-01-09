/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view.consumer;

import com.itextpdf.text.DocumentException;
import controller.BillingController;
import controller.MeterReadingController;
import controller.PaybillController;
import controller.ViewBillDetailsController;
import controller.billPDFGenenerator.billPdfGenerator;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.bills.Bills;
import model.bills.BillsModel;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class ConsumerBillHistoryController implements Initializable {

    private Label consumerNameLabel;
    @FXML
    private TableColumn<Bills, Integer> noCol;
    @FXML
    private TableColumn<Bills, Integer> meterNoCol;
    @FXML
    private TableColumn<Bills, LocalDate> dueDateCol;
    @FXML
    private TableColumn<Bills, Integer> consumptionCol;
    @FXML
    private TableColumn<Bills, Double> totalAmountCol;
    @FXML
    private TableColumn<Bills, String> statusCol;
    @FXML
    private TableColumn<Bills, LocalDate> billingDateCol;
    ObservableList<Bills> data;
    private BillsModel billsModel  = new BillsModel();;
    @FXML
    private TableView<Bills> billTable;
    @FXML
    private TableColumn<Bills, Integer> actionCol;
    private int id;
    private billPdfGenerator printPdfBill;
    @FXML
    private VBox parentContainer;
    @FXML
    private Label consumerName;
    @FXML
    private ChoiceBox<String> monthChoiceBox;
    @FXML
    private ChoiceBox<String> yearChoiceBox;
    @FXML
    private ChoiceBox<String> statusChoiceBox;
    @FXML
    private TableColumn<Bills, String> meterLoc;
    @FXML
    private TableColumn<Bills, Integer> rateCol;
    private static BorderPane mainPanel = new BorderPane();
    private static Pane pageSetter = new Pane();
    
    ObservableList<String> months = 
                FXCollections.observableArrayList(
                        "Jan","Feb","Mar","Apr","May","Jun",
                        "Jul","Aug","Sep","Oct","Nov","Dec");
    
    public void setConsumer(int id) throws SQLException {
        this.id = id;
        showConsumerBills();
      
        Bills bill = billsModel.getSelectedConsumer(id);
        if(bill != null){
            consumerName.setText(bill.getName()+" Bills History");
        }
    }
    public void mainPanel(BorderPane mainPanel){
        this.mainPanel = mainPanel;
        System.out.println(mainPanel);
    }
    public void pageSetter(Pane pageSetter){
        this.pageSetter = pageSetter;
    }
    @Override
    public void initialize(URL url, ResourceBundle consumerController) {
        try {
            setChoices();
        } catch (SQLException ex) {
            Logger.getLogger(ConsumerBillHistoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        billsModel = new BillsModel();
        noCol.setCellValueFactory(new PropertyValueFactory<>("No"));
        meterNoCol.setCellValueFactory(new PropertyValueFactory<>("MeterNo"));
        meterNoCol.setCellValueFactory(new PropertyValueFactory<>("MeterLocation"));
        billingDateCol.setCellValueFactory(new PropertyValueFactory<>("BillingDate"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("DueDate"));
        totalAmountCol.setCellValueFactory(new PropertyValueFactory<>("TotalAmount"));
        consumptionCol.setCellValueFactory(new PropertyValueFactory<>("Consumption"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("Status"));        
        rateCol.setCellFactory(new Callback<TableColumn<Bills, Integer>, TableCell<Bills, Integer>>() {
            @Override
            public TableCell<Bills, Integer> call(TableColumn<Bills, Integer> param) {
                return new TableCell<Bills, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            try {
                                setText(String.valueOf(billsModel.getRate()));
                            } catch (SQLException ex) {
                             //   Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
                            }
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
        actionCol.setCellFactory(new Callback<TableColumn<Bills, Integer>, TableCell<Bills, Integer>>() {
            @Override
            public TableCell<Bills, Integer> call(TableColumn<Bills, Integer> param) {
                return new TableCell<Bills, Integer>() {
                    final Button sendNoticeBtn = new Button();
                    final Button payBillBtn = new Button();
                    final Button viewBillDetailsBtn = new Button();
                    final Button viewPenaltyBtn = new Button();
                    final Button printBill = new Button();

                        {
                            sendNoticeBtn.getStyleClass().add("actionBtn");
                            sendNoticeBtn.setGraphic(loadSvgIcon("/assets/icons/speaker.png"));
                            payBillBtn.getStyleClass().add("actionBtn");
                            payBillBtn.setGraphic(loadSvgIcon("/assets/icons/payment.png"));
                            viewBillDetailsBtn.getStyleClass().add("actionBtn");
                            viewBillDetailsBtn.setGraphic(loadSvgIcon("/assets/icons/bills.png"));
                            viewPenaltyBtn.getStyleClass().add("actionBtn");
                            viewPenaltyBtn.setGraphic(loadSvgIcon("/assets/icons/warning.png"));
                            printBill.getStyleClass().add("actionBtn");
                            printBill.setGraphic(loadSvgIcon("/assets/icons/print.png"));
                            
                            sendNoticeBtn.setOnAction(event -> {
                            });

                            payBillBtn.setOnAction(event -> {
                                Bills selectedBill = getTableView().getItems().get(getIndex());

                            });
                            viewBillDetailsBtn.setOnAction(event -> {
                                Bills selectedBill = getTableView().getItems().get(getIndex());
                              
                            });
                            viewPenaltyBtn.setOnAction(event -> {
                                Bills selectedBill = getTableView().getItems().get(getIndex());
                               
                            });
                            printBill.setOnAction(event -> {
                                Bills selectedBill = getTableView().getItems().get(getIndex());
                                try {
                                    printPdfBill = new billPdfGenerator(selectedBill.getBillID());
                                } catch (DocumentException ex) {
                                    Logger.getLogger(ConsumerBillHistoryController.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(ConsumerBillHistoryController.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (SQLException ex) {
                                    Logger.getLogger(ConsumerBillHistoryController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                try {
                                    showConsumerBills();
                                } catch (SQLException ex) {
                                    Logger.getLogger(ConsumerBillHistoryController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                               
                            });
                        }

                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Bills selectedBill = getTableView().getItems().get(getIndex());
                        try {
                            if(billsModel.checkPaymentStatus(selectedBill.getBillID())){
                                payBillBtn.setDisable(true);
                            }else{
                                payBillBtn.setDisable(false);
                                
                            }
                            if(billsModel.viewPenaltyDetails(selectedBill.getBillID()) == null){
                                viewPenaltyBtn.setDisable(true);
                            }
                            if(billsModel.checkInvoice(selectedBill.getBillID())){
                                printBill.setDisable(true);
                            }else{
                                printBill.setDisable(false);
                                
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        HBox buttonsBox = new HBox(3, sendNoticeBtn, payBillBtn, viewBillDetailsBtn,viewPenaltyBtn,printBill);
                        buttonsBox.setAlignment(Pos.CENTER);
                       
                        setGraphic(buttonsBox);
                        setText(null);
                    }
                }

                };
            }
        });              
        billTable.setRowFactory(tv -> new TableRow<Bills>() {
            @Override
            protected void updateItem(Bills item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle(""); 
                } else {
                    // Set the background color based on the status
                    switch (item.getStatus().toLowerCase()) {
                        case "paid":
                         //   setStyle("-fx-background-color: lightgreen;");
                            break;
                        case "unpaid":
                            setStyle("-fx-background-color: fae4c8;");
                            break;
                        case "over due":
                            setStyle("-fx-background-color: facbc8;");
                            break;
                        default:
                            setStyle(""); 
                    }
                }
            }
        });
        monthChoiceBox.setOnAction(this::choiceBox);
        yearChoiceBox.setOnAction(this::choiceBox); 
        statusChoiceBox.setOnAction(this::choiceBox); 
    
    }
    public void choiceBox(ActionEvent event){
        try {
            showBilling();
        } catch (SQLException ex) {
            Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void showConsumerBills() throws SQLException{
        billTable.getItems().clear(); 
       this.data = billsModel.getConsumerBillsById(this.id);
        if(data != null){
            billTable.getItems().addAll(data);
        }
    }
    public void showBilling() throws SQLException {
        billTable.getItems().clear();

        if (monthChoiceBox.getValue().equals("All") &&
            yearChoiceBox.getValue().equals("All") &&
            statusChoiceBox.getValue().equals("All")) {
            this.data = billsModel.getConsumerBillsById(this.id);
        } else {
            int month = 0;
            if (!monthChoiceBox.getValue().equals("All")) {
                month = months.indexOf(monthChoiceBox.getValue()) + 1;
            }
            int year = 0;
            if (!yearChoiceBox.getValue().equals("All")) {
                year = Integer.parseInt(yearChoiceBox.getValue());
            }
            int status = 0; // Default value for "All"

            String selectedStatus = statusChoiceBox.getValue();
            if ("Unpaid".equals(selectedStatus)) {
                status = 1;
            } else if ("Paid".equals(selectedStatus)) {
                status = 2;
            } else if ("Over Due".equals(selectedStatus)) {
                status = 3;
            } else if ("Paid with penalty".equals(selectedStatus)) {
                status = 4;
            }

            this.data = billsModel.filterConsumerBillsById(id, month, year, status);
        }

    billTable.getItems().addAll(data);
}
    public void setChoices() throws SQLException {
        monthChoiceBox.getItems().clear();
        monthChoiceBox.getItems().add("All");
        for (Bills bill : billsModel.getConsumerBillsById(id)) {
            int month = bill.getBillingDate().getMonthValue();
            if (!monthChoiceBox.getItems().contains(months.get(month - 1))) {
                monthChoiceBox.getItems().add(months.get(month - 1));
            }
        }
        //monthChoiceBox.setValue(months.get(LocalDate.now().getMonthValue() - 1));
        monthChoiceBox.setValue("All");

        yearChoiceBox.getItems().clear();
        yearChoiceBox.getItems().add("All");
        for (Bills bill : billsModel.getConsumerBillsById(id)) {
            int year = bill.getBillingDate().getYear();
            if (!yearChoiceBox.getItems().contains(String.valueOf(year))) {
                yearChoiceBox.getItems().add(String.valueOf(year));
            }
        }
        yearChoiceBox.setValue(String.valueOf(LocalDate.now().getYear()));
        yearChoiceBox.setValue("All");

        statusChoiceBox.getItems().clear();
        statusChoiceBox.getItems().addAll("All", "Paid", "Unpaid", "Over Due");
        statusChoiceBox.setValue("All");
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

        Stage modalWindow = new Stage();
        Parent rootNode = loader.load();
        modalWindow.setScene(new Scene(rootNode));
        PaybillController payBillCon = loader.getController();
      //  payBillCon.setController(this);
        payBillCon.setBillID(billID);
        modalWindow.setResizable(false);
        modalWindow.initModality(Modality.APPLICATION_MODAL);
        modalWindow.centerOnScreen();
        modalWindow.setTitle("Pay Bill");
        modalWindow.setOpacity(1);
        modalWindow.show();
        // Explicitly close the modal window when the Controller is closed
        modalWindow.setOnCloseRequest(e -> modalWindow.close());
        
    }
    public void viewPenaltyDetails(int billID) throws IOException, SQLException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewPenaltyDetails.fxml"));
        Parent rootNode = loader.load();
        Stage modalWindow = new Stage();
//        ViewBillDetailsController viewBillController = loader.getController();
//        viewBillController.setBill(billID);
        modalWindow.setResizable(false);
        modalWindow.setScene(new Scene(rootNode));
        modalWindow.initModality(Modality.APPLICATION_MODAL);
        modalWindow.centerOnScreen(); // Corrected method call to centerOnScreen
        modalWindow.setTitle("Bill Details");
        modalWindow.setOpacity(1);
        modalWindow.show();
    }
    public void viewBillingDetails(int billID) throws IOException, SQLException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewBillDetails.fxml"));
        Parent rootNode = loader.load();
        Stage modalWindow = new Stage();
        ViewBillDetailsController viewBillController = loader.getController();
        viewBillController.setBill(billID);
        modalWindow.setResizable(false);
        modalWindow.setScene(new Scene(rootNode));
        modalWindow.initModality(Modality.APPLICATION_MODAL);
        modalWindow.centerOnScreen(); // Corrected method call to centerOnScreen
        modalWindow.setTitle("Bill Details");
        modalWindow.setOpacity(1);
        modalWindow.show();
    }
    
}
