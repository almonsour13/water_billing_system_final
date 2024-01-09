package view.settings.settingPages;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import view.settings.Settings;
import view.settings.SettingsModel;

public class CompanyInformationController implements Initializable {

    @FXML
    private TextField Province;
    @FXML
    private TextField municipality;
    @FXML
    private Button edit;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private TextField companyName;
    @FXML
    private TextField country;
    @FXML
    private TextField region;
    @FXML
    private TextField baranggay;
    @FXML
    private TextField purok;
    private SettingsModel settingsModel;
    private Settings settings;
    
    public CompanyInformationController(){
        settingsModel = new SettingsModel();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            setValue();
        } catch (SQLException ex) {
            Logger.getLogger(CompanyInformationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setValue() throws SQLException{
        settings = settingsModel.getComInfo();
        if(settings != null){
            companyName.setText(settings.getCompanyName());
            country.setText(settings.getCompanyCountry());
            region.setText(settings.getCompanyRegion());
            Province.setText(settings.getCompanyProvince());
            municipality.setText(settings.getCompanyMunicipality());
            baranggay.setText(settings.getCompanyBaranggay());
            purok.setText(settings.getCompanyPurok());
                
        }
    }
    public void disable(boolean con){
        companyName.setDisable(con);
        country.setDisable(con);
        region.setDisable(con);
        Province.setDisable(con);
        municipality.setDisable(con);
        baranggay.setDisable(con);
        purok.setDisable(con);
        edit.setDisable(!con);
        cancelBtn.setDisable(con);
        saveBtn.setDisable(con);
    }
    private boolean areAddressFieldsNotEmpty() {
        return !companyName.getText().isEmpty() &&
               !country.getText().isEmpty() &&
               !region.getText().isEmpty() &&
               !Province.getText().isEmpty() &&
               !municipality.getText().isEmpty() &&
               !baranggay.getText().isEmpty() &&
               !purok.getText().isEmpty();
    }
    @FXML
    private void edit(ActionEvent event) {
        disable(false);
    }

    @FXML
    private void cancel(ActionEvent event) throws SQLException {
        setValue();
        disable(true);
    }

    @FXML
    private void save(ActionEvent event) throws SQLException {
        if (areAddressFieldsNotEmpty()) {
            Settings newConInfo = new Settings(
                    settings.getCompanyID(),
                    companyName.getText(),
                    country.getText(),
                    region.getText(),
                    Province.getText(),
                    municipality.getText(),
                    baranggay.getText(),
                    purok.getText()
                     );
            if(settings != null){
                settingsModel.updateConInfo(newConInfo);
            }else{
                settingsModel.insertConInfo(newConInfo);
            }
            setValue(); // Refresh the values after saving
            disable(true);
        } else {
            System.out.println("Please fill in all address fields.");
        }
    }

    @FXML
    private void typeEvent(KeyEvent event) {
    }
}
