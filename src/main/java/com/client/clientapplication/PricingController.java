package com.client.clientapplication;

import com.quickrest.entities.Prices;
import com.quickrest.entities.Product;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PricingController implements Initializable {
    @FXML
    private TableView<Prices> priceTable;
    @FXML
    private AnchorPane pricingAnchor;
    @FXML
    private HBox priceHbox;
    @FXML
    private TextField price;
    @FXML
    private TextField subDepartment;
    @FXML
    private TextField packaging;
    @FXML
    private TextField code;
    @FXML
    private TextField barcode;
    @FXML
    private TextField supplier;
    @FXML
    private TextField desc;
    @FXML
    private TextField dept;
    @FXML
    private TextField excl;
    @FXML
    private TextField incl;
    @FXML
    private ComboBox<Integer> vat;
    @FXML
    private ComboBox<Integer> disc;
    @FXML
    private CheckBox changeVat;
    @FXML
    private Label date;
    @FXML
    private CheckBox discCheck;
    @FXML
    private Button save;
    @FXML
    private Button pricingShowAllPricesButton;
    @FXML
    private TextField userName;
    @FXML
    private TextField searchField;
    @FXML
    private CheckBox search;
    @FXML
    private TextField autoCompleteField;
    @FXML
    private ImageView pricingImageView;


    private static final String  HOST = ApplicationPath.getHqPath();
    List<Product> products = new ArrayList<>();
    List<Prices> prices = new ArrayList<>();
    private DateTimeFormatter dtf;
    private String unit;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dtf = DateTimeFormatter.ofPattern("MMMM d,yyyy");

        priceHbox.prefWidthProperty().bind(pricingAnchor.widthProperty());
        disc.setDisable(true);
        save.setDisable(true);
        vat.setDisable(true);
        searchField.setDisable(true);

        //Populating fields with data from the database
        code.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("\\d*" )){
                    code.setText(t1.replaceAll("[^\\d]",""));
                } else if (t1.length() >= 7) {
                    code.setDisable(true);
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Code does not exist");

                    Optional<ButtonType> optional = alert.showAndWait();
                    if(optional.isPresent() && optional.get() == ButtonType.OK){
                        code.setDisable(false);
                        supplier.setText("");
                        desc.setText("");
                        barcode.setText("");
                        dept.setText("");
                        price.setText("");
                        subDepartment.setText("");
                        packaging.setText("");
                        pricingImageView.setImage(new Image(""));
                        alert.close();
                    }
                }else {
                    String bc = null;
                    if(t1.length() == 6){
                        //Quering the database with products
                        WebTarget target = ClientBuilder.newClient().target(HOST+"/product/findByCode/"+t1);
                        GenericType<List<Product>> genericType = new GenericType<>(){};
                        products = target.request(MediaType.APPLICATION_JSON).get(genericType);

                        if(t1.equals(products.get(0).getCode())){
                            supplier.setText(products.get(0).getSupplierName());
                            desc.setText(products.get(0).getName());
                            barcode.setText(products.get(0).getBarcode());
                            dept.setText(products.get(0).getDepartment());
                            bc = barcode.getText();
                            subDepartment.setText(products.get(0).getSubDepartment());
                            packaging.setText(products.get(0).getPackaging());
                            unit = products.get(0).getUnit();

                            byte [] image = products.get(0).getImage();
                            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(image);
                            try {
                                BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
                                ImageIO.write(bufferedImage,"png",new File("C:\\System product images\\Photo.png"));
                                pricingImageView.setImage(new Image("C:\\System product images\\Photo.png"));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    if (t1.length() == 6 && bc == null){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Code does not exist");
                        Optional<ButtonType> optional = alert.showAndWait();
                        if(optional.isPresent() && optional.get() == ButtonType.OK){
                            supplier.setText("");
                            desc.setText("");
                            barcode.setText("");
                            dept.setText("");
                            subDepartment.setText("");
                            packaging.setText("");
                            alert.close();
                        }
                    }
                }
            }
        });

        //highlighting text in the textfield
        code.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                code.selectAll();
            }
        });

        //Accepting didgits for an exclusive price
        excl.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("\\d*")){
                    excl.setText(t1.replaceAll("[^\\d]",""));

                }else {
                    int tax = 100+vat.getValue();
                    if(t1.equals("")){
                        String newVal = t1.replaceAll(".*","0");
                        double inclusive = (double) (tax * Integer.parseInt(newVal)) /100;
                        incl.setText(String.valueOf(inclusive));
                        price.setText(String.format("%,.2f",Double.parseDouble(incl.getText()) * 1.15));

                    }else {
                        double inclusive = (double) (tax * Integer.parseInt(t1)) /100;
                        incl.setText(String.format("%,.2f",inclusive));
                        String priceInclusive = incl.getText();
                        priceInclusive = priceInclusive.replaceAll(",","");
                        price.setText(String.format("%,.2f",Double.parseDouble(priceInclusive) * 1.15));
                    }

                }
            }
        });


        //Calculating inclusive cost
        vat.setOnAction(e->{
            incl.setText(String.valueOf(vat.getValue()*Integer.parseInt(excl.getText())));
        });

        //Enabling change of VAT
        changeVat.setOnAction(e->{
            if(vat.isDisable()){
                vat.setDisable(false);
                excl.setText("0");
                excl.setDisable(true);
            }else {
                vat.setDisable(true);
                excl.setDisable(false);
            }
        });

        //Selecting text in the exclusice price textfield
        excl.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                excl.selectAll();
            }
        });

        //Enabling save button
        incl.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.equals("0.0")){
                    save.setDisable(false);
                }else {
                    save.setDisable(true);
                }

            }
        });

        //Updating prices table
        pricingShowAllPricesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                updatePrcices();
            }
        });

        List<String> stringList = new ArrayList<>();
        for(Prices p:prices){
            stringList.add(p.getDescription());
        }

    }

    @FXML
    public void allowDiscount(){
        if(discCheck.isSelected()){
            disc.setDisable(false);
        }else {
            disc.setDisable(true);

        }

    }
    @FXML
    public void savePrices(){

        if(code.getText().equals("") || code.getText().length() < 6 || dept.getText().equals("") || desc.getText().equals("") || barcode.getText().equals("")||
         supplier.getText().equals("") || excl.getText().equals("") || excl.getText().equals("0") || incl.getText().equals("") ||
        subDepartment.getText().equals("") || packaging.getText().equals("")){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Fill in all the fields appropriately");

            Optional<ButtonType> optional = alert.showAndWait();
            if(optional.isPresent() && optional.get() == ButtonType.OK)
                alert.close();

        } else if(!(discCheck.isSelected()) && disc.getValue() != 0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Kindly set the discount to zero if no discount is needed");

            Optional<ButtonType> optional = alert.showAndWait();
            if(optional.isPresent() && optional.get() == ButtonType.OK)
                alert.close();

        } else {
            Prices prices = new Prices();
            prices.setCode(code.getText());
            prices.setUnit(products.get(0).getUnit());
            prices.setBarcode(barcode.getText());
            prices.setDescription(desc.getText());
            prices.setUnit(unit);
            prices.setDept(dept.getText());
            prices.setSupplier(supplier.getText());
            prices.setExcl(Double.parseDouble(excl.getText()));
            prices.setVat(vat.getValue());
            prices.setPackaging(packaging.getText());
            prices.setSubDepartment(subDepartment.getText());
            prices.setSellingPrice(Double.parseDouble(price.getText().replaceAll(",","")));
            prices.setProduct(products.get(0));
            if(disc.getValue() > 0){
                double discount = disc.getValue();
                discount = (100 - discount)/100;
                double inclusive = discount*Double.parseDouble(incl.getText());
                prices.setDisc(disc.getValue());
                prices.setInclusive(inclusive);
            }else {
                prices.setDisc(0);
                prices.setInclusive(Double.parseDouble(incl.getText().replaceAll(",","")));
            }
            Date date1 = new Date();
            prices.setDate(date1);
            prices.setUser(userName.getText());

            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("PriceConfirmation.fxml"));
            try {
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.show();
                PriceConfirmationController controller = loader.getController();
                controller.setValues(code.getText(),desc.getText(),supplier.getText());
                controller.setCostValues(excl.getText(),String.valueOf(vat.getValue()),String.valueOf(disc.getValue()),
                        String.valueOf(prices.getInclusive()),String.valueOf(price.getText()));
                controller.setPrices(prices,stage,priceTable);
                controller.setUnitAndPacksize(prices.getUnit(),prices.getPackaging());
                code.setText("");
                excl.setText("");
                search.setSelected(false);
                searchField.setDisable(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void populateUserField(String name){
        userName.setText(name);
    }

    private List<Prices> updatePrcices(){
      WebTarget target1 = ClientBuilder.newClient().target(HOST+"/prices/findAll");
      List<Prices> list = target1.request(MediaType.APPLICATION_JSON).get(new GenericType<List<Prices>>(){});
      prices = list;
      ObservableList<Prices> ol = FXCollections.observableArrayList(list);
      priceTable.setItems(ol);
      return prices;
    }
    @FXML
    public void searchProductPrice(){
        if(search.isSelected()){
            searchField.setDisable(false);
            WebTarget target1 = ClientBuilder.newClient().target(HOST+"/prices/findAll");
            List<Prices> list = target1.request(MediaType.APPLICATION_JSON).get(new GenericType<List<Prices>>(){});
            ObservableList<Prices> pricesObservableList = FXCollections.observableArrayList(list);

            FilteredList<Prices> filteredList = new FilteredList<>(pricesObservableList, prices -> true);
            searchField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    filteredList.setPredicate(prices1 -> {
                        //If there is no search value,display all prices
                        if(t1.isEmpty() || t1.isBlank()){
                            return true;
                        }
                        if(prices1.getCode().contains(t1)){
                            return true;
                        }else if(prices1.getSupplier().contains(t1.toUpperCase())){
                            return true;
                        }else if(prices1.getDescription().toLowerCase().contains(t1.toUpperCase())){
                            return true;
                        } else if (prices1.getDate().toString().contains(t1.toUpperCase())) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
            });
            SortedList<Prices> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(priceTable.comparatorProperty());
            priceTable.setItems(sortedList);

        }else {
            searchField.setDisable(true);
        }
    }
}
