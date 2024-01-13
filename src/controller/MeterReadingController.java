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
import view.settings.settingPages.SystemLogsModel;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class MeterReadingController implements Initializable {
    
    
    private  LoggedAccountSetter logAccount = new LoggedAccountSetter();
    private SystemLogsModel systemLogsModel = new SystemLogsModel();
    ObservableList<String> months = 
                FXCollections.observableArrayList(
                        "Jan","Feb","Mar","Apr","May","Jun",
                        "Jul","Aug","Sep","Oct","Nov","Dec");
    
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
    @FXML
    private TextField searchVal;
    /**
     * Initializes the controller class.
     */
    public MeterReadingController() throws SQLException{
        this.meterReadingModel = new MeterReadingModel();
    }
    public void mainPanel(BorderPane mainPanel){
        this.mainPanel = mainPanel;
    }
    public void pageSetter(Pane pageSetter){
        this.pageSetter = pageSetter;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {       
            setChoices();
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
        statusCol.setCellValueFactory(new PropertyValueFactory<>("Status"));
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
                                        if(Integer.parseInt(updatedCurReadingString)>=selectedMeterReading.getPrevReading()){
                                           
                                            LocalDate currentDate = selectedMeterReading.getReadingDate();
                                            try {
                                                LocalDate billDateValue = currentDate.plusDays(meterReadingModel.getBillDaysOffset());
                                                LocalDate dueDateValue = billDateValue.plusDays(meterReadingModel.getDueDateDays());
                                                int ratePerCubicMeter = meterReadingModel.getRate();
                                            
                                                int consumptionValue = Integer.parseInt(updatedCurReadingString) - selectedMeterReading.getPrevReading();

                                                double billAmount = consumptionValue*ratePerCubicMeter;
                                            
                                                meterReadingModel.saveCurrentReading(selectedMeterReading.getMeterReadingId(),Integer.parseInt(updatedCurReadingString),logAccount.getAccount());                       

                                                meterReadingModel.addBillToConsumer(selectedMeterReading.getMeterId(), selectedMeterReading.getcID() ,selectedMeterReading.getMeterReadingId(), 
                                                                                                  billDateValue, dueDateValue, consumptionValue, ratePerCubicMeter,billAmount);
                                                modal("Meter Reading Save",1);
                                                loadMeterReading();
                                            } catch (SQLException ex) {
                                                Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
                                            } catch (IOException ex) {
                                                Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }else{
                                            try {
                                                modal("Current reading should exceed the previous reading",2);
                                            } catch (IOException ex) {
                                                Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    }
                            });
                            viewBills.setOnAction(event -> {
                            });
                            editButton.setOnAction(event -> {
                                MeterReading meterReading = getTableView().getItems().get(getIndex());
                                try {
                                    editReading(meterReading.getMeterReadingId(),meterReading.getPrevReading() ,meterReading.getCurReading());
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
                        MeterReading meterReading = getTableView().getItems().get(getIndex());
                        try {
                            if(meterReading.getCurReading() != 0){
                                saveButton.setDisable(true);
                                viewBills.setDisable(false);
                            }else{ 
                                saveButton.setDisable(false);
                                viewBills.setDisable(true);
                            }
                            
                            boolean checkInvoice = meterReadingModel.checkInvoice(meterReading.getMeterReadingId());
                            if(meterReading.getCurReading() == 0 || checkInvoice == true ||
                                    meterReadingModel.checkBillingStatusByMrID(meterReading.getMeterReadingId()) == true){
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
        monthChoiceBox.setOnAction(this::monthChoice);
        yearChoiceBox.setOnAction(this::yearChoice);
        statusChoiceBox.setOnAction(this::choiceBox);
        
    }  
    private ImageView loadSvgIcon(String path) {
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(18); // Set the width as needed
        imageView.setFitHeight(18); // Set the height as needed
        return imageView;
    }
    public void choiceBox(ActionEvent event){
        try {
            loadMeterReading();
        } catch (SQLException ex) {
            Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setChoices() throws SQLException {      
        yearChoiceBox.getItems().clear();
        yearChoiceBox.getItems().add("All");
        for(MeterReading meterReading: meterReadingModel.getMeterReading()){
            String splitDate[] = String.valueOf(meterReading.getReadingDate()).split("-");
            if(!yearChoiceBox.getItems().contains(splitDate[0])){
                yearChoiceBox.getItems().add(splitDate[0]);
            }
        }
        yearChoiceBox.setValue(String.valueOf(LocalDate.now().getYear()));
     
        monthChoiceBox.getItems().clear();
        monthChoiceBox.setValue(months.get(LocalDate.now().getMonthValue() - 1));
        monthChoiceBox.getItems().add("All");
        for (MeterReading meterReading: meterReadingModel.getMeterReading()) {
            String splitDate[] = String.valueOf(meterReading.getReadingDate()).split("-");
            if (!monthChoiceBox.getItems().contains(months.get(Integer.parseInt(splitDate[1]) - 1))
              && yearChoiceBox.getValue().equals(splitDate[0])) {
                monthChoiceBox.getItems().add(months.get(Integer.parseInt(splitDate[1]) - 1));
            }
        }
    }
    public void monthChoice(ActionEvent event){
        try {
            loadMeterReading();
        } catch (SQLException ex) {
            Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void yearChoice(ActionEvent event){
        monthChoiceBox.getItems().clear();
        monthChoiceBox.setValue("All");
        monthChoiceBox.getItems().add("All");
        try {
            for (MeterReading meterReading: meterReadingModel.getMeterReading()) {
                String splitDate[] = String.valueOf(meterReading.getReadingDate()).split("-");
                if (!monthChoiceBox.getItems().contains(months.get(Integer.parseInt(splitDate[1]) - 1))
                && yearChoiceBox.getValue().equals(splitDate[0])) {
                    monthChoiceBox.getItems().add(months.get(Integer.parseInt(splitDate[1]) - 1));
                }
            }
            loadMeterReading();
        } catch (SQLException ex) {
          
        }
    }
    public void loadMeterReading() throws SQLException {
        int month = monthChoiceBox.getValue()== "All"?0: months.indexOf(monthChoiceBox.getValue())+1;
        int year = yearChoiceBox.getValue()== "All"?0: Integer.parseInt(yearChoiceBox.getValue());
        addMeterReading.setDisable(meterReadingModel.checkCurrentReadingMonth());
        meterReadingTable.setItems(meterReadingModel.filterMeterReading(month, year,searchVal.getText()));
        updateSaveAllBtn();
    }
    public void editReading(int meterReadingID, int curReading, int oldValue) throws IOException, SQLException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/meterReading/editReading.fxml"));
       
        VBox modal = loader.load();
        EditReadingController editCon = loader.getController();
        editCon.setMeterReading(meterReadingID,curReading, oldValue);
        editCon.setController(this);
        editCon.mainPanel(mainPanel);
        Stage internalFrame = new Stage();
        internalFrame.initStyle(StageStyle.UTILITY); // Customize the stage style
        internalFrame.initModality(Modality.WINDOW_MODAL);
        internalFrame.initOwner(mainPanel.getScene().getWindow()); // Set the owner stage (mainPanel's stage)
       // internalFrame.getIcons().add(getIcon());
        internalFrame.setTitle("Edit Reading Value");
        Scene scene = new Scene(modal);
        internalFrame.setScene(scene);
        internalFrame.setResizable(false);

        internalFrame.show();
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
                        meterReadingModel.addBillToConsumer(meterReading.getMeterId(), meterReading.getcID() ,meterReading.getMeterReadingId(), billDate, dueDate, consumption,rate, billAmount);
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
//        if(modal == 1){
//            modal("Some of readings cannot be saved because current reading field is empty.");
//        }else{
//            modal("Some of readings does not exceed the previous reading.");
//        }
        loadMeterReading();
    }
    public void updateSaveAllBtn(){
        for(MeterReading meterReading : meterReadingTable.getItems()){
            if(meterReading.getCurReading() != 0){
                saveAllBtn.setDisable(true);
            }else{
                saveAllBtn.setDisable(false);
            }
        }
    }
    public void modal(String prompt, int type) throws IOException{
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

        internalFrame.setTitle(type == 1?"Success":"Error");
        internalFrame.setResizable(false);

        internalFrame.show();
    }

    @FXML
    private void search(ActionEvent event) throws SQLException {
        loadMeterReading();
    }

}