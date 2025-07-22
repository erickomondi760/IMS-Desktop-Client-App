package com.client.clientapplication;

import com.quickrest.entities.ClientTrader;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddClientController implements Initializable {
    @FXML
    private TextArea addClientAddress;
    @FXML
    private TextField addClentIdentity;
    @FXML
    private Label addClentinfoLabel;
    @FXML
    private TextField addClientDate;
    @FXML
    private TextField addClientEmail;
    @FXML
    private TextField addClientEmail1;
    @FXML
    private TextField addClientMobile;
    @FXML
    private TextField addClientMobile1;
    @FXML
    private Button addClientSaveButton;
    @FXML
    private RadioButton addLientBuyer;
    @FXML
    private RadioButton addLientIntermediary;
    @FXML
    private RadioButton addClientService;
    @FXML
    private TextArea addClientTrade;
    @FXML
    private Label addClientMobile1Star;
    @FXML
    private Label addClientMobileStar;
    @FXML
    private Label addClientEmail1Star;
    @FXML
    private Label addClientEmailStar;
    @FXML
    private Label addClientIdStar;
    @FXML
    private Label addClientAddressStar;

    String user;
    private TableView<ClientTrader> clientTable;
    private Label countLabel;
    private static final String  HOST = ApplicationPath.getHqPath();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Disabling save
        addClientSaveButton.setDisable(true);

        //Enabling save
        addClientEmail1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.equals("")){
                    addClientSaveButton.setDisable(false);
                }
            }
        });

        addClientTrade.setDisable(true);

        //Enabling trade field
        addClientService.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(addClientService.isSelected()){
                    addClientTrade.setDisable(false);
                }
            }
        });
        //Disabling trade field
        addLientBuyer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!addClientTrade.isDisabled()){
                    addClientTrade.setDisable(true);
                }
            }
        });
        //Disabling trade field
        addLientIntermediary.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!addClientTrade.isDisabled()){
                    addClientTrade.setDisable(true);
                }
            }
        });


        //Disabling all stars
        addClientIdStar.setVisible(false);
        addClientAddressStar.setVisible(false);
        addClientMobileStar.setVisible(false);
        addClientMobile1Star.setVisible(false);
        addClientEmail1Star.setVisible(false);
        addClientEmailStar.setVisible(false);

        //Disablind identity star
        addClentIdentity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.equals("")){
                    addClientIdStar.setVisible(false);
                }
            }
        });

        //Disablind address star
        addClientAddress.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.equals("")){
                    addClientAddressStar.setVisible(false);
                }
            }
        });

        //Disablind mobile star
        addClientMobile.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.equals("")){
                    addClientMobileStar.setVisible(false);
                }
            }
        });
        addClientMobile1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.equals("")){
                    addClientMobile1Star.setVisible(false);
                }
            }
        });

        //Disabling email star
        addClientEmail.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.equals("")){
                    addClientEmailStar.setVisible(false);
                }
            }
        });
        addClientEmail1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.equals("")){
                    addClientEmail1Star.setVisible(false);
                }
            }
        });

        //Setting date
        addClientDate.setText(new Date().toString());

    }

    @FXML
    public void addClient(){
        String emailRegex = "^[\\p{L}0-9!#$%&'*+/=?^_`{|}~-][\\p{L}0-9.!#$%&'*+/=?^_`{|}~-]{0,63}@[\\p{L}0-9-]+(?:\\.[\\p{L}0-9-]{2,7})*$";
        String phoneRegex = "^(0{1}[1|7][0-9]{8})$";

        String identity = addClentIdentity.getText();
        String contract = null;

        if(addLientBuyer.isSelected()){
            contract = addLientBuyer.getText();

        } else if (addLientIntermediary.isSelected()) {
            contract = addLientIntermediary.getText();

        }else {
            if(addClientService.isSelected())
                contract = addClientService.getText();
        }

        String address = addClientAddress.getText();
        String mobile = addClientMobile.getText();
        String mobile1 = addClientMobile1.getText();
        String email = addClientEmail.getText();
        String email1 = addClientEmail1.getText();
        String trade = addClientTrade.getText();

        ClientTrader client = new ClientTrader();
        client.setIdentity(identity);
        client.setAddress(address);
        client.setTerms(contract);
        client.setPhoneNumber1(mobile);
        client.setPhoneNumber2(mobile1);
        client.setEmail1(email);
        client.setEmail2(email1);
        client.setDateRegistered(new Date());
        client.setUserName(getUser());
        client.setService(trade);

        if(identity.equals("") || address.equals("") || mobile.isEmpty() || mobile1.isEmpty()
                || email.isEmpty() || email1.isEmpty() || !mobile.matches(phoneRegex) || !mobile1.matches(phoneRegex) ||
                !email.matches(emailRegex) || !email1.matches(emailRegex)){

            if(identity.equals("")){
                addClientIdStar.setVisible(true);
            }

            if(mobile1.equals("") || !mobile1.matches(phoneRegex))
                addClientMobile1Star.setVisible(true);

            if(mobile.equals("") || !mobile.matches(phoneRegex))
                addClientMobileStar.setVisible(true);

            if(address.equals("")){
                addClientAddressStar.setVisible(true);
            }

            if(email.equals("") || !email.matches(emailRegex))
                addClientEmailStar.setVisible(true);

            if(email1.equals("") || !email1.matches(emailRegex))
                addClientEmail1Star.setVisible(true);

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incorrect Data");
            alert.setHeaderText("Fill in all the required field");
            alert.setContentText("Some data is not correct, the fields marked with * must be filled");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get() == ButtonType.OK)
                alert.close();
        }else {
            WebTarget target = ClientBuilder.newClient().target(HOST+"/client/create");
            Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(client, MediaType.APPLICATION_JSON));
            if(response.getStatus() == 201){
                updateClientsTable();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Added a client");
                alert.setHeaderText("Success!");
                alert.setContentText("A new client successfully created!");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK)
                    alert.close();
                Stage stage = (Stage) addClientSaveButton.getScene().getWindow();
                stage.close();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Failed to add a client");
                alert.setHeaderText("Failure!");
                alert.setContentText("Something may be wrong, check your network or server!");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK)
                    alert.close();
            }
        }


    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public TableView<ClientTrader> getClientTable() {
        return clientTable;
    }

    public void setClientTable(TableView<ClientTrader> clientTable) {
        this.clientTable = clientTable;
    }

    public Label getCountLabel() {
        return countLabel;
    }

    public void setCountLabel(Label countLabel) {
        this.countLabel = countLabel;
    }

    public void updateClientsTable(){
        WebTarget target = ClientBuilder.newClient().target(HOST+"/client/findAll");
        GenericType<List<ClientTrader>> genericType = new GenericType<>(){};
        List<ClientTrader> list = target.request(MediaType.APPLICATION_JSON).get(genericType);
        ObservableList<ClientTrader> clientTraders = FXCollections.observableArrayList(list);
        getClientTable().setItems(clientTraders);
        getCountLabel().setText(String.format("%,d",clientTraders.size()));
    }
}
