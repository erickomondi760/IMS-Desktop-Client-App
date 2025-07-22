package com.client.clientapplication;

import com.quickrest.entities.ClientTrader;
import com.quickrest.entities.ClientTraderOutlet;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientTraderOutletController implements Initializable {
    @FXML
    private AnchorPane clientTraderOutlet;

    @FXML
    private TextField clientTraderOutletAltEmail;

    @FXML
    private TextField clientTraderOutletAltPhone;

    @FXML
    private AnchorPane clientTraderOutletDetailsAnchor;

    @FXML
    private Button clientTraderOutletDetailsButton;

    @FXML
    private TextField clientTraderOutletEmail;

    @FXML
    private TextField clientTraderOutletLocation;

    @FXML
    private TextField clientTraderOutletPhone;

    @FXML
    private AnchorPane clientTraderOutletTitleAnchor;

    @FXML
    private ComboBox<String> clientsOutletCombo;

    @FXML
    private TextField clientTraderOutletAddress;


    private static final String HOST = ApplicationPath.getHqPath();
    private String userName;
    List<ClientTrader> traders;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        traders = new ArrayList<>();
        //Fetching clients
        WebTarget webTarget = ClientBuilder.newClient().target(HOST + "/client/findAll");
        GenericType<List<ClientTrader>> genericType = new GenericType<>() {};
        List<ClientTrader> list = webTarget.request(MediaType.APPLICATION_JSON).get(genericType);
        traders = list;
        ObservableList<String> ol = FXCollections.observableArrayList();
        list.forEach(e -> {
            ol.add(e.getIdentity());
        });
        clientsOutletCombo.setItems(ol);
    }

    @FXML
    public void addOutlet() {
        if (clientsOutletCombo.getItems() == null) {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setHeaderText("No client to chose from");
            alert1.setContentText("Kindly add a client to create an outlet");
            Optional<ButtonType> optional1 = alert1.showAndWait();
            if (optional1.isPresent() && optional1.get() == ButtonType.OK)
                alert1.close();
        } else {
            ClientTraderOutlet outlet = new ClientTraderOutlet();
            outlet.setClientName(clientsOutletCombo.getValue());
            outlet.setLocation(clientTraderOutletLocation.getText());
            outlet.setAddress(clientTraderOutletAddress.getText());
            outlet.setEmail(clientTraderOutletEmail.getText());
            outlet.setAltEmail(clientTraderOutletAltEmail.getText());
            outlet.setPhoneNumber(clientTraderOutletPhone.getText());
            outlet.setAltPhoneNumber(clientTraderOutletAltPhone.getText());
            outlet.setUserName(getUserName());


            //Persisting client outlet
            WebTarget target = ClientBuilder.newClient().target(HOST + "/outlet/create");
            Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(outlet,
                    MediaType.APPLICATION_JSON));
            if (response.getStatus() == 201) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setHeaderText("Client outlet successfully added");
                alert1.setContentText("Outlet location = " + outlet.getLocation());
                Optional<ButtonType> optional1 = alert1.showAndWait();
                if (optional1.isPresent() && optional1.get() == ButtonType.OK)
                    alert1.close();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setHeaderText("Failed");
                alert1.setContentText("Reason : " + response.readEntity(String.class));
                Optional<ButtonType> optional1 = alert1.showAndWait();
                if (optional1.isPresent() && optional1.get() == ButtonType.OK)
                    alert1.close();
            }
        }


    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
