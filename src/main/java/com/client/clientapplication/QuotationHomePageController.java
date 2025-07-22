package com.client.clientapplication;

import com.quickrest.entities.ProductQuotation;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class QuotationHomePageController implements Initializable {

    @FXML
    private TableView<ProductQuotation> quotationHomeTable;
    @FXML
    private Label quotationHomeCountLabel;

    private String userName;
    private static final String  HOST = ApplicationPath.getApplicationPath();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Fetchind quotation data
        WebTarget target = ClientBuilder.newClient().target(HOST+"/quotation/findAll");
        GenericType<List<ProductQuotation>> genericType = new GenericType<>(){};
        List<ProductQuotation> myList = target.request(MediaType.APPLICATION_JSON).get(genericType);
        quotationHomeTable.setItems(FXCollections.observableArrayList(myList));

        //Setting the number of records
        quotationHomeCountLabel.setText(myList.size()+"");

    }

    @FXML
    public void newQuotation(){
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Quotation.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);

            QuotationController controller = fxmlLoader.getController();
            controller.setUserName(getUserName());
            controller.setTableView(quotationHomeTable);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
