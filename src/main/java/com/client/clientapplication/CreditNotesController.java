package com.client.clientapplication;

import com.quickrest.entities.CreditNote;
import com.quickrest.entities.Prices;
import com.quickrest.resources.ApplicationPath;
import com.quickrest.resources.CnSummary;
import com.quickrest.resources.ColumnFormatter;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class CreditNotesController implements Initializable {

    @FXML
    private TableView<CreditNote> cnsTable;

    @FXML
    private Label cnCountLabel;

    @FXML
    private TableColumn<CreditNote,Double> amountColumn;


    private String userName;
    private static final String  HOST = ApplicationPath.getApplicationPath();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Formatting amount column
        amountColumn.setCellFactory(new ColumnFormatter<CreditNote,Double>(new DecimalFormat("#,###.00")));
        //Fetch cns
        loadCreditNotes();


    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @FXML
    public void chooseCreditNoteType(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("CreditNoteChooser.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            CreditNoteChooserController controller = loader.getController();
            controller.setTableView(cnsTable);
            controller.setCnCountLabel(cnCountLabel);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void addComment(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("CreditNoteComment.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            CreditNoteCommentController controller = loader.getController();
            controller.setUserName(getUserName());

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void deleteCreditNote() {
        if (cnsTable.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Delete Credit Note");
            alert.setContentText("Are you sure you want to delete the selected credit note?");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get().equals(ButtonType.OK)) {
                WebTarget target = ClientBuilder.newClient().target(ApplicationPath.getApplicationPath()+
                        "/creditNotes/delete/"+cnsTable.getSelectionModel().getSelectedItem().getId());
                Response response = target.request(MediaType.APPLICATION_JSON).delete();
                if (response.getStatus() == 200) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Deleted");
                    optional = alert.showAndWait();
                    if (optional.isPresent() && optional.get().equals(ButtonType.OK)) {
                        alert.close();
                    }
                    loadCreditNotes();
                } else {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Failed");
                    optional = alert.showAndWait();
                    if (optional.isPresent() && optional.get().equals(ButtonType.OK)) {
                        alert.close();
                    }
                    alert.close();
                }

            } else {

            }

        }
    }

    public void loadCreditNotes(){
        WebTarget webTarget = ClientBuilder.newClient().target(HOST+"/creditNotes/findAll");
        GenericType<List<CreditNote>> generic = new GenericType<>(){};
        List<CreditNote> cn = webTarget.request(MediaType.APPLICATION_JSON).get(generic);
        cnsTable.setItems(FXCollections.observableArrayList(cn));

        cnCountLabel.setText(String.format("%,d",cnsTable.getItems().size()));
    }

}
