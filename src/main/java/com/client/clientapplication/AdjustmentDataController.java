package com.client.clientapplication;

import com.quickrest.entities.ProductStockAdjustment;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class AdjustmentDataController implements Initializable {
    @FXML
    private TableView<ProductStockAdjustment> adjustmentTable;
    @FXML
    private DatePicker adjustmentDatadateFrom;
    @FXML
    private DatePicker adjustmentDatadateTo;
    @FXML
    private Button adjustmentDeleteButton;

    private static final String  HOST = ApplicationPath.getApplicationPath();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Fetch Data
        getAdjustmentData();

        //Allowing only one selection from table cells
        adjustmentTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    //Fetching data from the db
    void getAdjustmentData(){
        WebTarget target = ClientBuilder.newClient().target(HOST+"/adjustment/findAll");
        GenericType<List<ProductStockAdjustment>> listGenericType = new GenericType<>(){};
        List<ProductStockAdjustment> adjustmentList = target.request(MediaType.APPLICATION_JSON).get(listGenericType);
        ObservableList<ProductStockAdjustment> ol = FXCollections.observableArrayList();
        //Feeding in the first 1000 records
        int count = 0;
        if(adjustmentList.size() > 0){
            for(ProductStockAdjustment p:adjustmentList){
                if(count <= 1000){
                    ol.add(p);
                    count++;
                }else {
                    return;
                }
            }

            Collections.sort(ol, new Comparator<ProductStockAdjustment>() {
                @Override
                public int compare(ProductStockAdjustment o1, ProductStockAdjustment o2) {
                    if(o1.getAdjustmentNumber() > o2.getAdjustmentNumber())
                        return 1;
                    else if (o2.getAdjustmentNumber() > o1.getAdjustmentNumber())
                        return -1;
                    else
                        return 0;
                }
            });
            adjustmentTable.setItems(ol);

            //Setting date from
            Date dateFrom = ol.get(0).getAdjustmentDate();
            LocalDate localDate = LocalDate.ofInstant(dateFrom.toInstant(), ZoneId.systemDefault());
            adjustmentDatadateFrom.setValue(localDate);

            //Setting date to
            Date dateTo = ol.get(ol.size()-1).getAdjustmentDate();
            localDate = LocalDate.ofInstant(dateTo.toInstant(),ZoneId.systemDefault());
            adjustmentDatadateTo.setValue(localDate);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Data To Display");
            alert.setHeaderText("Null");
            alert.setContentText("There is no information to dysplay");
            Optional<ButtonType> optional = alert.showAndWait();
            if(optional.isPresent() & optional.get() == ButtonType.OK)
                alert.close();
        }

    }

    @FXML
    public void deleteAdjustment(){
        ProductStockAdjustment adjustment = adjustmentTable.getSelectionModel().getSelectedItem();
        if(adjustment != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deletion Confirmation");
            alert.setHeaderText("Selected Adjustment Will Be Deleted");
            alert.setContentText("Are You Sure you Want to delete adjustment : "+adjustment.getAdjustmentNumber()+" ?");
            Optional<ButtonType> optional = alert.showAndWait();

            if(optional.isPresent() && optional.get() == ButtonType.OK){
                WebTarget target = ClientBuilder.newClient().target(HOST+"/adjustment/delete/"+adjustment.getAdjustmentNumber());
                GenericType<List<Integer>> genericType = new GenericType<>(){};
                List<Integer> result = target.request(MediaType.APPLICATION_JSON).get(genericType);
                if(result.get(0) == 1) {

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Deletion Successful");
                    alert.setHeaderText("Selected Adjustment Has Successfully Been Deleted");
                    alert.setContentText("Adjustment:" + adjustment.getAdjustmentNumber() + " has been deleted");
                    optional = alert.showAndWait();
                    if (optional.isPresent() && optional.get() == ButtonType.OK) {
                        alert.close();
                    }
                }else {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Deletion Failed");
                    alert.setHeaderText("Something went wrong");
                    alert.setContentText("Adjustment:" + adjustment.getAdjustmentNumber() + " has failed deletion");
                    optional = alert.showAndWait();
                    if (optional.isPresent() && optional.get() == ButtonType.OK) {
                        alert.close();
                    }
                }
            }else if (optional.isPresent() && optional.get() == ButtonType.CANCEL){
                    alert.close();
            }
        }
    }
}
