package com.client.clientapplication;

import com.quickrest.entities.Prices;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditLpoSearchTableController implements Initializable {
    @FXML
    private Button ediLpoSeachButton;
    @FXML
    private TableView<Prices> editLpoSearchTable;
    @FXML
    private TextField editLpoSearchText;

    private TextField code;
    private TextField desc;
    private TextField unit;
    private TextField cost;
    private TextField vat;

    private static final String HOST = ApplicationPath.getHqPath();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Accepting selected price
        ediLpoSeachButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Prices p = editLpoSearchTable.getSelectionModel().getSelectedItem();
                if(p != null){
                    getCode().setText(p.getCode());
                    getCost().setText(String.format("%,.2f",p.getInclusive()));
                    getDesc().setText(p.getDescription());
                    getUnit().setText(p.getUnit());
                    getVat().setText(String.format("%,.2f",p.getInclusive()-p.getExcl()));
                    Stage s = (Stage) ediLpoSeachButton.getScene().getWindow();
                    s.close();
                }
            }
        });

        //Searching product
        editLpoSearchText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                try{
                    if (editLpoSearchText.getText().isEmpty() || editLpoSearchText.getText().isBlank()) {
                        editLpoSearchTable.setItems(FXCollections.observableArrayList());
                    } else {
                        if (t1.length() >= 2) {
                            WebTarget target = ClientBuilder.newClient().target(HOST + "/prices/findDynamically/" + t1.toUpperCase());
                            GenericType<List<Prices>> stock = new GenericType<>() {
                            };
                            List<Prices> list = target.request(MediaType.APPLICATION_JSON).get(stock);

                            ObservableList<Prices> observableList = FXCollections.observableArrayList();
                            observableList.addAll(list);
                            editLpoSearchTable.setItems(observableList);
                        }

                    }
                }catch (Exception  e){

                }

            }
        });

    }

    public TextField getCode() {
        return code;
    }

    public void setCode(TextField code) {
        this.code = code;
    }

    public TextField getDesc() {
        return desc;
    }

    public void setDesc(TextField desc) {
        this.desc = desc;
    }

    public TextField getUnit() {
        return unit;
    }

    public void setUnit(TextField unit) {
        this.unit = unit;
    }

    public TextField getCost() {
        return cost;
    }

    public void setCost(TextField cost) {
        this.cost = cost;
    }

    public TextField getVat() {
        return vat;
    }

    public void setVat(TextField vat) {
        this.vat = vat;
    }
}
