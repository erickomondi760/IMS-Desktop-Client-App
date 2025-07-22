package com.client.clientapplication;

import com.quickrest.entities.CompanyDetails;
import com.quickrest.entities.Product;
import com.quickrest.entities.Supplier;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddSupplierController implements Initializable {
    @FXML
    private AnchorPane addSupplierAnchor;
    @FXML
    private TextArea addSupplierAddress;
    @FXML
    private Button addSupplierButton;
    @FXML
    private Label addSupplierContactLabel;
    @FXML
    private Label addSupplierDate;
    @FXML
    private TextField addSupplierEmail;
    @FXML
    private TextField addSupplierEmail1;
    @FXML
    private TextField addSupplierIdentity;
    @FXML
    private TextField addSupplierMobile;
    @FXML
    private TextField addSupplierMobile1;
    @FXML
    private TextArea addSupplierSupply;
    @FXML
    private TextField addSupplierCategoryText;

    String user;
    TableView<Supplier> tableView;
    TableView<Product> productTableView;
    Product product;
    Label label;
    private static final String  HOST = ApplicationPath.getHqPath();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //Accepting digits for the first phone number
        addSupplierMobile.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("\\d*")){
                    addSupplierMobile.setText(t1.replaceAll("[^\\d]",""));
                }
            }
        });
        //Accepting digits for the second phone number
        addSupplierMobile1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("\\d*")){
                    addSupplierMobile1.setText(t1.replaceAll("[^\\d]",""));
                }
            }
        });

        //Setting date
        addSupplierDate.setText(new Date()+"");

        //Disabling save button
        addSupplierButton.setDisable(true);

        //Enabling save button
        addSupplierEmail1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.isEmpty() || !t1.equals("")){
                    addSupplierButton.setDisable(false);
                }
            }
        });
    }

    @FXML
    public void addSupplier(){
        System.out.println("User = "+getUser());
        String emailRegex = "^[\\p{L}0-9!#$%&'*+/=?^_`{|}~-][\\p{L}0-9.!#$%&'*+/=?^_`{|}~-]{0,63}@[\\p{L}0-9-]+(?:\\.[\\p{L}0-9-]{2,7})*$";
        String phoneRegex = "^(0{1}[1|7][0-9]{8})$";
        String identity = addSupplierIdentity.getText();
        String products = addSupplierSupply.getText();
        String address = addSupplierAddress.getText();
        String mobile = addSupplierMobile.getText();
        String mobile1 = addSupplierMobile1.getText();
        String email = addSupplierEmail.getText();
        String email1 = addSupplierEmail1.getText();

        if(!email1.matches(emailRegex) || !email.matches(emailRegex)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Error in email address");
            alert.setContentText("Kindly input the correct email address");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get() == ButtonType.OK){
                alert.close();
            }
        }else if (!mobile.matches(phoneRegex) || !mobile1.matches(phoneRegex)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Error in mobile number");
            alert.setContentText("Kindly input the correct phone number");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get() == ButtonType.OK){
                alert.close();
            }
        }else {

            if(mobile1.isEmpty() || mobile.isEmpty() || email.isEmpty() || email1.equals("") || address.equals("") || products.equals("")
                    || identity.equals("") || addSupplierCategoryText.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Empty Fileds");
                alert.setContentText("All fields are needed");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK){
                    alert.close();
                }
            }else {
                URL url = null;
                try {
                    url = new URL(ApplicationPath.getApplicationPath());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                WebTarget target = ClientBuilder.newClient().target(HOST+"/company/findByBranchName/"+
                        url.getPath().split("/")[1]);

                GenericType<List<CompanyDetails>> g = new GenericType<List<CompanyDetails>>(){};
                List<CompanyDetails> company = target.request(MediaType.APPLICATION_JSON).get(g);

                if(company.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Company not found");
                    alert.setContentText("Register a company to proceed");
                    Optional<ButtonType> optional = alert.showAndWait();
                    if (optional.isPresent() && optional.get() == ButtonType.OK)
                        alert.close();
                }else {

                    Supplier supplier = new Supplier();
                    supplier.setIdentity(identity);
                    supplier.setSupply(products);
                    supplier.setAddress(address);
                    supplier.setPhoneNumber(mobile);
                    supplier.setAltPhoneNumber(mobile1);
                    supplier.setEmail(email);
                    supplier.setAltEmail(email1);
                    supplier.setDateListed(new Date());
                    supplier.setSupplierType(addSupplierCategoryText.getText());
                    supplier.setCompanyDetails(company.get(0));

                    target = ClientBuilder.newClient().target(HOST +"/supplier/create");
                    Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(supplier,
                            MediaType.APPLICATION_JSON));

                    if (response.getStatus() == 201) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("New supplier successfully added");
                        Optional<ButtonType> optional = alert.showAndWait();
                        if (optional.isPresent() && optional.get() == ButtonType.OK)
                            alert.close();

                        Stage stage = (Stage) addSupplierButton.getScene().getWindow();
                        stage.close();

                        getTableView().setItems(updateSuppliersTable());
                        getTableView().refresh();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Failed!");
                        Optional<ButtonType> optional = alert.showAndWait();

                        if (optional.isPresent() && optional.get() == ButtonType.OK)
                            alert.close();
                    }

                }
            }
        }


    }

    private ObservableList<Supplier> updateSuppliersTable(){
        WebTarget target = ClientBuilder.newClient().target(HOST+"/supplier/findAll");
        GenericType<List<Supplier>> g = new GenericType<List<Supplier>>(){};
        List<Supplier> s = target.request(MediaType.APPLICATION_JSON).get(g);
        return FXCollections.observableArrayList(s);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public TableView<Supplier> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<Supplier> tableView) {
        this.tableView = tableView;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

}
