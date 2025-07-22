package com.client.clientapplication;

import com.quickrest.entities.BranchDetails;
import com.quickrest.entities.CompanyDetails;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class AddBranchController implements Initializable {
    @FXML
    private TextField branchDetailContact;

    @FXML
    private TextField branchDetailsAltEmail;

    @FXML
    private AnchorPane branchDetailsAnchor;

    @FXML
    private ComboBox<String> branchDetailsComboBox;

    @FXML
    private Button branchDetailsCreateButton;

    @FXML
    private TextField branchDetailsEmail;

    @FXML
    private TextField branchDetailsLocationTextField;

    @FXML
    private AnchorPane branchDetailsMainAnchor;

    @FXML
    private TextField branchDetailsNumberTextField;

    @FXML
    private TextField branchDetailsServerTextField;

    @FXML
    private TextField branchDetailsStreetTextField;

    @FXML
    private AnchorPane branhDetailsTitleAnchor;

    @FXML
    private TextField branchDetailsNameTextField;

    @FXML
    private DatePicker branchDetailsDateOpened;

    @FXML
    private DatePicker branchDetailsDateCreated;

    private String user;
    private static final String  HOST = ApplicationPath.getHqPath();
    private WebTarget target;
    private CompanyDetails companyDetails;
    private Button button;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //Fetching companies
        target = ClientBuilder.newClient().target(HOST+"/company/findAll");
        GenericType<List<CompanyDetails>> genericType = new GenericType<>(){};
        final List<CompanyDetails> companyDetailsList = target.request(MediaType.APPLICATION_JSON).get(genericType);
        ObservableList<String> companyNames = FXCollections.observableArrayList();
        for(CompanyDetails c:companyDetailsList){
            companyNames.add(c.getName());
        }
        branchDetailsComboBox.setItems(companyNames);


        //Accepting digits for brach number
        branchDetailsNumberTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("\\d*")){
                    branchDetailsNumberTextField.setText(t1.replaceAll("[^\\d]",""));
                }
            }
        });

    }

    @FXML
    public void addBranch(){
        String srverRegex = "";
        String phoneRegex = "^(0{1}[1|7][0-9]{8})$";

        int branchNumber = Integer.parseInt(branchDetailsNumberTextField.getText());
        String branch = branchNumber+"-"+ branchDetailsNameTextField.getText();
        String location = branchDetailsLocationTextField.getText();
        String ip = branchDetailsServerTextField.getText();
        String street = branchDetailsStreetTextField.getText();
        String email = branchDetailsEmail.getText();
        String altEmail = branchDetailsAltEmail.getText();
        String contact = branchDetailContact.getText();

        ZoneId id = ZoneId.systemDefault();
        LocalDate created = branchDetailsDateCreated.getValue();
        Date createdDate = Date.from(created.atStartOfDay(id).toInstant());

        LocalDate opened = branchDetailsDateOpened.getValue();
        Date openedDate = Date.from(opened.atStartOfDay(id).toInstant());

        if(branchNumber < 1 || location.equals("") || street.equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Some data is not correct.Make sure the contact,emails a");
            Optional<ButtonType> optional1 = alert.showAndWait();
            if(optional1.isPresent() && optional1.get() == ButtonType.OK)
                alert.close();
        }else {
            BranchDetails branchDetails = new BranchDetails();
            branchDetails.setBranchName(branch);
            branchDetails.setBranchNumber(branchNumber);
            branchDetails.setCompanyName(branchDetailsComboBox.getValue());
            branchDetails.setBranchLocation(location);
            branchDetails.setStreet(street);
            branchDetails.setServerIp(ip);
            branchDetails.setUserName(getUser());
            branchDetails.setDateCreated(createdDate);
            branchDetails.setDateOpened(openedDate);
            branchDetails.setContact(contact);
            branchDetails.setEmail(email);
            branchDetails.setAltEmail(altEmail);


            boolean state = true;
            target = ClientBuilder.newClient().target(HOST+"/branch/findAll");
            GenericType<List<BranchDetails>> genericType = new GenericType<>(){};
            List<BranchDetails> list = target.request(MediaType.APPLICATION_JSON).get(genericType);
            if(!list.isEmpty()){
                for(BranchDetails b:list){
                    if(b.getBranchName().equals(branchDetails.getBranchName()) || b.getBranchNumber() ==
                            branchDetails.getBranchNumber() ||
                            b.getServerIp().equals(branchDetails.getServerIp())){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("A branch with the specified details already exist");
                        Optional<ButtonType> optional1 = alert.showAndWait();
                        if(optional1.isPresent() && optional1.get() == ButtonType.OK)
                            alert.close();

                        state = false;
                        break;
                    }
                }
            }

            if(state){
                WebTarget target = ClientBuilder.newClient().target(HOST+"/branch/create");
                Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(branchDetails,MediaType.APPLICATION_JSON));

                System.out.println("Creating");
                if(response.getStatus() == 201){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("A new branch successfully created:\nBranch name = "+branch+"\n" +
                            "Number = "+branchNumber);
                    Optional<ButtonType> optional1 = alert.showAndWait();
                    if(optional1.isPresent() && optional1.get() == ButtonType.OK)
                        alert.close();
                    branchDetailsNameTextField.setText("");
                    branchDetailsNumberTextField.setText("");
                    branchDetailsServerTextField.setText("");
                    branchDetailsStreetTextField.setText("");
                    branchDetailsLocationTextField.setText("");

                    Stage stage = (Stage) branchDetailsCreateButton.getScene().getWindow();
                    stage.close();

                }else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Failed to add a branch:");
                    alert.setContentText("Message:"+response.readEntity(String.class)+"\n\n" +
                            "Kindly input the correct details to proceed");
                    Optional<ButtonType> optional1 = alert.showAndWait();
                    if(optional1.isPresent() && optional1.get() == ButtonType.OK)
                        alert.close();
                }
            }

        }

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public CompanyDetails getCompanyDetails() {
        return companyDetails;
    }

    public void setCompanyDetails(CompanyDetails companyDetails) {
        this.companyDetails = companyDetails;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public ComboBox<String> getBranchDetailsComboBox() {
        return branchDetailsComboBox;
    }

    public void setBranchDetailsComboBox(ComboBox<String> branchDetailsComboBox) {
        this.branchDetailsComboBox = branchDetailsComboBox;
    }

}
