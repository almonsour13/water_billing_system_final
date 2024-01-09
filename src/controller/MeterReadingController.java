/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;
import controller.accountLoggedSetter.LoggedAccountSetter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import model.meterReading.MeterReading;
import model.meterReading.MeterReadingModel;
import view.consumer.ModalController;
import view.meterReading.EditReadingController;
import view.meterReading.ViewEditRequestController;
import view.meterReading.ViewEditRequestStatusController;
import view.settings.settingPages.SystemLogsModel;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class MeterReadingController implements Initializable {
    
    
    private  LoggedAccountSetter logAccount = new LoggedAccountSetter();
    private SystemLogsModel systemLogsModel = new SystemLogsModel();
    
    ObservableList<MeterReading> data = FXCollections.observableArrayList();
    private MeterReadingModel meterReadingModel; 
    @FXML
    private ChoiceBox<String> monthChoiceBox;
    @FXML
    private ChoiceBox<String> yearChoiceBox;
    @FXML
    private ChoiceBox<String> statusChoiceBox;
    @FXML
    private Button addMeterReading;
    private ObservableList<MeterReading> meterReadingData = FXCollections.observableArrayList();
    @FXML
    private Button saveAllBtn;
    @FXML
    private TableView<MeterReading> meterReadingTable;
    @FXML
    private TableColumn<MeterReading, Integer> noCol;
    @FXML
    private TableColumn<MeterReading, String> meterNoCol;
    @FXML
    private TableColumn<MeterReading, String> nameCol;
    @FXML
    private TableColumn<MeterReading, Integer> prevReadingCol;
    @FXML
    private TableColumn<MeterReading, Integer> curReadingCol;
    @FXML
    private TableColumn<MeterReading, String> dateAddedCol;
    @FXML
    private TableColumn<MeterReading, String> eneteredByCol;
    @FXML
    private TableColumn<MeterReading, String> meterLocationCol;
    @FXML
    private TableColumn<MeterReading, Integer> requestCol;
    @FXML
    private TableColumn<MeterReading, Integer> statusCol;
    @FXML
    private TableColumn<MeterReading, Integer> actionCol;
    private static BorderPane mainPanel = new BorderPane();
    private static Pane pageSetter = new Pane();
    /**
     * Initializes the controller class.
     */
    public MeterReadingController() throws SQLException{
        this.meterReadingModel = new MeterReadingModel();
    }
    public void mainPanel(BorderPane mainPanel){
        this.mainPanel = mainPanel;
        System.out.println(mainPanel);
    }
    public void pageSetter(Pane pageSetter){
        this.pageSetter = pageSetter;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {       
            setChoices();
        } catch (SQLException ex) {
            Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {               
               loadMeterReading();
        } catch (SQLException ex) {
            Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        noCol.setCellValueFactory(new PropertyValueFactory<>("no"));
        meterNoCol.setCellValueFactory(new PropertyValueFactory<>("MeterNo"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        meterLocationCol.setCellValueFactory(new PropertyValueFactory<>("MeterLocation"));
        prevReadingCol.setCellValueFactory(new PropertyValueFactory<>("PrevReading"));
        curReadingCol.setCellFactory(new Callback<TableColumn<MeterReading, Integer>, TableCell<MeterReading, Integer>>() {
            @Override
            public TableCell<MeterReading, Integer> call(TableColumn<MeterReading, Integer> param) {
              return new TableCell<MeterReading, Integer>() {
                final TextField textField;
                {
                    textField = new TextField();
                    textField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
                        try {
                            String input = event.getCharacter();
                            Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            event.consume();
                        }
                    });
                    textField.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
                        MeterReading selectedMeterReading = getTableView().getItems().get(getIndex());
                        if(!textField.getText().isEmpty()){
                            int curReadingValue = Integer.parseInt(textField.getText());
                            selectedMeterReading.setCurReading(curReadingValue);
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
                      
                    int curReadingValue = getTableView().getItems().get(getIndex()).getCurReading();
                    String conCurReadingValue = curReadingValue != 0?String.valueOf(curReadingValue):"";
                    System.out.println(conCurReadingValue);
                    if (!conCurReadingValue.isEmpty()) {
                        textField.setText(conCurReadingValue);
                        textField.setDisable(true);
                    } else {
                        textField.clear();
                        textField.setDisable(false);
                    }

                    setGraphic(textField);
                    setText(null);
               

                  }
                }
              };
            }
          });       
        dateAddedCol.setCellValueFactory(new PropertyValueFactory<>("CurReadingDate"));
        eneteredByCol.setCellValueFactory(new PropertyValueFactory<>("CollectorName"));
        statusCol.setCellFactory(new Callback<TableColumn<MeterReading, Integer>, TableCell<MeterReading, Integer>>() {
            @Override
            public TableCell<MeterReading, Integer> call(TableColumn<MeterReading, Integer> param) {
                return new TableCell<MeterReading, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            MeterReading selectedMeterReading = getTableView().getItems().get(getIndex());
                            try {
                                MeterReading request = meterReadingModel.viewRequest(selectedMeterReading.getMeterReadingId());
                                    if(request!= null){
                                       String statusName = request.getStatus();
                                       if(statusName.equals("Approved")){
                                           setText("Edited");
                                       }
                                    }else{
                                       setText(null);
                                    }
                            } catch (SQLException ex) {
                            }
                        }
                    }
                };
            }
        });            
        requestCol.setCellFactory(new Callback<TableColumn<MeterReading, Integer>, TableCell<MeterReading, Integer>>() {
            @Override
            public TableCell<MeterReading, Integer> call(TableColumn<MeterReading, Integer> param) {
                return new TableCell<MeterReading, Integer>() {
                    final Button viewButton = new Button("view");

                    {
                        viewButton.getStyleClass().add("requestBtn");
                        viewButton.setOnAction(event -> {
                            MeterReading selectedMeterReading = getTableView().getItems().get(getIndex());
                            try {
                                if(viewButton.getText().equals("Pending")){
                                    viewEditRequest(selectedMeterReading.getMeterReadingId());
                                }else{
                                    viewEditRequestStatus(selectedMeterReading.getMeterReadingId());
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
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
                            MeterReading selectedMeterReading = getTableView().getItems().get(getIndex());
                            try {
                                MeterReading request = meterReadingModel.viewRequest(selectedMeterReading.getMeterReadingId());
                                if(request!= null){
                                   String statusName = request.getStatus();
                                  // if(!statusName.equals("Rejected")){
                                        viewButton.setText(statusName);
                                        HBox buttonsBox = new HBox(new HBox(5, viewButton));
                                        buttonsBox.setAlignment(Pos.CENTER);
                                        setGraphic(buttonsBox);
                                //   }
                                }else{
                                   setText(null);
                                }
                            } catch (SQLException ex) {
                            }
                        }
                    }
                };
            }
        });       
        actionCol.setCellFactory(new Callback<TableColumn<MeterReading, Integer>, TableCell<MeterReading, Integer>>() {
            @Override
            public TableCell<MeterReading, Integer> call(TableColumn<MeterReading, Integer> param) {
                return new TableCell<MeterReading, Integer>() {
                    final Button saveButton = new Button();
                    final Button viewBills = new Button();
                    final Button editButton = new Button();
                    

                        {
                            saveButton.getStyleClass().add("actionBtn");
                            saveButton.setGraphic(loadSvgIcon("/assets/icons/save.png"));
                            viewBills.getStyleClass().add("actionBtn");
                            viewBills.setGraphic(loadSvgIcon("/assets/icons/view.png"));
                            editButton.getStyleClass().add("actionBtn");
                            editButton.setGraphic(loadSvgIcon("/assets/icons/edit.png"));
                           
                               
                            saveButton.setOnAction(event -> {
                                MeterReading selectedMeterReading = getTableView().getItems().get(getIndex());
                                String updatedCurReadingString = String.valueOf(selectedMeterReading.getCurReading());

                                    if (!updatedCurReadingString.isEmpty()) {
                                        if(selectedMeterReading.getPrevReading()<Integer.parseInt(updatedCurReadingString)){
                                        System.out.println("Saving updated meter reading for meter Reading ID: " + selectedMeterReading.getMeterReadingId());
                                            
                                            LocalDate currentDate = selectedMeterReading.getReadingDate();
                                            LocalDate billDateValue = null;
                                            LocalDate dueDateValue = null;
                                            int ratePerCubicMeter = 0;
                                            try {
                                                billDateValue = currentDate.plusDays(meterReadingModel.getBillDaysOffset());
                                                dueDateValue = billDateValue.plusDays(meterReadingModel.getDueDateDays());
                                                ratePerCubicMeter = meterReadingModel.getRate();
                                            } catch (SQLException ex) {
                                                 Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                             int consumptionValue = Integer.parseInt(updatedCurReadingString) - selectedMeterReading.getPrevReading();

                                            double billAmount = consumptionValue*ratePerCubicMeter;
                                            try {
                                                meterReadingModel.saveCurrentReading(selectedMeterReading.getMeterReadingId(),Integer.parseInt(updatedCurReadingString),logAccount.getAccount());                       

                                                meterReadingModel.addBillToConsumer(selectedMeterReading.getMeterId(), selectedMeterReading.getMeterReadingId(), 
                                                                                                  billDateValue, dueDateValue, consumptionValue, ratePerCubicMeter,billAmount);
                                                modal("Meter Reading Save");
                                                loadMeterReading();
                                            } catch (SQLException ex) {
                                                Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
                                            } catch (IOException ex) {
                                                Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }else{
                                            try {
                                                modal("Current reading should exceed the previous reading");
                                            } catch (IOException ex) {
                                                Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    }
                            });


                            viewBills.setOnAction(event -> {
                            });
                            editButton.setOnAction(event -> {
                                MeterReading selectedMeterReading = getTableView().getItems().get(getIndex());
                            
                            });
                        }

                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        MeterReading selectedMeterReading = getTableView().getItems().get(getIndex());
                        try {
                            if(meterReadingModel.checkCurrentReading(selectedMeterReading.getMeterId(),selectedMeterReading.getMeterReadingId())){
                                saveButton.setDisable(true);
                                viewBills.setDisable(false);
                            }else{ 
                                saveButton.setDisable(false);
                                viewBills.setDisable(true);
                            }
                            
                            boolean checkInvoice = meterReadingModel.checkInvoice(selectedMeterReading.getMeterReadingId());
                            if(meterReadingModel.viewRequest(selectedMeterReading.getMeterReadingId())!=null || checkInvoice == true){
                                editButton.setDisable(true);
                            }else{
                                editButton.setDisable(false);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                            
                            HBox buttonsBox = new HBox(new HBox(3,saveButton,viewBills, editButton));
                            buttonsBox.setAlignment(Pos.CENTER);
                       
                            setGraphic(buttonsBox);
                            setText(null);
                    }
                }

                };
            }
        });            
        monthChoiceBox.setOnAction(this::choiceBox);
        yearChoiceBox.setOnAction(this::choiceBox);
    }  
    private ImageView loadSvgIcon(String path) {
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(18); // Set the width as needed
        imageView.setFitHeight(18); // Set the height as needed
        return imageView;
    }
    ObservableList<String> months = 
                FXCollections.observableArrayList(
                        "Jan","Feb","Mar","Apr","May","Jun",
                        "Jul","Aug","Sep","Oct","Nov","Dec");
    public void setChoices() throws SQLException{
        monthChoiceBox.setItems(months);
        LocalDate date = LocalDate.now();
        int month = date.getMonthValue()-1;
        monthChoiceBox.setValue(months.get(month));
        for(MeterReading meterReading: meterReadingModel.getMeterReading()){
            String yearSplit[]= String.valueOf(meterReading.getReadingDate()).split("-");
            
            if(!yearChoiceBox.getItems().contains(yearSplit[0])){
                yearChoiceBox.getItems().add(yearSplit[0]);
            }
        }
        yearChoiceBox.setValue(String.valueOf(date.getYear()));
    }
    public void choiceBox(ActionEvent event){
        try {
            loadMeterReading();
        } catch (SQLException ex) {
            Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void loadMeterReading() throws SQLException {
        int month = months.indexOf(monthChoiceBox.getValue()) + 1;
        int year = Integer.parseInt(yearChoiceBox.getValue());
        addMeterReading.setDisable(meterReadingModel.checkCurrentReadingMonth());
        try {
           meterReadingTable.setItems(meterReadingModel.filterMeterReading(month, year));
          // meterReadingTable.setItems(meterReadingModel.getMeterReading());
        } catch (SQLException ex) {
            Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void viewEditRequest(int meterReadingID) throws IOException, SQLException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/meterReading/viewEditRequest.fxml"));
       
        Parent rootNode = loader.load();

        ViewEditRequestController ViewEditRequestController = loader.getController();
        ViewEditRequestController.setMeterReading(meterReadingID);
        ViewEditRequestController.setController(this);
        Stage modalWindow = new Stage();
        modalWindow.setResizable(false);
        modalWindow.setScene(new Scene(rootNode));

        modalWindow.initModality(Modality.APPLICATION_MODAL);
        modalWindow.centerOnScreen();
        modalWindow.setTitle("Request Edit");
        modalWindow.setOpacity(1);
        modalWindow.show();
       
    }
    public void viewEditRequestStatus(int meterReadingID) throws IOException, SQLException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/meterReading/viewEditRequestStatus.fxml"));
       
        Parent rootNode = loader.load();

        ViewEditRequestStatusController ViewEditRequestStatusController = loader.getController();
        ViewEditRequestStatusController.setMeterReading(meterReadingID);
        ViewEditRequestStatusController.setController(this);
        Stage modalWindow = new Stage();
        modalWindow.setResizable(false);
        modalWindow.setScene(new Scene(rootNode));

        modalWindow.initModality(Modality.APPLICATION_MODAL);
        modalWindow.centerOnScreen();
        modalWindow.setTitle("Request Edit");
        modalWindow.setOpacity(1);
        modalWindow.show();
       
    }
    @FXML
    private void addMeterReading(ActionEvent event) throws SQLException {
        meterReadingModel.addNewMeterReading();
        YearMonth currentYearMonth = YearMonth.now();
        
        // Extract the current month
        Month currentMonth = currentYearMonth.getMonth();
        systemLogsModel.insertLog(logAccount.getAccount(), "Generated meter readings for the month of "+currentMonth+" "+currentYearMonth+"");      
        loadMeterReading();
    }
    @FXML
    private void saveAllCurrentReading() throws SQLException, IOException {
        int modal = 0;
        for (MeterReading meterReading : meterReadingTable.getItems()) {
            int currentReading = meterReading.getCurReading();
            if (currentReading > 0 && currentReading > meterReading.getPrevReading()) {
                LocalDate currentDate = meterReading.getReadingDate();
                LocalDate billDate = currentDate.plusDays(meterReadingModel.getBillDaysOffset());
                LocalDate dueDate = billDate.plusDays(meterReadingModel.getDueDateDays());
                int consumption = currentReading - meterReading.getPrevReading();
                int rate =  meterReadingModel.getRate();
                double billAmount = consumption *rate;
                if(!meterReadingModel.checkCurrentReading(meterReading.getMeterId(), meterReading.getMeterReadingId())){
                    try {
                        meterReadingModel.saveCurrentReading(meterReading.getMeterReadingId(), currentReading, logAccount.getAccount());
                        meterReadingModel.addBillToConsumer(meterReading.getMeterId(), meterReading.getMeterReadingId(), billDate, dueDate, consumption,rate, billAmount);
                    } catch (SQLException ex) {
                        Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                if (currentReading == 0) {
                    modal = 1;
                } else {
                    modal = 2;
                }
            }
        }
        if(modal == 1){
            modal("Some of readings cannot be saved because current reading field is empty.");
        }else{
            modal("Some of readings does not exceed the previous reading.");
        }
        loadMeterReading();
    }
    public void updateSaveAllBtn(){
        for(MeterReading meter:meterReadingData){
            if(meter.getCurReading() == 0){
                saveAllBtn.setDisable(true);
            }else{
                saveAllBtn.setDisable(false);
            }
        }
    }
    public void modal(String prompt) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Modal.fxml"));
        VBox modal = loader.load();
        ModalController modCOn = loader.getController();
        modCOn.setPromptLabel(prompt);
        // Create a new stage for the internal frame
        Stage internalFrame = new Stage();
        internalFrame.initStyle(StageStyle.UTILITY); // Customize the stage style
        internalFrame.initModality(Modality.WINDOW_MODAL);
        internalFrame.initOwner(mainPanel.getScene().getWindow()); // Set the owner stage (mainPanel's stage)

        Scene scene = new Scene(modal);
        internalFrame.setScene(scene);

        internalFrame.setTitle("Internal Frame");
        internalFrame.setResizable(false);

        internalFrame.show();
    }

}