/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;
import model.account.Account;
import model.account.AccountModel;

/**
 * FXML Controller class
 *
 * @author Merry Ann
 */
public class AccountsController implements Initializable {
    
    private AccountModel accountModel;
    @FXML
    private TableView<Account> accountTable;
    @FXML
    private TableColumn<Account, Integer> noCol;
    @FXML
    private TableColumn<Account, Integer> idCol;
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
    @FXML
    private Button editBtn;
    @FXML
    private Button archiveBtn;
    @FXML
    private Button cancelBtn;
    private int id;
    @FXML
    private ChoiceBox<String> filterStatus;
    @FXML
    private ChoiceBox<String> filterRole;
    /**
     * Initializes the controller class.
     */
    public AccountsController() throws SQLException{
       this.accountModel = new AccountModel();
       this.data = accountModel.getAccounts();
        
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       this.accountModel = new AccountModel();
       noCol.setCellValueFactory(new PropertyValueFactory<>("No"));
       idCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
       fNameCol.setCellValueFactory(new PropertyValueFactory<>("FName"));
       mNameCol.setCellValueFactory(new PropertyValueFactory<>("MName"));
       lNameCol.setCellValueFactory(new PropertyValueFactory<>("LName"));
       uNameCol.setCellValueFactory(new PropertyValueFactory<>("UName"));
       pWordCol.setCellValueFactory(new PropertyValueFactory<>("PWord"));
       roleCol.setCellValueFactory(new PropertyValueFactory<>("Role"));
       statusCol.setCellValueFactory(new PropertyValueFactory<>("Status"));
       accountTable.getItems().addAll(data);
       setChoices();
       filterRole.setOnAction(this::filterDisplay);
       filterStatus.setOnAction(this::filterDisplay);
        
    }   
    public void setChoices(){
        ObservableList<String> roles = FXCollections.observableArrayList("Admin", "Collector");
        roleChoices.setItems(roles);
        roles.add(0,"All");
        filterRole.setItems(roles);
        filterRole.setValue("All");
        ObservableList<String> statuss = FXCollections.observableArrayList("Active", "Inactive");
        statusChoices.setItems(statuss);
        statuss.add(0, "All");
        filterStatus.setItems(statuss);
        filterStatus.setValue("All");
    }
    public void showAccountTable() throws SQLException{
       this.data = accountModel.getAccounts();
       accountTable.getItems().clear();
       for (Account account : data) {
             if(!filterStatus.getValue().equals("All") && !filterRole.getValue().equals("All")){
                if(filterStatus.getValue().equals(account.getStatus()) && filterRole.getValue().equals(account.getRole())){
                    accountTable.getItems().add(account);
                }
            }else if(!filterStatus.getValue().equals("All") && filterRole.getValue().equals("All")){
                if(filterStatus.getValue().equals(account.getStatus())){
                    accountTable.getItems().add(account);
                }
            }else if(filterStatus.getValue().equals("All") && !filterRole.getValue().equals("All")){
                if(filterRole.getValue().equals(account.getRole())){
                    accountTable.getItems().add(account);
                }
            }else{
                accountTable.getItems().addAll(account);
            }
        }
    }
    
    private void filterDisplay(ActionEvent event){
       accountTable.getItems().clear();
       for (Account account : data) {
             if(!filterStatus.getValue().equals("All") && !filterRole.getValue().equals("All")){
                if(filterStatus.getValue().equals(account.getStatus()) && filterRole.getValue().equals(account.getRole())){
                    accountTable.getItems().add(account);
                }
            }else if(!filterStatus.getValue().equals("All") && filterRole.getValue().equals("All")){
                if(filterStatus.getValue().equals(account.getStatus())){
                    accountTable.getItems().add(account);
                }
            }else if(filterStatus.getValue().equals("All") && !filterRole.getValue().equals("All")){
                if(filterRole.getValue().equals(account.getRole())){
                    accountTable.getItems().add(account);
                }
            }else{
                accountTable.getItems().addAll(account);
            }
        }
    }
    @FXML
    private void selectAccount(MouseEvent event) {
       Account selectedAccount = accountTable.getSelectionModel().getSelectedItem();
       fNameField.setText(selectedAccount.getFName());
       mNameField.setText(selectedAccount.getMName());
       lNameField.setText(selectedAccount.getLName());
       uNameField.setText(selectedAccount.getUName());
       pWordField.setText(selectedAccount.getPWord());
       roleChoices.setValue(selectedAccount.getRole());
       statusChoices.setValue(selectedAccount.getStatus());
       editBtn.setDisable(false);
       archiveBtn.setDisable(false);
       addSaveBtn.setText("Save");
       this.id = selectedAccount.getId();
    }
    public void Enabled(){
       fNameField.setDisable(false);
       mNameField.setDisable(false);
       lNameField.setDisable(false);
       uNameField.setDisable(false);
       pWordField.setDisable(false);
       roleChoices.setDisable(false);
       statusChoices.setDisable(false);
    }
    public void Disabled(){
       fNameField.setDisable(true);
       mNameField.setDisable(true);
       lNameField.setDisable(true);
       uNameField.setDisable(true);
       pWordField.setDisable(true);
       roleChoices.setDisable(true);
       statusChoices.setDisable(true);
    }

    @FXML
    private void addAccount(ActionEvent event) {
        Enabled();
        addSaveBtn.setText("Add");
        editBtn.setDisable(true);
        archiveBtn.setDisable(true);
        cancelBtn.setDisable(false);
        addSaveBtn.setDisable(false);
        resetFields();
    }

    @FXML
    private void addSave(ActionEvent event) throws SQLException {
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
            }
            showAccountTable();
        }else{
            JOptionPane.showMessageDialog(null, "Fill All Required Fields");
        }
    }
    @FXML
    private void editInfo(ActionEvent event) {
        Enabled();
        addSaveBtn.setText("Save");
        cancelBtn.setDisable(false);
        addSaveBtn.setDisable(false);
    }

    @FXML
    private void archive(ActionEvent event) throws SQLException {
       Account selectedAccount = accountTable.getSelectionModel().getSelectedItem();
       int id = selectedAccount.getId();  
       accountModel.archiveAccount(id);
       showAccountTable();
    }
    @FXML
    private void cancel(ActionEvent event) {
       addSaveBtn.setText("Add");
       cancelBtn.setDisable(true);
       addSaveBtn.setDisable(true);
       editBtn.setDisable(true);
       archiveBtn.setDisable(true);
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
