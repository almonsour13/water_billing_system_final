/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import model.account.Account;
import model.account.AccountModel;
import model.meterNumber.MeterNumber;
import view.consumer.ModalController;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class AccountsController implements Initializable {
    
    ObservableList<String> months = 
                FXCollections.observableArrayList(
                        "Jan","Feb","Mar","Apr","May","Jun",
                        "Jul","Aug","Sep","Oct","Nov","Dec");
    private AccountModel accountModel;
    @FXML
    private TableView<Account> accountTable;
    @FXML
    private TableColumn<Account, Integer> noCol;
    @FXML
    private TableColumn<Account, String> fNameCol;
    @FXML
    private TableColumn<Account, String> mNameCol;
    @FXML
    private TableColumn<Account, String> lNameCol;
    @FXML
    private TableColumn<Account, String> uNameCol;
    @FXML
    private TableColumn<Account, String> pWordCol;
    @FXML
    private TableColumn<Account, String> roleCol;
    @FXML
    private TableColumn<String, String> statusCol;
    ObservableList<Account> data;
    @FXML
    private TextField fNameField;
    @FXML
    private TextField mNameField;
    @FXML
    private TextField lNameField;
    @FXML
    private ChoiceBox<String> roleChoices;
    @FXML
    private TextField uNameField;
    @FXML
    private TextField pWordField;
    @FXML
    private ChoiceBox<String> statusChoices;
    @FXML
    private Button addSaveBtn;
    private Button editBtn;
    private Button archiveBtn;
    @FXML
    private Button cancelBtn;
    private int id;
    private ChoiceBox<String> filterStatus;
    private ChoiceBox<String> filterRole;
    @FXML
    private TableColumn<Account, Integer> actionCol;
    @FXML
    private Label headerLabel;
    @FXML
    private ChoiceBox<String> monthChoiceBox;
    @FXML
    private ChoiceBox<String> yearChoiceBox;
    @FXML
    private ChoiceBox<String> statusChoiceBox;
    @FXML
    private ChoiceBox<String> roleChoiceBox;
    @FXML
    private TextField searchVal;
    @FXML
    private Button addAccountBtn;
    private static BorderPane mainPanel = new BorderPane();
    private static Pane pageSetter = new Pane();
    /**
     * Initializes the controller class.
     */
    public AccountsController() throws SQLException{
       this.accountModel = new AccountModel();
       this.data = accountModel.getAccounts();
        
    }
    public void mainPanel(BorderPane mainPanel){
        this.mainPanel = mainPanel;
    }
    public void pageSetter(Pane pageSetter){
        this.pageSetter = pageSetter;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       this.accountModel = new AccountModel();
       noCol.setCellValueFactory(new PropertyValueFactory<>("No"));
       fNameCol.setCellValueFactory(new PropertyValueFactory<>("FName"));
       mNameCol.setCellValueFactory(new PropertyValueFactory<>("MName"));
       lNameCol.setCellValueFactory(new PropertyValueFactory<>("LName"));
       uNameCol.setCellValueFactory(new PropertyValueFactory<>("UName"));
       pWordCol.setCellValueFactory(new PropertyValueFactory<>("PWord"));
       roleCol.setCellValueFactory(new PropertyValueFactory<>("Role"));
       statusCol.setCellValueFactory(new PropertyValueFactory<>("Status"));
       actionCol.setCellFactory(new Callback<TableColumn<Account, Integer>, TableCell<Account, Integer>>() {
            @Override
            public TableCell<Account, Integer> call(TableColumn<Account, Integer> param) {
                return new TableCell<Account, Integer>() {
                    final Button archiveBtn = new Button();
                    final Button editBtn = new Button();
                    final Button viewLogsBtn = new Button();

                    {
                        archiveBtn.getStyleClass().add("actionBtn");
                        archiveBtn.setGraphic(loadSvgIcon("/assets/icons/archive.png"));
                        editBtn.getStyleClass().add("actionBtn");
                        editBtn.setGraphic(loadSvgIcon("/assets/icons/edit.png"));
                        viewLogsBtn.getStyleClass().add("actionBtn");
                        viewLogsBtn.setGraphic(loadSvgIcon("/assets/icons/bar_1.png"));
                        
                        archiveBtn.setOnAction(event -> {
                            Account account = getTableView().getItems().get(getIndex());
                            try {
                                archive(account);
                            } catch (SQLException ex) {
                                Logger.getLogger(AccountsController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        editBtn.setOnAction(event -> {
                            Account account = getTableView().getItems().get(getIndex());
                            selectAccount(account);
                        });
                        viewLogsBtn.setOnAction(event -> {
                            Account account = getTableView().getItems().get(getIndex());
                            
                        });
                    }

                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Account account = getTableView().getItems().get(getIndex());
                            
                            HBox buttonsBox = new HBox(new HBox(3,viewLogsBtn,editBtn,archiveBtn));
                            buttonsBox.setAlignment(Pos.CENTER);
                       
                            setGraphic(buttonsBox);
                            setText(null);
                        }
                    }
                };
            }
        });
        
       accountTable.getItems().addAll(data);
        monthChoiceBox.setOnAction(this::monthChoice);
        yearChoiceBox.setOnAction(this::yearChoice);
        statusChoices.setOnAction(this::choiceBox);
        statusChoiceBox.setOnAction(this::choiceBox); 
        roleChoiceBox.setOnAction(this::choiceBox);
        try {
            setChoices();
            showAccountTable();
        } catch (SQLException ex) {
            Logger.getLogger(AccountsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        addAccountBtn.setGraphic(loadSvgIcon("/assets/icons/addPerson.png"));
    }   
    public void setChoices() throws SQLException {      
        yearChoiceBox.getItems().clear();
        yearChoiceBox.getItems().add("All");
        for(Account account:accountModel.getAccounts()){
            String splitDate[] = String.valueOf(account.getDateAdded()).split("-");
            if(!yearChoiceBox.getItems().contains(splitDate[0])){
                yearChoiceBox.getItems().add(splitDate[0]);
            }
        }
        yearChoiceBox.setValue("All");
     
        monthChoiceBox.getItems().clear();
        monthChoiceBox.setValue("All");
        monthChoiceBox.getItems().add("All");
        for (Account account:accountModel.getAccounts()) {
            String splitDate[] = String.valueOf(account.getDateAdded()).split("-");
            if (!monthChoiceBox.getItems().contains(months.get(Integer.parseInt(splitDate[1]) - 1))
              && yearChoiceBox.getValue().equals(splitDate[0])) {
                monthChoiceBox.getItems().add(months.get(Integer.parseInt(splitDate[1]) - 1));
            }
        }
        statusChoiceBox.getItems().clear();
        statusChoiceBox.setValue("All");
        statusChoiceBox.getItems().addAll("All","Active","Inactive");
        roleChoiceBox.getItems().clear();
        roleChoiceBox.setValue("All");
        roleChoiceBox.getItems().addAll("All","Admin","Collector");
        
        
    }
    public void yearChoice(ActionEvent event){
        monthChoiceBox.getItems().clear();
        monthChoiceBox.setValue("All");
        monthChoiceBox.getItems().add("All");
        try {
            for (Account account:accountModel.getAccounts()) {
                String splitDate[] = String.valueOf(account.getDateAdded()).split("-");
                if (!monthChoiceBox.getItems().contains(months.get(Integer.parseInt(splitDate[1]) - 1))
                && yearChoiceBox.getValue().equals(splitDate[0])) {
                    monthChoiceBox.getItems().add(months.get(Integer.parseInt(splitDate[1]) - 1));
                }
            }
            showAccountTable();
        } catch (SQLException ex) {
          
        }
    }
    public void choiceBox(ActionEvent event){
        try {
            showAccountTable();
        } catch (SQLException ex) {
            
        }
    }
    public void monthChoice(ActionEvent event){
        try {
            showAccountTable();
        } catch (SQLException ex) {
           
        }
    }
    public void showAccountTable() throws SQLException{
         accountTable.getItems().clear();
        int month = monthChoiceBox.getValue()== "All"?0: months.indexOf(monthChoiceBox.getValue())+1;
        int year = yearChoiceBox.getValue()== "All"?0: Integer.parseInt(yearChoiceBox.getValue());
        String roleChoice = roleChoiceBox.getValue();
        int role = 0;
        if(roleChoice != null){
            switch(roleChoice){
                case "All":
                    role = 0;
                    break;
                case "Admin":
                    role = 1;
                    break;
                case "Collector":
                    role = 2;
                    break;
                default:
                    role = 0;
            }
        }else{
            role = 0;
        }
        String statusChoice = statusChoiceBox.getValue();
        int status = 0;
        if(statusChoice != null){
            switch(statusChoice){
                case "All":
                    status = 0;
                    break;
                case "Active":
                    status = 1;
                    break;
                case "Inactive":
                    status = 2;
                    break;
                default:
                    status = 0;
            }
        }else{
            status = 0;
        }
        
       // accountTable.getItems().addAll(accountModel.getAccounts());
        accountTable.getItems().addAll(accountModel.filterAccounts(searchVal.getText(), role, status, month, year));
    }
    private ImageView loadSvgIcon(String path) {
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(18); // Set the width as needed
        imageView.setFitHeight(18); // Set the height as needed
        return imageView;
    }
    private void selectAccount(Account account) {
       fNameField.setText(account.getFName());
       mNameField.setText(account.getMName());
       lNameField.setText(account.getLName());
       uNameField.setText(account.getUName());
       pWordField.setText(account.getPWord());
       roleChoices.setValue(account.getRole());
       statusChoices.setValue(account.getStatus());
       addSaveBtn.setText("Save");
       this.id = account.getId();
       Enabled();
    }
    public void Enabled(){
       fNameField.setDisable(false);
       mNameField.setDisable(false);
       lNameField.setDisable(false);
       uNameField.setDisable(false);
       pWordField.setDisable(false);
       roleChoices.setDisable(false);
       statusChoices.setDisable(false);
       cancelBtn.setDisable(false);
       addSaveBtn.setDisable(false);
    }
    public void Disabled(){
       fNameField.setDisable(true);
       mNameField.setDisable(true);
       lNameField.setDisable(true);
       uNameField.setDisable(true);
       pWordField.setDisable(true);
       roleChoices.setDisable(true);
       statusChoices.setDisable(true);
       cancelBtn.setDisable(true);
       addSaveBtn.setDisable(true);
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
    private void addAccount(ActionEvent event) {
        Enabled();
        addSaveBtn.setText("Add");
        cancelBtn.setDisable(false);
        addSaveBtn.setDisable(false);
        resetFields();
    }

    @FXML
    private void addSave(ActionEvent event) throws SQLException, IOException {
        if(!fNameField.getText().isEmpty() && !lNameField.getText().isEmpty() &&
           !uNameField.getText().isEmpty() && !pWordField.getText().isEmpty() && !statusChoices.getValue().isEmpty()&&
           !roleChoices.getValue().isEmpty()   
          ){
            if(addSaveBtn.getText().equals("Add")){
                int role = 0;
                if(roleChoices.getValue().equals("Admin")){
                    role = 1;
                }else if(roleChoices.getValue().equals("Collector")){
                    role = 2;
                }
                int status = 0;
                if (statusChoices.getValue().equals("Active")){
                    status = 1;
                }else{
                    status = 2;
                }

                accountModel.addAccount( fNameField.getText(), mNameField.getText().isEmpty()? "":mNameField.getText(),
                                        lNameField.getText(), uNameField.getText(), pWordField.getText(),
                                        role, status);
                modal("Account Added Successfully",1);

            }else{
                int role = 0;
                if(roleChoices.getValue().equals("Admin")){
                    role = 1;
                }else if(roleChoices.getValue().equals("Collector")){
                    role = 2;
                }
                int status = 0;
                if (statusChoices.getValue().equals("Active")){
                    status = 1;
                }else{
                    status = 2;
                }
                
                accountModel.updateAccount(this.id, fNameField.getText(), mNameField.getText().isEmpty()? "":mNameField.getText(),
                                        lNameField.getText(), uNameField.getText(), pWordField.getText(),
                                        role, status);
                modal("Account Updated Successfully",1);
            }
            showAccountTable();
            Disabled();
            resetFields();
        }else{
            JOptionPane.showMessageDialog(null, "Fill All Required Fields");
        }
    }

    private void archive(Account account) throws SQLException {
       int id = account.getId();  
       accountModel.archiveAccount(id);
       showAccountTable();
    }
    @FXML
    private void cancel(ActionEvent event) {
       addSaveBtn.setText("Add");
       cancelBtn.setDisable(true);
       addSaveBtn.setDisable(true);
       Disabled();
       resetFields();
    }
    public void resetFields(){
       fNameField.setText("");
       mNameField.setText("");
       lNameField.setText("");
       uNameField.setText("");
       pWordField.setText("");
       roleChoices.setValue("Admin");
       statusChoices.setValue("Active");    
    }
}
