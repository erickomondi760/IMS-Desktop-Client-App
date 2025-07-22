package com.client.clientapplication;

import com.quickrest.entities.Prices;
import com.quickrest.entities.ProductStock;
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
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StockProductSearchController implements Initializable {
    @FXML
    public TableView<Prices> stockProductSearchTable;
    @FXML
    private TextField stockProductSearchSearchField;
    @FXML
    private Button stockProductSearchButton;

    private TextField codeField;
    private TextField descField;
    private TextField stockField;
    private static final String  HOST = ApplicationPath.getHqPath();

    public TextField getDescField() {
        return descField;
    }

    public void setDescField(TextField descField) {
        this.descField = descField;
    }

    public TextField getStockField() {
        return stockField;
    }

    public void setStockField(TextField stockField) {
        this.stockField = stockField;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Searching product
        stockProductSearchSearchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(stockProductSearchSearchField.getText().isEmpty() || stockProductSearchSearchField.getText().isBlank()){
                    stockProductSearchTable.setItems(FXCollections.observableArrayList());
                }else {
                    if(t1.length() >= 2){
                        try {
                            WebTarget target = ClientBuilder.newClient().target(HOST + "/prices/findDynamically/" + t1.toUpperCase());
                            GenericType<List<Prices>> prices = new GenericType<>() {};
                            List<Prices> list = target.request(MediaType.APPLICATION_JSON).get(prices);

                            ObservableList<Prices> observableList = FXCollections.observableArrayList();
                            observableList.addAll(list);
                            stockProductSearchTable.setItems(observableList);
                        }catch (Exception e){

                        }

                    }

                }
            }
        });


        //Accepting a seleted item
        stockProductSearchButton.setOnAction(e->{
            Prices price = stockProductSearchTable.getSelectionModel().getSelectedItem();
            Stage stage = (Stage) stockProductSearchButton.getScene().getWindow();
            if(price == null){
                getCodeField().setText("");
                stage.close();
            }else {
                WebTarget target = ClientBuilder.newClient().target(ApplicationPath.getApplicationPath()+
                        "/stock/find/"+price.getCode());
                GenericType<List<ProductStock>> generic = new GenericType<>(){};
                List<ProductStock> stockList = target.request(MediaType.APPLICATION_JSON).get(generic);

                getCodeField().setText(price.getCode());
                stage.close();
            }
        });
    }

    public TextField getCodeField() {
        return codeField;
    }

    public void setCodeField(TextField codeField) {
        this.codeField = codeField;
    }

}
