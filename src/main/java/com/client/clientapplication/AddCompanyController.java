package com.client.clientapplication;

import com.quickrest.entities.BranchDetails;
import com.quickrest.entities.CompanyDetails;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddCompanyController implements Initializable {

    @FXML
    private TextField companyAltEmailText;

    @FXML
    private TextField companyAltNumberText;

    @FXML
    private AnchorPane companyAnchor;

    @FXML
    private TextField companyBussinessTextField;

    @FXML
    private Button companyCreateButton;

    @FXML
    private DatePicker companyDateCreated;

    @FXML
    private DatePicker companyDateOpened;

    @FXML
    private AnchorPane companyDetailsAnchor;

    @FXML
    private Label companyDetailsTitleLabel;

    @FXML
    private TextField companyEmailText;

    @FXML
    private TextField companyAddressTextField;

    @FXML
    private TextField companyNameText;

    @FXML
    private TextField companyPhoneText;

    @FXML
    private TextField companyServerTextField;

    @FXML
    private TextField companyStreetText;

    @FXML
    private AnchorPane companyTitleAnchor;

    @FXML
    private TextField companyWebsiteTextField;



    private static final String  HOST = ApplicationPath.getHqPath();
    String userName;
    ObservableList<BranchDetails> list;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    @FXML
    public void createACompany(){
        String emailRegex = "^[\\p{L}0-9!#$%&'*+/=?^_`{|}~-][\\p{L}0-9.!#$%&'*+/=?^_`{|}~-]{0,63}@[\\p{L}0-9-]+(?:\\.[\\p{L}0-9-]{2,7})*$";
        String phoneRegex = "^(0{1}[1|7][0-9]{8})$";

         String name = companyNameText.getText();
         String location = companyAddressTextField.getText();
         String street = companyStreetText.getText();
         LocalDate dateStarted = companyDateCreated.getValue();
        LocalDate dateOpened = companyDateOpened.getValue();
        String email = companyEmailText.getText();
         String altEmail = companyAltEmailText.getText();
         String contact = companyPhoneText.getText();
         String altContact = companyAltNumberText.getText();
         String typeOfBusiness = companyBussinessTextField.getText();
         String website = companyWebsiteTextField.getText();

         if(name.matches(".*\\p{Upper}.*") || location.matches(".*\\p{Upper}.*") || name.matches("") ||
         location.matches("") || street.matches(".*\\p{Upper}.*") || street.matches("") ||
         email.matches(emailRegex) || email.matches(".*\\p{Upper}.*") || altEmail.matches(emailRegex) ||
         altEmail.matches(".*\\p{Upper}.*") || contact.matches(phoneRegex) || altContact.matches(phoneRegex) ||
         typeOfBusiness.matches(".*\\p{Upper}.*") || typeOfBusiness.length() >= 4 || website.matches(".*\\p{Upper}.*")){

             CompanyDetails companyDetails = new CompanyDetails();
             companyDetails.setAddress(location);
             companyDetails.setName(name);
             companyDetails.setAltContact(altContact);
             companyDetails.setContact(contact);
             companyDetails.setEmail(email);
             companyDetails.setAltEmail(altEmail);
             companyDetails.setWebsite(website);
             companyDetails.setDateStarted(dateStarted);
             companyDetails.setDateOpened(dateOpened);
             companyDetails.setCreatedBy(getUserName());
             companyDetails.setTypeOfBusiness(typeOfBusiness);

             WebTarget target = ClientBuilder.newClient().target(HOST+"/company/create");
             Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(companyDetails,
                     MediaType.APPLICATION_JSON));

             if(response.getStatus() == 201){
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                 alert.setContentText("A new company is successfully created:\nName = "+name+"\n" +
                         "business = "+typeOfBusiness);
                 Optional<ButtonType> optional1 = alert.showAndWait();
                 if(optional1.isPresent() && optional1.get() == ButtonType.OK)
                     alert.close();
                 Stage stage = (Stage) companyCreateButton.getScene().getWindow();
                 stage.close();

             }else {
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                 alert.setHeaderText("Failed to add a company:");
                 alert.setContentText("Message:"+response.readEntity(String.class)+"\n\n" +
                         "Kindly input the correct details to proceed");
                 Optional<ButtonType> optional1 = alert.showAndWait();
                 if(optional1.isPresent() && optional1.get() == ButtonType.OK)
                     alert.close();
             }


         }else {
             Alert alert = new Alert(Alert.AlertType.WARNING);
             alert.setTitle("Error in one ore more inputs");
             alert.setHeaderText("Data entry error");
             alert.setContentText("Ensure that all field are in upper case and non is empty");
             Optional<ButtonType> optional = alert.showAndWait();
             if (optional.isPresent() && optional.get() == ButtonType.OK){
                 alert.close();
             }
         }
    }

    public void setUser(String user){
        userName = user;
    }

    public String getUserName() {
        return userName;
    }
}
