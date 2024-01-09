/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.bills.Bills;
import model.payment.Payment;
import model.payment.PaymentModel;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class PaymentController implements Initializable {

    @FXML
    private TableColumn<Payment, Integer> noCol;
    @FXML
    private TableColumn<Payment, String> nameCol;
    @FXML
    private TableColumn<Payment, LocalDate> paymentDateCol;
    @FXML
    private TableColumn<Payment, Integer> totalAmountCol;
    @FXML
    private TableColumn<Payment, Double> paymentAmountCol;
    @FXML
    private TableColumn<Payment, String> statusCol;
    @FXML
    private TableColumn<Payment, Integer> actionCol;
    ObservableList<Payment> data;
    private PaymentModel paymentModel;
    @FXML
    private TableView<Payment> paymentTable;
    @FXML
    private TableColumn<?, ?> meterNumber;
    @FXML
    private TableColumn<?, ?> meterLocation;
    @FXML
    private ChoiceBox<String> monthChoiceBox;
    @FXML
    private ChoiceBox<String> yearChoiceBox;
    @FXML
    private ChoiceBox<String> statusChoiceBox;
    @FXML
    private ChoiceBox<String> meterLocChoiceBox;
    ObservableList<String> months = 
                FXCollections.observableArrayList(
                        "Jan","Feb","Mar","Apr","May","Jun",
                        "Jul","Aug","Sep","Oct","Nov","Dec");
    @FXML
    private TableColumn<Payment, Integer> billAmountCol;
    @FXML
    private TextField search;
    /**
     * Initializes the controller class.
     */
    public PaymentController(){
        this.paymentModel = new PaymentModel();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            setChoices();
            showPayments();
        } catch (SQLException ex) {
            Logger.getLogger(PaymentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        noCol.setCellValueFactory(new PropertyValueFactory<>("No"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        paymentDateCol.setCellValueFactory(new PropertyValueFactory<>("PaymentDate"));
        totalAmountCol.setCellFactory(new Callback<TableColumn<Payment, Integer>, TableCell<Payment, Integer>>() {
            @Override
            public TableCell<Payment, Integer> call(TableColumn<Payment, Integer> param) {
                return new TableCell<Payment, Integer>() {
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
                            Payment payment = getTableView().getItems().get(getIndex());
                            billAmountText.setText(String.valueOf(payment.getTotalAmount()));
                            HBox textContainer = null;
                            if (payment.getPenaltyAmount() != 0) {
                                penaltyAmountText.setText(String.valueOf(payment.getPenaltyAmount()));
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
        billAmountCol.setCellFactory(new Callback<TableColumn<Payment, Integer>, TableCell<Payment, Integer>>() {
            @Override
            public TableCell<Payment, Integer> call(TableColumn<Payment, Integer> param) {
                return new TableCell<Payment, Integer>() {
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
                            Payment Payment = getTableView().getItems().get(getIndex());
                            billAmountText.setText(String.valueOf(Payment.getTotalAmount()));
                            HBox textContainer = null;
                            if (Payment.getPenaltyAmount() != 0) {
                                penaltyAmountText.setText(String.valueOf(Payment.getPenaltyAmount()));
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
        totalAmountCol.setCellFactory(new Callback<TableColumn<Payment, Integer>, TableCell<Payment, Integer>>() {
            @Override
            public TableCell<Payment, Integer> call(TableColumn<Payment, Integer> param) {
                return new TableCell<Payment, Integer>() {
                    final Text totalAmount = new Text();

                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Payment Payment = getTableView().getItems().get(getIndex());
                            totalAmount.setText(String.valueOf(Payment.getTotalAmount()+Payment.getPenaltyAmount()));
                            HBox textContainer = textContainer = new HBox(totalAmount);

                            textContainer.setAlignment(Pos.CENTER_LEFT);
                            setGraphic(textContainer);
                            setText(null);
                        }
                    }
                };
            }
        });
        meterNumber.setCellValueFactory(new PropertyValueFactory<>("MeterNumber"));
        meterLocation.setCellValueFactory(new PropertyValueFactory<>("MeterLocation"));
        paymentAmountCol.setCellValueFactory(new PropertyValueFactory<>("PaymentAmount"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("Status"));
        statusCol.setCellFactory(column -> new TableCell<Payment, String>() {
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
                        case "paid with penalty":
                            getStyleClass().setAll("disconnected-cell");
                            break;
                        default:
                            getStyleClass().setAll("default-cell");
                            break;
                    }
                }
            }
        });       
        actionCol.setCellFactory(new Callback<TableColumn<Payment, Integer>, TableCell<Payment, Integer>>() {
            @Override
            public TableCell<Payment, Integer> call(TableColumn<Payment, Integer> param) {
                return new TableCell<Payment, Integer>() {
                    final Button paymentDetailsBtn = new Button("PD");
                    final Button archiveBtn = new Button("arc");

                        {
                            paymentDetailsBtn.getStyleClass().add("actionBtn");
                            archiveBtn.getStyleClass().add("actionBtn-arc");
                            
                            paymentDetailsBtn.setOnAction(event -> {
                            });

                            archiveBtn.setOnAction(event -> {
                               // Bills selectedBill = getTableView().getItems().get(getIndex());
                            });
                        }

                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        
                        HBox buttonsBox = new HBox(5, paymentDetailsBtn, archiveBtn);
                        buttonsBox.setAlignment(Pos.CENTER);
                       
                        setGraphic(buttonsBox);
                        setText(null);
                    }
                }

                };
            }
        });
        monthChoiceBox.setOnAction(this::monthChoice);
        yearChoiceBox.setOnAction(this::yearChoice); 
        meterLocChoiceBox.setOnAction(this::choiceBox); 
        statusChoiceBox.setOnAction(this::choiceBox); 
        
    }    
    public void choiceBox(ActionEvent event){
        try {
            showPayments();
        } catch (SQLException ex) {
            Logger.getLogger(PaymentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setChoices() throws SQLException{
        yearChoiceBox.getItems().clear();
        yearChoiceBox.getItems().add("All");
        for(Payment payment:paymentModel.getPayments()){
            String splitDate[] = String.valueOf(payment.getPaymentDate()).split("-");
            if(!yearChoiceBox.getItems().contains(splitDate[0])){
                yearChoiceBox.getItems().add(splitDate[0]);
            }
        }
        yearChoiceBox.setValue(String.valueOf(LocalDate.now().getYear()));
     
        monthChoiceBox.getItems().clear();
        monthChoiceBox.setValue(months.get(LocalDate.now().getMonthValue() - 1));
        monthChoiceBox.getItems().add("All");
        for (Payment payment:paymentModel.getPayments()) {
            String splitDate[] = String.valueOf(payment.getPaymentDate()).split("-");
            if (!monthChoiceBox.getItems().contains(months.get(Integer.parseInt(splitDate[1]) - 1))
              && yearChoiceBox.getValue().equals(splitDate[0])) {
                monthChoiceBox.getItems().add(months.get(Integer.parseInt(splitDate[1]) - 1));
            }
        }
        meterLocChoiceBox.setValue("All");
        meterLocChoiceBox.getItems().add("All");
        for (Payment payment:paymentModel.getPayments()) {
            if (!meterLocChoiceBox.getItems().contains(payment.getMeterLocation())) {
                meterLocChoiceBox.getItems().add(payment.getMeterLocation());
            }
        }
        statusChoiceBox.setValue("All");
        statusChoiceBox.getItems().add("All");
        for (Payment payment:paymentModel.getPayments()) {
            if (!statusChoiceBox.getItems().contains(payment.getStatus())) {
                statusChoiceBox.getItems().add(payment.getStatus());
            }
        }
        
    }
    public void monthChoice(ActionEvent event){
        try {
            showPayments();
        } catch (SQLException ex) {
            Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void yearChoice(ActionEvent event){
        monthChoiceBox.getItems().clear();
        monthChoiceBox.setValue("All");
        monthChoiceBox.getItems().add("All");
        try {
            for (Payment payment:paymentModel.getPayments()) {
                String splitDate[] = String.valueOf(payment.getPaymentDate()).split("-");
                if (!monthChoiceBox.getItems().contains(months.get(Integer.parseInt(splitDate[1]) - 1))
                && yearChoiceBox.getValue().equals(splitDate[0])) {
                    monthChoiceBox.getItems().add(months.get(Integer.parseInt(splitDate[1]) - 1));
                }
            }
            showPayments();
        } catch (SQLException ex) {
          
        }
    }
    public void showPayments() throws SQLException{
        paymentTable.getItems().clear();
        int month = monthChoiceBox.getValue()== "All"?0: months.indexOf(monthChoiceBox.getValue())+1;
        int year = yearChoiceBox.getValue()== "All"?0: Integer.parseInt(yearChoiceBox.getValue());
        
        int status;
            switch (statusChoiceBox.getValue()) {
                case "paid":
                    status = 1;
                    break;
                case "Paid with penalty":
                    status = 2;
                    break;
                default:
                    status = 0;
            }
        data = paymentModel.filterPayments(month, year, meterLocChoiceBox.getValue(),status,search.getText());
        paymentTable.getItems().addAll(data);
    }
    @FXML
    private void selectInfo(MouseEvent event) {
    }

    @FXML
    private void search(KeyEvent event) throws SQLException {
        showPayments();
    }

    
}
