package com.client.clientapplication;

import com.quickrest.entities.Prices;
import com.quickrest.resources.ApplicationPath;
import com.quickrest.resources.PODetails;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditLpoDetailsConfirmationController implements Initializable {
    @FXML
    private TextField code;
    @FXML
    private TextField description;
    @FXML
    private TextField discount;
    @FXML
    private AnchorPane editLpoDetailsConfirmationAnchor;
    @FXML
    private AnchorPane editLpoDetailsConfirmationLowerAnchor;
    @FXML
    private AnchorPane editLpoDetailsConfirmationTopAnchor;
    @FXML
    private TextField excl;
    @FXML
    private TextField incl;
    @FXML
    private TextField pack;
    @FXML
    private TextField supplier;
    @FXML
    private TextField sysCost;
    @FXML
    private TextField units;
    @FXML
    private TextField vat;

    private List<PODetails> poDetailsList = new ArrayList<>();
    EditLpoController controller;
    private static final String HOST = ApplicationPath.getApplicationPath();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public TextField getCode() {
        return code;
    }

    public TextField getDescription() {
        return description;
    }

    public TextField getExcl() {
        return excl;
    }

    public TextField getIncl() {
        return incl;
    }

    public TextField getPack() {
        return pack;
    }

    public TextField getSupplier() {
        return supplier;
    }

    public TextField getUnits() {
        return units;
    }

    public TextField getVat() {
        return vat;
    }

    public TextField getDiscount() {
        return discount;
    }

    public TextField getSysCost() {
        return sysCost;
    }

    public EditLpoController getController() {
        return controller;
    }

    public void setController(EditLpoController controller) {
        this.controller = controller;
    }
}
