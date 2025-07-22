package com.client.clientapplication;

import com.quickrest.entities.Product;
import com.quickrest.entities.Supplier;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductConfirmationPageController implements Initializable {


    @FXML
    private AnchorPane productConfirmationAnchor;

    @FXML
    private TextField productConfirmationBarcode;

    @FXML
    private Button productConfirmationCancel;

    @FXML
    private TextField productConfirmationCode;

    @FXML
    private TextField productConfirmationDept;

    @FXML
    private TextField productConfirmationDesc;

    @FXML
    private TextField productConfirmationFixedCost;

    @FXML
    private Button productConfirmationSave;

    @FXML
    private TextField productConfirmationStatus;

    @FXML
    private TextField productConfirmationSubDept;

    @FXML
    private TextField productConfirmationSupplier;

    @FXML
    private TextField productConfirmationUnit;
    @FXML
    private ImageView productConfImageView;
    @FXML
    private Label productConfirmationImageLabel;

    @FXML
    private TextField productConfirmationPackaging;

    private TableView<Product> tableView;
    private String dept;
    private String subDept;
    private String costStatus;
    private String codeStatus;
    private Product product;
    private String supplierName;
    String userName;
    Label label;
    String path;
    private static final String HOST = ApplicationPath.getHqPath();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public TextField getProductConfirmationBarcode() {
        return productConfirmationBarcode;
    }

    public void setProductConfirmationBarcode(TextField productConfirmationBarcode) {
        this.productConfirmationBarcode = productConfirmationBarcode;
    }

    public TextField getProductConfirmationCode() {
        return productConfirmationCode;
    }


    public TextField getProductConfirmationDesc() {
        return productConfirmationDesc;
    }

    public void setCostStatus(String costStatus) {
        this.costStatus = costStatus;
        productConfirmationFixedCost.setText(costStatus);
    }

    public void setCodeStatus(String codeStatus) {
        this.codeStatus = codeStatus;
        productConfirmationStatus.setText(codeStatus);
    }

    public TextField getProductConfirmationSupplier() {
        return productConfirmationSupplier;
    }

    public void setProductConfirmationSupplier(TextField productConfirmationSupplier) {
        this.productConfirmationSupplier = productConfirmationSupplier;
    }

    public TableView<Product> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<Product> tableView) {
        this.tableView = tableView;
    }

    public void setDept(String dept) {
        this.dept = dept;
        productConfirmationDept.setText(dept);
    }

    public void setSubDept(String subDept) {
        this.subDept = subDept;
        productConfirmationSubDept.setText(subDept);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setProductConfirmationDesc(TextField productConfirmationDesc) {
        this.productConfirmationDesc = productConfirmationDesc;
    }

    public TextField getProductConfirmationPackaging() {
        return productConfirmationPackaging;
    }

    public void setProductConfirmationPackaging(TextField productConfirmationPackaging) {
        this.productConfirmationPackaging = productConfirmationPackaging;
    }

    public TextField getProductConfirmationUnit() {
        return productConfirmationUnit;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public ImageView getProductConfImageView() {
        return productConfImageView;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @FXML
    public void saveProduct() {
        Product product2 = new Product();
        product2.setDepartment(productConfirmationDept.getText());
        product2.setName(productConfirmationDesc.getText());
        product2.setCode(productConfirmationCode.getText());
        product2.setBarcode(productConfirmationBarcode.getText());
        product2.setUnit(productConfirmationUnit.getText());
        product2.setSubDepartment(productConfirmationSubDept.getText());
        product2.setProductionDate(new Date());
        product2.setExpiryDate(new Date());
        product2.setCostStatus(productConfirmationFixedCost.getText());
        product2.setBatchNumber(0);
        product2.setOrderStatus(productConfirmationStatus.getText());
        product2.setListDate(new Date());
        product2.setCreatedBy(getUserName());
        product2.setPackaging(productConfirmationPackaging.getText());
        product2.setSupplierName(getSupplierName());

        try {
            BufferedImage bufferedImage = ImageIO.read(new File(getPath()));
            if (bufferedImage == null) {
            } else {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                product2.setImage(imageBytes);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        WebTarget target = ClientBuilder.newClient().target(HOST + "/product/create");
        Response response1 = target.request(MediaType.APPLICATION_JSON).
                post(Entity.entity(product2, MediaType.APPLICATION_JSON));

        if (response1.getStatus() == 200) {
            Alert alert4 = new Alert(Alert.AlertType.CONFIRMATION);
            alert4.setContentText("A New sku successfully added");
            Optional<ButtonType> optional4 = alert4.showAndWait();
            if (optional4.isPresent() && optional4.get() == ButtonType.OK)
                alert4.close();

            getTableView().setItems(updateProductsTable(response1));
            getTableView().refresh();

            Stage s = (Stage) productConfirmationSave.getScene().getWindow();
            s.close();
            getProductConfirmationDesc().setText("");
            label.setText(getTableView().getItems().size() + "");

        } else {
            Alert alert5 = new Alert(Alert.AlertType.WARNING);
            alert5.setContentText("Failed to add a product!");
            Optional<ButtonType> optional5 = alert5.showAndWait();

            if (optional5.isPresent() && optional5.get() == ButtonType.OK)
                alert5.close();
        }


    }

    @FXML
    public void cancelOperaion() {
        Stage s = (Stage) productConfirmationSave.getScene().getWindow();
        s.close();
    }

    private ObservableList<Product> updateProductsTable(Response response) {
        Product p = response.readEntity(Product.class);
        ObservableList<Product> ol = FXCollections.observableArrayList();
        ol.add(p);
        return ol;
    }

}
