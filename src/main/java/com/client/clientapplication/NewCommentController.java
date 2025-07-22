package com.client.clientapplication;

import com.quickrest.entities.CreditNoteComment;
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
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewCommentController implements Initializable {

    @FXML
    private TextField newCommentTextField;
    @FXML
    private Button newCommentAdditionButton;

    private String userName;
    private TableView<CreditNoteComment> myTable;
    private static final String  HOST = ApplicationPath.getHqPath();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Disabling save button
        newCommentAdditionButton.setDisable(true);

        //Enabling save button
        newCommentTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.isEmpty() || t1.isBlank()){
                    newCommentAdditionButton.setDisable(true);
                }else {
                    newCommentAdditionButton.setDisable(false);
                }
            }
        });
        //Saving a new comment
        newCommentAdditionButton.setOnAction(e->{
            CreditNoteComment comment = new CreditNoteComment();
            comment.setName(newCommentTextField.getText());
            comment.setUserName(getUserName());
            comment.setDateCreated(new Date());

            //Checking if the comment already exists
            WebTarget target = ClientBuilder.newClient().target(HOST+"/comment/findAll");
            GenericType<List<CreditNoteComment>> generic = new GenericType<>(){};
            List<CreditNoteComment> commentList = target.request(MediaType.APPLICATION_JSON).get(generic);

            boolean state = false;
            for(CreditNoteComment c:commentList){
                if(c.getName().equalsIgnoreCase(comment.getName())){
                    state = true;
                }
            }
            if(state){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Failed!");
                alert.setContentText("A comment with the identity already exists, kind create a new one.");
                Optional<ButtonType> optional1 = alert.showAndWait();
                if (optional1.isPresent() && optional1.get() == ButtonType.OK)
                    alert.close();
            }else {
                WebTarget webTarget = ClientBuilder.newClient().target(HOST + "/comment/create");
                Response response = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(comment, MediaType.APPLICATION_JSON));
                if (response.getStatus() == 204) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("A new comment successfully created");
                    Optional<ButtonType> optional1 = alert.showAndWait();
                    if (optional1.isPresent() && optional1.get() == ButtonType.OK)
                        alert.close();
                    else if(optional1.isPresent() && optional1.get() == ButtonType.CANCEL)
                        alert.close();

                    //Filling the table with created comment
                    webTarget = ClientBuilder.newClient().target(HOST + "/comment/findAll");
                    GenericType<List<CreditNoteComment>> genericType = new GenericType<>() {
                    };
                    List<CreditNoteComment> list = webTarget.request(MediaType.APPLICATION_JSON).get(genericType);
                    Task<ObservableList<CreditNoteComment>> task = new Task<ObservableList<CreditNoteComment>>() {
                        @Override
                        protected ObservableList<CreditNoteComment> call() throws Exception {
                            return FXCollections.observableArrayList(list);
                        }
                    };
                    new Thread(task).start();
                    myTable.itemsProperty().bind(task.valueProperty());

                    Stage stage1 = (Stage) newCommentAdditionButton.getScene().getWindow();
                    stage1.close();


                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Failed");
                    alert.setContentText("An error has occurred, comment not created!");
                    Optional<ButtonType> optional2 = alert.showAndWait();
                    if (optional2.isPresent() && optional2.get() == ButtonType.OK)
                        alert.close();
                }
            }

        });
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public TableView<CreditNoteComment> getMyTable() {
        return myTable;
    }

    public void setMyTable(TableView<CreditNoteComment> myTable) {
        this.myTable = myTable;
    }
}
