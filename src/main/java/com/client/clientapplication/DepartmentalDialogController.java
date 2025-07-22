package com.client.clientapplication;

import com.quickrest.entities.ProductSubDepartment;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentalDialogController implements Initializable {

    @FXML
    private RadioButton deptDialogsubDeptRadio;
    @FXML
    private RadioButton deptDialogDeptRadio;
    @FXML
    private Button deptDialogButton;

    private String userName;
    private TableView<ProductSubDepartment> tableView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        deptDialogButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(deptDialogDeptRadio.isSelected()){
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("NewDepartment.fxml"));
                    try {
                        Scene scene = new Scene(fxmlLoader.load());
                        stage.setScene(scene);

                        NewDepartmentController controller = fxmlLoader.getController();
                        controller.setUserName(getUserName());
                        stage.show();
                        Stage stage1 = (Stage) deptDialogButton.getScene().getWindow();
                        stage1.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("SubDepartment.fxml"));
                    try {
                        Scene scene = new Scene(fxmlLoader.load());
                        stage.setScene(scene);

                        SubDepartmentController controller = fxmlLoader.getController();
                        controller.setUserName(getUserName());
                        controller.setTableView(getTableView());
                        stage.show();
                        Stage stage1 = (Stage) deptDialogButton.getScene().getWindow();
                        stage1.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public TableView<ProductSubDepartment> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<ProductSubDepartment> tableView) {
        this.tableView = tableView;
    }
}
