package com.client.clientapplication;

import com.quickrest.entities.GoodsReceivedNote;
import com.quickrest.entities.HqGrn;
import com.quickrest.entities.ProductLpos;
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
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class GRNsAtHqViewController implements Initializable {
    @FXML
    private TableColumn<HqGrn, Double> amountExcl;
    @FXML
    private TableColumn<HqGrn, Double> amountIncl;
    @FXML
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateTo;
    @FXML
    private Button deleteHqGrnButton;
    @FXML
    private Button editHqGrnButton;
    @FXML
    private Button exporthQGrnButton;
    @FXML
    private TextField grnViewSupplierSearchText;
    @FXML
    private Label hQGrnViewAmountLabel;
    @FXML
    private TextField hQGrnViewGrnSearchText;
    @FXML
    private Label hQGrnViewRecordsLabel;
    @FXML
    private AnchorPane hqGrnAnchor;
    @FXML
    private TableView<HqGrn> hqGrnViewTable;
    @FXML
    private Button printHqGrnButton;
    @FXML
    private Button searchByDateButton;
    @FXML
    private Button viewhQGrnButton;

    private static final String HOST = ApplicationPath.getHqPath();


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

    public AnchorPane getHqGrnAnchor() {
        return hqGrnAnchor;
    }


    private void fetchingGrns(){

        new Thread(()->{
            URL url = null;
            try {
                url = new URL(ApplicationPath.getApplicationPath());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            WebTarget target = ClientBuilder.newClient().target(HOST+"/hqGrn/findAll/"
                    +url.getPath().split("/")[1]);
            GenericType<List<HqGrn>> gt = new GenericType<>(){};
            List<HqGrn> list = target.request(MediaType.APPLICATION_JSON).get(gt);

            if(list == null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Request Timeout");
                alert.setContentText("The server took too long to respond, kindly retry");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK)
                    alert.close();
            }else {
                Platform.runLater(()->{
                    Collections.sort(list, new Comparator<HqGrn>() {
                        @Override
                        public int compare(HqGrn o1, HqGrn o2) {
                            if(o1.getHqGrnNumber() > o2.getHqGrnNumber())
                                return 1;
                            else if (o2.getHqGrnNumber() > o1.getHqGrnNumber()) {
                                return -1;
                            }else
                                return 0;
                        }
                    });
                    hqGrnViewTable.setItems(FXCollections.observableArrayList(list));

                    hQGrnViewRecordsLabel.setText(String.format("%,2d",list.size()));

                    double amount = 0;
                    for(HqGrn e:list){
                        amount += e.getAmountReceivedIncl();
                    }
                    hQGrnViewAmountLabel.setText(String.format("%,.2f",amount));

                });
            }
        }).start();



    }

    private void searchByGrnNumber(){
        hQGrnViewGrnSearchText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.equals("") && t1.matches("\\d*")){
                    WebTarget target = ClientBuilder.newClient().target(HOST+"/hqGrn/searchByGrnNumber/"
                            +Long.parseLong(t1));
                    GenericType<List<HqGrn>> genericType = new GenericType<>() {};
                    List<HqGrn> myList = target.request(MediaType.APPLICATION_JSON).get(genericType);
                    if(!myList.isEmpty()){
                        hqGrnViewTable.setItems(FXCollections.observableArrayList(myList));
                    }
                }else {
                    fetchingGrns();
                }
            }
        });

    }
    private void searchBySupplier(){
        ObservableList<HqGrn> grns = hqGrnViewTable.getItems();
        FilteredList<HqGrn> filteredList = new FilteredList<>(grns, p->true);
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
        SortedList<HqGrn> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(hqGrnViewTable.comparatorProperty());
        hqGrnViewTable.setItems(sortedList);
    }

    @FXML
    public void findGrnByDateRange() {

        if (dateFrom.getValue() != null && dateTo.getValue() != null) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String from = dateFrom.getValue().format(dtf);
            String to = dateTo.getValue().format(dtf);

            new Thread(() -> {
                ObservableList<HqGrn> list = FXCollections.observableArrayList();

                WebTarget target = ClientBuilder.newClient().target(HOST + "/hqGrn/findRange/" + from + "/" + to);
                GenericType<List<HqGrn>> genericType = new GenericType<>() {
                };
                List<HqGrn> list1 = target.request(MediaType.APPLICATION_JSON).get(genericType);
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
                        Collections.sort(list, new Comparator<HqGrn>() {
                            @Override
                            public int compare(HqGrn o1, HqGrn o2) {
                                if(o1.getHqGrnNumber() > o2.getHqGrnNumber())
                                    return 1;
                                else if (o2.getHqGrnNumber() > o1.getHqGrnNumber()) {
                                    return -1;
                                }else
                                    return 0;
                            }
                        });
                        hqGrnViewTable.setItems(FXCollections.observableArrayList(list));

                        hQGrnViewRecordsLabel.setText(String.format("%,2d",list.size()));

                        double amount = 0;
                        for(HqGrn e:list){
                            amount += e.getAmountReceivedIncl();
                        }
                        hQGrnViewAmountLabel.setText(String.format("%,.2f",amount));

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
        ApplicationPath.exportTableData(hqGrnViewTable);
    }

    @FXML
    public void viewGrn(){
        HqGrn grn = hqGrnViewTable.getSelectionModel().getSelectedItem();
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
}
