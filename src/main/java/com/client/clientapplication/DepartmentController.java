package com.client.clientapplication;

import com.quickrest.entities.ProductDepartment;
import com.quickrest.entities.ProductSubDepartment;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentController  implements Initializable {
    @FXML
    private TableView<ProductSubDepartment> departmentTable;
    @FXML
    private ComboBox<String> departmentComboBox;
    @FXML
    private Label recordsLabel;
    @FXML
    private Button dptPageButton;

    private String userName;
    private static final String  HOST = ApplicationPath.getHqPath();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Fetching departments
        ObservableList<String> productDepartments = FXCollections.observableArrayList();
        productDepartments.add("All");
        WebTarget target = ClientBuilder.newClient().target(HOST+"/department/findAll");
        GenericType<List<ProductDepartment>> gt = new GenericType<>(){};
        List<ProductDepartment> departmentList = target.request(MediaType.APPLICATION_JSON).get(gt);
        for (ProductDepartment productDepartment : departmentList) {
            productDepartments.add(productDepartment.getName());
        }
        departmentComboBox.setItems(productDepartments);

        //Fetching sub-departments
        target = ClientBuilder.newClient().target(HOST+"/subdepartment/findAll");
        GenericType<List<ProductSubDepartment>> g = new GenericType<>(){};
        List<ProductSubDepartment> subDepartmentsList = target.request(MediaType.APPLICATION_JSON).get(g);
        ObservableList<ProductSubDepartment> subDepartments = FXCollections.observableArrayList(subDepartmentsList);
        recordsLabel.setText(subDepartments.size()+"");

        //Retrieving sub depts
        showSubDepartments();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @FXML
    public void createDepartment(){
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("DepartmentalDialog.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);

            DepartmentalDialogController controller = fxmlLoader.getController();
            controller.setUserName(getUserName());
            controller.setTableView(departmentTable);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Fetch subdepartments
    public void showSubDepartments(){
        WebTarget target = ClientBuilder.newClient().target(HOST+"/subdepartment/findAll");
        GenericType<List<ProductSubDepartment>> gt = new GenericType<>(){};
        List<ProductSubDepartment> departmentList = target.request(MediaType.APPLICATION_JSON).get(gt);
        ObservableList<ProductSubDepartment> subDepartments = FXCollections.observableArrayList(departmentList);
        departmentTable.setItems(subDepartments);

    }

    @FXML
    public void showDepartments(){
        String dpt = departmentComboBox.getValue();
        if(dpt.equals("All")){
            WebTarget target = ClientBuilder.newClient().target(HOST+"/subdepartment/findAll");
            GenericType<List<ProductSubDepartment>> gt = new GenericType<>(){};
            List<ProductSubDepartment> departmentList = target.request(MediaType.APPLICATION_JSON).get(gt);
            ObservableList<ProductSubDepartment> subDepartments = FXCollections.observableArrayList(departmentList);
            departmentTable.setItems(subDepartments);
            recordsLabel.setText(subDepartments.size()+"");
        }else {
            WebTarget target = ClientBuilder.newClient().target(HOST+"/subdepartment/find/"+dpt);
            GenericType<List<ProductSubDepartment>> gt = new GenericType<>(){};
            List<ProductSubDepartment> departmentList = target.request(MediaType.APPLICATION_JSON).get(gt);
            ObservableList<ProductSubDepartment> subDepartments = FXCollections.observableArrayList(departmentList);
            departmentTable.setItems(subDepartments);
            recordsLabel.setText(subDepartments.size()+"");
        }
    }
    @FXML
    public void subDepartmentsPage(ActionEvent actionEvent){
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Department.fxml"));
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
