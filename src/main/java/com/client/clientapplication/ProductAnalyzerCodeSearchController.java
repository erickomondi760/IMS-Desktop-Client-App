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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProductAnalyzerCodeSearchController implements Initializable {

    @FXML
    private TableView<Prices> analyzerSearchTable;
    @FXML
    private TextField analyzerSearchSearchField;
    @FXML
    private Button analyzerSearchButton;

    TextField codeField;
    private static final String HOST = ApplicationPath.getHqPath();


    public TextField getCodeField() {
        return codeField;
    }

    public void setCodeField(TextField codeField) {
        this.codeField = codeField;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Filter products table
        productsFilter();

        //Enabling accept button
        analyzerSearchTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (analyzerSearchTable.getSelectionModel().getSelectedItem() != null) {
                    analyzerSearchButton.setDisable(false);
                }
            }
        });
    }

    //Filtering items in the table
    private void productsFilter() {

        analyzerSearchSearchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                try{
                    if (analyzerSearchSearchField.getText().isEmpty() || analyzerSearchSearchField.getText().isBlank()) {
                        analyzerSearchTable.setItems(FXCollections.observableArrayList());
                    } else {
                        if (t1.length() >= 2) {
                            WebTarget target = ClientBuilder.newClient().target(HOST + "/prices/findDynamically/" + t1.toUpperCase());
                            GenericType<List<Prices>> stock = new GenericType<>() {
                            };
                            List<Prices> list = target.request(MediaType.APPLICATION_JSON).get(stock);

                            ObservableList<Prices> observableList = FXCollections.observableArrayList();
                            observableList.addAll(list);
                            analyzerSearchTable.setItems(observableList);
                        }

                    }
                }catch (Exception  e){

                }

            }
        });
    }

    private Prices getSelectedItem() {
        return analyzerSearchTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void acceptProduct() {
        getCodeField().setText(getSelectedItem().getCode());
        Stage stage = (Stage) analyzerSearchButton.getScene().getWindow();
        stage.close();
    }

}
