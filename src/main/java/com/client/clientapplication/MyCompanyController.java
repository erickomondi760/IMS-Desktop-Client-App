package com.client.clientapplication;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MyCompanyController implements Initializable {
    @FXML
    private Button companyBranchDetailsButton;
    @FXML
    private Button companyDetailsButton;
    @FXML
    private BorderPane companyDetailsBorderPane;
    @FXML
    private Button addCompanyBranch;

    String userName;
    CompanyDetailsController controller;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Showing branches
        companyBranchDetailsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("BranchDetailsView.fxml"));
                try {
                    stage.setScene(new Scene(fxmlLoader.load()));
                    BranchDetailsViewController controller = fxmlLoader.getController();
                    companyDetailsBorderPane.setCenter(controller.getBranchDetailsViewBodyAnchor());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //Show companies
        companyDetailsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("CompanyDetails.fxml"));
                try {
                    stage.setScene(new Scene(fxmlLoader.load()));
                    controller = fxmlLoader.getController();
                    companyDetailsBorderPane.setCenter(controller.getCompanyDetailsViewBodyAnchor());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        //Setting company on window loading
         openCompanyDetails();
    }

    private void openCompanyDetails(){
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("CompanyDetails.fxml"));
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
            controller = fxmlLoader.getController();
            companyDetailsBorderPane.setCenter(controller.getCompanyDetailsViewBodyAnchor());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void addABranch() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddBranch.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();

            AddBranchController controller = loader.getController();
            controller.setUser(getUserName());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void addACompany() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddCompany.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();

            AddCompanyController controller = loader.getController();
            controller.setUser(getUserName());

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
