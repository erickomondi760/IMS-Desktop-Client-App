package com.client.clientapplication;

import com.quickrest.resources.ApplicationPath;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.*;

public class TrendsController implements Initializable {

    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

    @FXML
    private Button trendsViewButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Displaying stock holding data
        trendsViewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(dateFrom.getValue() != null && dateTo.getValue() != null){
                    Date firstDate = Date.from(dateFrom.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                    Date endDate = Date.from(dateTo.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

                    try {
                        JasperDesign jasperDesign = JRXmlLoader.load("src\\main\\resources\\Reports\\Trend.jrxml");
                        JRDesignQuery jrDesignQuery = new JRDesignQuery();

                        jrDesignQuery.setText("SELECT * FROM PRODUCT_STOCK_VALUE WHERE DATE_RECALCULATED BETWEEN '"+firstDate+"' AND " +
                                "'"+endDate+"'");

                        jasperDesign.setQuery(jrDesignQuery);
                        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                        Map<String,Object> map = new HashMap<>();
                        map.put("department","All Departments");

                        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL",
                                "system","Erick12345");

                        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,map,connection);
                        JasperViewer jv = new JasperViewer(jasperPrint,false);
                        jv.setVisible(true);
                        jv.setExtendedState(Frame.MAXIMIZED_BOTH);
                        JasperViewer.setDefaultLookAndFeelDecorated(true);
                    } catch (JRException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Error While Connecting To The Database");
                        alert.setContentText("Kindly check your db connection...");
                        Optional<ButtonType> optional = alert.showAndWait();
                        if (optional.isPresent() && optional.get().equals(ButtonType.OK)){
                            alert.close();
                        }
                    }
                }else {
                    try {
                        JasperDesign jasperDesign = JRXmlLoader.load("src\\main\\resources\\Reports\\Trend.jrxml");
                        JRDesignQuery jrDesignQuery = new JRDesignQuery();
                        jrDesignQuery.setText("SELECT * FROM PRODUCT_STOCK_VALUE");
                        jasperDesign.setQuery(jrDesignQuery);
                        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                        Map<String,Object> map = new HashMap<>();
                        map.put("department","All Departments");

                        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL",
                                "system","Erick12345");

                        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,map,connection);
                        JasperViewer.viewReport(jasperPrint,false);

                    } catch (JRException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Error While Connecting To The Database");
                        alert.setContentText("Kindly check your db connection...");
                        Optional<ButtonType> optional = alert.showAndWait();
                        if (optional.isPresent() && optional.get().equals(ButtonType.OK)){
                            alert.close();
                        }
                    }
                }


            }
        });
    }


}
