package com.client.clientapplication;

import com.quickrest.entities.GoodsReceivedNote;
import com.quickrest.entities.HqGrn;
import com.quickrest.resources.ApplicationPath;
import com.quickrest.resources.ColumnFormatter;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class GoodsReceivedViewController implements Initializable {


    @FXML
    private Button deleteGrnButton;
    @FXML
    private Button editGrnButton;
    @FXML
    private Button exportGrnButton;
    @FXML
    private AnchorPane grnViewAnchor;
    @FXML
    private TableView<GoodsReceivedNote> grnViewTable;
    @FXML
    private HBox grnViewTitleAnchor;
    @FXML
    private TextField grnViewGrnSearchText;
    @FXML
    private TextField grnViewSupplierSearchText;
    @FXML
    private Button printGrnButton;
    @FXML
    private Button viewGrnButton;
    @FXML
    private TableColumn<GoodsReceivedNote,Double>amountExcl;
    @FXML
    private TableColumn<GoodsReceivedNote,Double>amountIncl;
    @FXML
    private Label grnViewRecordsLabel;
    @FXML
    private Label grnViewAmountLabel;
    @FXML
    private Button searchByDateButton;
    @FXML
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateTo;


    private static final String HOST = ApplicationPath.getApplicationPath();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Fetching grns
        fetchingGrns();

        //Formating amount cols
        amountIncl.setCellFactory(new ColumnFormatter<>(new DecimalFormat("#,###.00")));
        amountExcl.setCellFactory(new ColumnFormatter<>(new DecimalFormat("#,###.00")));

        //Searching by grn no
        searchByGrnNumber();

        //Search by supplier
        searchBySupplier();
    }

    public AnchorPane getGrnViewAnchor() {
        return grnViewAnchor;
    }

    private void fetchingGrns(){

       new Thread(()->{
           WebTarget target = ClientBuilder.newClient().target(HOST+"/received/findAll");
           GenericType<List<GoodsReceivedNote>> gt = new GenericType<>(){};
           List<GoodsReceivedNote> list = target.request(MediaType.APPLICATION_JSON).get(gt);

           if(list == null){
               Alert alert = new Alert(Alert.AlertType.INFORMATION);
               alert.setHeaderText("Request Timeout");
               alert.setContentText("The server took too long to respond, kindly retry");
               Optional<ButtonType> optional = alert.showAndWait();
               if (optional.isPresent() && optional.get() == ButtonType.OK)
                   alert.close();
           }else {
               Platform.runLater(()->{
                   Collections.sort(list, new Comparator<GoodsReceivedNote>() {
                       @Override
                       public int compare(GoodsReceivedNote o1, GoodsReceivedNote o2) {
                           if(o1.getBranchGrnNumber() > o2.getBranchGrnNumber())
                               return 1;
                           else if (o2.getBranchGrnNumber() > o1.getBranchGrnNumber()) {
                               return -1;
                           }else
                               return 0;
                       }
                   });
                   grnViewTable.setItems(FXCollections.observableArrayList(list));

                   grnViewRecordsLabel.setText(String.format("%,2d",list.size()));

                   double amount = 0;
                   for(GoodsReceivedNote e:list){
                       amount += e.getAmountReceivedIncl();
                   }
                   grnViewAmountLabel.setText(String.format("%,.2f",amount));

               });
           }
       }).start();



    }

    private void searchByGrnNumber(){
        grnViewGrnSearchText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.equals("") && t1.matches("\\d*")){
                    WebTarget target = ClientBuilder.newClient().target(HOST+"/received/searchByGrnNumber/"
                            +Long.parseLong(t1));
                    GenericType<List<GoodsReceivedNote>> genericType = new GenericType<>() {};
                    List<GoodsReceivedNote> myList = target.request(MediaType.APPLICATION_JSON).get(genericType);
                    if(!myList.isEmpty()){
                        grnViewTable.setItems(FXCollections.observableArrayList(myList));
                    }
                }else {
                    fetchingGrns();
                }
            }
        });

    }
    private void searchBySupplier(){
        ObservableList<GoodsReceivedNote> grns = grnViewTable.getItems();
        FilteredList<GoodsReceivedNote> filteredList = new FilteredList<>(grns, p->true);
        grnViewSupplierSearchText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                filteredList.setPredicate(p->{

                    if(t1.isBlank() || t1.isEmpty()){
                        return true;
                    } else return p.getSupplier().contains(t1.toUpperCase());
                });

            }
        });
        SortedList<GoodsReceivedNote> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(grnViewTable.comparatorProperty());
        grnViewTable.setItems(sortedList);
    }

    @FXML
    public void findGrnByDateRange() {

        if (dateFrom.getValue() != null && dateTo.getValue() != null) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String from = dateFrom.getValue().format(dtf);
            String to = dateTo.getValue().format(dtf);

            new Thread(() -> {
                ObservableList<GoodsReceivedNote> list = FXCollections.observableArrayList();

                WebTarget target = ClientBuilder.newClient().target(HOST + "/received/findRange/" + from + "/" + to);
                GenericType<List<GoodsReceivedNote>> genericType = new GenericType<>() {
                };
                List<GoodsReceivedNote> list1 = target.request(MediaType.APPLICATION_JSON).get(genericType);
                list.addAll(list1);

                Platform.runLater(() -> {
                    if (list1 == null) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("Failed to retrieve GRNs");
                        alert.setContentText("An error occurred,kindly retry");
                        Optional<ButtonType> optional = alert.showAndWait();
                        if (optional.isPresent() && optional.get() == ButtonType.OK) {
                            alert.close();
                        }
                    } else {
                        Collections.sort(list, new Comparator<GoodsReceivedNote>() {
                            @Override
                            public int compare(GoodsReceivedNote o1, GoodsReceivedNote o2) {
                                if (o1.getBranchGrnNumber() > o2.getBranchGrnNumber())
                                    return 1;
                                else if (o2.getBranchGrnNumber() > o1.getBranchGrnNumber()) {
                                    return -1;
                                } else
                                    return 0;
                            }
                        });
                        grnViewTable.setItems(FXCollections.observableArrayList(list));

                        grnViewRecordsLabel.setText(String.format("%,2d", list.size()));

                        double amount = 0;
                        for (GoodsReceivedNote e : list) {
                            amount += e.getAmountReceivedIncl();
                        }
                        grnViewAmountLabel.setText(String.format("%,.2f", amount));

                        dateFrom.setValue(null);
                        dateTo.setValue(null);
                    }

                });
            }).start();

        } else {
            fetchingGrns();
        }
    }

    @FXML
    public void exportGrns(){
        ApplicationPath.exportTableData(grnViewTable);
    }

    @FXML
    public void viewGrn(){
        GoodsReceivedNote grn = grnViewTable.getSelectionModel().getSelectedItem();
        if (grn != null) {
            try (ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(
                    new FileOutputStream("C:\\System generated dat files\\Lpo.pdf")))) {
                os.writeObject(grn.getGrnFile());
                os.flush();
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(new File("C:\\System generated dat files\\Lpo.pdf"));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {

        }

    }

    @FXML
    public void editGrn(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("EditBranchGrn.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("GRN Details");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            EditBranchGrnController controller = loader.getController();
            controller.setTableView(grnViewTable);
            controller.setGrn(grnViewTable.getSelectionModel().getSelectedItem());
            controller.getEditGrnCu().setText(grnViewTable.getSelectionModel().getSelectedItem().getCuInvoiceNumber());
            controller.getEditGrnNoLabel().setText(String.valueOf(grnViewTable.getSelectionModel().getSelectedItem().
                    getBranchGrnNumber()));
            controller.getEditGrnInvNumber().setText(grnViewTable.getSelectionModel().getSelectedItem().getInvoiceNumber());
            controller.getEditGrnSupplierLabel().setText(grnViewTable.getSelectionModel().getSelectedItem().getSupplier());
            controller.setAmountLabel(grnViewAmountLabel);
            controller.setRecordsLabel(grnViewRecordsLabel);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
