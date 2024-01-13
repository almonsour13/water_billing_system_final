/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import controller.accountLoggedSetter.LoggedAccountSetter;
import controller.consumer.AddMeterNoController;
import model.consumer.ConsumerModel;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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
import javax.swing.JOptionPane;
import model.consumer.Consumer;
import view.consumer.ConsumerBillHistoryController;
import view.consumer.ModalController;
import view.consumer.consumerProfile.ConsumerProfileController;
import view.settings.settingPages.SystemLogsModel;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class ConsumerController implements Initializable {
    @FXML
    private TableColumn<Consumer, Integer> noColumn;
    private TableColumn<Consumer, Integer> idColumn;
    @FXML
    private TableColumn<Consumer, String> firstNameColumn;
    @FXML
    private TableColumn<Consumer, String> middleNameColumn;
    @FXML
    private TableColumn<Consumer, String> lastNameColumn;
    @FXML
    private TableColumn<Consumer, Integer> contactNoColumn;
    @FXML
    private TableColumn<Consumer, String> purokColumn;
    @FXML
    private TableColumn<Consumer, String> postalCodeColumn;
    @FXML
    private TableColumn<Consumer, Integer> statusColumn;
    private HomeWindowController conCon;
    ObservableList<Consumer> data;
    public ConsumerModel ConsumerModel;
    @FXML
    private TableView<Consumer> consumerTable = new TableView<>();
    @FXML
    private ChoiceBox<String> purokChoices;
    @FXML
    private ChoiceBox<String> statusChoices;
    @FXML
    private TextField searchValue;
    private Button archiveBtn;
    private Button editInfoBtn;
    private Button viewBillsBtn;
    private Button addBillBtn;
    private Button viewPaymentHistBtn;
    @FXML
    private TableColumn<Consumer, Integer> actionCol;
    @FXML
    private TableColumn<?, ?> dateAddedCol;
    @FXML
    private ChoiceBox<String> monthChoices;
    @FXML
    private ChoiceBox<String> yearChoices;
    ObservableList<String> months = 
                FXCollections.observableArrayList(
                        "Jan","Feb","Mar","Apr","May","Jun",
                        "Jul","Aug","Sep","Oct","Nov","Dec");
    private static BorderPane mainPanel = new BorderPane();
    private static Pane pageSetter = new Pane();
    
    private  LoggedAccountSetter logAccount = new LoggedAccountSetter();
    private SystemLogsModel systemLogsModel = new SystemLogsModel();
    @FXML
    private Button addConsumerBtn;
    /**
     * Initializes the controller class.
     */
    public ConsumerController() throws SQLException{
        this.ConsumerModel = new ConsumerModel();
      
    }
    public void setController(HomeWindowController conCon){
        this.conCon = conCon;
    }
    public void mainPanel(BorderPane mainPanel){
        this.mainPanel = mainPanel;
    }
    public void pageSetter(Pane pageSetter){
        this.pageSetter = pageSetter;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conCon = new HomeWindowController();
        
        addConsumerBtn.setGraphic(loadSvgIcon("/assets/icons/addPerson.png"));
        noColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        middleNameColumn.setCellValueFactory(new PropertyValueFactory<>("MidName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        contactNoColumn.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        purokColumn.setCellValueFactory(new PropertyValueFactory<>("purok"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        dateAddedCol.setCellValueFactory(new PropertyValueFactory<>("DateAdded"));
        statusColumn.setCellFactory(new Callback<TableColumn<Consumer, Integer>, TableCell<Consumer, Integer>>() {
            @Override
            public TableCell<Consumer, Integer> call(TableColumn<Consumer, Integer> param) {
                return new TableCell<Consumer, Integer>() {
                    final ChoiceBox<String> status = new ChoiceBox();
                   
                    {
                        status.setItems(FXCollections.observableArrayList("Active","Inactive"));
                        status.getSelectionModel().selectedItemProperty().addListener((observable, oldItem, newItem) -> {
                            if (newItem != null) {
                                Consumer consumer = getTableView().getItems().get(getIndex());

                                try {
                                    String statusValue = newItem;
                                    if (!statusValue.equals(consumer.getStatus())) {
                                        ConsumerModel.updateConsumerStatus(consumer.getID(), statusValue.equals("Active") ? 1 : 2);
                                        modal("Consumer Status Updated");
                                        showConsumerTable();
                                        systemLogsModel.insertLog(logAccount.getAccount(), "Changed status to "+statusValue+" for a consumer ("+consumer.getFirstName()+" "+consumer.getLastName()+")");
                                    }
                                } catch (SQLException ex) {
                                    Logger.getLogger(ConsumerController.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(ConsumerController.class.getName()).log(Level.SEVERE, null, ex);
                                }
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
                            Consumer consumer = getTableView().getItems().get(getIndex());
                            String statusValue = consumer.getStatus();
                            status.setValue(statusValue);
                            status.getStyleClass().clear();
                            status.getStyleClass().add("statusChoiceBox");
                            status.getStyleClass().add("statusChoiceBox");
                            if(statusValue.equals("Active")){
                                status.getStyleClass().add("green");
                            }else if(statusValue.equals("Inactive")){
                                status.getStyleClass().add("red");
                            }

                            
                            HBox buttonsBox = new HBox(new HBox(status));
                            buttonsBox.setAlignment(Pos.CENTER);
                       
                            setGraphic(buttonsBox);
                           // setText(statusValue);
                        }
                    }
                };
            }
        });        
        actionCol.setCellFactory(new Callback<TableColumn<Consumer, Integer>, TableCell<Consumer, Integer>>() {
            @Override
            public TableCell<Consumer, Integer> call(TableColumn<Consumer, Integer> param) {
                return new TableCell<Consumer, Integer>() {
                    final Button addMeterNoButton = new Button();
                    final Button viewProfileButton = new Button();
                    final Button editProfileButton = new Button();
                    final Button archiveButton = new Button();

                    {
                        addMeterNoButton.getStyleClass().add("actionBtn");
                        addMeterNoButton.setGraphic(loadSvgIcon("/assets/icons/meter.png"));
                        viewProfileButton.getStyleClass().add("actionBtn");
                        viewProfileButton.setGraphic(loadSvgIcon("/assets/icons/view.png"));
                        editProfileButton.getStyleClass().add("actionBtn");
                        editProfileButton.setGraphic(loadSvgIcon("/assets/icons/edit.png"));
                        archiveButton.getStyleClass().add("actionBtn-arc");
                        archiveButton.setGraphic(loadSvgIcon("/assets/icons/archive.png"));
                        
                        addMeterNoButton.setOnAction(event -> {
                            Consumer selectedConsumer = getTableView().getItems().get(getIndex());
                            try {
                                addMeterNo(selectedConsumer.getID());
                            } catch (IOException ex) {
                                Logger.getLogger(ConsumerController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(ConsumerController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        viewProfileButton.setOnAction(event -> {
                            Consumer selectedConsumer = getTableView().getItems().get(getIndex());
                            try {
                                viewConsumerProfile(selectedConsumer.getID());
                            } catch (IOException ex) {
                                Logger.getLogger(ConsumerController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(ConsumerController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                           
                        });
                        editProfileButton.setOnAction(event -> {
                            Consumer selectedConsumer = getTableView().getItems().get(getIndex());
                            try {
                                editConsumer(selectedConsumer.getID());
                            } catch (IOException ex) {
                                Logger.getLogger(ConsumerController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        archiveButton.setOnAction(event -> {
                            Consumer selectedConsumer = getTableView().getItems().get(getIndex());
                            try {
                                archiveConsumer(selectedConsumer.getID());
                                modal("Consumer Moved to Archived");
                                systemLogsModel.insertLog(logAccount.getAccount(), "Successfully archived a consumer ("+selectedConsumer.getFirstName()+" "+selectedConsumer.getLastName()+")");
                                    
                            } catch (SQLException ex) {
                                Logger.getLogger(ConsumerController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(ConsumerController.class.getName()).log(Level.SEVERE, null, ex);
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
                            Consumer consumer = getTableView().getItems().get(getIndex());
                            if(!consumer.getStatus().equals("Active")){
                                addMeterNoButton.setDisable(true);
                            }else{
                                addMeterNoButton.setDisable(false);
                            }
                            HBox buttonsBox = new HBox(new HBox(3,addMeterNoButton,viewProfileButton, editProfileButton,archiveButton));
                            buttonsBox.setAlignment(Pos.CENTER);
                       
                            setGraphic(buttonsBox);
                            setText(null);
                        }
                    }
                };
            }
        });
        consumerTable.setRowFactory(tv -> new TableRow<Consumer>() {
            @Override
            protected void updateItem(Consumer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    getStyleClass().removeAll("active", "inactive");
                } else {
                    switch (item.getStatus().toLowerCase()) {
                        case "inactive":
                            setStyle("-fx-background-color: facbc8;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });
        try {
            setChoices();
            showConsumerTable();
        } catch (SQLException ex) {
            Logger.getLogger(ConsumerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        statusChoices.setOnAction(this::filterChoices);
        purokChoices.setOnAction(this::filterChoices);
        monthChoices.setOnAction(this::monthChoice);
        yearChoices.setOnAction(this::yearChoice);
        purokChoices.setOnAction(this::filterChoices);
    }  
    private ImageView loadSvgIcon(String path) {
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(18); // Set the width as needed
        imageView.setFitHeight(18); // Set the height as needed
        return imageView;
    }
    public void showConsumerTable() throws SQLException{
        consumerTable.getItems().clear();
        int month = monthChoices.getValue()== "All"?0: months.indexOf(monthChoices.getValue())+1;
        int year = yearChoices.getValue()== "All"?0: Integer.parseInt(yearChoices.getValue());
        consumerTable.getItems().clear();
        int status = 0;
        if(statusChoices.getValue().equals("Active")){
            status = 1;
        }else if(statusChoices.getValue().equals("Disconnected")){
            status = 2;
        }else if(statusChoices.getValue().equals("Cut")){
            status = 3;
        }
        this.data = ConsumerModel.filterConsumer(purokChoices.getValue(),status,month,year,searchValue.getText());
        consumerTable.getItems().addAll(data);
    }
    public void setChoices() throws SQLException {
        yearChoices.getItems().clear();
        yearChoices.getItems().add("All");
        yearChoices.getItems().add("All");
        for(Consumer consumer:ConsumerModel.getConsumers()){
            String splitDate[] = String.valueOf(consumer.getDateAdded()).split("-");
            if(!yearChoices.getItems().contains(splitDate[0])){
                yearChoices.getItems().add(splitDate[0]);
            }
        }
        yearChoices.setValue("All");
     
        monthChoices.getItems().clear();
        monthChoices.setValue("All");
        monthChoices.getItems().add("All");
        for (Consumer consumer:ConsumerModel.getConsumers()) {
            String splitDate[] = String.valueOf(consumer.getDateAdded()).split("-");
            if (!monthChoices.getItems().contains(months.get(Integer.parseInt(splitDate[1]) - 1))
              && yearChoices.getValue().equals(splitDate[0])) {
                monthChoices.getItems().add(months.get(Integer.parseInt(splitDate[1]) - 1));
            }
        }
        purokChoices.setValue("All");
        purokChoices.getItems().add("All");
        for (Consumer consumer:ConsumerModel.getConsumers()) {
            String splitDate[] = String.valueOf(consumer.getPurok()).split("-");
            if (!purokChoices.getItems().contains(consumer.getPurok())){
                purokChoices.getItems().add(consumer.getPurok());
            }
        }
        statusChoices.setValue("All");
        statusChoices.getItems().add("All");
        for (Consumer consumer:ConsumerModel.getConsumers()) {
            String splitDate[] = String.valueOf(consumer.getStatus()).split("-");
            if (!statusChoices.getItems().contains(consumer.getStatus())){
                statusChoices.getItems().add(consumer.getStatus());
            }
        }
    }
     public void yearChoice(ActionEvent event){
        monthChoices.getItems().clear();
        monthChoices.setValue("All");
        monthChoices.getItems().add("All");
        try {
            for (Consumer consumer:ConsumerModel.getConsumers()) {
                String splitDate[] = String.valueOf(consumer.getDateAdded()).split("-");
                if (!monthChoices.getItems().contains(months.get(Integer.parseInt(splitDate[1]) - 1))
                && yearChoices.getValue().equals(splitDate[0])) {
                    monthChoices.getItems().add(months.get(Integer.parseInt(splitDate[1]) - 1));
                }
            }
            showConsumerTable();
        } catch (SQLException ex) {
          
        }
    }
    public void filterChoices(ActionEvent event){
        try {
            showConsumerTable();
        } catch (SQLException ex) {
            Logger.getLogger(ConsumerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    public void monthChoice(ActionEvent event){
        try {
            showConsumerTable();
        } catch (SQLException ex) {
            Logger.getLogger(MeterReadingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void addMeterNo(int id) throws IOException, SQLException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/consumer/addMeterNo.fxml"));
        VBox modal = loader.load();
        AddMeterNoController AddMeterNoController = loader.getController();
        AddMeterNoController.setController(this);
        AddMeterNoController.setConsumer(id);

        Stage internalFrame = new Stage();
        internalFrame.initStyle(StageStyle.UTILITY); // Customize the stage style
        internalFrame.initModality(Modality.WINDOW_MODAL);
        internalFrame.initOwner(mainPanel.getScene().getWindow()); // Set the owner stage (mainPanel's stage)
        internalFrame.getIcons().add(getIcon());
        
        Scene scene = new Scene(modal);
        internalFrame.setScene(scene);

        internalFrame.setTitle("Add Meter No");
        internalFrame.setResizable(false);

        internalFrame.show();
    }
    private void editConsumer(int id) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/consumer/editConsumer.fxml"));
        VBox modal = loader.load();
        EditConsumerController editConsumerController = loader.getController();
        editConsumerController.setController(this);
        editConsumerController.setConsumer(id);

        Stage internalFrame = new Stage();
        internalFrame.initStyle(StageStyle.UTILITY); // Customize the stage style
        internalFrame.initModality(Modality.WINDOW_MODAL);
        internalFrame.initOwner(mainPanel.getScene().getWindow()); // Set the owner stage (mainPanel's stage)
        internalFrame.getIcons().add(getIcon());
        
        Scene scene = new Scene(modal);
        internalFrame.setScene(scene);

        internalFrame.setTitle("Edit Consumer");
        internalFrame.setResizable(false);

        internalFrame.show();

    }
    private void archiveConsumer(int id) throws SQLException {
        ConsumerModel.archiveConsumer(id);
        showConsumerTable();
    } 
    private void viewConsumerProfile(int id) throws IOException, SQLException{
        pageSetter.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/consumer/consumerProfile/consumerProfile.fxml"));
        Pane root = loader.load();
        ConsumerProfileController conPro = loader.getController();
        conPro.mainPanel(mainPanel);
        conPro.pageSetter(pageSetter);
        conPro.setConsumer(id);
        conPro.initialize(null, null);
        pageSetter.getChildren().setAll(root);
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
        internalFrame.getIcons().add(getIcon());
        Scene scene = new Scene(modal);
        internalFrame.setScene(scene);

        internalFrame.setTitle("");
        internalFrame.setResizable(false);

        internalFrame.show();
    }
    public Image getIcon(){
        Image icon = new Image(getClass().getResourceAsStream("../assets/logo.png"));
        return icon;
    }
    @FXML
    private void addConsumer(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/consumer/addConsumer.fxml"));
        VBox modal = loader.load();
        AddConsumerController addConsumerController = loader.getController();
        addConsumerController.setController(this);
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
    @FXML
    private void searchConsumer(KeyEvent event) throws SQLException {
        consumerTable.getItems().clear();
        showConsumerTable();
    }
    
}
