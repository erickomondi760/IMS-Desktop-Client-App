package com.client.clientapplication;

import com.quickrest.entities.ProductStock;
import com.quickrest.entities.Supplier;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class StockListSupplierSearchController implements Initializable {
    @FXML
    private AnchorPane stockListSupplierSearchAnchor;
    @FXML
    private Button stockListSupplierSearchButton;
    @FXML
    private AnchorPane stockListSupplierSearchDetailsAnchor;
    @FXML
    private TableView<Supplier> stockListSupplierSearchTable;
    @FXML
    private TextField stockListSupplierSearchTextField;


    private static final String  HOST = ApplicationPath.getHqPath();
    private TextField supplier;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Supplier search from the db
        stockListSupplierSearchTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(stockListSupplierSearchTextField.getText().isEmpty() || stockListSupplierSearchTextField.getText().isBlank()){
                    stockListSupplierSearchTable.setItems(FXCollections.observableArrayList());
                }else {
                    if(t1.length() >= 2){
                        WebTarget target = ClientBuilder.newClient().target(HOST + "/supplier/search/" + t1.toUpperCase());
                        GenericType<List<Supplier>> stock = new GenericType<>() {};
                        List<Supplier> list = target.request(MediaType.APPLICATION_JSON).get(stock);

                        ObservableList<Supplier> observableList = FXCollections.observableArrayList();
                        observableList.addAll(list);
                        stockListSupplierSearchTable.setItems(observableList);
                    }

                }
            }
        });

        //Enable button
        stockListSupplierSearchTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Supplier>() {
            @Override
            public void changed(ObservableValue<? extends Supplier> observableValue, Supplier supplier,
                                Supplier supplier1) {
                if(supplier1 != null){
                    stockListSupplierSearchButton.setDisable(false);
                }
            }
        });
    }

    @FXML
    public void acceptSupplier(){
        Supplier s = stockListSupplierSearchTable.getSelectionModel().getSelectedItem();
        if(s!= null){
            getSupplier().setText(s.getIdentity());
            Stage stage = (Stage) stockListSupplierSearchButton.getScene().getWindow();
            stage.close();
        }
    }

    public TextField getSupplier() {
        return supplier;
    }

    public void setSupplier(TextField supplier) {
        this.supplier = supplier;
    }


    }
