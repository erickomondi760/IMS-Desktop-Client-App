package com.client.clientapplication;

import com.quickrest.entities.Transporter;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TransporterController implements Initializable {
    @FXML
    private TableView<Transporter> transporterTable;
    private String user;
    private static final String  HOST = ApplicationPath.getHqPath();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        WebTarget webTarget = ClientBuilder.newClient().target(HOST+"/transporter/findAll");
        GenericType<List<Transporter>> genericType = new GenericType<>(){};
        List<Transporter> transporterList= webTarget.request(MediaType.APPLICATION_JSON).get(genericType);

        ObservableList<Transporter> transporterObservableList = FXCollections.observableArrayList(transporterList);
        transporterTable.setItems(transporterObservableList);
    }

    @FXML
    public void addTransporter(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddTransporter.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();

            AddTransporterController controller = loader.getController();
            controller.setUser(getUser());
            controller.setTableView(transporterTable);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


}
