package com.client.clientapplication;

import com.quickrest.entities.Transporter;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddTransporterController implements Initializable {
    @FXML
    private AnchorPane addSupplierDetailsAnchor;
    @FXML
    private TextArea addTransporterAddress;
    @FXML
    private AnchorPane addTransporterAnchor;
    @FXML
    private TextField addTransporterEmail;
    @FXML
    private TextField addTransporterIdentity;
    @FXML
    private TextField addTransporterMobile;
    @FXML
    private TextField addTransporterMobile1;
    @FXML
    private Button addTransporterSave;
    @FXML
    private TextArea addTransporterService;
    @FXML
    private TextField addTransporterVehicleNumber;
    @FXML
    private Label addTransporterDate;

    private TableView<Transporter> tableView;
    private String user;
    private static final String  HOST = ApplicationPath.getHqPath();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Disabling save
        addTransporterSave.setDisable(true);

        //Enabling save
        addTransporterEmail.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.equals("")){
                    addTransporterSave.setDisable(false);
                }
            }
        });


        //Setting date
        addTransporterDate.setText(new Date().toString());

        //Accepting digits for the first phone number
        addTransporterMobile.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("\\d*")){
                    addTransporterMobile.setText(t1.replaceAll("[^\\d]",""));
                }
            }
        });
        //Accepting digits for the second phone number
        addTransporterMobile1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("\\d*")){
                    addTransporterMobile1.setText(t1.replaceAll("[^\\d]",""));
                }
            }
        });

        //Enabling save button
        addTransporterEmail.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.isEmpty() || !t1.equals("")){
                    addTransporterSave.setDisable(false);
                }
            }
        });
    }

    @FXML
    public void addTransporter(){
        String emailRegex = "^[\\p{L}0-9!#$%&'*+/=?^_`{|}~-][\\p{L}0-9.!#$%&'*+/=?^_`{|}~-]{0,63}@[\\p{L}0-9-]+(?:\\.[\\p{L}0-9-]{2,7})*$";
        String phoneRegex = "^(0{1}[1|7][0-9]{8})$";
        String identity = addTransporterIdentity.getText();
        String service = addTransporterService.getText();
        String address = addTransporterAddress.getText();
        String mobile = addTransporterMobile.getText();
        String mobile1 = addTransporterMobile1.getText();
        String email = addTransporterEmail.getText();
        String plateNumber = addTransporterVehicleNumber.getText();

        if (!email.matches(emailRegex)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Error in email address");
            alert.setContentText("Kindly input the correct email address");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get() == ButtonType.OK){
                alert.close();
            }
        }else if (!mobile.matches(phoneRegex) || !mobile1.matches(phoneRegex)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Error in mobile number");
            alert.setContentText("Kindly input the correct phone number");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get() == ButtonType.OK){
                alert.close();
            }

        }else {
            if(mobile1.isEmpty() || mobile.isEmpty() || email.isEmpty() || address.equals("") || service.equals("")
                    || identity.equals("")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Empty Fileds");
                alert.setContentText("All fields are needed");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK){
                    alert.close();
                }
            }else {
                Transporter transporter = new Transporter();
                transporter.setIdentity(identity);
                transporter.setService(service);
                transporter.setAddress(address);
                transporter.setPhone(mobile);
                transporter.setPhone1(mobile1);
                transporter.setEmail(email);
                transporter.setVehicleNumber(plateNumber);
                transporter.setDateRegistered(new Date());
                transporter.setUserName(getUser());

                WebTarget target = ClientBuilder.newClient().target(HOST+"/transporter/create");
                Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(transporter,MediaType.APPLICATION_JSON));

                if(response.getStatus() == 204){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Transporter Added");
                    alert.setContentText("A new transporter successfully added");
                    Optional<ButtonType> optional = alert.showAndWait();
                    if (optional.isPresent() && optional.get() == ButtonType.OK){
                        updateTransporterTable();
                        alert.close();
                    }
                    Stage stage = (Stage) addTransporterSave.getScene().getWindow();
                    stage.close();
                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Failed!");
                    alert.setContentText("Failed to add new transporter");
                    Optional<ButtonType> optional = alert.showAndWait();
                    if (optional.isPresent() && optional.get() == ButtonType.OK){
                        alert.close();
                    }
                }
            }

        }
    }

    private void updateTransporterTable(){
        WebTarget webTarget = ClientBuilder.newClient().target(HOST+"/transporter/findAll");
        GenericType<List<Transporter>> genericType = new GenericType<>(){};
        List<Transporter> transporterList= webTarget.request(MediaType.APPLICATION_JSON).get(genericType);

        ObservableList<Transporter> transporterObservableList = FXCollections.observableArrayList(transporterList);
        tableView.setItems(transporterObservableList);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public TableView<Transporter> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<Transporter> tableView) {
        this.tableView = tableView;
    }
}
