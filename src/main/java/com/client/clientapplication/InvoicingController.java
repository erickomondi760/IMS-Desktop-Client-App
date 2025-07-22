package com.client.clientapplication;

import com.quickrest.entities.*;
import com.quickrest.resources.ApplicationPath;
import com.quickrest.resources.InvoiceData;
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
import javafx.collections.ObservableSet;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

public class InvoicingController implements Initializable {
    @FXML
    private AnchorPane invoicingAnchor;
    @FXML
    private Button createInvoice;
    @FXML
    private TableView<Prices> invoiceProductsTable;
    @FXML
    private TextField invoiceSubTotal;
    @FXML
    private TableView<InvoiceData> invoiceTable;
    @FXML
    private TextField invoiceTotal;
    @FXML
    private TextField invoiceVat;
    @FXML
    private ComboBox<String> invoicingBranchText;
    @FXML
    private TextField invoicingCodeText;
    @FXML
    private TextField invoicingCostText;
    @FXML
    private TextField invoicingDescriptionText;
    @FXML
    private TextField invoicingDiscountText;
    @FXML
    private ComboBox<String> invoicingProducerText;
    @FXML
    private TextField invoicingSearchLabel;
    @FXML
    private TextField invoicingSearchLabel1;
    @FXML
    private TextField invoicingStockText;
    @FXML
    private ComboBox<String> invoicingCompanyCombo;
    @FXML
    private TextField invoicingQuantityText;
    @FXML
    private ComboBox<String> invoicingClientText;
    @FXML
    private ComboBox<String> invoicingTransporter;
    @FXML
    private ComboBox<String> invoicingVehicleNumber;
    @FXML
    private CheckBox productCheckBox;
    @FXML
    private Button invoicingSaveButton;
    @FXML
    private Label invoiceBarode;
    @FXML
    private TextField invoiceTotalDiscount;
    @FXML
    private ComboBox<String> invoicingOutlet;

    String user;
    private static final String HOST = ApplicationPath.getHqPath();
    ObservableList<InvoiceData> invoicedItem = FXCollections.observableArrayList();
    ObservableList<BranchDetails> branchDetails = FXCollections.observableArrayList();
    ObservableList<CompanyDetails> companyDetails = FXCollections.observableArrayList();
    ObservableList<ClientTraderOutlet> outlet = FXCollections.observableArrayList();
    ObservableList<ClientTrader> client = FXCollections.observableArrayList();
    TableView<Invoice> invoiceSummaryTableView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Setting branches
        showBranches();
        //Setting clients
        showClients();
        //Setting transporters
        showTransporters();
        //Setting vehicle
        invoicingTransporter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String transporter = invoicingTransporter.getValue();
                if (transporter != null) {
                    showVehicles(transporter);
                }
            }
        });

        //Sett suppliers
        showSuppliers();

        //Disabling products search label
        invoicingSearchLabel.setDisable(true);

        //Disabling checkbox
        invoicingProducerText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                productCheckBox.setSelected(false);
            }
        });

        //Populating products table
        showProducts();

        //Setting values
        setProductData();

        //Moving focus to quantity field
        invoiceProductsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (invoiceProductsTable.getSelectionModel().getSelectedItem() != null)
                    invoicingQuantityText.requestFocus();
            }
        });

        //Moving focus to save button
        invoicingQuantityText.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                    invoicingSaveButton.requestFocus();
            }
        });

        //Show outlets
        showOutlets();
        //Show company
        showCompany();


    }

    //Displaying branch details
    private void showBranches() {
        WebTarget webTarget = ClientBuilder.newClient().target(HOST + "/branch/findAll");
        GenericType<List<BranchDetails>> genericType = new GenericType<>() {
        };
        List<BranchDetails> list = webTarget.request(MediaType.APPLICATION_JSON).get(genericType);
        branchDetails = FXCollections.observableArrayList(list);
        ObservableList<String> observableList = FXCollections.observableArrayList();
        list.forEach(e -> {
            observableList.add(e.getBranchName());
        });

        invoicingBranchText.setItems(observableList);
    }

    //Displaying outlets
    private void showOutlets() {
        WebTarget webTarget = ClientBuilder.newClient().target(HOST + "/outlet/findAll");
        GenericType<List<ClientTraderOutlet>> genericType = new GenericType<>() {};
        List<ClientTraderOutlet> list = webTarget.request(MediaType.APPLICATION_JSON).get(genericType);
        outlet = FXCollections.observableArrayList(list);
        ObservableList<String> observableList = FXCollections.observableArrayList();

        list.forEach(e -> {
            observableList.add(e.getLocation());
        });

        invoicingOutlet.setItems(observableList);
    }

    //Displaying clients
    private void showCompany() {
        WebTarget webTarget = ClientBuilder.newClient().target(HOST + "/company/findAll");
        GenericType<List<CompanyDetails>> genericType = new GenericType<>() {
        };
        List<CompanyDetails> list = webTarget.request(MediaType.APPLICATION_JSON).get(genericType);
        companyDetails = FXCollections.observableArrayList(list);
        ObservableList<String> observableList = FXCollections.observableArrayList();

        list.forEach(e -> {
            observableList.add(e.getName());
        });

        invoicingCompanyCombo.setItems(observableList);
    }

    //Displaying clients
    private void showClients() {
        WebTarget webTarget = ClientBuilder.newClient().target(HOST + "/client/findAll");
        GenericType<List<ClientTrader>> genericType = new GenericType<>() {
        };
        List<ClientTrader> list = webTarget.request(MediaType.APPLICATION_JSON).get(genericType);
        ObservableList<String> observableList = FXCollections.observableArrayList();
        client = FXCollections.observableArrayList(list);

        list.forEach(e -> {
            observableList.add(e.getIdentity());
        });

        invoicingClientText.setItems(observableList);
    }

    //Displaying clients
    private void showTransporters() {
        WebTarget webTarget = ClientBuilder.newClient().target(HOST + "/transporter/findAll");
        GenericType<List<Transporter>> genericType = new GenericType<>() {
        };
        List<Transporter> list = webTarget.request(MediaType.APPLICATION_JSON).get(genericType);
        ObservableList<String> observableList = FXCollections.observableArrayList();

        list.forEach(e -> {
            observableList.add(e.getIdentity());
        });

        invoicingTransporter.setItems(observableList);
    }

    //Displaying vehicle
    private void showVehicles(String transporter) {
        WebTarget webTarget = ClientBuilder.newClient().target(HOST + "/transporter/find/" + transporter);
        GenericType<List<Transporter>> genericType = new GenericType<>() {
        };
        List<Transporter> list = webTarget.request(MediaType.APPLICATION_JSON).get(genericType);
        ObservableList<String> observableList = FXCollections.observableArrayList();

        list.forEach(e -> {
            observableList.add(e.getVehicleNumber());
        });

        invoicingVehicleNumber.setItems(observableList);
    }

    //Displaying suppliers
    private void showSuppliers() {
        WebTarget webTarget = ClientBuilder.newClient().target(HOST + "/product/findAll");
        GenericType<List<Product>> genericType = new GenericType<>() {
        };
        List<Product> list = webTarget.request(MediaType.APPLICATION_JSON).get(genericType);
        ObservableSet<String> observableList = FXCollections.observableSet();
        list.forEach(e -> {
            observableList.add(e.getSupplier().getIdentity());
        });


        invoicingProducerText.setItems(FXCollections.observableArrayList(observableList));
    }

    //Displaying products
    public void showProducts() {
        invoicingProducerText.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                WebTarget webTarget = ClientBuilder.newClient().target(HOST + "/prices/searchProductBySupplier/" + t1);
                GenericType<List<Prices>> genericType = new GenericType<>() {
                };
                List<Prices> list = webTarget.request(MediaType.APPLICATION_JSON).get(genericType);
                ObservableList<Prices> observableList = FXCollections.observableArrayList(list);
                invoicingSearchLabel.setDisable(true);
                invoiceProductsTable.setItems(observableList);
            }
        });


    }

    //Filtering the table
    @FXML
    public void filterProductsTable() {
        if (productCheckBox.isSelected()) {
            invoicingSearchLabel.setDisable(false);

            if (invoicingProducerText.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Choose a producer to proceed");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK)
                    alert.close();
                productCheckBox.setSelected(false);

            } else {
                WebTarget webTarget = ClientBuilder.newClient().target(HOST + "/prices/searchProductBySupplier/" + invoicingProducerText.getValue());
                GenericType<List<Prices>> genericType = new GenericType<>() {
                };
                List<Prices> list = webTarget.request(MediaType.APPLICATION_JSON).get(genericType);
                ObservableList<Prices> products = FXCollections.observableArrayList(list);

                FilteredList<Prices> filteredList = new FilteredList<>(products, e -> true);
                invoicingSearchLabel.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                        filteredList.setPredicate(new Predicate<Prices>() {
                            @Override
                            public boolean test(Prices product) {
                                if (t1.isEmpty() || t1.isBlank()) {
                                    return true;
                                } else if (product.getBarcode().contains(t1)) {
                                    return true;
                                } else if (product.getCode().contains(t1)) {
                                    return true;
                                } else if (product.getDescription().contains(t1.toUpperCase())) {
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });
                    }
                });
                SortedList<Prices> sortedList = new SortedList<>(filteredList);
                sortedList.comparatorProperty().bind(invoiceProductsTable.comparatorProperty());
                invoiceProductsTable.setItems(sortedList);
            }
        } else {
            invoicingSearchLabel.setDisable(true);
        }
    }

    //Setting data into textfields
    private void setProductData() {
        invoiceProductsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        invoiceProductsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Prices>() {
            @Override
            public void changed(ObservableValue<? extends Prices> observableValue, Prices prices, Prices t1) {
                Prices price = invoiceProductsTable.getSelectionModel().getSelectedItem();
                if (price != null) {
                    WebTarget webTarget = ClientBuilder.newClient().target(ApplicationPath.getApplicationPath()
                            + "/stock/find/" + price.getCode());
                    GenericType<List<ProductStock>> genericType = new GenericType<>() {
                    };
                    List<ProductStock> list = webTarget.request(MediaType.APPLICATION_JSON).get(genericType);

                    if (list.size() > 0) {
                        list.sort((o1, o2) -> {
                            if (o1.getStockIndex() > o2.getStockIndex())
                                return 1;
                            else if (o2.getStockIndex() > o1.getStockIndex())
                                return -1;
                            else
                                return 0;
                        });
                        invoicingCodeText.setText(price.getCode());
                        invoicingDescriptionText.setText(price.getDescription());
                        invoicingCostText.setText(String.format("%,.2f", price.getInclusive()));
                        invoicingStockText.setText(String.format("%,d", list.get(list.size() - 1).getBal()));
                        invoicingDiscountText.setText(String.format("%.2f", price.getDisc()));
                        invoiceBarode.setText(price.getBarcode());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Error!!!");
                        alert.setHeaderText("Stock not updated");
                        alert.setContentText("Update the stock to proceed");
                        Optional<ButtonType> optional = alert.showAndWait();
                        if (optional.isPresent() && optional.get() == ButtonType.OK)
                            alert.close();
                    }

                }
            }
        });
    }

    //Inserting an item into the invoice table
    @FXML
    public void addIntoInvoiceTable() {
        if (invoicingBranchText.getValue() == null || invoicingClientText.getValue() == null || invoicingTransporter.getValue() == null
                || invoicingVehicleNumber.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!!!");
            alert.setHeaderText("Some fields maybe empty");
            alert.setContentText("Check on Branch/Client/Transporter/Vehicle");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get() == ButtonType.OK)
                alert.close();
        } else {
            String stock = invoicingStockText.getText();
            stock = stock.replace(",", "");
            if (Integer.parseInt(stock) == 0 ||
                    Integer.parseInt(stock) < Integer.parseInt(invoicingQuantityText.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error!!!");
                alert.setHeaderText("Insufficient Stock");
                alert.setContentText("Quantity invoiced is more than the current stock");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK)
                    alert.close();
            } else {
                WebTarget webTarget = ClientBuilder.newClient().target(HOST + "/prices/findByCode/" + invoicingCodeText.getText());
                GenericType<List<Prices>> genericType = new GenericType<>() {
                };
                List<Prices> list = webTarget.request(MediaType.APPLICATION_JSON).get(genericType);

                InvoiceData invoiceData = new InvoiceData();
                for(CompanyDetails c:companyDetails){
                    if(c.getName().equals(invoicingCompanyCombo.getValue()))
                        invoiceData.setCompanyDetails(c);

                }

                for(BranchDetails b:branchDetails){
                    if(b.getBranchName().equals(invoicingBranchText.getValue()))
                        invoiceData.setBranchDetails(b);

                }
                for(ClientTrader c:client){
                    if(c.getIdentity().equals(invoicingClientText.getValue())){
                        invoiceData.setClientName(c.getIdentity());
                        invoiceData.setClientAddress(c.getAddress());
                        invoiceData.setClientEmail(c.getEmail1());
                    }


                }

                for(ClientTraderOutlet o:outlet){
                    if(o.getLocation().equals(invoicingOutlet.getValue())){
                        invoiceData.setOutletPnoneNumber(o.getPhoneNumber());
                        invoiceData.setOutletEmail(o.getEmail());
                        invoiceData.setOutletLocation(o.getLocation());

                    }

                }
                invoiceData.setBranchName(invoicingBranchText.getValue());
                invoiceData.setUserName(getUser());
                invoiceData.setClientName(invoicingClientText.getValue());
                invoiceData.setTransporter(invoicingTransporter.getValue());
                invoiceData.setVehicle(invoicingVehicleNumber.getValue());
                invoiceData.setCode(invoicingCodeText.getText());
                invoiceData.setBarcode(invoiceBarode.getText());
                invoiceData.setDescription(invoicingDescriptionText.getText());
                invoiceData.setQuantity(Integer.parseInt(invoicingQuantityText.getText()));
                invoiceData.setCostExclusive(list.get(0).getSellingPrice());
                invoiceData.setVat(list.get(0).getVat());
                invoiceData.setCostInclusive(list.get(0).getSellingPrice() * invoiceData.getVat());
                invoiceData.setDisc(list.get(0).getDisc());
                invoiceData.setAmountExclusive(Integer.parseInt(invoicingQuantityText.getText()) * list.get(0).getExcl());
                invoiceData.setAmountInclusive(Integer.parseInt(invoicingQuantityText.getText()) * list.get(0).getInclusive());
                invoiceData.setVatAmount(Integer.parseInt(invoicingQuantityText.getText()) * list.get(0).getInclusive() -
                        Integer.parseInt(invoicingQuantityText.getText()) * list.get(0).getExcl());
;               invoiceData.setInvoiceDate(LocalDate.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
                invoiceData.setUnits(list.get(0).getUnit());
                invoiceData.setPackaging(list.get(0).getPackaging());
                invoiceData.setStatus("Pending");

                boolean status = true;

                if (invoicedItem.size() > 0) {
                    for (InvoiceData inv : invoicedItem) {
                        if (inv.getCode().equals(invoiceData.getCode())) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setContentText("Item already invoiced");
                            Optional<ButtonType> optional = alert.showAndWait();
                            if (optional.isPresent() && optional.get() == ButtonType.OK)
                                alert.close();
                            status = false;
                        }

                    }
                }

                if (status) {
                    invoicedItem.add(invoiceData);
                    invoiceTable.setItems(invoicedItem);
                }

                ObservableList<InvoiceData> tableInvoice = invoiceTable.getItems();
                //Setting invoice total
                double subTotal = 0;
                double vat = 0;
                double total = 0;
                double totalDisc = 0;

                for (InvoiceData invoice1 : tableInvoice) {
                    subTotal += invoice1.getAmountExclusive();
                    total += invoice1.getAmountInclusive();
                    vat += (invoice1.getAmountInclusive() - invoice1.getAmountExclusive());
                    totalDisc += (((invoice1.getDisc() * invoice1.getQuantity()) / 100) * subTotal);
                }

                System.out.println("Size=" + invoicedItem.size());
                invoiceTable.setItems(invoicedItem);
                invoiceSubTotal.setText(String.format("%,.2f", subTotal));
                invoiceVat.setText(String.format("%,.2f", vat));
                invoiceTotal.setText(String.format("%,.2f", total));
                invoiceTotalDiscount.setText(String.format("%,.2f", totalDisc));

                invoicingCostText.setText("");
                invoicingDescriptionText.setText("");
                invoicingQuantityText.setText("");
                invoicingStockText.setText("");
                invoicingDiscountText.setText("");
                invoiceBarode.setText("");

            }
        }

    }

    @FXML
    public void invoice() {
        List<InvoiceData> dataList = invoiceTable.getItems();
        if(dataList.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("No item to invoice");
            alert.setContentText("There aro no items in the table");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get() == ButtonType.OK) {
                alert.close();
            }
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure tou want to save?");
        Optional<ButtonType> optional = alert.showAndWait();
        if (optional.isPresent() && optional.get() == ButtonType.OK) {

            //Searching branch inv numbers
            WebTarget webTarget = ClientBuilder.newClient().target(ApplicationPath.getApplicationPath() +
                    "/invoiceNumber/findAll");
            GenericType<List<InvoiceNumber>> genericType = new GenericType<>() {};
            List<InvoiceNumber> list = webTarget.request(MediaType.APPLICATION_JSON).get(genericType);

            //Searching hq inv numbers
            WebTarget webTarget2 = ClientBuilder.newClient().target(HOST + "/hqInvoiceNumber/findAll");
            GenericType<List<HQInvoiceNumber>> genericType2 = new GenericType<>() {};
            List<HQInvoiceNumber> list2 = webTarget2.request(MediaType.APPLICATION_JSON).get(genericType2);

            long hqInvNum = 0;
            if (list2.isEmpty()) {
                hqInvNum++;
            } else {
                hqInvNum = list2.get(list2.size() - 1).getHqInvoiceNumber();
                hqInvNum++;
            }

            Response response = null;
            Response resp = null;

            int invoiceNumber = 0;

            if (list.size() > 0) {
                Collections.sort(list, new Comparator<InvoiceNumber>() {
                    @Override
                    public int compare(InvoiceNumber o1, InvoiceNumber o2) {
                        if (o1.getInvoiceNumber() > o2.getInvoiceNumber())
                            return 1;
                        else if (o2.getInvoiceNumber() > o1.getInvoiceNumber())
                            return -1;
                        else
                            return 0;
                    }
                });
                invoiceNumber = list.get(list.size() - 1).getInvoiceNumber();
                invoiceNumber++;
            }else {
                invoiceNumber = 1;
            }


            String excl = String.format("%s", invoiceSubTotal.getText());
            excl = excl.replace(",", "");

            String vat = String.format("%s", invoiceVat.getText());
            vat = vat.replace(",", "");

            String incl = String.format("%s", invoiceTotal.getText());
            incl = incl.replace(",", "");

            double exclusive = Double.parseDouble(excl);
            double tax = Double.parseDouble(vat);
            double inclusive = Double.parseDouble(incl);

            List<InvoiceData> invoiceList = invoiceTable.getItems();
            List<InvoiceData> finalInvoice = new ArrayList<>();
            for (InvoiceData invoice : invoiceList) {
                invoice.setInvoiceNumber(invoiceNumber);
                System.out.println("Num="+invoiceNumber);
                invoice.setHqInvoiceNumber(hqInvNum);
                invoice.setInvoiceSubTotal(exclusive);
                invoice.setInvoiceVat(tax);
                invoice.setInvoiceTotal(inclusive);
                finalInvoice.add(invoice);
            }
            JRBeanCollectionDataSource jrBean = new JRBeanCollectionDataSource(finalInvoice);
            try {
                JasperDesign jasperDesign = JRXmlLoader.load("src\\main\\resources\\" +
                        "Reports\\Invoice.jrxml");
                Map<String, Object> invoiceDetails = new HashMap<>();
                invoiceDetails.put("address", finalInvoice.get(0).getCompanyDetails().getAddress());
                invoiceDetails.put("companyEmail", finalInvoice.get(0).getCompanyDetails().getEmail());
                invoiceDetails.put("companyName",finalInvoice.get(0).getCompanyDetails().getName());
                invoiceDetails.put("companyPhone", finalInvoice.get(0).getCompanyDetails().getContact());
                invoiceDetails.put("clientName", finalInvoice.get(0).getClientName());
                invoiceDetails.put("clientAddress", finalInvoice.get(0).getClientAddress());
                invoiceDetails.put("clientEmail", finalInvoice.get(0).getClientEmail());
                invoiceDetails.put("invoiceNumber", finalInvoice.get(0).getInvoiceNumber());
                invoiceDetails.put("outletLocation", finalInvoice.get(0).getOutletLocation());
                invoiceDetails.put("outletEmail", finalInvoice.get(0).getOutletEmail());
                invoiceDetails.put("outletContact", finalInvoice.get(0).getOutletPnoneNumber());
                invoiceDetails.put("collection", jrBean);
                invoiceDetails.put("invoiceSubTotal", finalInvoice.get(0).getInvoiceSubTotal());
                invoiceDetails.put("invoiceDiscount", finalInvoice.get(0).getInvoiceDiscount());
                invoiceDetails.put("invoiceVat", finalInvoice.get(0).getInvoiceVat());
                invoiceDetails.put("invoiceTotal", finalInvoice.get(0).getInvoiceTotal());

                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, invoiceDetails, new JREmptyDataSource());
                JasperViewer jv = new JasperViewer(jasperPrint,false);
                jv.setVisible(true);
                jv.setExtendedState(Frame.MAXIMIZED_BOTH);
                JasperViewer.setDefaultLookAndFeelDecorated(true);

                OutputStream outputStream = new FileOutputStream("src\\main\\resources\\Reports\\Invoice.pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            finalInvoice.forEach(e -> {
                Path path = Paths.get("src\\main\\resources\\Reports\\Invoice.pdf");
                try {
                    byte[] bites = Files.readAllBytes(path);
                    e.setInvoiceFile(bites);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            });


            //Creating data list
            Invoice invoice = new Invoice();
            invoice.setInvoiceFile(finalInvoice.get(0).getInvoiceFile());
            invoice.setOutletName(finalInvoice.get(0).getOutletLocation());
            invoice.setInvoiceDate(finalInvoice.get(0).getInvoiceDate());
            invoice.setInvoiceDiscount(finalInvoice.get(0).getInvoiceDiscount());
            invoice.setInvoiceNumber(finalInvoice.get(0).getInvoiceNumber());
            System.out.println("num2="+finalInvoice.get(0).getInvoiceNumber());
            invoice.setClientName(finalInvoice.get(0).getClientName());
            invoice.setBranchName(finalInvoice.get(0).getBranchName());
            invoice.setInvoiceTotal(finalInvoice.get(0).getInvoiceTotal());
            invoice.setBranchDetails(finalInvoice.get(0).getBranchDetails());
            invoice.setStatus(finalInvoice.get(0).getStatus());
            invoice.setHqInvoiceNumber(finalInvoice.get(0).getHqInvoiceNumber());
            invoice.setInvoiceVat(finalInvoice.get(0).getInvoiceVat());
            invoice.setInvoiceSubTotal(finalInvoice.get(0).getInvoiceSubTotal());
            invoice.setCompanyName(finalInvoice.get(0).getCompanyDetails().getName());
            invoice.setUserName(finalInvoice.get(0).getUserName());
            invoice.setTransporter(finalInvoice.get(0).getTransporter());

            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            try {
                ObjectOutputStream os = new ObjectOutputStream(bo);
                os.writeObject(finalInvoice);
                os.flush();
                byte[] dataBytes = bo.toByteArray();
                invoice.setCreatedInvoice(dataBytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            //Persisting an invoice
            webTarget = ClientBuilder.newClient().target(ApplicationPath.getApplicationPath() + "/invoices/create/");
            response = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(invoice,
                    MediaType.APPLICATION_JSON));

            if (response.getStatus() == 201) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setContentText("Invoice successfully created \n" +
                        "Invoice number = " + invoice.getInvoiceNumber());
                Optional<ButtonType> optional1 = alert1.showAndWait();
                if (optional1.isPresent() && optional1.get() == ButtonType.OK)
                    alert1.close();

                Stage stage = (Stage) createInvoice.getScene().getWindow();
                stage.close();

                //Updating invoice table
                WebTarget target = ClientBuilder.newClient().target(ApplicationPath.getApplicationPath() + "/invoices/findAll");
                GenericType<List<Invoice>> genericType1 = new GenericType<>() {};
                List<Invoice> invoices = target.request(MediaType.APPLICATION_JSON).get(genericType1);
                ObservableList<Invoice> observableList = FXCollections.observableArrayList(invoices);
                Collections.sort(observableList, new Comparator<Invoice>() {
                    @Override
                    public int compare(Invoice o1, Invoice o2) {
                        if (o1.getInvoiceNumber() > o2.getInvoiceNumber())
                            return 1;
                        if (o2.getInvoiceNumber() > o1.getInvoiceNumber())
                            return -1;
                        else
                            return 0;
                    }
                });
                getInvoiceSummaryTableView().setItems(observableList);


            } else {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setHeaderText("Error Occurred");
                alert1.setContentText("Info:" + response.readEntity(String.class));
                Optional<ButtonType> optional1 = alert1.showAndWait();
                if (optional1.isPresent() && optional1.get() == ButtonType.OK)
                    alert1.close();
            }

        } else if (optional.isPresent() && optional.get() == ButtonType.CANCEL) {
            alert.close();
        }

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public TableView<Invoice> getInvoiceSummaryTableView() {
        return invoiceSummaryTableView;
    }

    public void setInvoiceSummaryTableView(TableView<Invoice> invoiceSummaryTableView) {
        this.invoiceSummaryTableView = invoiceSummaryTableView;
    }

}
