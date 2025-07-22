package com.client.clientapplication;

import com.quickrest.entities.ProductDepartment;
import com.quickrest.entities.ProductSubDepartment;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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
import java.util.*;
import java.util.List;

public class DepartmentalStockListController implements Initializable {

    @FXML
    private AnchorPane departmentalStockListAnchor;
    @FXML
    private AnchorPane departmentalStockListBodyAnchor;
    @FXML
    private TreeView<String> departmentalStockListTreeView;


    String selectedSubDepartment = null;
    String selectedDepartment = null;
    private static final String  HOST = ApplicationPath.getHqPath();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Fetch departments
        fetchProductsAndDepartments();

        //Enabling the tree view to select multiple departments
        departmentalStockListTreeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //Selecting a department or a sub-department
        departmentalStockListTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observableValue, TreeItem<String> stringTreeItem, TreeItem<String> t1) {
                TreeItem<String> selected = departmentalStockListTreeView.getSelectionModel().getSelectedItem();
                selectedSubDepartment = selected.getValue();
                selectedDepartment = selected.getValue();
            }
        });
    }

    public AnchorPane getDepartmentalStockListAnchor() {
        return departmentalStockListAnchor;
    }

    public AnchorPane getDepartmentalStockListBodyAnchor() {
        return departmentalStockListBodyAnchor;
    }

    private void fetchProductsAndDepartments(){
        WebTarget target = ClientBuilder.newClient().target(HOST+"/department/findAll");
        GenericType<List<ProductDepartment>> genericType = new GenericType<>(){};
        List<ProductDepartment> list = target.request(MediaType.APPLICATION_JSON).get(genericType);

        TreeItem<String> treeItem = new TreeItem<>("Departments & Sub-Departments");

        if(list.size() > 0){
            Set<String> set = new HashSet<>();
            for(ProductDepartment dept:list){
                set.add(dept.getName());
            }
            TreeItem<String> item1 =new TreeItem<>();
            for(String dept:set){
                item1 = new TreeItem<>(dept);
                treeItem.getChildren().add(item1);

                //Adding sub department
                target = ClientBuilder.newClient().target(HOST+"/subdepartment/find/"+dept);
                GenericType<List<ProductSubDepartment>> genericType1 = new GenericType<>(){};
                List<ProductSubDepartment> subDepartments = target.request(MediaType.APPLICATION_JSON).get(genericType1);
                for(ProductSubDepartment subDepartment:subDepartments){
                    item1.getChildren().add(new TreeItem<>(subDepartment.getName()));
                }

            }
            departmentalStockListTreeView.setRoot(treeItem);

        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("No Department To Show");
            alert.setContentText("No department has been added");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get().equals(ButtonType.OK)){
                alert.close();
            }
        }

    }

    //Fetching stock of selected sub department
    @FXML
    public void fetchSubDepartmentStockList(){
        if(selectedSubDepartment != null){
            try {
                JasperDesign jasperDesign = JRXmlLoader.load("src\\main\\resources\\Reports\\" +
                        "DepartmentalStockList.jrxml");
                JRDesignQuery jrDesignQuery = new JRDesignQuery();
                String query = "select code,description,live_stock,cost_exclusive," +
                        "cost_inclusive,total_cost_exclusive,total_cost_inclusive from product_live_stock" +
                        " where sub_department = '"+selectedSubDepartment+"'";
                jrDesignQuery.setText(query);
                jasperDesign.setQuery(jrDesignQuery);

                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

                Map<String,Object> map = new HashMap<>();
                map.put("department",selectedSubDepartment);

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
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("No sub-department has been chosen");
            alert.setContentText("Kindly select one to continue...");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get().equals(ButtonType.OK)){
                alert.close();
            }
        }
    }

    //Fetching stock of selected department
    @FXML
    public void fetchDepartmentStockList(){
        if(selectedDepartment != null){
            try {
                JasperDesign jasperDesign = JRXmlLoader.load("src\\main\\resources\\Reports\\" +
                        "DepartmentalStockList.jrxml");
                JRDesignQuery jrDesignQuery = new JRDesignQuery();
                String query = "select code,description,live_stock,cost_exclusive," +
                        "cost_inclusive,total_cost_exclusive,total_cost_inclusive from product_live_stock" +
                        " where department = '"+selectedDepartment+"'";
                jrDesignQuery.setText(query);
                jasperDesign.setQuery(jrDesignQuery);

                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

                Map<String,Object> map = new HashMap<>();
                map.put("department",selectedDepartment);

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

        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("No Category has been chosen");
            alert.setContentText("Kindly select one to continue...");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get().equals(ButtonType.OK)){
                alert.close();
            }
        }
    }

    //Fetching stock of selected sub department
    @FXML
    public void fetchAllDepartmentsStockList(){
        try {
            JasperDesign jasperDesign = JRXmlLoader.load("src\\main\\resources\\Reports\\" +
                    "DepartmentalStockList.jrxml");
            JRDesignQuery jrDesignQuery = new JRDesignQuery();
            String query = "select code,description,live_stock,cost_exclusive," +
                    "cost_inclusive,total_cost_exclusive,total_cost_inclusive from product_live_stock";
            jrDesignQuery.setText(query);
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
