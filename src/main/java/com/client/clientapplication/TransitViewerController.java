package com.client.clientapplication;

import com.quickrest.entities.CreditNote;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TransitViewerController implements Initializable {
    @FXML
    private TableColumn<CreditNote, Double> amountIncl;

    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

    @FXML
    private Label grnViewAmountLabel1;

    @FXML
    private Button searchByDateButton;

    @FXML
    private AnchorPane transitAnchor;

    @FXML
    private Button transitDeleteButton;

    @FXML
    private Button transitExportButton;

    @FXML
    private HBox transitMenuAnchor;

    @FXML
    private Button transitReceiveButton;

    @FXML
    private TextField transitSearchText;

    @FXML
    private Label transitViewAmountLabel;

    @FXML
    private Button transitViewButton;

    @FXML
    private Label transitViewRecordsLabel;

    @FXML
    private TableView<CreditNote> transitViewTable;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public AnchorPane getTransitAnchor() {
        return transitAnchor;
    }
}
