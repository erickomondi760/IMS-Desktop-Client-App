package com.client.clientapplication;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class StockListSupplierController implements Initializable {
    @FXML
    AnchorPane supplierStockListDetailsAnchor;
    @FXML
    Button stockListSupplierButton;
    @FXML
    TextField stockListSupplierTextField;
    @FXML
    private Button stockListSupplierOkayButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Disabling stockListSupplierOkayButton
        stockListSupplierOkayButton.setDisable(true);

        //Adding a listener to stockListSupplierOkayButton
        stockListSupplierTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.isEmpty())
                    stockListSupplierOkayButton.setDisable(false);
                else
                    stockListSupplierOkayButton.setDisable(true);
            }
        });
    }

    @FXML
    public void supplierSearch(){
        stockListSupplierButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("StockListSupplierSearch.fxml"));
                try {
                    Stage stage = new Stage();
                    Scene scene = new Scene(loader.load());
                    stage.setScene(scene);
                    StockListSupplierSearchController controller = loader.getController();
                    controller.setSupplier(stockListSupplierTextField);
                    stage.show();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }

    @FXML
    public void createSupplierStockListReport(){
        try {
            JasperDesign jasperDesign = JRXmlLoader.load("src\\main\\resources\\Reports\\StockList.jrxml");
            JRDesignQuery jrDesignQuery = new JRDesignQuery();

            String query = "SELECT CODE,DESCRIPTION,LIVE_STOCK,COST_INCLUSIVE,LAST_RECEIVED,THIRTY_DAYS_MOVEMENT,SIXTY_DAYS_MOVEMENT FROM PRODUCT_LIVE_STOCK WHERE " +
                    "SUPPLIER = '"+stockListSupplierTextField.getText()+"'";

            jrDesignQuery.setText(query);
            jasperDesign.setQuery(jrDesignQuery);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            Connection connection = DriverManager.
                    getConnection("jdbc:oracle:thin:@localhost:1521:ORCL","system","Erick12345");
            if (connection != null){

                //Setting supplier name into the header
                Map<String,Object> map = new HashMap<>();
                map.put("supplier",stockListSupplierTextField.getText());

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,map,connection);
                JasperViewer jv = new JasperViewer(jasperPrint,false);
                jv.setVisible(true);
                jv.setExtendedState(Frame.MAXIMIZED_BOTH);
                JasperViewer.setDefaultLookAndFeelDecorated(true);
            }

        } catch (JRException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Error While Connecting To The Database");
            alert.setContentText("Kindly check your db connection...");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get().equals(ButtonType.OK)){
                alert.close();
            }
        }

    }

    public AnchorPane getSupplierStockListDetailsAnchor() {
        return supplierStockListDetailsAnchor;
    }

    public Button getStockListSupplierButton() {
        return stockListSupplierButton;
    }

    public TextField getStockListSupplierTextField() {
        return stockListSupplierTextField;
    }
}
