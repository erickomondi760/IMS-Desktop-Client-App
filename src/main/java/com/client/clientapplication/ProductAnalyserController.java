package com.client.clientapplication;

import com.quickrest.entities.Prices;
import com.quickrest.entities.Product;
import com.quickrest.entities.ProductDepartment;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import javax.imageio.*;

public class ProductAnalyserController implements Initializable {
    @FXML
    private TextField analyserCodeField;
    @FXML
    private Label analyserCostExcl;
    @FXML
    private Label analyserCostInclusive;
    @FXML
    private Label analyserDate;
    @FXML
    private Label analyserDepartment;
    @FXML
    private Label analyserDescriptionField;
    @FXML
    private AnchorPane analyserDetailsAnchor;
    @FXML
    private CheckBox analyserFixedCost;
    @FXML
    private Label analyserMarkUp;
    @FXML
    private Label analyserPackField;
    @FXML
    private Label analyserProductImage;
    @FXML
    private Label analyserSellingPrice;
    @FXML
    private Label analyserSubDepartment;
    @FXML
    private Label analyserSupplier;
    @FXML
    private Label analyserUserName;
    @FXML
    private Label analyserVat;
    @FXML
    private Label analyserBarcode;
    @FXML
    private CheckBox analyserActive;
    @FXML
    private CheckBox analyserOpenCost;
    @FXML
    private CheckBox analyserBlocked;
    @FXML
    ImageView analyserImageView;

    private static final String  HOST = ApplicationPath.getHqPath();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Validate product code
        codeValidator();

        //ProductSearch
        searchProduct();

        //Selecting items in code field
        analyserCodeField.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                analyserCodeField.selectAll();
            }
        });
    }

    private void codeValidator(){
        analyserCodeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("\\d*")){
                    analyserCodeField.setText(t1.replaceAll("\\D",""));
                }else {
                    if(t1.length() == 6){
                        WebTarget target = ClientBuilder.newClient().target(HOST+"/product/findByCode/"+t1);
                        GenericType<List<Product>> genericType = new GenericType<>(){};
                        List<Product> myList = target.request(MediaType.APPLICATION_JSON).get(genericType);
                        if(myList.size() == 0){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Invalid code");
                            Optional<ButtonType> optional1 = alert.showAndWait();
                            if(optional1.isPresent() && optional1.get() == ButtonType.OK)
                                alert.close();
                        }else {
                            analyserDescriptionField.setText(myList.get(myList.size()-1).getName());
                            analyserBarcode.setText(myList.get(myList.size()-1).getBarcode());
                            analyserSupplier.setText(myList.get(myList.size()-1).getSupplier().getIdentity());
                            analyserUserName.setText(myList.get(myList.size()-1).getCreatedBy());
                            analyserDate.setText(myList.get(myList.size()-1).getListDate()+"");
                            analyserPackField.setText(myList.get(myList.size()-1).getPackaging());
                            analyserDepartment.setText(myList.get(myList.size()-1).getDepartment());
                            analyserSubDepartment.setText(myList.get(myList.size()-1).getSubDepartment());

                            byte [] image = myList.get(myList.size()-1).getImage();
                            ByteArrayInputStream bais = new ByteArrayInputStream(image);
                            try {
                                BufferedImage bufferedImage = ImageIO.read(bais);
                                ImageIO.write(bufferedImage,"png",new File("C:\\System product images\\Photo.png"));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            analyserImageView.setImage(new Image("C:\\System product images\\Photo.png"));


                            //Checking if active or blocked for purchase
                            if (myList.get(myList.size()-1).getOrderStatus().equals("Active")){
                                analyserActive.setSelected(true);
                                analyserBlocked.setSelected(false);
                            }else {
                                analyserBlocked.setSelected(true);
                                analyserActive.setSelected(false);
                            }

                            //Checking if cost is open or fixed
                            if (myList.get(myList.size()-1).getCostStatus().equals("Fixed")){
                                analyserFixedCost.setSelected(true);
                                analyserOpenCost.setSelected(false);
                            }else {
                                analyserOpenCost.setSelected(true);
                                analyserFixedCost.setSelected(false);
                            }

                            //Fetching prices
                            target = ClientBuilder.newClient().target(HOST+"/prices/findByCode/"+t1);
                            GenericType<List<Prices>> gen = new GenericType<>(){};
                            List<Prices> pricesList = target.request(MediaType.APPLICATION_JSON).get(gen);
                            if(pricesList.size() > 0){
                                analyserCostInclusive.setText(pricesList.get(pricesList.size()-1).getInclusive()+"");
                                analyserVat.setText(pricesList.get(pricesList.size()-1).getVat()+"");
                                analyserCostExcl.setText(pricesList.get(pricesList.size()-1).getExcl()+"");
                                analyserSellingPrice.setText(pricesList.get(pricesList.size()-1).getSellingPrice()+"");
                                analyserMarkUp.setText(String.format("%,.2f",Double.parseDouble(analyserSellingPrice.getText()) -
                                        Double.parseDouble(analyserCostInclusive.getText())));

                            }else {
                                analyserCostInclusive.setText("");
                                analyserVat.setText("");
                                analyserCostExcl.setText("");
                                analyserSellingPrice.setText("");
                                analyserMarkUp.setText("");
                            }


                            //Fetching department
                            target = ClientBuilder.newClient().target(HOST+"/department/findAll");
                            GenericType<List<ProductDepartment>> gt = new GenericType<>(){};
                            List<ProductDepartment> departmentList = target.request(MediaType.APPLICATION_JSON).get(gt);

                        }

                    } else if (t1.length() > 6){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Invalid code");
                        Optional<ButtonType> optional1 = alert.showAndWait();
                        if(optional1.isPresent() && optional1.get() == ButtonType.OK)
                            alert.close();
                    }
                }
            }
        });
    }

    private void searchProduct(){
        analyserCodeField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode().equals(KeyCode.SHIFT)){
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("ProductAnalyzerCodeSearch.fxml"));
                    Stage stage = new Stage();
                    try {
                        Scene scene = new Scene(fxmlLoader.load());
                        stage.setScene(scene);
                        ProductAnalyzerCodeSearchController controller = fxmlLoader.getController();
                        controller.setCodeField(analyserCodeField);
                        stage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }




















}
