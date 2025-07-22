package com.client.clientapplication;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdjustmentReportMenueController implements Initializable {
    @FXML
    private RadioButton dataRadioButton;
    @FXML
    private RadioButton detailedRadioButton;
    @FXML
    private RadioButton summaryRadioButton;
    @FXML
    private Button adjustmentReportMenueButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Viewing Adjustment Data
        adjustmentReportMenueButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(dataRadioButton.isSelected()){
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("AdjustmentData.fxml"));
                    try {
                        Scene scene = new Scene(loader.load());
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.show();
                        Stage s = (Stage) adjustmentReportMenueButton.getScene().getWindow();
                        s.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }
}
