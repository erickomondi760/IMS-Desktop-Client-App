package com.client.clientapplication;

import com.quickrest.entities.GoodsReceivedNote;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.*;

public class EditBranchGrnController implements Initializable {

    @FXML
    private AnchorPane editGrnAnchor;
    @FXML
    private AnchorPane editGrnBodyAnchor;
    @FXML
    private Label editGrnNoLabel;
    @FXML
    private Button editGrnSave;
    @FXML
    private Label editGrnSupplierLabel;
    @FXML
    private AnchorPane editGrnTitleAnchor;
    @FXML
    private TextField editGrnInvNumber;
    @FXML
    private TextField editGrnCu;

    private TableView<GoodsReceivedNote> tableView;
    private GoodsReceivedNote grn;
    private Label recordsLabel;
    private Label amountLabel;

    private static final String HOST = ApplicationPath.getApplicationPath();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    @FXML
    public void updateGrn(){
        if(!editGrnCu.getText().equals("") && !editGrnInvNumber.getText().equals("")){
            GoodsReceivedNote grn = getGrn();
            grn.setCuInvoiceNumber(editGrnCu.getText());
            grn.setInvoiceNumber(editGrnInvNumber.getText());

            WebTarget target = ClientBuilder.newClient().target(HOST+"/received/update");
            Response response = target.request(MediaType.APPLICATION_JSON).put(Entity.entity(grn,MediaType.APPLICATION_JSON));

            if(response.getStatus() == 200){
                fetchingGrns();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("GRN Updated");
                alert.setContentText("Grn details have been updated");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK)
                    alert.close();
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Failed!");
                alert.setContentText("Something went wrong,kindly find out from system admin");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK)
                    alert.close();
            }
        }

    }

    private void fetchingGrns() {

        new Thread(() -> {
            WebTarget target = ClientBuilder.newClient().target(HOST + "/received/findAll");
            GenericType<List<GoodsReceivedNote>> gt = new GenericType<>() {
            };
            List<GoodsReceivedNote> list = target.request(MediaType.APPLICATION_JSON).get(gt);

            if (list == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Request Timeout");
                alert.setContentText("The server took too long to respond, kindly retry");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK)
                    alert.close();
            } else {
                Platform.runLater(() -> {
                    Collections.sort(list, new Comparator<GoodsReceivedNote>() {
                        @Override
                        public int compare(GoodsReceivedNote o1, GoodsReceivedNote o2) {
                            if (o1.getBranchGrnNumber() > o2.getBranchGrnNumber())
                                return 1;
                            else if (o2.getBranchGrnNumber() > o1.getBranchGrnNumber()) {
                                return -1;
                            } else
                                return 0;
                        }
                    });
                    getTableView().setItems(FXCollections.observableArrayList(list));

                    getRecordsLabel().setText(String.format("%,2d", list.size()));

                    double amount = 0;
                    for (GoodsReceivedNote e : list) {
                        amount += e.getAmountReceivedIncl();
                    }
                    getAmountLabel().setText(String.format("%,.2f", amount));

                });
            }
        }).start();
    }

    public TableView<GoodsReceivedNote> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<GoodsReceivedNote> tableView) {
        this.tableView = tableView;
    }

    public GoodsReceivedNote getGrn() {
        return grn;
    }

    public void setGrn(GoodsReceivedNote grn) {
        this.grn = grn;
    }

    public Label getEditGrnNoLabel() {
        return editGrnNoLabel;
    }

    public Label getEditGrnSupplierLabel() {
        return editGrnSupplierLabel;
    }

    public TextField getEditGrnInvNumber() {
        return editGrnInvNumber;
    }

    public TextField getEditGrnCu() {
        return editGrnCu;
    }

    public Label getRecordsLabel() {
        return recordsLabel;
    }

    public void setRecordsLabel(Label recordsLabel) {
        this.recordsLabel = recordsLabel;
    }

    public Label getAmountLabel() {
        return amountLabel;
    }

    public void setAmountLabel(Label amountLabel) {
        this.amountLabel = amountLabel;
    }
}
