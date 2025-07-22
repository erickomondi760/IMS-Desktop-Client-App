package com.client.clientapplication;

import com.quickrest.resources.ApplicationPath;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;

public class LPOSController implements Initializable {
    @FXML
    private AnchorPane lpoAnchor;
    @FXML
    private Separator lpoVertSeparetor;
    @FXML
    private BorderPane lpoBorderpane;
    @FXML
    private Button grnsAtBranchButton;
    @FXML
    private Button lposButton;
    @FXML
    private Button grnsAtWarehouseButton;
    @FXML
    private Button transitButton;


    private static final String HOST = ApplicationPath.getApplicationPath();
    private String user;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //View lpos
        setLpoViewAnchor();

        lposButton.setOnAction(e1->{
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("LpoView.fxml"));
            try {
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                LpoViewController controller = loader.getController();
                lpoBorderpane.setCenter(controller.getLpoViewAnchor());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            lposButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white;-fx-border-size:2px;" +
                    " -fx-border-color:#d6ffff; -fx-font-weight:bold;");
            grnsAtBranchButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white");
            transitButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white");
            grnsAtWarehouseButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white");
        });


    }

    @FXML
    public void setLpoViewAnchor(){

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LpoView.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            LpoViewController controller = loader.getController();
            lpoBorderpane.setCenter(controller.getLpoViewAnchor());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        lposButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white;-fx-border-size:2px;" +
                " -fx-border-color:#d6ffff; -fx-font-weight:bold;");
        grnsAtBranchButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white");
        transitButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white");
        grnsAtWarehouseButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white");
    }

    @FXML
    public void viewGrnsAtBranch(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("GoodsReceivedView.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            GoodsReceivedViewController controller = loader.getController();
            lpoBorderpane.setCenter(controller.getGrnViewAnchor());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        lposButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white");
        grnsAtBranchButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white;-fx-border-size:2px;" +
                " -fx-border-color:#d6ffff; -fx-font-weight:bold;");
        transitButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white");
        grnsAtWarehouseButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white");
    }

    @FXML
    public void viewGrnsAtHq(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("GRNsAtHqView.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            GRNsAtHqViewController controller = loader.getController();
            lpoBorderpane.setCenter(controller.getHqGrnAnchor());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        lposButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white");
        grnsAtBranchButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white");
        transitButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white");
        grnsAtWarehouseButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white;-fx-border-size:2px;" +
                " -fx-border-color:#d6ffff; -fx-font-weight:bold;");
    }

    @FXML
    public void viewTransitDocs(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("TransitViewer.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            TransitViewerController controller = loader.getController();
            lpoBorderpane.setCenter(controller.getTransitAnchor());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        lposButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white");
        grnsAtBranchButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white");
        transitButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white;-fx-border-size:2px;" +
                " -fx-border-color:#d6ffff; -fx-font-weight:bold;");
        grnsAtWarehouseButton.setStyle("-fx-background-color:transparent;-fx-text-fill:white");
    }
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}

