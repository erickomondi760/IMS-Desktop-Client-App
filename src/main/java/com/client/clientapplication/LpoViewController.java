package com.client.clientapplication;

import com.quickrest.entities.ProductLpos;
import com.quickrest.resources.ApplicationPath;
import com.quickrest.resources.ColumnFormatter;
import com.quickrest.resources.PODetails;
import com.quickrest.resources.ReceivedGrn;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import javafx.util.Callback;



public class LpoViewController implements Initializable {
    @FXML
    private AnchorPane lpoViewAnchor;
    @FXML
    private TableView<ProductLpos> lpoTable;
    @FXML
    private TableColumn<ProductLpos,Double> amount;
    @FXML
    private TextField lpoViewLpoNumSearchText;
    @FXML
    private TextField lpoViewLpoSupplierSearchText;
    @FXML
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateTo;
    @FXML
    private Button searchByDateButton;
    @FXML
    private TableColumn<ProductLpos,LocalDate>expDateCol;


    private static final String HOST = ApplicationPath.getApplicationPath();
    private String user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Formatting amount column
        amount.setCellFactory(new ColumnFormatter<>(new DecimalFormat("#,###.00")));

        //Fetching lpos
        populateLpoTable();

        //Enabling lpo to select a single row
        lpoTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //Search lpo by po number
        searchLpoByPoNumber();

        //Search lpo by supplier
        searchLpoBySupplier();

        //Update cell color if lpo expired
        changeCellColor();
    }
    @FXML
    public void openLpoGenerator() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LPOGenerator.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            LPOGeneratorController controller = loader.getController();
            controller.setLpoTable(lpoTable);
            controller.setUser(getUser());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void receiveAnLpo() {
        if (lpoTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Choose an LPO to receive");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get() == ButtonType.OK)
                alert.close();
            return;
        }
        ProductLpos po = lpoTable.getSelectionModel().getSelectedItem();
        if (po.getStatus().equalsIgnoreCase("Pending") &&
                !(po.getExpiryDate().equals(LocalDate.now()) || LocalDate.now().isAfter(po.getExpiryDate()))) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ReceivingLpo.fxml"));
            try {
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.show();

                List<PODetails> details = new ArrayList<>();
                ObservableList<ReceivedGrn> receivedGrnList = FXCollections.observableArrayList();

                //Fetching lpo details list
                byte[]poData = lpoTable.getSelectionModel().getSelectedItem().getPoDetails();
                ByteArrayInputStream is = new ByteArrayInputStream(poData);
                try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is))){
                    details = (List<PODetails>) ois.readObject();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }


                details.forEach(p -> {
                    ReceivedGrn receivedGrn = new ReceivedGrn();
                    receivedGrn.setCode(p.getCode());
                    receivedGrn.setBarcode(p.getBarcode());
                    receivedGrn.setDescription(p.getDescription());
                    receivedGrn.setQtyOrdered(p.getQuantity());
                    receivedGrn.setCostExclusive(p.getCostExclusive()/p.getQuantity());
                    receivedGrn.setCostInclusive(p.getCostInclusive()/p.getQuantity());
                    receivedGrn.setQtyReceived(0);
                    receivedGrn.setBalQty(0);
                    receivedGrn.setSupplier(p.getSupplier());
                    receivedGrn.setVat(receivedGrn.getCostInclusive() - receivedGrn.getCostExclusive());
                    receivedGrnList.add(receivedGrn);
                });

                ReceivingLpoController controller = loader.getController();
                controller.getTable().setItems(receivedGrnList);


                controller.getSupplierLabel().setText(receivedGrnList.get(0).getSupplier());
                controller.setLposTableView(lpoTable);
                controller.setLpos(lpoTable.getSelectionModel().getSelectedItem());
                controller.setDetailsList(details);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Expired Purchase Order");
            alert.setContentText("Selected LPO is expired,kindly update to receive");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get() == ButtonType.OK)
                alert.close();
        }
    }

    @FXML
    public void viewLpo() {
        ProductLpos lpo = lpoTable.getSelectionModel().getSelectedItem();
        if (lpo != null) {
            try (ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(
                    new FileOutputStream("C:\\System generated dat files\\Lpo.pdf")))) {
                os.writeObject(lpo.getLpoFile());
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
    public void exportTableData(){
        ApplicationPath.exportTableData(lpoTable);
    }

    @FXML
    private void editLpo(){
        //Checking if received
        if(lpoTable.getSelectionModel().getSelectedItem().getReceived().equals("Received")){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Unable to update");
            alert.setContentText("LPO is already received therefore can not be edited");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get() == ButtonType.OK)
                alert.close();
        }else {
            Stage stage = new Stage();
            EditLpoDetailsConfirmationController c = null;
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("EditLpoDialog.fxml"));
            try {
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.initModality(Modality.APPLICATION_MODAL);

                EditLpoDialogController controller = fxmlLoader.getController();
                ProductLpos lpo = lpoTable.getSelectionModel().getSelectedItem();

                if(lpo != null){
                    controller.setProductLpos(lpo);
                    controller.setLpoTable(lpoTable);
                    controller.getEditLpoDialogPoDate().setText(lpo.getDateRaised().toString());
                    controller.getEditLpoDialogPoNum().setText(String.valueOf(lpo.getLpoNumber()));
                    controller.getEditLpoDialogPoExpDate().setText(lpo.getExpiryDate().toString());
                    controller.getEditLpoDialogSupplier().setText(lpo.getSupplier());
                    List<PODetails> poDetailsList = new ArrayList<>();

                    //Fetching terms
                    ByteArrayInputStream is = new ByteArrayInputStream(lpo.getPoDetails());
                    try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is))){

                        poDetailsList = (List<PODetails>) ois.readObject();

                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    controller.getEditLpoDialogTerms().setText(String.valueOf(poDetailsList.get(0).getTerms()));
                }

                stage.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
    }

    @FXML
    public void deleteLpo() {
        ProductLpos lpo = lpoTable.getSelectionModel().getSelectedItem();
        if (lpo != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("You are bout to delete an LPO");
            alert.setContentText("Are you sure you want to proceed?");
            Optional<ButtonType> optional = alert.showAndWait();
            Response response = null;
            if (optional.isPresent() && optional.get() == ButtonType.OK) {

                WebTarget target = ClientBuilder.newClient().target(HOST + "/lpos/delete/" + lpo.getId());
                response = target.request(MediaType.APPLICATION_JSON).delete();

                if (response.getStatus() == 200) {
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("LPO successfully delete");
                    optional = alert.showAndWait();
                    if (optional.isPresent() && optional.equals(ButtonType.OK))
                        alert.close();
                    populateLpoTable();
                } else {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Failed");
                    alert.setContentText("Message:" + response.readEntity(String.class));
                    optional = alert.showAndWait();
                    if (optional.isPresent() && optional.equals(ButtonType.OK))
                        alert.close();
                }
            } else {
                alert.close();

            }
        }
    }

    //Fetch lpos
    private void populateLpoTable() {
        new Thread(() -> {
            WebTarget target = ClientBuilder.newClient().target(HOST + "/lpos/findAll");
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
                    lpoTable.setItems(list);
                    lpoTable.refresh();
                }

            });
        }).start();

    }

    private void searchLpoByPoNumber(){
        lpoViewLpoNumSearchText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.equals("") && t1.matches("\\d*")){
                    WebTarget target = ClientBuilder.newClient().target(HOST+"/lpos/searchBypoNumber/"
                            +Long.parseLong(t1));
                    GenericType<List<ProductLpos>> genericType = new GenericType<>() {};
                    List<ProductLpos> myList = target.request(MediaType.APPLICATION_JSON).get(genericType);
                    if(!myList.isEmpty()){
                        lpoTable.setItems(FXCollections.observableArrayList(myList));
                    }
                }else {
                    populateLpoTable();
                }
            }
        });

    }
    private void searchLpoBySupplier(){
        ObservableList<ProductLpos> productLpos = lpoTable.getItems();
        FilteredList<ProductLpos> filteredList = new FilteredList<>(productLpos,p->true);
        lpoViewLpoSupplierSearchText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                filteredList.setPredicate(p->{

                    if(t1.isBlank() || t1.isEmpty()){
                        return true;
                    } else return p.getSupplier().contains(t1.toUpperCase());
                });

            }
        });
        SortedList<ProductLpos> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(lpoTable.comparatorProperty());
        lpoTable.setItems(sortedList);
    }

    @FXML
    public void findLpoByDateRange(){

        if(dateFrom.getValue() != null && dateTo.getValue() != null){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String from = dateFrom.getValue().format(dtf);
            String to = dateTo.getValue().format(dtf);

            new Thread(() -> {
                ObservableList<ProductLpos> list = FXCollections.observableArrayList();

                WebTarget target = ClientBuilder.newClient().target(HOST+"/lpos/findRange/"+from+"/"+to);
                GenericType<List<ProductLpos>> genericType = new GenericType<>() {};
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
                        lpoTable.setItems(list);
                        lpoTable.refresh();
                        dateFrom.setValue(null);
                        dateTo.setValue(null);
                    }

                });
            }).start();

        }else {
            populateLpoTable();
        }
    }

    private void changeCellColor(){
        lpoTable.setRowFactory(new Callback<TableView<ProductLpos>, TableRow<ProductLpos>>() {
            @Override
            public TableRow<ProductLpos> call(TableView<ProductLpos> productLposTableView) {
                return new TableRow<ProductLpos>(){
                    @Override
                    public void updateItem(ProductLpos p,boolean b){
                        super.updateItem(p,b);
                        if(p == null){
                            setStyle("");
                        }else {
                            if(p.getExpiryDate().equals(LocalDate.now()) || LocalDate.now().isAfter(p.getExpiryDate())){
                                setStyle("-fx-text-background-color:tomato");
                            }
                        }

                    }
                };
            }
        });
    }

    public AnchorPane getLpoViewAnchor() {
        return lpoViewAnchor;
    }

    public void setLpoViewAnchor(AnchorPane lpoViewAnchor) {
        this.lpoViewAnchor = lpoViewAnchor;
    }

    public TableView<ProductLpos> getLpoTable() {
        return lpoTable;
    }

    public void setLpoTable(TableView<ProductLpos> lpoTable) {
        this.lpoTable = lpoTable;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
