package com.client.clientapplication;

import com.quickrest.entities.*;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

public class StockController implements Initializable {


    @FXML
    private TextField balanceQtyText;

    @FXML
    private TextField fourteenDaysText;

    @FXML
    private TextField sevenDaysText;

    @FXML
    private AnchorPane stockAnchorPane;

    @FXML
    private TextField stockCodeText;

    @FXML
    private TextField stockDescriptionText;

    @FXML
    private TableView<ProductStock> stockTable;

    @FXML
    private TextField thirtyDaysText;

    @FXML
    private Label titleLable;

    String userName;
    private static final String  HOST = ApplicationPath.getApplicationPath();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Setting focus on code field
        Platform.runLater(()->{
            stockCodeText.requestFocus();
        });

        stockDescriptionText.setEditable(false);

        stockCodeText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("\\d*")){
                    stockCodeText.setText(t1.replaceAll("[^\\d]",""));
                }else {
                    if(t1.length() == 6){
                        WebTarget target = ClientBuilder.newClient().target(HOST+"/stock/find/"+t1);
                        GenericType<List<ProductStock>> genericType = new GenericType<>(){};
                        List<ProductStock> list = target.request(MediaType.APPLICATION_JSON).get(genericType);

                        if(list.isEmpty()){
                            target = ClientBuilder.newClient().target(HOST+"/product/findByCode/"+t1);
                            GenericType<List<Product>> genericType1 = new GenericType<>(){};
                            List<Product> productList = target.request(MediaType.APPLICATION_JSON).get(genericType1);
                            if(!productList.isEmpty()){
                                stockDescriptionText.setText(productList.get(productList.size()-1).getName());
                                stockTable.setItems(FXCollections.observableArrayList());
                            }else {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setContentText("Invalid code");
                                Optional<ButtonType> optional1 = alert.showAndWait();
                                if(optional1.isPresent() && optional1.get() == ButtonType.OK)
                                    alert.close();
                                stockTable.setItems(FXCollections.observableArrayList());
                            }
                        }else {
                            //Sorting the list
                            list.sort(new Comparator<ProductStock>() {
                                @Override
                                public int compare(ProductStock o1, ProductStock o2) {
                                    if(o1.getStockIndex() > o2.getStockIndex())
                                        return 1;
                                    else if (o2.getStockIndex() > o1.getStockIndex())
                                        return -1;
                                    else
                                        return 0;
                                }
                            });

                            stockDescriptionText.setText(list.get(list.size()-1).getDescription());
                            ObservableList<ProductStock> ol = FXCollections.observableArrayList(list);
                            balanceQtyText.setText(String.format("%,d",ol.get(ol.size()-1).getBal()));
                            stockTable.setItems(ol);
                        }
                    } else if (t1.length() >6) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Invalid code");
                        Optional<ButtonType> optional1 = alert.showAndWait();
                        if(optional1.isPresent() && optional1.get() == ButtonType.OK)
                            alert.close();
                        stockCodeText.setText("");
                        stockDescriptionText.setText("");
                    }
                }
            }
        });

        //Highlighting the code in code field
        stockCodeText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stockCodeText.selectAll();
            }
        });

        //Refreshing stock table by enter button
        if(stockCodeText.isFocused() && stockTable.getItems() != null){
            stockCodeText.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    if(keyEvent.getCode().equals(KeyCode.ENTER)){
                        WebTarget target = ClientBuilder.newClient().target(HOST+"/stock/find/"+stockCodeText.getText());
                        GenericType<List<ProductStock>> genericType = new GenericType<>(){};
                        List<ProductStock> list = target.request(MediaType.APPLICATION_JSON).get(genericType);
                        //Sorting the list
                        Collections.sort(list, new Comparator<ProductStock>() {
                            @Override
                            public int compare(ProductStock o1, ProductStock o2) {
                                if(o1.getStockIndex() > o2.getStockIndex())
                                    return 1;
                                else if(o2.getStockIndex() > o1.getStockIndex())
                                    return -1;
                                else
                                    return 0;
                            }
                        });
                        stockTable.setItems(FXCollections.observableArrayList(list));
                    }
                }
            });
        }

        //Product Search
        productSearch();
    }

    //Search a product
    private void productSearch(){
        stockCodeText.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode().equals(KeyCode.SHIFT)){
                    Stage stage = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("StockProductSearch.fxml"));
                    try {
                        Scene scene = new Scene(loader.load());
                        stage.setScene(scene);
                        StockProductSearchController controller = loader.getController();
                        controller.setCodeField(stockCodeText);
                        controller.setDescField(stockDescriptionText);
                        stage.show();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    //Open stock list
    @FXML
    public void openStockList(){
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("StockList.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Show stock profile for a code
    @FXML
    public void showStockProfile(){
        try {
            JasperDesign jasperDesign = JRXmlLoader.load("src\\main\\resources\\Reports\\" +
                    "ProductStockProfile.jrxml");
            JRDesignQuery jrDesignQuery = new JRDesignQuery();
            String query = "select code,description,transaction_type,doc_number,date_updated,qty_in,qty_out,bal from" +
                    " product_stock where code = '"+stockCodeText.getText()+"' order by date_updated";
            jrDesignQuery.setText(query);
            jasperDesign.setQuery(jrDesignQuery);

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            Map<String,Object> map = new HashMap<>();
            map.put("code",stockCodeText.getText());
            map.put("description",stockDescriptionText.getText());

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
    @FXML
    public void recalculateStock(){
        //Fetching current value
        WebTarget target = ClientBuilder.newClient().target(HOST+"/stockHolding/findAll/");
        GenericType<List<ProductDepartmentalStockholding>> generic = new GenericType<>(){};
        List<ProductDepartmentalStockholding> stockValues = target.request(MediaType.APPLICATION_JSON).get(generic);

        //Calculating stock holding
        double valueExclusive = 0;
        double valueInclusive = 0;

        for(ProductDepartmentalStockholding sh:stockValues){
            valueExclusive =+ sh.getExclusiveValue();
            valueInclusive =+ sh.getInclusiveValue();
        }
        valueInclusive = Double.parseDouble(String.format("%.2f",valueInclusive));
        valueExclusive = Double.parseDouble(String.format("%.2f",valueExclusive));
        System.out.println("Ex = "+valueExclusive);
        System.out.println("In = "+valueInclusive);



        //Fetching current stock holding
        target = ClientBuilder.newClient().target(HOST+"/stockValue/findAll/");
        GenericType<List<ProductStockValue>> genericType = new GenericType<>(){};
        List<ProductStockValue> list = target.request(MediaType.APPLICATION_JSON).get(genericType);


        if(list.size() == 0){

            ProductStockValue productStockValue = new ProductStockValue();
            productStockValue.setDateRacalculated(new Date());
            productStockValue.setExclusiveValue(valueExclusive);
            productStockValue.setInclusiveValue(valueInclusive);

            target = ClientBuilder.newClient().target(HOST+"/stockValue/create/");
            Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(productStockValue,
                    MediaType.APPLICATION_JSON));

            if(response.getStatus() == 204){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Successfully completed recalculation");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get().equals(ButtonType.OK)){
                    alert.close();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Recalculation has failed!");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get().equals(ButtonType.OK)){
                    alert.close();
                }
            }
        }else {
            boolean exists = true;

            for(ProductStockValue productStockValue:list){

                LocalDate localDate = LocalDate.ofInstant(productStockValue.getDateRacalculated().toInstant(),
                        ZoneId.systemDefault());

                Date date = new Date();
                LocalDate now = LocalDate.ofInstant(date.toInstant(),ZoneId.systemDefault());

                if(localDate.getDayOfMonth() == now.getDayOfMonth())
                    exists = false;

            }

            if(exists){
                ProductStockValue productStockValue = new ProductStockValue();
                productStockValue.setDateRacalculated(new Date());
                productStockValue.setExclusiveValue(valueExclusive);
                productStockValue.setInclusiveValue(valueInclusive);

                target = ClientBuilder.newClient().target(HOST+"/stockValue/create/");
                Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(productStockValue,
                        MediaType.APPLICATION_JSON));

                if(response.getStatus() == 204){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Successfully recalculated");
                    Optional<ButtonType> optional = alert.showAndWait();
                    if (optional.isPresent() && optional.get().equals(ButtonType.OK)){
                        alert.close();
                    }
                }else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Failed to complete!");
                    Optional<ButtonType> optional = alert.showAndWait();
                    if (optional.isPresent() && optional.get().equals(ButtonType.OK)){
                        alert.close();
                    }
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Stock has already been recalculated, do you want to override?");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get().equals(ButtonType.OK)){

                    ProductStockValue productStockValue = new ProductStockValue();
                    productStockValue.setDateRacalculated(new Date());
                    productStockValue.setExclusiveValue(valueExclusive);
                    productStockValue.setInclusiveValue(valueInclusive);

                    target = ClientBuilder.newClient().target(HOST+"/stockValue/create/");
                    Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(productStockValue,
                            MediaType.APPLICATION_JSON));
                    System.out.println("Ex after = "+valueExclusive);
                    System.out.println("In after = "+valueInclusive);

                    if(response.getStatus() == 204){
                        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                        alert1.setContentText("Successfully recalculated");
                        Optional<ButtonType> optional1 = alert1.showAndWait();
                        if (optional1.isPresent() && optional1.get().equals(ButtonType.OK)){
                            alert1.close();
                        }
                    }

                }
            }
        }


    }

    @FXML
    public void viewTrends(){
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
            JasperViewer jv = new JasperViewer(jasperPrint,false);
            jv.setVisible(true);
            jv.setExtendedState(JFrame.MAXIMIZED_BOTH);

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

    @FXML
    public void stockAdjustment(){
        if(stockCodeText.getText().equals("") || stockCodeText.getText() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Input an item code to modify");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get().equals(ButtonType.OK)){
                alert.close();
            }
        }else {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("StockAdjustment.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage= new Stage();
                stage.setScene(scene);

                StockAdjustmentController controller = fxmlLoader.getController();
                controller.setCode(stockCodeText.getText());
                controller.setUserName(getUserName());
                controller.setStockTableView(stockTable);
                controller.setBalanceQuantityField(balanceQtyText);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @FXML
    public void viewAdjustmentReport(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("AdjustmentReportMenue.fxml"));
        try {
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public TextField getBalanceQtyText() {
        return balanceQtyText;
    }

    public void setBalanceQtyText(TextField balanceQtyText) {
        this.balanceQtyText = balanceQtyText;
    }
}
