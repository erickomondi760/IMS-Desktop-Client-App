package com.client.clientapplication;

import com.quickrest.entities.ProductDepartment;
import com.quickrest.entities.ProductSubDepartment;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
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
import javafx.stage.Stage;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SubDepartmentController implements Initializable {
    @FXML
    private ComboBox<String> subDeptCombox;
    @FXML
    private TextField subDeptName;
    @FXML
    private TextField subDeptNumber;
    @FXML
    private TextField createdSubDepartment;
    @FXML
    private Button suDeptButton;

    private String userName;
    private TableView<ProductSubDepartment> tableView;
    private static final String  HOST = ApplicationPath.getHqPath();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        validateSubDeptNumber();
        validateSubDeptName();

        //Retrieving departments
        WebTarget target = ClientBuilder.newClient().target(HOST+"/department/findAll");
        GenericType<List<ProductDepartment>> gt = new GenericType<>(){};
        List<ProductDepartment> departmentList = target.request(MediaType.APPLICATION_JSON).get(gt);
        ObservableList<String> productDepartments = FXCollections.observableArrayList();
        for (ProductDepartment productDepartment : departmentList) {
            productDepartments.add(productDepartment.getName());
        }
        subDeptCombox.setItems(productDepartments);




    }

    //Validating sub dept number
    private void validateSubDeptNumber(){
        subDeptNumber.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.matches("\\d*")){
                    createdSubDepartment.setText(subDeptNumber.getText()+"-"+subDeptName.getText());
                }else {
                    subDeptNumber.setText(t1.replaceAll("[^\\d]",""));
                }
            }
        });
    }

    //Validating sub dept name
    private void validateSubDeptName(){
        subDeptName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.matches("\\D*")){
                    createdSubDepartment.setText(subDeptNumber.getText()+"-"+subDeptName.getText());
                }else {
                    subDeptName.setText(t1.replaceAll("[^\\D]",""));
                }
            }
        });
    }

    @FXML
    public void savingSubDepartment(){
        String name = subDeptName.getText();
        int code = Integer.parseInt(subDeptNumber.getText());

        if(!name.equals("") && code != 0){

            if(name.matches(".*\\p{Lower}.*")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Only Upper Case Accepted!");
                alert.setContentText("Kindly turn on caps lock.");
                Optional<ButtonType> optional = alert.showAndWait();
                if(optional.isPresent() && optional.get() == ButtonType.OK)
                    alert.close();
            }else {
                WebTarget target = ClientBuilder.newClient().target(HOST+"/subdepartment/findAll");
                GenericType<List<ProductSubDepartment>> gt = new GenericType<>(){};
                List<ProductSubDepartment> departmentList = target.request(MediaType.APPLICATION_JSON).get(gt);

                boolean status = true;
                for(ProductSubDepartment psd:departmentList){
                    if(psd.getCode() == code || name.equals(psd.getName()))
                        status = false;
                }

                if(status){
                    ProductSubDepartment psd = new ProductSubDepartment();
                    psd.setCode(code);
                    psd.setName(name);
                    psd.setDepartment(subDeptCombox.getValue());
                    psd.setUserName(getUserName());
                    psd.setDateCreated(new Date());

                    target = ClientBuilder.newClient().target(HOST+"/subdepartment/create");
                    Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(psd,MediaType.APPLICATION_JSON));
                    if(response.getStatus() == 204){
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("Success!");
                        alert.setContentText("Successfully created a new sub-department");
                        Optional<ButtonType> optional = alert.showAndWait();
                        if(optional.isPresent() && optional.get() == ButtonType.OK)
                            alert.close();
                        Stage stage = (Stage) suDeptButton.getScene().getWindow();
                        stage.close();
                        getTableView().setItems(retrievingDepartments());

                    }else {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("Failed!");
                        alert.setContentText("An error may have occurred, kindly retry");
                        Optional<ButtonType> optional = alert.showAndWait();
                        if(optional.isPresent() && optional.get() == ButtonType.OK)
                            alert.close();
                    }

                }else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Failed!!!");
                    alert.setContentText("Sub-department code or name already exists");
                    Optional<ButtonType> optional = alert.showAndWait();
                    if(optional.isPresent() && optional.get() == ButtonType.OK)
                        alert.close();
                }

            }

        }


    }

    private ObservableList<ProductSubDepartment> retrievingDepartments(){
        WebTarget target = ClientBuilder.newClient().target(HOST+"/subdepartment/findAll");
        GenericType<List<ProductSubDepartment>> gt = new GenericType<>(){};
        List<ProductSubDepartment> departmentList = target.request(MediaType.APPLICATION_JSON).get(gt);
        return FXCollections.observableArrayList(departmentList);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public TableView<ProductSubDepartment> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<ProductSubDepartment> tableView) {
        this.tableView = tableView;
    }
}
