package com.client.clientapplication;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StockListController implements Initializable {
    @FXML
    private BorderPane stockListBorderPane;
    @FXML
    private RadioButton supplierRadio;
    @FXML
    private RadioButton departmentRadio;
    @FXML
    private AnchorPane stockListBorderPaneLeftAnchorPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    public void switchToSupplier(javafx.event.ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("StockListSupplier.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            StockListSupplierController controller = loader.getController();
            stockListBorderPane.setCenter(controller.supplierStockListDetailsAnchor);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public void switchToDepartment(javafx.event.ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("DepartmentalStockList.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            DepartmentalStockListController controller = fxmlLoader.getController();
            stockListBorderPane.setCenter(controller.getDepartmentalStockListBodyAnchor());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
