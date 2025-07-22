package com.client.clientapplication;

import com.quickrest.entities.CreditNoteComment;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CreditNoteCommentController implements Initializable {

    @FXML
    private Button newCommentButton;
    @FXML
    private Button editCommentButton;
    @FXML
    private TableView<CreditNoteComment> commentTable;

    private String userName;
    private CreditNoteComment creditNoteComment = null;
    private static final String  HOST = ApplicationPath.getHqPath();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Fetching all comments
        WebTarget target = ClientBuilder.newClient().target(HOST+"/comment/findAll");
        GenericType<List<CreditNoteComment>> generic = new GenericType<>(){};
        List<CreditNoteComment> commentList = target.request(MediaType.APPLICATION_JSON).get(generic);
        Task<ObservableList<CreditNoteComment>> task1 = new Task<ObservableList<CreditNoteComment>>() {
            @Override
            protected ObservableList<CreditNoteComment> call() throws Exception {
                return FXCollections.observableArrayList(commentList);
            }
        };
        new Thread(task1).start();
        commentTable.itemsProperty().bind(task1.valueProperty());


        //Open new Comment pagge
        newCommentButton.setOnAction(e1->{
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("NewComment.fxml"));
            try {
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);

                NewCommentController controller = loader.getController();
                controller.setUserName(getUserName());
                controller.setMyTable(commentTable);

                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        //Allow only one table row to be selected
        commentTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    @FXML
    public void editComment(){
        if(commentTable.getSelectionModel().getSelectedItem() != null){
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("EditComment.fxml"));
            try {
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);

                EditCommentController controller = loader.getController();
                controller.getEditCommentLabel().setText(getUserName());
                controller.setTableView(commentTable);
                controller.getEditCommentText().setText(commentTable.getSelectionModel().getSelectedItem().getName());
                controller.getEditCommentId().setText(commentTable.getSelectionModel().getSelectedItem().getId()+"");
                controller.setUserName(commentTable.getSelectionModel().getSelectedItem().getUserName());
                controller.setComment(commentTable.getSelectionModel().getSelectedItem());

                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error In Processing Request");
            alert.setContentText("Kindly click on a comment to edit");
            Optional<ButtonType> optional = alert.showAndWait();
            if(optional.isPresent() && optional.get() == ButtonType.CLOSE)
                alert.close();
        }

    }

    @FXML
    public void delete(){
        CreditNoteComment comment = commentTable.getSelectionModel().getSelectedItem();
        if(comment != null){
            WebTarget target = ClientBuilder.newClient().target(HOST+"/comment/delete/"+comment.getId());
            Response response = target.request(MediaType.APPLICATION_JSON).delete();
            if(response.getStatus() == 202){

                target = ClientBuilder.newClient().target(HOST+"/comment/findAll");
                GenericType<List<CreditNoteComment>> generic1 = new GenericType<>(){};
                List<CreditNoteComment> commentList1 = target.request(MediaType.APPLICATION_JSON).get(generic1);
                Task<ObservableList<CreditNoteComment>> task1 = new Task<ObservableList<CreditNoteComment>>() {
                    @Override
                    protected ObservableList<CreditNoteComment> call() throws Exception {
                        return FXCollections.observableArrayList(commentList1);
                    }
                };
                new Thread(task1).start();
                commentTable.itemsProperty().bind(task1.valueProperty());

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Comment Deletion Successful");
                alert.setContentText("Successfully deleted!");
                Optional<ButtonType> optional = alert.showAndWait();
                if(optional.isPresent() && optional.get() == ButtonType.CLOSE)
                    alert.close();

            }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error In Processing a Request");
            alert.setContentText("Kindly click on a comment to delete");
            Optional<ButtonType> optional = alert.showAndWait();
            if(optional.isPresent() && optional.get() == ButtonType.CLOSE)
                alert.close();
        }

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
