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
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class ProductSupplierSearchController implements Initializable {
    @FXML
    private TextField productSupplierSearchTextField;
    @FXML
    private Button productSupplierSearchButton;
    @FXML
    private TableView<Supplier> productSupplierSearchTable;

    private static final String HOST = ApplicationPath.getHqPath();
    List<ProductStock> initialList = new ArrayList<>();
    List<ProductStock> newList = new ArrayList<>();
    private TextField supplier;
    private Supplier sup;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Searching supplier
        productSupplierSearchTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(productSupplierSearchTextField.getText().isEmpty() || productSupplierSearchTextField.getText().isBlank()){
                    productSupplierSearchTable.setItems(FXCollections.observableArrayList());
                }else {
                    if(t1.length() >= 2){
                        WebTarget target = ClientBuilder.newClient().target(HOST + "/supplier/search/" + t1.toUpperCase());
                        GenericType<List<Supplier>> supplier = new GenericType<>() {};
                        List<Supplier> list = target.request(MediaType.APPLICATION_JSON).get(supplier);

                        ObservableList<Supplier> observableList = FXCollections.observableArrayList();
                        observableList.addAll(list);
                        productSupplierSearchTable.setItems(observableList);
                    }

                }
            }
        });


        //Accepting a seleted item
        productSupplierSearchButton.setOnAction(e->{
            Supplier s = productSupplierSearchTable.getSelectionModel().getSelectedItem();
            Stage stage = (Stage)  productSupplierSearchButton.getScene().getWindow();
            if(s == null){
                getSupplier().setText("");
                stage.close();
            }else {

                getSupplier().setText(s.getIdentity());
                stage.close();
            }
        });

    }



    @FXML
    public void acceptSupplier() {

    }

    public TextField getSupplier() {
        return supplier;
    }

    public void setSupplier(TextField supplier) {
        this.supplier = supplier;
    }

}
