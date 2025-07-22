package com.client.clientapplication;

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
import java.util.List;
import java.util.ResourceBundle;

public class CreditNoteSearchTableController implements Initializable {
    @FXML
    private TextField searchTableSearchField;
    @FXML
    private TableView<Supplier> searchTableView;
    @FXML
    private Button searchTableDoneButton;


    private TextField supplierField;
    private Supplier supplierSelected = null;
    private static final String  HOST = ApplicationPath.getHqPath();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Searching a supplier in the db
        searchTableSearchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (searchTableSearchField.getText().isEmpty() || searchTableSearchField.getText().isBlank()) {
                    searchTableView.setItems(FXCollections.observableArrayList());
                } else {
                    if (t1.length() >= 2) {
                        System.out.println("Searching");
                        WebTarget target = ClientBuilder.newClient().target(HOST +"/supplier/search/"+ t1.toUpperCase());
                        GenericType<List<Supplier>> sup = new GenericType<>() {};
                        List<Supplier> list = target.request(MediaType.APPLICATION_JSON).get(sup);

                        ObservableList<Supplier> observableList = FXCollections.observableArrayList();
                        observableList.addAll(list);
                        searchTableView.setItems(observableList);
                    }

                }
            }
        });

        //Ensuring only a single row can be selected
        searchTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //Getting the selected supplier name
        searchTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Supplier>() {
            @Override
            public void changed(ObservableValue<? extends Supplier> observableValue, Supplier s, Supplier t1) {
                Supplier selected = searchTableView.getSelectionModel().getSelectedItem();
                if(selected != null){
                    supplierSelected = selected;
                }
            }
        });

    }

    public TextField getSearchTableSearchField() {
        return searchTableSearchField;
    }


    @FXML
    public void setSelectedSupplierInSupplierField(){
        if(supplierSelected != null){
            getSupplierField().setText(supplierSelected.getIdentity());
            Stage stage = (Stage) searchTableDoneButton.getScene().getWindow();
            stage.close();
        }else {
            Stage stage = (Stage) searchTableDoneButton.getScene().getWindow();
            stage.close();
        }

    }

    public TextField getSupplierField() {
        return supplierField;
    }

    public void setSupplierField(TextField supplierField) {
        this.supplierField = supplierField;
    }
}
