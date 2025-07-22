package com.client.clientapplication;

import com.quickrest.entities.ProductLpos;
import com.quickrest.resources.ApplicationPath;
import com.quickrest.resources.PODetails;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.text.TabableView;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.*;

public class EditLpoDialogController implements Initializable {
    @FXML
    private Button editLpoDialogEditContent;
    @FXML
    private Button editLpoDialogEditDone;
    @FXML
    private Label editLpoDialogPoDate;
    @FXML
    private Label editLpoDialogPoExpDate;
    @FXML
    private Label editLpoDialogPoNum;
    @FXML
    private Button editLpoDialogSearchutton;
    @FXML
    private TextField editLpoDialogSupplier;
    @FXML
    private TextField editLpoDialogTerms;

    ProductLpos productLpos = null;
    private TableView<ProductLpos> lpoTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void editLpoContent(){
        //Displaying product confirmation page
        Stage stage1 = new Stage();
        EditLpoDetailsConfirmationController c = null;
        FXMLLoader fxmlLoader1 = new FXMLLoader();
        fxmlLoader1.setLocation(getClass().getResource("EditLpoDetailsConfirmation.fxml"));
        try {
            stage1.setScene(new Scene(fxmlLoader1.load()));
            stage1.setX(1030);
            stage1.setY(105);

            c = fxmlLoader1.getController();
            stage1.initStyle(StageStyle.UNDECORATED);
            stage1.initModality(Modality.APPLICATION_MODAL);


            stage1.show();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(getProductLpos() != null){
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("EditLpo.fxml"));
            try {
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.setX(100);
                stage.setY(100);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initModality(Modality.APPLICATION_MODAL);

                //Exctracting podetails and passing to po editor
                EditLpoController controller = fxmlLoader.getController();
                List<PODetails> poDetailsList = new ArrayList<>();

                ByteArrayInputStream is = new ByteArrayInputStream(getProductLpos().getPoDetails());
                try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is))){

                    poDetailsList = (List<PODetails>) ois.readObject();

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                List<PODetails> myList = new ArrayList<>();
                for(PODetails p:poDetailsList){
                    p.setVat(Double.parseDouble(String.format("%.2f",(p.getVat()/p.getCostExclusive())*100)));
                    p.setCostInclusive(Double.parseDouble(String.format("%.2f",p.getCostInclusive()/p.getQuantity())));
                    p.setCostExclusive(Double.parseDouble(String.format("%.2f",p.getCostExclusive()/p.getQuantity())));
                    myList.add(p);
                }

                controller.getEditLpoTable().setItems(FXCollections.observableArrayList(myList));
                controller.getEditLpoAmountLabel().setText(String.format("%,.2f",getProductLpos().getAmount()));
                controller.getEditLpoLpoNumber().setText(String.valueOf(getProductLpos().getLpoNumber()));
                controller.setProductLpo(getProductLpos());
                controller.setC(c);
                controller.setLposTableView(getLpoTable());

                stage.show();




            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Stage stage2 = (Stage) editLpoDialogEditContent.getScene().getWindow();
        stage2.close();
    }

    @FXML
    public void editTerms(){
        ProductLpos order = getProductLpos();
        order.setSupplier(editLpoDialogSupplier.getText());
        order.setExpiryDate(order.getExpiryDate().plusDays(Integer.parseInt(editLpoDialogTerms.getText())));
        order.setUserName(ApplicationPath.getUser());

        WebTarget target = ClientBuilder.newClient().target(ApplicationPath.getApplicationPath()+"/lpos/update");
        Response response = target.request(MediaType.APPLICATION_JSON).put(Entity.entity(order,MediaType.APPLICATION_JSON));

        if(response.getStatus() == 200){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Updated");
            alert.setContentText("Lpo has been successfully edited");
            Optional<ButtonType> optional = alert.showAndWait();
            if(optional.isPresent() && optional.get() == ButtonType.OK){
                alert.close();
            }

            Stage s = (Stage) editLpoDialogEditContent.getScene().getWindow();
            s.close();
            populateLpoTable();

        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Update Failed");
            alert.setContentText("Kindly try again");
            Optional<ButtonType> optional = alert.showAndWait();
            if(optional.isPresent() && optional.get() == ButtonType.OK){
                alert.close();
            }
        }
    }

    @FXML
    public void searchSupplier(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("ProductSupplierSearch.fxml"));
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
            ProductSupplierSearchController controller = fxmlLoader.getController();
            controller.setSupplier(editLpoDialogSupplier);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //Fetch lpos
    private void populateLpoTable() {
        new Thread(() -> {
            WebTarget target = ClientBuilder.newClient().target(ApplicationPath.getApplicationPath() + "/lpos/findAll");
            GenericType<List<ProductLpos>> genericType = new GenericType<>() {};

            ObservableList<ProductLpos> list = FXCollections.observableArrayList();

            List<ProductLpos> list1 = target.request(MediaType.APPLICATION_JSON).get(genericType);
            list.addAll(list1);

            Platform.runLater(() -> {
                if(list1 == null){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Failed to load lpos");
                    alert.setContentText("An error occured,kindly retry");
                    Optional<ButtonType> optional = alert.showAndWait();
                    if (optional.isPresent() && optional.get() == ButtonType.OK) {
                        alert.close();
                    }
                }else {
                    Collections.sort(list, new Comparator<ProductLpos>() {
                        @Override
                        public int compare(ProductLpos o1, ProductLpos o2) {
                            if (o1.getLpoNumber() > o2.getLpoNumber())
                                return 1;
                            if (o2.getLpoNumber() > o1.getLpoNumber())
                                return -1;
                            else
                                return 0;
                        }
                    });
                    getLpoTable().setItems(list);
                    getLpoTable().refresh();
                }

            });
        }).start();
    }

    public ProductLpos getProductLpos() {
        return productLpos;
    }

    public void setProductLpos(ProductLpos productLpos) {
        this.productLpos = productLpos;
    }

    public TableView<ProductLpos> getLpoTable() {
        return lpoTable;
    }

    public void setLpoTable(TableView<ProductLpos> lpoTable) {
        this.lpoTable = lpoTable;
    }

    public Label getEditLpoDialogPoDate() {
        return editLpoDialogPoDate;
    }

    public Label getEditLpoDialogPoExpDate() {
        return editLpoDialogPoExpDate;
    }

    public Label getEditLpoDialogPoNum() {
        return editLpoDialogPoNum;
    }

    public Button getEditLpoDialogSearchutton() {
        return editLpoDialogSearchutton;
    }

    public TextField getEditLpoDialogSupplier() {
        return editLpoDialogSupplier;
    }

    public TextField getEditLpoDialogTerms() {
        return editLpoDialogTerms;
    }
}
