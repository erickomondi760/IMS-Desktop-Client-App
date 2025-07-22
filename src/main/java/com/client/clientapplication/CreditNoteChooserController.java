package com.client.clientapplication;

import com.quickrest.entities.*;
import com.quickrest.resources.ApplicationPath;
import com.quickrest.resources.CnSummary;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CreditNoteChooserController implements Initializable {
    @FXML
    private RadioButton creditNoteA;
    @FXML
    private RadioButton creditNoteB;
    @FXML
    private RadioButton supplierRadio;
    @FXML
    private RadioButton branchRadio;
    @FXML
    private TextField cnChooserSupplier;
    @FXML
    private ComboBox<String> cnChooserBranch;
    @FXML
    private Button cnChooserSearchButton;
    @FXML
    private Button cnChooserButton;
    @FXML
    private ComboBox<String> cnChooserPurpose;
    @FXML
    private Button cnChooserCloseButton;

    private TableView<CreditNote> tableView;
    private String supplier;
    private String userName;
    private Label cnCountLabel;
    private static final String  HOST = ApplicationPath.getHqPath();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Enabling supplier field
        supplierRadio.setOnAction(e ->{
            if(supplierRadio.isSelected()){
                cnChooserSupplier.setDisable(false);
                cnChooserBranch.setDisable(true);
                cnChooserSearchButton.setDisable(false);
                cnChooserBranch.valueProperty().setValue("");
            }
        });

        //Enabling branch combobox
        branchRadio.setOnAction(e->{
            if(branchRadio.isSelected()){
                cnChooserBranch.setDisable(false);
                cnChooserSupplier.setDisable(true);
                cnChooserSearchButton.setDisable(true);
                cnChooserSupplier.setText("");
            }
        });

        //Setting branches into the com box
        fetchBranches();

        //Fetching comments
        fetchComments();

        //Close window
        cnChooserCloseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) cnChooserCloseButton.getScene().getWindow();
                stage.close();
            }
        });

    }

    @FXML
    public void newCreditNote(){
        if(creditNoteA.isSelected()){
            System.out.println("Do nothing");
            Stage stage1 = (Stage) creditNoteB.getScene().getWindow();
            stage1.close();
        }else if (creditNoteB.isSelected()){

            try {

                String entityName = setEntity();
                if(entityName != null && !entityName.equals("") && cnChooserPurpose.getValue() != null){
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("NewCreditNotePage.fxml"));

                    Scene scene = new Scene(fxmlLoader.load());
                    stage.setScene(scene);

                    NewCreditNotePageController controller = fxmlLoader.getController();
                    controller.setCreditNoteType("Credit Note B");
                    controller.setEntityType(entityName);
                    controller.setCreditNoteTableView(getTableView());
                    controller.setCnCountLabel(getCnCountLabel());
                    stage.show();

                    Stage stage1 = (Stage) creditNoteB.getScene().getWindow();
                    stage1.close();

                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error In Processing Request");
                    alert.setContentText("Invalid supplier/branch/comment");
                    Optional<ButtonType> optional = alert.showAndWait();
                    if(optional.isPresent() && optional.get() == ButtonType.CLOSE)
                        alert.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String setEntity(){
        String name = null;
        if(supplierRadio.isSelected()){
            name = cnChooserSupplier.getText();
        }else {
            name = cnChooserBranch.getValue();
        }
        return name;
    }


    @FXML
    public void openSearchTable(){
        Stage stage = new Stage();
        stage.setX(800);
        stage.setY(70);
        stage.setRenderScaleY(100);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("SearchTable.fxml"));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);

            CreditNoteSearchTableController controller = fxmlLoader.getController();
            controller.setSupplierField(cnChooserSupplier);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getSupplier() {
        return supplier;
    }

    //Fetching branches
    private void fetchBranches(){
        WebTarget target = ClientBuilder.newClient().target(HOST+"/branch/findAll");
        GenericType<List<BranchDetails>> genericType = new GenericType<>(){};
        List<BranchDetails> branchDetails = target.request(MediaType.APPLICATION_JSON).get(genericType);
        List<String> branch = new ArrayList<>();
        branchDetails.forEach(e->{
            branch.add(e.getBranchName());
        });

        cnChooserBranch.setItems(FXCollections.observableArrayList(branch));
    }

    //Fetch credit note comments
    private void fetchComments(){
        WebTarget target = ClientBuilder.newClient().target(HOST+"/comment/findAll");
        GenericType<List<CreditNoteComment>> generic = new GenericType<>(){};
        List<CreditNoteComment> commentList = target.request(MediaType.APPLICATION_JSON).get(generic);
        ObservableList<String> ol = FXCollections.observableArrayList();
        commentList.forEach(e->{
            ol.add(e.getName());
        });
        cnChooserPurpose.setItems(ol);
    }

    public TableView<CreditNote> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<CreditNote> tableView) {
        this.tableView = tableView;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Label getCnCountLabel() {
        return cnCountLabel;
    }

    public void setCnCountLabel(Label cnCountLabel) {
        this.cnCountLabel = cnCountLabel;
    }
}
