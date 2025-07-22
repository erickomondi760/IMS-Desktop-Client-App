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
import java.util.*;

public class CnProductSearchController implements Initializable {

    @FXML
    public TableView<Prices> productSearchTable;
    @FXML
    private TextField productSearchSearchField;
    @FXML
    private Button productSearchButton;

    private TextField codeField;
    private TextField descField;
    private TextField stockField;
    private TextField costField;
    private static final String  HOST = ApplicationPath.getApplicationPath();

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

    public TextField getCostField() {
        return costField;
    }

    public void setCostField(TextField costField) {
        this.costField = costField;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Fetching prices from the db
        productSearchSearchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                try {
                    if (productSearchSearchField.getText().isEmpty() || productSearchSearchField.getText().isBlank()) {
                        productSearchTable.setItems(FXCollections.observableArrayList());
                    } else {
                        if (t1.length() >= 2) {
                            WebTarget target = ClientBuilder.newClient().target(ApplicationPath.getHqPath() +
                                    "/prices/findDynamically/" + t1.toUpperCase());
                            GenericType<List<Prices>> stock = new GenericType<>() {
                            };
                            List<Prices> list = target.request(MediaType.APPLICATION_JSON).get(stock);

                            ObservableList<Prices> observableList = FXCollections.observableArrayList();
                            observableList.addAll(list);
                            productSearchTable.setItems(observableList);
                        }

                    }
                }catch (Exception e){

                }

            }
        });

        //Accepting a seleted item
        productSearchButton.setOnAction(e->{
            Prices price = productSearchTable.getSelectionModel().getSelectedItem();
            Stage stage = (Stage) productSearchButton.getScene().getWindow();
            if(price == null){
                getCodeField().setText("");
                stage.close();
            }else {
                WebTarget target = ClientBuilder.newClient().target(HOST+"/stock/find/"+price.getCode());
                GenericType<List<ProductStock>> generic = new GenericType<>(){};
                List<ProductStock> stockList = target.request(MediaType.APPLICATION_JSON).get(generic);

                getCodeField().setText(price.getCode());
                getDescField().setText(price.getDescription());
                getCostField().setText(price.getInclusive()+"");
                if(stockList.size() > 0){
                    if(stockList.size() > 1){
                        Collections.sort(stockList, new Comparator<ProductStock>() {
                            @Override
                            public int compare(ProductStock o1, ProductStock o2) {
                                if(o1.getStockIndex() > o2.getStockIndex())
                                    return 1;
                                else if (o2.getStockIndex() > o1.getStockIndex())
                                    return -1;
                                else return 0;
                            }
                        });
                    }

                    getStockField().setText(stockList.get(stockList.size()-1).getBal()+"");
                }else {
                    getStockField().setText("0");
                }

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
