package com.client.clientapplication;

import com.quickrest.entities.CompanyDetails;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CompanyDetailsController implements Initializable {

    private static final String  HOST = ApplicationPath.getHqPath();

    @FXML
    private TableView<CompanyDetails> companyDetailsViewTable;

    @FXML
    private AnchorPane companyDetailsViewBodyAnchor;
    @FXML
    private Label companyDetailsViewCount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Fetching companies
        WebTarget target = ClientBuilder.newClient().target(HOST+"/company/findAll");
        GenericType<List<CompanyDetails>> genericType = new GenericType<>(){};
        List<CompanyDetails> companyDetailsList = target.request(MediaType.APPLICATION_JSON).get(genericType);

        companyDetailsViewTable.setItems(FXCollections.observableArrayList(companyDetailsList));
        companyDetailsViewCount.setText(String.format("%,d", companyDetailsList.size()));

    }

    public AnchorPane getCompanyDetailsViewBodyAnchor() {
        return companyDetailsViewBodyAnchor;
    }
}
