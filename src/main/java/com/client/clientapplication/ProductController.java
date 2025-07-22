package com.client.clientapplication;

import com.quickrest.entities.Product;
import com.quickrest.entities.ProductDepartment;
import com.quickrest.entities.ProductSubDepartment;
import com.quickrest.entities.Supplier;
import com.quickrest.resources.ApplicationPath;
import jakarta.persistence.Enumerated;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.*;

public class ProductController implements Initializable {
    @FXML
    private ProgressBar barCodeProgressBar;
    @FXML
    private TextField newProductbarcode;
    @FXML
    private Button barcodeGenerator;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button editProductButton;
    @FXML
    private Button productButton;
    @FXML
    private AnchorPane productAnchor;
    @FXML
    private TextField newProductCode;
    @FXML
    private TextField newProductDescription;
    @FXML
    private AnchorPane productLeftAnchor;
    @FXML
    private TextField range;
    @FXML
    private CheckBox rangeSet;
    @FXML
    private ComboBox<String> newProductdepartment;
    @FXML
    private ComboBox<String> newProductSubDepartment;
    @FXML
    private Button newProductsaveButton;
    @FXML
    TextField newProductsupplier;
    @FXML
    private TableView<Product> newProductTable;
    @FXML
    private TextField newProductsearchField;
    @FXML
    private CheckBox newProductsearchBox;
    @FXML
    private TextField newProductPackaging;
    @FXML
    private TextField newProductUnit;
    @FXML
    private Label newProductRecords;
    @FXML
    private CheckBox newProductFixedCost;
    @FXML
    private RadioButton active;
    @FXML
    private RadioButton blocked;
    @FXML
    private Label newProductImage;
    @FXML
    ImageView newProductImageView;
    @FXML
    private CheckBox productShowSkusCheck;
    @FXML
    private Button addProductSupplierSearchButton;




    private static final String foodDept = "Food";
    private static final String homeCareDept = "Home care";
    private static final String personalCareDept = "Personal care";
    private static final String houseHoldDept = "House hold";
    private static final String electronicDept = "Electronic";
    private String code;
    private String userName;
    private static final String  HOST = ApplicationPath.getHqPath();
    Client client = ClientBuilder.newClient();
    WebTarget target;
    List<Product> newProduct = new ArrayList<>();
    String imagePath;
    private Supplier supplier;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        progressBar.setVisible(false);
        barCodeProgressBar.setVisible(false);
        newProductsaveButton.setDisable(true);
        newProductsearchField.setDisable(true);

        //Finding departments & sub-departments
        target = ClientBuilder.newClient().target(HOST+"/department/findAll");
        GenericType<List<ProductDepartment>> dep = new GenericType<>(){};
        List<ProductDepartment> departmentList = target.request(MediaType.APPLICATION_JSON).get(dep);
        ObservableList<String> dl = FXCollections.observableArrayList();
        departmentList.forEach(e->{
            dl.add(e.getName());
        });
        newProductdepartment.setItems(dl);

        newProductdepartment.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1==null){
                    newProductSubDepartment.setItems(FXCollections.observableArrayList());
                }else {
                    WebTarget target = ClientBuilder.newClient().target(HOST+"/subdepartment/find/"+t1);
                    GenericType<List<ProductSubDepartment>> subDep = new GenericType<>(){};
                    List<ProductSubDepartment> subDepartmentList = target.request(MediaType.APPLICATION_JSON).get(subDep);
                    ObservableList<String> subDepList = FXCollections.observableArrayList();
                    subDepartmentList.forEach(e->{
                        subDepList.add(e.getName());
                    });
                    newProductSubDepartment.setItems(subDepList);
                }
            }
        });

        //Popping supplier search table on key press
        addProductSupplierSearchButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showSupplierSearchTable();
            }
        });

    }
    @FXML
    public void generateCode(){
        SecureRandom random = new SecureRandom();
        code = String.format("%04d",random.nextInt(9999));

        if(newProductdepartment.getValue() == null || newProductDescription.getText().equals("")|| newProductsupplier.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Kindly fill all the fields to proceed!");
            Optional<ButtonType> optional = alert.showAndWait();

            if(optional.isPresent() && optional.get() == ButtonType.OK)
                alert.close();
        }else {
            if(newProductdepartment.getValue().equalsIgnoreCase(foodDept)){
                code = 10+code;
                System.out.println(code);
            } else if (newProductdepartment.getValue().equalsIgnoreCase(homeCareDept)) {
                code = 20+code;
            }else if (newProductdepartment.getValue().equalsIgnoreCase(personalCareDept)) {
                code = 30+code;
            }else if (newProductdepartment.getValue().equalsIgnoreCase(houseHoldDept)) {
                code = 40+code;
            }else{
                code = 20+code;
            }

            List<String> list = new ArrayList<>();
            list.add(code);
            Task<ObservableList<String>> task = new Task<ObservableList<String>>() {
                @Override
                protected ObservableList<String> call() throws Exception {
                    return FXCollections.observableList(list);
                }
            };
            progressBar.progressProperty().bind(task.progressProperty());
            progressBar.setVisible(true);
            newProductCode.setText(list.get(0));

            task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    progressBar.setVisible(false);
                }
            });
            task.setOnFailed(e->progressBar.setVisible(true));
            new Thread(task).start();
        }


    }
    @FXML
    public void generateBarCode(){
        if(newProductdepartment.getValue() == null || newProductDescription.getText().equals("")
                || newProductSubDepartment.getValue() == null || newProductDescription.getText().equals("")
                || newProductCode.getText().equals("") || newProductsupplier.getText().equals("")){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Kindly fill in all the fields to proceed!");
            Optional<ButtonType> optional = alert.showAndWait();

            if(optional.isPresent() && optional.get() == ButtonType.OK)
                alert.close();
        }else {
            SecureRandom secureRandom = new SecureRandom();
            String code = String.format("%09d",secureRandom.nextInt(999999999));
            code = ("6161"+code);

            List<String> list = new ArrayList<>();
            list.add(code.trim());
            Task<ObservableList<String>> task = new Task<ObservableList<String>>() {
                @Override
                protected ObservableList<String> call() throws Exception {
                    return FXCollections.observableList(list);
                }
            };
            barCodeProgressBar.progressProperty().bind(task.progressProperty());
            barCodeProgressBar.setVisible(true);
            task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    barCodeProgressBar.setVisible(false);
                }
            });
            new Thread(task).start();
            newProductbarcode.setText(list.get(0));
            newProductsaveButton.setDisable(false);



        }

    }
    @FXML
    public void saveProduct(){
        if(newProductdepartment.getValue() == null || newProductDescription.getText().equals("") ||
                newProductDescription.getText().equals("") || newProductSubDepartment.getValue() == null
                || newProductCode.getText().equals("") || newProductbarcode.getText().equals("") ||
                newProductsupplier.getText().equals("") || newProductSubDepartment.getValue().equals("")
                || newProductUnit.getText().equals("") || newProductPackaging.getText().equals("") || getImagePath() == null){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Kindly fill in all the fields to proceed!");
            Optional<ButtonType> optional = alert.showAndWait();

            if(optional.isPresent() && optional.get() == ButtonType.OK)
                alert.close();
        }else{

            String costStatus = null;
            String codeStatus = null;

            if(newProductFixedCost.isSelected()){
                costStatus = "Fixed";
            }else {
                costStatus = "Open";
            }

            if(active.isSelected()){
                codeStatus = "Active";
            }

            if(blocked.isSelected()){
                codeStatus = "Blocked";
            }

            Product product = new Product();
            product.setDepartment(newProductdepartment.getValue());

            String name = newProductDescription.getText();
            String supplier = newProductsupplier.getText();
            String pack = newProductPackaging.getText();
            String unit = newProductUnit.getText();

            if(!name.matches(".*\\p{Lower}.*") && !supplier.matches(".*\\p{Lower}.*") &&
            !pack.matches(".*\\p{Lower}.*") || unit.matches(".*\\p{Lower}.*")){
                product.setName(newProductDescription.getText());
                product.setCode(newProductCode.getText());
                product.setBarcode(newProductbarcode.getText());

                product.setPackaging(newProductPackaging.getText());
                product.setUnit(newProductUnit.getText());
                product.setSubDepartment(newProductSubDepartment.getValue());
                product.setProductionDate(new Date());
                product.setExpiryDate(new Date());
                product.setCostStatus(costStatus);
                product.setBatchNumber(0);
                product.setOrderStatus(codeStatus);
                product.setListDate(new Date());
                product.setCreatedBy(getUserName());
                product.setSupplierName(supplier);

                newProduct.add(product);

                List<Product> list = checkAllProducts(product.getCode());

                boolean status = true;

                if(list.size() > 0) {
                    for (Product product1 : list) {

                        if (product1.getName().equals(product.getName()) || product1.getCode().equals(product.getCode())
                                || product1.getBarcode().equals(product.getBarcode())) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setContentText("Description/Code/Barcode already exists. Kindly verify your data to proceede");
                            Optional<ButtonType> optional = alert.showAndWait();
                            if (optional.isPresent() && optional.get() == ButtonType.OK)
                                alert.close();
                            status = false;
                        }
                    }

                    if (status) {
                        newProductbarcode.setText("");
                        newProductCode.setText("");

                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("SKU Verification Successful");
                        alert.setContentText("SKUS has been verified, kindly proceed");
                        Optional<ButtonType> optional = alert.showAndWait();
                        if (optional.isPresent() && optional.get() == ButtonType.OK)
                            alert.close();

                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader();
                        try {
                            loader.setLocation(getClass().getResource("ProductConfirmationPage.fxml"));
                            Scene scene = new Scene(loader.load());
                            stage.setScene(scene);

                            ProductConfirmationPageController controller = loader.getController();
                            controller.getProductConfirmationBarcode().setText(newProductbarcode.getText());
                            controller.getProductConfirmationCode().setText(newProductCode.getText());
                            controller.getProductConfirmationDesc().setText(newProductDescription.getText());
                            controller.getProductConfirmationUnit().setText(newProductPackaging.getText());
                            controller.setDept(newProductdepartment.getValue());
                            controller.setSubDept(newProductSubDepartment.getValue());
                            controller.setCodeStatus(codeStatus);
                            controller.setCostStatus(costStatus);
                            controller.getProductConfirmationSupplier().setText(newProductsupplier.getText());
                            controller.setTableView(newProductTable);
                            controller.setUserName(getUserName());
                            controller.setProductConfirmationSupplier(newProductsupplier);
                            controller.setProductConfirmationDesc(newProductDescription);
                            controller.setLabel(newProductRecords);
                            controller.getProductConfImageView().setImage(new Image(getImagePath()));
                            controller.setPath(getImagePath());
                            controller.setSupplierName(newProductsupplier.getText());
                            controller.getProductConfirmationPackaging().setText(newProductPackaging.getText());

                            stage.show();

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }else {
                    Stage stage = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    try {
                        loader.setLocation(getClass().getResource("ProductConfirmationPage.fxml"));
                        Scene scene = new Scene(loader.load());
                        stage.setScene(scene);

                        ProductConfirmationPageController controller = loader.getController();
                        controller.getProductConfirmationBarcode().setText(newProductbarcode.getText());
                        controller.getProductConfirmationCode().setText(newProductCode.getText());
                        controller.getProductConfirmationDesc().setText(newProductDescription.getText());
                        controller.getProductConfirmationUnit().setText(newProductUnit.getText());
                        controller.getProductConfirmationPackaging().setText(newProductPackaging.getText());
                        controller.setDept(newProductdepartment.getValue());
                        controller.setSubDept(newProductSubDepartment.getValue());
                        controller.setCodeStatus(codeStatus);
                        controller.setCostStatus(costStatus);
                        controller.getProductConfirmationSupplier().setText(newProductsupplier.getText());
                        controller.setTableView(newProductTable);
                        controller.setProduct(newProduct.get(0));
                        controller.setLabel(newProductRecords);
                        controller.setProductConfirmationSupplier(newProductsupplier);
                        controller.setProductConfirmationDesc(newProductDescription);
                        controller.getProductConfImageView().setImage(new Image(getImagePath()));
                        controller.setPath(getImagePath());
                        controller.setUserName(getUserName());
                        controller.setSupplierName(newProductsupplier.getText());

                        stage.show();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }else {
                newProductsupplier.selectAll();
                newProductDescription.selectAll();
                newProductPackaging.selectAll();
                newProductUnit.selectAll();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Only upper case input is expected for Supplier/Description & Packaing");
                alert.setContentText("Kindly turn on caps lock to proceed");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK){
                    alert.close();
                }

            }


        }
    }

    private List<Product> checkAllProducts(String code){
        WebTarget webTarget = ClientBuilder.newClient().target(HOST+"/product/findByCode/"+code);
        GenericType<List<Product>> list = new GenericType<List<Product>>(){};
        return webTarget.request(MediaType.APPLICATION_JSON).get(list);
    }


    //Searching a product
    public void searchAProduct(){
        if(newProductsearchBox.isSelected()){
            newProductsearchField.setDisable(false);
            WebTarget target1 = ClientBuilder.newClient().target(HOST+"/product/findAll");
            GenericType<List<Product>> genericType = new GenericType<>(){};

            List<Product> productList = target1.request(MediaType.APPLICATION_JSON).get(genericType);
            if(productList != null){
                ObservableList<Product> products = FXCollections.observableArrayList(productList);

                FilteredList<Product> productsFilteredList = new FilteredList<>(products, p->true);
                newProductsearchField.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                        productsFilteredList.setPredicate(product -> {
                            if(t1.isBlank() || t1.isEmpty()){
                                return true;
                            }else if(product.getCode().toLowerCase().contains(t1)){
                                return true;
                            } else if (product.getName().contains(t1.toUpperCase())) {
                                return true;
                            }else if (product.getDepartment().contains(t1.toUpperCase())) {
                                return true;
                            }else return product.getSupplier().getIdentity().contains(t1.toUpperCase());
                        });
                    }
                });
                SortedList<Product> productsSortedList = new SortedList<>(productsFilteredList);
                productsSortedList.comparatorProperty().bind(newProductTable.comparatorProperty());
                newProductTable.setItems(productsSortedList);
            }else {
                newProductsearchField.setDisable(true);
            }

        }

    }

    @FXML
    public void searchImage(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open file");
        File file = chooser.showOpenDialog(new Stage());
        if(file != null){
            String path = file.getPath();
            Image image = new Image(path);
            newProductImageView.setImage(image);
            setImagePath(path);
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Information Dialog");
            alert.setHeaderText("Please select a file");
            alert.setContentText("Kindly select a file proceed");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get() == ButtonType.OK)
                alert.close();
        }
    }

    //Accepting only upper case for supplier field
    private void convertSupplierToUpperCase(){
        newProductsupplier.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches(".*\\p{Lu}\\p{Punct}*\\s*")){

                }
            }
        });
    }

    @FXML
    public void showAllProducts(){
        new Thread(){
            @Override
            public void run(){
                WebTarget target = ClientBuilder.newClient().target(HOST+"/product/findAll");
                GenericType<List<Product>> gt = new GenericType<>(){};
                List<Product> list = target.request(MediaType.APPLICATION_JSON).get(gt);

                if(list == null){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Request Timeout");
                    alert.setContentText("The server took too long to respond, kindly retry");
                    Optional<ButtonType> optional = alert.showAndWait();
                    if (optional.isPresent() && optional.get() == ButtonType.OK)
                        alert.close();
                }else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ObservableList<Product> ol = FXCollections.observableArrayList(list);
                            newProductTable.setItems(ol);
                            newProductRecords.setText(String.format("%,d",list.size()));
                            newProductTable.refresh();
                        }
                    });

                }
            }
        }.start();


    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    private void showSupplierSearchTable(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("ProductSupplierSearch.fxml"));
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
            ProductSupplierSearchController controller = fxmlLoader.getController();
            controller.setSupplier(newProductsupplier);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}









