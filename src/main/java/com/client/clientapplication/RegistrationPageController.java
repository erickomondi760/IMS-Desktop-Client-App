package com.client.clientapplication;

import com.quickrest.entities.Groups;
import com.quickrest.entities.QuickUsers;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class RegistrationPageController implements Initializable {

    @FXML
    private Button register;
    @FXML
    private Button cancel;
    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;
    @FXML
    private AnchorPane regPane;
    @FXML
    private TextField groupUser;
    @FXML
    private ComboBox<String> groupName;



    private static final String  HOST = ApplicationPath.getHqPath();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void registration(ActionEvent e){

        QuickUsers users = new QuickUsers();
        users.setUserName(userName.getText());
        users.setPassword(password.getText());


        try(Client client = ClientBuilder.newClient()){
            //User registration
            WebTarget target = client.target(HOST + "/users/create");
            Response response = target.request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(users, MediaType.APPLICATION_JSON));
            System.out.println(response.getStatus()+"");

            if (response.getStatus() == 204) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Succesfully created a user");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK) {
                    alert.close();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Failed to create a user");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK) {
                    alert.close();
                }
            }

        }



    }

    @FXML
    public void addGroup(){
        Groups group = new Groups();
        group.setGroupName(groupName.getValue());
        group.setUserName(groupUser.getText());

        //Group registration
        try(Client client = ClientBuilder.newClient()){
            WebTarget target = client.target(HOST+"/groups/create");
            try (Response response1 = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(group, MediaType.APPLICATION_JSON))) {
                System.out.println(response1.getStatus() + "");
                if(response1.getStatus() == 204){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("User group successfully added");
                    Optional<ButtonType> optional = alert.showAndWait();
                    if (optional.isPresent() && optional.get() == ButtonType.OK){
                        alert.close();
                    }
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Failed to add group");
                    Optional<ButtonType> optional = alert.showAndWait();
                    if (optional.isPresent() && optional.get() == ButtonType.OK){
                        alert.close();
                    }
                }
            }
        }

    }

}
