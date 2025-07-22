package com.client.clientapplication;

import com.quickrest.entities.Invoice;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InvoiceDnoteChooser implements Initializable {
    @FXML
    private RadioButton invoiceToggle;
    @FXML
    private RadioButton deliveryToggle;
    @FXML
    private Button chooseButton;

    String user;
    TableView<Invoice> tableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        chooseButton.setOnAction(e1->{
            if(deliveryToggle.isSelected()){
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("DNote.fxml"));
                try {
                    Scene scene = new Scene(loader.load());
                    stage.setScene(scene);
                    stage.show();

                    Stage stage1 = (Stage) chooseButton.getScene().getWindow();
                    stage1.close();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (invoiceToggle.isSelected()) {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("Invoicing.fxml"));
                try {
                    Scene scene = new Scene(loader.load());
                    stage.setScene(scene);
                    stage.show();

                    InvoicingController controller = loader.getController();
                    controller.setUser(getUser());
                    controller.setInvoiceSummaryTableView(getTableView());
                    Stage stage1 = (Stage) chooseButton.getScene().getWindow();
                    stage1.close();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public TableView<Invoice> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<Invoice> tableView) {
        this.tableView = tableView;
    }
}
