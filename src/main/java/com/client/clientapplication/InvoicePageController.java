package com.client.clientapplication;

import com.quickrest.entities.Invoice;

import com.quickrest.entities.ProductLpos;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;

public class InvoicePageController implements Initializable {

    @FXML
    private TableView<Invoice> invoiceTable;
    @FXML
    private CheckBox invoiceDispatchCheck;
    @FXML
    private CheckBox invoiceUnDispatchCheck;
    @FXML
    private TextField invoicePageSearchField;
    @FXML
    private Button invoicePageViewButton;

    String user;
    private static final String  HOST = ApplicationPath.getApplicationPath();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showInvoices();

        invoiceTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        enableDispatch();

        //Search an invoice
        searchInvoiceTable();
    }

    @FXML
    public void openInvoicingPage(){

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("InvoiceDnoteChooser.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();

            InvoiceDnoteChooser controller = loader.getController();
            controller.setUser(getUser());
            controller.setTableView(invoiceTable);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private void showInvoices(){
        WebTarget target = ClientBuilder.newClient().target(HOST+"/invoices/findAll");
        GenericType<List<Invoice>> genericType = new GenericType<>(){};
        List<Invoice> invoices = target.request(MediaType.APPLICATION_JSON).get(genericType);
        ObservableList<Invoice> observableList = FXCollections.observableArrayList(invoices);
        Collections.sort(observableList, new Comparator<Invoice>() {
            @Override
            public int compare(Invoice o1, Invoice o2) {
                if(o1.getInvoiceNumber() > o2.getInvoiceNumber())
                    return 1;
                if(o2.getInvoiceNumber() > o1.getInvoiceNumber())
                    return -1;
                else
                    return 0;
            }
        });
        invoiceTable.setItems(observableList);

    }

    @FXML
    public void dispatchInvoice(){
        if(invoiceDispatchCheck.isSelected()){
            Invoice invoice = invoiceTable.getSelectionModel().getSelectedItem();
            if(invoice != null){

                if(invoice.getStatus().equals("Pending")){
                    WebTarget webTarget = ClientBuilder.newClient().target(HOST+"/invoices/update/"+ invoice.getInvoiceNumber()
                            +"/Dispatched");
                    Response response = webTarget.request(MediaType.APPLICATION_JSON).
                            put(Entity.entity(invoice,MediaType.APPLICATION_JSON));

                    if(response.getStatus() == 201){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Invoice succesfully dispatched");
                        Optional<ButtonType> optional = alert.showAndWait();
                        if (optional.isPresent() && optional.get() == ButtonType.OK)
                            alert.close();
                        showInvoices();
                        invoiceDispatchCheck.setSelected(false);

                    }else {
                        Alert alert1 = new Alert(Alert.AlertType.WARNING);
                        alert1.setContentText("Kindly confirm with your system admin");
                        alert1.setHeaderText("Failed!!!");
                        Optional<ButtonType> optional1 = alert1.showAndWait();
                        if (optional1.isPresent() && optional1.get() == ButtonType.OK)
                            alert1.close();
                    }
                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Invoice already dispatched");
                    Optional<ButtonType> optional = alert.showAndWait();
                    if (optional.isPresent() && optional.get() == ButtonType.OK)
                        alert.close();
                    showInvoices();
                    invoiceDispatchCheck.setSelected(false);
                }

            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setContentText("Select an invoice to dispatch");

                Optional<ButtonType> optional = alert.showAndWait();
                if(optional.isPresent() && optional.get() == ButtonType.OK)
                    alert.close();
                invoiceDispatchCheck.setSelected(false);
            }

        }

    }

    public void undispatchInvoice(){
        if(invoiceUnDispatchCheck.isSelected()){
            Invoice invoice = invoiceTable.getSelectionModel().getSelectedItem();
            if(invoice != null){
                if(invoice.getStatus().equals("Pending")){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Invoice is already pending");
                    Optional<ButtonType> optional = alert.showAndWait();
                    if (optional.isPresent() && optional.get() == ButtonType.OK)
                        alert.close();
                    showInvoices();
                    invoiceUnDispatchCheck.setSelected(false);
                }else {
                    WebTarget webTarget = ClientBuilder.newClient().target(HOST+"/invoices/undispatch/"+ invoice.getInvoiceNumber()
                            +"/Pending");
                    Response response = webTarget.request(MediaType.APPLICATION_JSON).
                            put(Entity.entity(invoice,MediaType.APPLICATION_JSON));

                    if(response.getStatus() == 201){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Dispatch cancelled");
                        Optional<ButtonType> optional = alert.showAndWait();
                        if (optional.isPresent() && optional.get() == ButtonType.OK)
                            alert.close();
                        showInvoices();
                        invoiceUnDispatchCheck.setSelected(false);

                    }else {
                        Alert alert1 = new Alert(Alert.AlertType.WARNING);
                        alert1.setContentText("Kindly confirm with your system admin");
                        alert1.setHeaderText("Failed!!!");
                        Optional<ButtonType> optional1 = alert1.showAndWait();
                        if (optional1.isPresent() && optional1.get() == ButtonType.OK)
                            alert1.close();
                    }
                }

            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setContentText("Select an invoice to proceed");

                Optional<ButtonType> optional = alert.showAndWait();
                if(optional.isPresent() && optional.get() == ButtonType.OK)
                    alert.close();
                invoiceUnDispatchCheck.setSelected(false);
            }

        }

    }

    private void enableDispatch(){
        Invoice invoice = invoiceTable.getSelectionModel().getSelectedItem();
        if(invoice != null)
            invoiceDispatchCheck.setDisable(false);
    }

    private void searchInvoiceTable(){
        ObservableList<Invoice> list = invoiceTable.getItems();
        FilteredList<Invoice> filteredList = new FilteredList<>(list, e->true);

        invoicePageSearchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                filteredList.setPredicate(new Predicate<Invoice>() {
                    @Override
                    public boolean test(Invoice invoice) {
                        if(t1.isBlank() || t1.isEmpty()){
                            return true;
                        }
                        else if (invoice.getInvoiceDate().toString().toLowerCase().contains(t1)) {
                            return true;
                        } else if (invoice.getTransporter().toLowerCase().contains(t1)) {
                            return true;
                        }  else if (invoice.getUserName().toLowerCase().contains(t1)) {
                            return true;
                        }else {
                            return false;
                        }
                    }
                });
                SortedList<Invoice> sortedList = new SortedList<>(filteredList);
                sortedList.comparatorProperty().bind(invoiceTable.comparatorProperty());
                invoiceTable.setItems(sortedList);

            }
        });
    }

    @FXML
    public void view(){
        Invoice invoice = invoiceTable.getSelectionModel().getSelectedItem();
        if(invoice!=null){
            byte[] fileB = invoice.getInvoiceFile();
            try(ObjectOutputStream os = new ObjectOutputStream(
                    new BufferedOutputStream(new FileOutputStream("C:\\System generated dat files\\Invoice.pdf")))){
                os.writeObject(fileB);
                os.flush();
                if(Desktop.isDesktopSupported()){
                    Desktop.getDesktop().open(new File("C:\\System generated dat files\\Invoice.pdf"));
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {

        }
    }
}
