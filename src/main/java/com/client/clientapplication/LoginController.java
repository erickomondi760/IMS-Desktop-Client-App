package com.client.clientapplication;

import com.quickrest.entities.QuickUsers;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import jakarta.ws.rs.client.Client;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class LoginController implements Initializable {

    @FXML
    private TextField loginUserName;
    @FXML
    private PasswordField loginPassword;
    @FXML
    private Button login;
    @FXML
    private Button cancel;
    @FXML
    private Button register;
    @FXML
    private AnchorPane loginPane;

    Stage stage = new Stage();

    private static final String  HOST = ApplicationPath.getHqPath();



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void openLoginWindow(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.stage.setScene(scene);
        this.stage.show();
    }

    @FXML
    public void login(){
        List<String> lis = new ArrayList<>();
        lis.add("Erick");


        if(loginUserName.getText().equals("") || loginPassword.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Enter username/password");

            Optional<ButtonType> optional = alert.showAndWait();
            if(optional.isPresent() && optional.get() == ButtonType.OK)
                alert.close();

        }else {
            try {
                WebTarget webTarget = null;
                Client client = ClientBuilder.newClient();
                webTarget = client.target(HOST+"/users/findAll");
                webTarget.request(MediaType.APPLICATION_JSON).get(new GenericType<List<QuickUsers>>(){});

                GenericType<List<QuickUsers>> list = new GenericType<>() {};
                List<QuickUsers> users = webTarget.request("application/json").get(list);

                boolean finder = true;
                do{

                    for(int i=0; i<users.size(); i++){
                        QuickUsers q = users.get(i);
                        String name = loginUserName.getText();
                        String pass = loginPassword.getText();

                        if(q.getUserName().equals(name) && q.getPassword().equals(pass)){
                            try {

                                //Storing logged in user
                                ApplicationPath.setUser(name);

                                Stage stage = new Stage();
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("Home.fxml"));
                                Parent root = loader.load();
                                Scene scene = new Scene(root);
                                stage.setScene(scene);
                                stage.setMaximized(true);
                                stage.show();
                                HomeController controller = loader.getController();
                                controller.getUser(name);

                                Stage stage1 = (Stage) login.getScene().getWindow();
                                stage1.close();

                                finder = false;


                            } catch (IOException e1) {
                                throw new RuntimeException(e1);
                            }

                        }

                    }
                    if(finder){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Login Error : Username/Password is incorrect!");

                        Optional<ButtonType> optional = alert.showAndWait();
                        if(optional.isPresent() && optional.get() == ButtonType.OK)
                            alert.close();

                        return;
                    }

                }while (finder);


            }catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Network Error:Unable to connect to the server!!! Check your connection and try again");

                Optional<ButtonType> optional = alert.showAndWait();
                if(optional.isPresent() && optional.get() == ButtonType.OK)
                    alert.close();
            }

        }

    }

    @FXML
    public void cancelValues() throws SQLException {
        loginUserName.setText("");
        loginPassword.setText("");
    }




}
