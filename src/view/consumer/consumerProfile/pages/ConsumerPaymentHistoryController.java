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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.payment.Payment;
import model.payment.PaymentModel;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class ConsumerPaymentHistoryController implements Initializable {

    @FXML
    private TableView<Payment> paymentTable;
    @FXML
    private TableColumn<Payment, Integer> noCol;
    @FXML
    private TableColumn<?, ?> meterNumber;
    @FXML
    private TableColumn<?, ?> meterLocation;
    @FXML
    private TableColumn<Payment, String> nameCol;
    @FXML
    private TableColumn<Payment, LocalDate> paymentDateCol;
    @FXML
    private TableColumn<Payment, Integer> billAmountCol;
    @FXML
    private TableColumn<Payment, Integer> totalAmountCol;
    @FXML
    private TableColumn<Payment, Double> paymentAmountCol;
    @FXML
    private TableColumn<Payment, String> statusCol;
    @FXML
    private TableColumn<Payment, Integer> actionCol;
    private static BorderPane mainPanel = new BorderPane();
    private static Pane pageSetter = new Pane();
    private int id;
    private PaymentModel paymentModel = new PaymentModel();

    /**
     * Initializes the controller class.
     */
    public void setConsumer(int id) throws SQLException{
        this.id = id;
        showPaymentHistory();
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
        
    }    
    public void showPaymentHistory() throws SQLException{
        paymentTable.getItems().clear();
        paymentTable.getItems().addAll(paymentModel.getPaymentsById(id));
    }
    @FXML
    private void selectInfo(MouseEvent event) {
    }
    
}
