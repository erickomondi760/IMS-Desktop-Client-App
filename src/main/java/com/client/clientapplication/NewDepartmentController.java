package com.client.clientapplication;

import com.quickrest.entities.ProductDepartment;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewDepartmentController implements Initializable {


    @FXML
    private TextField newDeptName;
    @FXML
    private TextField newDeptNumber;
    @FXML
    private TextField createdDepartment;
    @FXML
    private Button newDeptSaveButton;

    private String  userName;
    private static final String  HOST = ApplicationPath.getHqPath();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //Validating dpt number
        validatingDeptNumber();

        //Disabling save button
        newDeptName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.equals("") || t1.matches("\\D*")){
                    createdDepartment.setText(newDeptNumber.getText() + "-" +newDeptName.getText());
                }
            }
        });
    }

    private void validatingDeptNumber(){
        newDeptNumber.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("\\d*")){
                    newDeptName.setText("");
                    newDeptNumber.setText(t1.replaceAll("[^\\d]",""));
                }else {
                    createdDepartment.setText(newDeptNumber.getText() + "-" +newDeptName.getText());
                }
            }
        });
    }

    @FXML
    public void savingDepartment(){
        String name = newDeptName.getText();
        int code = Integer.parseInt(newDeptNumber.getText());

        if(!name.equals("") && code != 0){

            if(name.matches(".*\\p{Lower}.*")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Only Upper Case Accepted!");
                alert.setContentText("Kindly turn on caps lock.");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK)
                    alert.close();
            }else {
                //Fetching departments
                WebTarget target = ClientBuilder.newClient().target(HOST+"/department/findAll");
                GenericType<List<ProductDepartment>> gt = new GenericType<>(){};
                List<ProductDepartment> departmentList = target.request(MediaType.APPLICATION_JSON).get(gt);

                boolean status = true;
                for(ProductDepartment pd:departmentList){
                    if(code == pd.getCode() || pd.getName().equals(name))
                        status = false;
                }
                if(status){
                    ProductDepartment pd = new ProductDepartment();
                    pd.setCode(code);
                    pd.setName(name);
                    pd.setUserName(getUserName());
                    pd.setDateCreated(new Date());

                    target = ClientBuilder.newClient().target(HOST+"/department/create");
                    Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(pd,MediaType.APPLICATION_JSON));
                    if(response.getStatus() == 204){
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("Success!");
                        alert.setContentText("Successfully created a new department");
                        Optional<ButtonType> optional = alert.showAndWait();
                        if(optional.isPresent() && optional.get() == ButtonType.OK)
                            alert.close();
                        Stage stage = (Stage) newDeptSaveButton.getScene().getWindow();
                        stage.close();
                    }
                }else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Failed!!!");
                    alert.setContentText("Department code or name already exists");
                    Optional<ButtonType> optional = alert.showAndWait();
                    if(optional.isPresent() && optional.get() == ButtonType.OK)
                        alert.close();
                }

            }

        }


    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
