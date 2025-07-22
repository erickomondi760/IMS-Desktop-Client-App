package com.client.clientapplication;

import com.quickrest.entities.Supplier;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SuppliersController implements Initializable {
    @FXML
    private TableView<Supplier> supplierTable;
    @FXML
    private Label supplierCountLabel;



    private String user;
    private List<Supplier> list = new ArrayList<>();
    private static final String  HOST = ApplicationPath.getHqPath();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Inserting suppliers into the table
        new Thread(){
            @Override
            public void run(){
                WebTarget webTarget = ClientBuilder.newClient().target(HOST+"/product/findAllSuppliers");
                GenericType<List<Supplier>> genericType = new GenericType<>(){};
                list = webTarget.request(MediaType.APPLICATION_JSON).get(genericType);
            }
        }.start();
        Platform.runLater(()->{
            ObservableList<Supplier> supplierObservableList = FXCollections.observableArrayList(list);
            supplierTable.setItems(supplierObservableList);

            //Displaying the number of suppliers
            supplierCountLabel.setText(String.format("%,d",supplierObservableList.size()));
        });

    }
    @FXML
    public void AddSupplier(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddSupplier.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            AddSupplierController controller = loader.getController();
            controller.setTableView(supplierTable);

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void setUser(String name){
        this.user = name;
    }

    public String getUser(){
        return user;
    }
}
