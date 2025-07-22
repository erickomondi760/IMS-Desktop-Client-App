package com.client.clientapplication;

import com.quickrest.entities.*;
import com.quickrest.resources.ApplicationPath;
import com.quickrest.resources.PODetails;
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
import javafx.concurrent.Task;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class LPOGeneratorController implements Initializable {
    @FXML
    private ComboBox<String> branchCombo;
    @FXML
    private ComboBox<String> supplierCombo;
    @FXML
    private TableView<Prices> productsTable;
    @FXML
    private TableView<LPOPrice> lpoGeneratedTable;
    @FXML
    private TextField code;
    @FXML
    private TextField lpoGenCostIncxclusive;
    @FXML
    private TextField lpoGenDescription;
    @FXML
    private TextField lpoGenQty;
    @FXML
    private TextField searchBox;
    @FXML
    private CheckBox checkBox;
    @FXML
    private Button save;
    @FXML
    private Button done;
    @FXML
    private Label lpoAmountLabel;
    @FXML
    private Label date;
    @FXML
    private TextField barcode;
    @FXML
    private CheckBox termsCheck;
    @FXML
    TextField lpoeneratorTermsText;

    private String user;

    ObservableList<LPOPrice> lpoPriceObservableList = FXCollections.observableArrayList();
    private static final String HOST = ApplicationPath.getApplicationPath();
    private TableView<ProductLpos> productLposTableView;
    List<BranchDetails> branchDetails = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        searchBox.setDisable(true);
        save.setDisable(true);
        code.setEditable(false);
        lpoGenDescription.setEditable(false);
        lpoGenCostIncxclusive.setEditable(false);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d MMMM,yyyy");
        LocalDate ld = LocalDate.now();
        date.setText(String.valueOf(ld.format(dtf)));

        //Disablin terms
        lpoeneratorTermsText.setDisable(true);


        //Setting lpo terms
        termsCheck.setOnAction(actionEvent -> {
            if(termsCheck.isSelected()){
                lpoeneratorTermsText.setDisable(false);
            }else {
                lpoeneratorTermsText.setDisable(true);
            }
        });

        //Pupulating supplier combox with suppliers
        WebTarget target = ClientBuilder.newClient().target(ApplicationPath.getHqPath() + "/prices/findAll");
        GenericType<List<Prices>> genericType = new GenericType<>() {
        };
        List<Prices> list = target.request(MediaType.APPLICATION_JSON).get(genericType);
        Task<ObservableList<String>> task = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws Exception {
                ObservableList<String> ol = FXCollections.observableArrayList();
                Set<String> set = new HashSet<>();
                list.forEach(prices -> {
                    set.add(prices.getSupplier());
                });
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()) {
                    ol.add(iterator.next());
                }

                return ol;
            }
        };
        new Thread(task).start();
        supplierCombo.itemsProperty().bind(task.valueProperty());

        //Adding products to product table
        supplierCombo.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                WebTarget target1 = ClientBuilder.newClient().target(ApplicationPath.getHqPath() +
                        "/prices/searchProductBySupplier/" + t1);
                GenericType<List<Prices>> genericType = new GenericType<>() {
                };
                List<Prices> products = target1.request(MediaType.APPLICATION_JSON).get(genericType);
                ObservableList<Prices> ol = FXCollections.observableArrayList(products);
                productsTable.setItems(ol);
                searchBox.setDisable(true);
            }
        });

        //Setting details in textfields
        productsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        productsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Prices>() {
            @Override
            public void changed(ObservableValue<? extends Prices> observableValue, Prices prices, Prices t1) {
                Prices prices1 = productsTable.getSelectionModel().getSelectedItem();
                if (prices1 != null) {
                    code.setText(prices1.getCode());
                    lpoGenCostIncxclusive.setText(String.valueOf(prices1.getInclusive()));
                    lpoGenDescription.setText(prices1.getDescription());
                    barcode.setText(prices1.getBarcode());
                }

            }
        });

        //Setting the cusor in the lpoGenQty textfield
        productsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                lpoGenQty.requestFocus();
            }
        });

        //disabling checkbox
        supplierCombo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                checkBox.setSelected(false);
            }
        });

        //Accepting only digits for lpoGenQty
        lpoGenQty.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.matches("\\d*")) {
                    lpoGenQty.setText(t1.replaceAll(".*", ""));
                }
            }
        });

        //enabling save button
        lpoGenQty.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.equals("") && Integer.parseInt(t1) > 0) {
                    save.setDisable(false);
                } else {
                    save.setDisable(true);
                }
            }
        });
        //Enter values into the lpo table
        save.setOnAction(e -> {
            LPOPrice lpoPrice = new LPOPrice();
            double totalCost = productsTable.getSelectionModel().getSelectedItem()
                    .getInclusive() * Double.parseDouble(lpoGenQty.getText());
            System.out.println("cost="+totalCost);
            lpoPrice.setCode(code.getText());
            lpoPrice.setCostIncl(
                    productsTable.getSelectionModel().getSelectedItem().getInclusive()
                            * Integer.parseInt(lpoGenQty.getText()));
            lpoPrice.setDescription(lpoGenDescription.getText());
            lpoPrice.setTotal(totalCost);
            lpoPrice.setQuantity(Integer.parseInt(lpoGenQty.getText()));
            lpoPrice.setBarcode(barcode.getText());
            lpoPrice.setUnits(productsTable.getSelectionModel().getSelectedItem().getUnit());
            lpoPrice.setCostExcl(productsTable.getSelectionModel()
                    .getSelectedItem().getExcl() *
                    Integer.parseInt(lpoGenQty.getText()));
            double vat = Double.parseDouble(String.format("%.2f",lpoPrice.getCostIncl() - lpoPrice.getCostExcl()));
            lpoPrice.setVat(vat);
            System.out.println("Vat="+(lpoPrice.getCostIncl() - lpoPrice.getCostExcl()));

            boolean state = true;
            for (LPOPrice lpo : lpoPriceObservableList) {
                if (lpo.getCode().equals(lpoPrice.getCode())) {
                    state = false;
                }
            }
            if (state) {
                double sum = 0;
                lpoPriceObservableList.add(lpoPrice);
                lpoGeneratedTable.setItems(lpoPriceObservableList);
                for (LPOPrice price : lpoPriceObservableList) {
                    double totalCost1 = price.getTotal();
                    sum = sum + totalCost1;
                }
                lpoAmountLabel.setText(String.format("%,.2f", sum));
                code.setText("");
                barcode.setText("");
                lpoGenDescription.setText("");
                lpoGenCostIncxclusive.setText("");
                lpoGenQty.setText("");
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Item already exist in the order");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK)
                    alert.close();
            }

        });

        //enabling and disabling save button
        lpoGenQty.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.equals("") && !t1.equals("0")) {
                    save.setDisable(false);
                } else {
                    save.setDisable(true);
                }

            }
        });

        //Calling the enter key and moving it to the sava button
        lpoGenQty.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                    save.requestFocus();
            }
        });


        //Returning all branches
        getBranch();


    }

    //Fetching branches
    public void getBranch() {
        WebTarget target = ClientBuilder.newClient().target(ApplicationPath.getHqPath() + "/branch/findAll");
        GenericType<List<BranchDetails>> genericType = new GenericType<>() {
        };
        List<BranchDetails> list = target.request(MediaType.APPLICATION_JSON).get(genericType);
        branchDetails = list;
        ObservableList<String> branch = FXCollections.observableArrayList();
        list.forEach(b -> {
            branch.add(b.getBranchName());
        });
        branchCombo.setItems(branch);
    }

    //Searching for products in the table
    @FXML
    public void searchItem() {
        if (supplierCombo.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Choose a supplier to proceed");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get() == ButtonType.OK)
                alert.close();
            checkBox.setSelected(false);
            return;
        }
        if (checkBox.isSelected()) {
            searchBox.setDisable(false);
            WebTarget target1 = ClientBuilder.newClient().target(ApplicationPath.getHqPath() +
                    "/prices/searchProductBySupplier/" + supplierCombo.getValue());
            GenericType<List<Prices>> genericType = new GenericType<>() {
            };

            List<Prices> prices = target1.request(MediaType.APPLICATION_JSON).get(genericType);
            ObservableList<Prices> prices1 = FXCollections.observableArrayList(prices);

            FilteredList<Prices> pricesFilteredList = new FilteredList<>(prices1, p -> true);
            searchBox.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    pricesFilteredList.setPredicate(prices -> {
                        if (t1.isBlank() || t1.isEmpty()) {
                            return true;
                        } else if (prices.getCode().contains(t1)) {
                            return true;
                        } else if (prices.getBarcode().contains(t1)) {
                            return true;
                        } else if (prices.getDescription().contains(t1.toUpperCase())) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
            });
            SortedList<Prices> pricesSortedList = new SortedList<>(pricesFilteredList);
            pricesSortedList.comparatorProperty().bind(productsTable.comparatorProperty());
            productsTable.setItems(pricesSortedList);
        } else {
            searchBox.setDisable(true);
        }

    }

    @FXML
    public void saveLPO() {
        if(branchCombo.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Branch has not been set");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent() && optional.get() == ButtonType.OK)
                alert.close();
        }else {
            String supplier = supplierCombo.getValue();
            double lpoAmount = Double.parseDouble(lpoAmountLabel.getText().replaceAll("[,]", ""));


            //fetching the current lpo number
            WebTarget webTarget = ClientBuilder.newClient().target(HOST + "/lponumber/findAll");
            GenericType<List<PoNumber>> gl = new GenericType<>() {
            };
            List<PoNumber> list = webTarget.request(MediaType.APPLICATION_JSON).get(gl);

            long poNumber = 0;
            if (list.size() > 0) {
                Collections.sort(list, new Comparator<PoNumber>() {
                    @Override
                    public int compare(PoNumber o1, PoNumber o2) {
                        if (o1.getLpoNumber() > o2.getLpoNumber())
                            return 1;
                        if (o2.getLpoNumber() > o1.getLpoNumber())
                            return -1;

                        if (o1.getLpoNumber() == o2.getLpoNumber()) {
                            return 0;
                        }
                        return 0;
                    }
                });
                poNumber = list.get(list.size() - 1).getLpoNumber();
                poNumber++;

            }else {
                poNumber++;
            }
            int counter = 0;
            List<PODetails> poDetailsList = new ArrayList<>();
            if (!lpoPriceObservableList.isEmpty()) {
                for (LPOPrice lp : lpoPriceObservableList) {
                    PODetails poDetails = new PODetails();
                    poDetails.setCode(lp.getCode());
                    poDetails.setBarcode(lp.getBarcode());
                    poDetails.setDescription(lp.getDescription());
                    poDetails.setQuantity(lp.getQuantity());
                    poDetails.setCostExclusive(lp.getCostExcl());
                    poDetails.setTotalCost(lp.getTotal());
                    poDetails.setSupplier(supplier);
                    poDetails.setLpoAmount(lpoAmount);
                    poDetails.setUnits(lp.getUnits());
                    poDetails.setCostInclusive(lp.getCostIncl());
                    poDetails.setVat(lp.getVat());
                    poDetails.setTerms(Integer.parseInt(lpoeneratorTermsText.getText()));
                    poDetailsList.add(poDetails);

                    //Fetching packaging
                    webTarget = ClientBuilder.newClient().target(ApplicationPath.getHqPath()+"/product/findByCode/"+lp.getCode());
                    GenericType<List<Product>> gn = new GenericType<>(){};
                    List<Product> productList = webTarget.request(MediaType.APPLICATION_JSON).get(gn);
                    poDetails.setPackaging(productList.get(0).getPackaging());
                    counter++;
                }
            }
            Response response1 = saveLPOData(supplier, lpoAmount,poNumber, poDetailsList);


            if (response1.getStatus() == 204) {
                if (counter == lpoPriceObservableList.size() && response1.getStatus() == 204) {
                    Stage stage = (Stage) save.getScene().getWindow();
                    stage.close();
                    updateLposTable();
                }
            }

        }

    }

    //Creates a new product lpo
    private Response saveLPOData(String supplier, double lpoValue,long poNumber, List<PODetails> list) {
        ProductLpos productLpos = new ProductLpos();
        Date date1 = new Date();
        productLpos.setDateRaised(LocalDate.ofInstant(date1.toInstant(),ZoneId.systemDefault()));
        productLpos.setLpoNumber(poNumber);
        productLpos.setAmount(lpoValue);
        productLpos.setSupplier(supplier);
        productLpos.setReceived("Not received");
        productLpos.setStatus("Pending");
        productLpos.setUserName(ApplicationPath.getUser());
        productLpos.setBranchName(branchCombo.getValue());
        LocalDate localDate = LocalDate.now();
        productLpos.setExpiryDate(localDate.plusDays(14));

        //Fetching company info
        boolean found = false;
        String companyName = null;
        BranchDetails branchDetails1 = null;
        for(BranchDetails b:branchDetails){
            if(b.getBranchName().equals(branchCombo.getValue())){
                companyName = b.getCompanyName();
                branchDetails1 = b;
                System.out.println(b);
                found = true;
            }
        }
        List<CompanyDetails> pd = new ArrayList<>();
        if(found){
            WebTarget webTarget = ClientBuilder.newClient().target(ApplicationPath.getHqPath()+"/company/findByName/"
                    + companyName);
            GenericType<List<CompanyDetails>> genericType = new GenericType<>() {};
            pd = webTarget.request(MediaType.APPLICATION_JSON).get(genericType);
        }



        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
        try {
            JasperDesign jasperDesign = JRXmlLoader.load("src\\main\\resources\\Reports\\" +
                    "Lpo.jrxml");
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("companyName",pd.get(0).getName());
            dataMap.put("branch",productLpos.getBranchName());
            dataMap.put("collection",dataSource);
            dataMap.put("supplier",productLpos.getSupplier());
            dataMap.put("email",pd.get(0).getEmail());
            dataMap.put("phoneNumber",pd.get(0).getContact());
            dataMap.put("address",pd.get(0).getAddress());
            dataMap.put("userName",productLpos.getUserName());
            dataMap.put("lpoNumber",productLpos.getLpoNumber());
            ZoneId zoneId = java.time.ZoneId.systemDefault();
            dataMap.put("expiryDate",Date.from(productLpos.getExpiryDate().atStartOfDay(zoneId).toInstant()));
            dataMap.put("terms",lpoeneratorTermsText.getText()+"Days");

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,dataMap,new JREmptyDataSource());
            JasperViewer jv = new JasperViewer(jasperPrint,false);
            JasperViewer.setDefaultLookAndFeelDecorated(true);
            jv.setExtendedState(Frame.MAXIMIZED_BOTH);
            jv.setVisible(true);
            JasperExportManager.exportReportToPdfStream(jasperPrint,
                    new FileOutputStream("src\\main\\resources\\Reports\\Lpo.pdf"));

            productLpos.setLpoFile(Files.readAllBytes(Path.of("src\\main\\resources\\Reports\\Lpo.pdf")));

        } catch (JRException | IOException e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try(ObjectOutputStream oos = new ObjectOutputStream(os)){
            oos.writeObject(list);
            oos.flush();
            productLpos.setPoDetails(os.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        WebTarget webTarget = ClientBuilder.newClient().target(HOST+"/lpos/create");
        return webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(productLpos, MediaType.APPLICATION_JSON));
    }

    //update lpo table
    private void updateLposTable() {
        WebTarget target = ClientBuilder.newClient().target(HOST + "/lpos/findAll");
        GenericType<List<ProductLpos>> genericType = new GenericType<>() {};
        List<ProductLpos> list1 = target.request(MediaType.APPLICATION_JSON).get(genericType);
        ObservableList<ProductLpos> list = FXCollections.observableArrayList();
        list.addAll(list1);
        getProductLposTableView().setItems(list);
    }

    public void setLpoTable(TableView<ProductLpos> lpo) {
        this.productLposTableView = lpo;
    }

    public TableView<ProductLpos> getProductLposTableView() {
        return productLposTableView;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
