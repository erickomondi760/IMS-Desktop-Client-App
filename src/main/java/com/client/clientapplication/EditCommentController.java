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

public class EditCommentController implements Initializable {

    @FXML
    private TextField editCommentText;
    @FXML
    private Label editCommentLabel;
    @FXML
    private Button editCommentDoneButton;
    @FXML
    private Label editCommentId;


    private String userName;
    private CreditNoteComment comment;
    TableView<CreditNoteComment> tableView;
    private static final String HOST = ApplicationPath.getHqPath();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Disable save button
        editCommentDoneButton.setDisable(true);

        //Enable button
        editCommentText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (t1.isBlank() || t1.isEmpty()) {
                    editCommentDoneButton.setDisable(true);
                } else {
                    if (t1.matches("\\d*")) {
                        editCommentText.setText("");
                    } else {
                        editCommentDoneButton.setDisable(false);
                    }
                }
            }
        });

        //Save changes
        editCommentDoneButton.setOnAction(e -> {
            CreditNoteComment comment1 = getComment();
            comment1.setName(editCommentText.getText());
            comment1.setDateCreated(new Date());

            WebTarget webTarget = ClientBuilder.newClient().target(HOST + "/comment/edit/"+Integer.parseInt(editCommentId.getText()));
            Response response = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.
                    entity(comment1, MediaType.APPLICATION_JSON));

            if (response.getStatus() == 204) {
                WebTarget target = ClientBuilder.newClient().target(HOST + "/comment/findAll");
                GenericType<List<CreditNoteComment>> generic = new GenericType<>() {
                };
                List<CreditNoteComment> commentList = target.request(MediaType.APPLICATION_JSON).get(generic);
                Task<ObservableList<CreditNoteComment>> task1 = new Task<ObservableList<CreditNoteComment>>() {
                    @Override
                    protected ObservableList<CreditNoteComment> call() throws Exception {
                        return FXCollections.observableArrayList(commentList);
                    }
                };
                new Thread(task1).start();
                getTableView().itemsProperty().bind(task1.valueProperty());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Comment Succesfully edited");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.CLOSE)
                    alert.close();

                Stage stage = (Stage) editCommentDoneButton.getScene().getWindow();
                stage.close();
            }
        });
    }

    public TableView<CreditNoteComment> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<CreditNoteComment> tableView) {
        this.tableView = tableView;
    }

    public Label getEditCommentLabel() {
        return editCommentLabel;
    }

    public TextField getEditCommentText() {
        return editCommentText;
    }

    public Label getEditCommentId() {
        return editCommentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public CreditNoteComment getComment() {
        return comment;
    }

    public void setComment(CreditNoteComment comment) {
        this.comment = comment;
    }
}
