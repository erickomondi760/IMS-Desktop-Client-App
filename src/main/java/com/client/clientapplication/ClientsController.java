package com.client.clientapplication;

import com.quickrest.entities.ClientTrader;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {
    @FXML
    private AnchorPane clientsAnchor;
    @FXML
    private Label clientsCountLabel;
    @FXML
    private Button clientsDeleteButton;
    @FXML
    private Button clientsEditButton;
    @FXML
    private Button clientsNewButton;
    @FXML
    private Button clientsPrintButton;
    @FXML
    private Button clientsViewButton;
    @FXML
    private TableView<ClientTrader> clientTable;
    @FXML
    private Button clientsOutletButton;

    private String user;
    private static final String  HOST = ApplicationPath.getHqPath();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ////Opening client addition page
        clientsNewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("AddClient.fxml"));
                try {
                    Scene scene = new Scene(loader.load());
                    stage.setScene(scene);
                    stage.show();

                    AddClientController clientController = loader.getController();
                    clientController.setUser(getUser());
                    clientController.setClientTable(clientTable);
                    clientController.setCountLabel(clientsCountLabel);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //Disabling outlet button
        clientTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        if(clientTable.getSelectionModel().getSelectedItem() != null){
            clientsOutletButton.setDisable(false);
        }



        //Filling the client table
        updateClients();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private void updateClients(){
        WebTarget target = ClientBuilder.newClient().target(HOST+"/client/findAll");
        GenericType<List<ClientTrader>> genericType = new GenericType<>(){};
        List<ClientTrader> list = target.request(MediaType.APPLICATION_JSON).get(genericType);
        ObservableList<ClientTrader> clientTraders = FXCollections.observableArrayList(list);
        clientTable.setItems(clientTraders);
        clientsCountLabel.setText(String.format("%,d",clientTraders.size()));
    }

    @FXML
    public void addOutlets(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ClientTraderOutlet.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();

            ClientTraderOutletController clientController = loader.getController();
            clientController.setUserName(getUser());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
