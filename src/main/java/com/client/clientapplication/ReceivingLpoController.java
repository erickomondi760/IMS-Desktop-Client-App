package com.client.clientapplication;

import com.quickrest.entities.*;
import com.quickrest.resources.ApplicationPath;
import com.quickrest.resources.PODetails;
import com.quickrest.resources.ReceivedGrn;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

public class ReceivingLpoController implements Initializable {

    @FXML
    private TableView<ReceivedGrn> grnTable;
    @FXML
    private Label receiveLpoSupplierLablel;
    @FXML
    private TextField receiveLpoOrderedQty;
    @FXML
    private TextField receiveLpoReceivedQty;
    @FXML
    private TextField receiveLpoBalQty;
    @FXML
    private TextField receiveLpoInvoiceNumber;
    @FXML
    private Button receiveLpoSaveButton;
    @FXML
    private Button receive;
    @FXML
    private DatePicker invoiceDate;
    @FXML
    private DatePicker taxDate;
    @FXML
    private TextField receiveLpoCode;
    @FXML
    private TextField receiveLpoDescription;
    @FXML
    private TextField receivedLpoTotalExcl;
    @FXML
    private TextField receivedLpoTotalIncl;
    @FXML
    private TextField receivedLpoVat;
    @FXML
    private TextField receiveLpoNorminalField;
    @FXML
    private TextField receiveLpoCUField;

    List<ReceivedGrn> fieldPopulatorList;
    List<Prices> findVatAndCost;
    List<Integer> selectedRowIndex = new ArrayList<>();
    List<ReceivedGrn> received = new ArrayList<>();
    TableView<ProductLpos> lposTableView;
    List<PODetails>detailsList = new ArrayList<>();
    private ProductLpos lpo;

    private int selectedIndex = 0;
    private long stockIndex = 0;


    private static final String  HOST = ApplicationPath.getApplicationPath();
    private static final String HQ_HOST = ApplicationPath.getHqPath();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        receiveLpoSaveButton.setDisable(true);
        receive.setDisable(true);

        grnTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //Set values in textFields
        grnTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ReceivedGrn>() {
            @Override
            public void changed(ObservableValue<? extends ReceivedGrn> observableValue, ReceivedGrn receivedGrn, ReceivedGrn t1) {
                grnTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(mouseEvent.getClickCount() == 2){
                            ReceivedGrn selectedReceivedGrn = grnTable.getItems().get(grnTable.getFocusModel().getFocusedIndex());
                            grnTable.getSelectionModel().select(grnTable.getFocusModel().getFocusedIndex());
                            fieldPopulatorList = new ArrayList<>();
                            fieldPopulatorList.add(selectedReceivedGrn);

                            receiveLpoOrderedQty.setText(String.valueOf(selectedReceivedGrn.getQtyOrdered()));
                            receiveLpoCode.setText(selectedReceivedGrn.getCode());
                            receiveLpoDescription.setText(selectedReceivedGrn.getDescription());

                            selectedRowIndex.add(grnTable.getFocusModel().getFocusedIndex());
                            selectedIndex = grnTable.getFocusModel().getFocusedIndex();
                            received.add(selectedReceivedGrn);
                            receive.setDisable(false);
                            receiveLpoReceivedQty.requestFocus();
                        }
                    }
                });

            }
        });

        reduceBalanceQty();
        enablingSaveButton();
        enableReceiveButton();
        moveFocusToReceiveButton();


    }

    public TableView<ProductLpos> getLposTableView() {
        return lposTableView;
    }

    public void setLposTableView(TableView<ProductLpos> lposTableView) {
        this.lposTableView = lposTableView;
    }

    public TableView<ReceivedGrn> getTable(){
        return grnTable;
    }

    public Label getSupplierLabel(){
        return receiveLpoSupplierLablel;
    }
    //Reducing bal quantity in the bal-textfield when qty received is typed into
    private void reduceBalanceQty(){
        receiveLpoReceivedQty.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("\\d*")){
                    receiveLpoReceivedQty.setText(t1.replaceAll(".*",""));
                }else {
                    if(t1.matches("")){
                        receiveLpoReceivedQty.setText(t1.replaceAll("[^\\d*]","0"));
                        receiveLpoBalQty.setText(receiveLpoOrderedQty.getText());
                    }else if(Integer.parseInt(t1) > 0 && !t1.equals("")){
                        receiveLpoBalQty.setText(String.valueOf(Integer.parseInt(receiveLpoOrderedQty.getText()) - Integer.parseInt(t1)));
                    }
                }

            }
        });
    }
    public void enablingSaveButton(){
        receiveLpoInvoiceNumber.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.equals("") || !t1.isBlank()){
                    receiveLpoSaveButton.setDisable(false);
                }else {
                    receiveLpoSaveButton.setDisable(true);
                }
            }
        });
    }
    @FXML
    public void receiveAnItem(){
        if(Integer.parseInt(receiveLpoReceivedQty.getText()) <= Integer.parseInt(receiveLpoOrderedQty.getText())){

            //Looking for changes on the received items and updating
            ReceivedGrn receivedGrn = grnTable.getSelectionModel().getSelectedItem();
            WebTarget target = ClientBuilder.newClient().target(ApplicationPath.getHqPath()+"/product/findByCode/"
                    +receivedGrn.getCode());
            GenericType<List<Product>> prodGen = new GenericType<>(){};
            List<Product> productList = target.request(MediaType.APPLICATION_JSON).get(prodGen);

            if(!productList.get(0).getUnit().equals(receivedGrn.getUnits())){
                receivedGrn.setUnits(productList.get(0).getUnit());
            } else if (!productList.get(0).getPackaging().equals(receivedGrn.getPackaging())) {
                receivedGrn.setPackaging(productList.get(0).getPackaging());
            }else {
                if(!productList.get(0).getBarcode().equals(receivedGrn.getBarcode()))
                    receivedGrn.setBarcode(productList.get(0).getBarcode());
            }


            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ReceivingLpoPriceConfirmation.fxml"));
            try {
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.show();

                //supplying selected item's details
                ReceivingLpoPriceConfirmation controller = loader.getController();

                controller.populateFields(receivedGrn.getCode(), receivedGrn.getDescription(), receivedGrn.getCostExclusive(),
                        receivedGrn.getDiscount(),receivedGrn.getCostInclusive(),receivedGrn.getVat());

                //Supplying vat and cost
                List<Prices> prices = getCurrentCost();
                if(prices != null){
                    prices.forEach(p->{
                        if(p.getCode().equals(receivedGrn.getCode())){
                            controller.fetchCurrentSystemCost(p.getInclusive());
                        }
                    });
                }
                //supplying qty received
                controller.setQtyReceived(Integer.parseInt(receiveLpoReceivedQty.getText()));

                //Supplying the grn table
                controller.setGrnTableView(grnTable);


                //Supplying row index
                controller.setRowIndex(grnTable.getFocusModel().getFocusedIndex());



                //Passing fields
                controller.setExclusiveTextField(receivedLpoTotalExcl);
                controller.setVatTextField(receivedLpoVat);
                controller.setInclusiveTextField(receivedLpoTotalIncl);
                controller.setReceiveLpoOrderedQty(receiveLpoOrderedQty);
                controller.setReceiveLpoCode(receiveLpoCode);
                controller.setReceiveLpoDescription(receiveLpoDescription);
                controller.setReceiveLpoReceivedQty(receiveLpoReceivedQty);
                controller.setIndexList(selectedRowIndex);
                controller.setReceived(received);
                controller.setReceive(receive);




            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Quantity received is greater than ordered quantity");
            Optional<ButtonType> optional = alert.showAndWait();
            if(optional.isPresent() && optional.get() == ButtonType.OK)
                alert.close();
        }

    }
    //fetching Vat and current cost
    private List<Prices> getCurrentCost(){
        WebTarget target = ClientBuilder.newClient().target(ApplicationPath.getHqPath()+"/prices/find/"
                +receiveLpoSupplierLablel.getText());
        GenericType<List<Prices>> prices = new GenericType<>(){};
        return findVatAndCost = target.request(MediaType.APPLICATION_JSON).get(prices);
    }
    //Enabling receive button
    private void enableReceiveButton(){
        receiveLpoReceivedQty.textProperty().addListener((o,t,t1) ->{
            if(t1.equals("") || t1.isBlank() || t1.isBlank()){
                receive.setDisable(true);
            }else {
                receive.setDisable(false);
            }
        });
    }
    //Moving focus to receive button
    private void moveFocusToReceiveButton(){
        receiveLpoReceivedQty.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode().equals(KeyCode.ENTER))
                    receive.requestFocus();
            }
        });
    }

    @FXML
    public void saveGrn(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to save the GRN?");
        Optional<ButtonType> optional = alert.showAndWait();
        if(optional.isPresent() && optional.get() == ButtonType.OK){
            alert.close();
            List<ReceivedGrn> receivedGrns = grnTable.getItems();

            String invoiceNumber = receiveLpoInvoiceNumber.getText();
            String norminalAcc = receiveLpoNorminalField.getText();
            String cuNumber = receiveLpoCUField.getText();

            double exclAmount = Double.parseDouble(receivedLpoTotalExcl.getText().replaceAll(",",""));
            double vatAmount = Double.parseDouble(receivedLpoVat.getText().replaceAll(",",""));
            double inclAmount = Double.parseDouble(receivedLpoTotalIncl.getText().replaceAll(",",""));

            ZoneId zoneId = ZoneId.systemDefault();
            LocalDate myDate = invoiceDate.getValue();
            Date invDate = Date.from(myDate.atStartOfDay(zoneId).toInstant());

            Date myTaxDate = Date.from(taxDate.getValue().atStartOfDay(zoneId).toInstant());

            if(norminalAcc.equals("") || norminalAcc.matches("\\d*") || cuNumber.equals("") || cuNumber.isBlank()){
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Some invoice information may be incorrect");
                Optional<ButtonType> optional1 = alert1.showAndWait();
                if(optional1.isPresent() && optional1.get() == ButtonType.OK)
                    alert.close();
            }else {

                WebTarget target1 = ClientBuilder.newClient().target(HOST+"/branchGrnNumber/findAll");
                GenericType<List<BranchGrnNumber>> genericType = new GenericType<>(){};
                List<BranchGrnNumber> branchGrnNumberList = target1.request(MediaType.APPLICATION_JSON).get(genericType);

                WebTarget target2 = ClientBuilder.newClient().target(HQ_HOST+"/hqGrnNumber/findAll");
                GenericType<List<HqGrnNumber>> genericType1 = new GenericType<>(){};
                List<HqGrnNumber> hqGrnNumberList = target2.request(MediaType.APPLICATION_JSON).get(genericType1);

                long branchgGrn = 0;
                if(branchGrnNumberList.size() > 0){
                    //sorting branch list
                    Collections.sort(branchGrnNumberList, new Comparator<BranchGrnNumber>() {
                        @Override
                        public int compare(BranchGrnNumber o1, BranchGrnNumber o2) {
                            if(o1.getGrnNumber() > o2.getGrnNumber())
                                return 1;
                            if(o2.getGrnNumber() > o1.getGrnNumber())
                                return -1;

                            if(o1.getGrnNumber() == o2.getGrnNumber()){
                                return 0;
                            }
                            return 0;
                        }
                    });

                    branchgGrn = branchGrnNumberList.get(branchGrnNumberList.size()-1).getGrnNumber();
                    branchgGrn = branchgGrn + 1;

                }


                long hqgGrn = 0;
                if(hqGrnNumberList.size() > 0){
                    //Sorting hq list
                    Collections.sort(hqGrnNumberList, new Comparator<HqGrnNumber>() {
                        @Override
                        public int compare(HqGrnNumber o1, HqGrnNumber o2) {
                            if(o1.getGrnNumber() > o2.getGrnNumber())
                                return 1;
                            if(o2.getGrnNumber() > o1.getGrnNumber())
                                return -1;

                            if(o1.getGrnNumber() == o2.getGrnNumber()){
                                return 0;
                            }
                            return 0;
                        }
                    });

                    hqgGrn = hqGrnNumberList.get(hqGrnNumberList.size()-1).getGrnNumber();
                    hqgGrn = hqgGrn + 1;

                }

                //Fetching branch details
                target2 = ClientBuilder.newClient().target(HQ_HOST+"/branch/find/"+getLpo().getBranchName());
                GenericType<List<BranchDetails>> branchList = new GenericType<>(){};
                List<BranchDetails> branchDetailsList = target2.request(MediaType.APPLICATION_JSON).get(branchList);

                //Fetching company details
                target2 = ClientBuilder.newClient().target(HQ_HOST+"/company/findByName/"+
                        branchDetailsList.get(0).getCompanyName());
                GenericType<List<CompanyDetails>> companyList = new GenericType<>(){};
                List<CompanyDetails> companyDetailsList = target2.request(MediaType.APPLICATION_JSON).get(companyList);

                //Creating a grn
                GoodsReceivedNote newGrn = new GoodsReceivedNote();
                ReceivedGrn g = receivedGrns.get(0);
                newGrn.setAmountReceivedExcl(exclAmount);
                newGrn.setAmountReceivedVat(vatAmount);
                newGrn.setAmountReceivedIncl(inclAmount);
                newGrn.setNorminalAcc("Purchases");
                newGrn.setInvoiceNumber(invoiceNumber);
                newGrn.setGrnDate(LocalDate.ofInstant(new Date().toInstant(),ZoneId.systemDefault()));
                newGrn.setInvoiceDate(invoiceDate.getValue());
                newGrn.setHqGrnNumber(hqgGrn);
                newGrn.setBranchGrnNumber(branchgGrn);
                newGrn.setCuInvoiceNumber(receiveLpoCUField.getText());
                newGrn.setSupplier(g.getSupplier());
                newGrn.setBranchDetails(branchDetailsList.get(0));
                newGrn.setBranchName(branchDetailsList.get(0).getBranchName());
                newGrn.setUserName(ApplicationPath.getUser());


                //Creating details to generate grn doc
                List<PODetails> poDetailsList = new ArrayList<>();
                for(ReceivedGrn grn:receivedGrns) {
                    if(grn.getQtyReceived() > 0){
                        PODetails p = new PODetails();
                        p.setCode(grn.getCode());
                        p.setQuantity(grn.getQtyReceived());
                        p.setCostInclusive(grn.getCostInclusive());
                        p.setCostExclusive(grn.getCostExclusive());

                        //Fetching sp from prices table
                        WebTarget target = ClientBuilder.newClient().target(HQ_HOST+"/prices/findByCode/"+grn.getCode());
                        GenericType<List<Prices>> type = new GenericType<>(){};
                        List<Prices> price = target.request(MediaType.APPLICATION_JSON).get(type);

                        p.setVat(Math.round((grn.getVat() / grn.getCostExclusive())*100));
                        p.setUnits(price.get(0).getUnit());
                        p.setSp(price.get(0).getSellingPrice());

                        double markUp = (price.get(0).getSellingPrice() - grn.getCostInclusive());
                        markUp = markUp /grn.getCostInclusive();
                        markUp = markUp *100;

                        p.setMarkUp(Double.parseDouble(String.format("%.1f",markUp)));
                        System.out.println("Modif.m="+p.getMarkUp());
                        p.setDescription(grn.getDescription());
                        p.setTotalCost(grn.getCostInclusive() * grn.getQtyReceived());
                        p.setLpoNumber(getLpo().getLpoNumber());
                        poDetailsList.add(p);
                    }

                }

                try {

                    JasperDesign jasperDesign = JRXmlLoader.load("src\\main\\resources\\Reports\\" +
                            "GRN.jrxml");
                    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(poDetailsList);
                    Map<String,Object> map = new HashMap<>();
                    map.put("supplier",newGrn.getSupplier());
                    map.put("grnNumber",newGrn.getBranchGrnNumber());
                    map.put("user",ApplicationPath.getUser());
                    map.put("branch",getLpo().getBranchName());
                    map.put("grnDate",Date.from(newGrn.getGrnDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    map.put("invDate",Date.from(newGrn.getInvoiceDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    map.put("companyName",companyDetailsList.get(0).getName());
                    map.put("invoiceNumber",invoiceNumber);
                    map.put("address",companyDetailsList.get(0).getAddress());
                    map.put("email",companyDetailsList.get(0).getEmail());
                    map.put("phone",companyDetailsList.get(0).getContact());
                    map.put("cu",newGrn.getCuInvoiceNumber());
                    map.put("collection",dataSource);
                    map.put("hqGrnNumber",newGrn.getHqGrnNumber());

                    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,map,new JREmptyDataSource());

                    JasperExportManager.exportReportToPdfStream(jasperPrint,new FileOutputStream(
                            new File("src\\main\\resources\\Reports\\GRN.pdf")));

                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ObjectOutputStream ous = new ObjectOutputStream(new BufferedOutputStream(os));
                    ous.writeObject(poDetailsList);
                    ous.flush();
                    ous.close();
                    newGrn.setGrnDetails(os.toByteArray());
                    newGrn.setGrnFile(Files.readAllBytes(Path.of("src\\main\\resources\\Reports\\GRN.pdf")));

                } catch (JRException | IOException e) {
                    throw new RuntimeException(e);
                }

                WebTarget webTarget = ClientBuilder.newClient().target(HOST+"/received/create");
                Response response = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(newGrn,MediaType.
                        APPLICATION_JSON));

                //Refreshing lpos table
                refreshLpoTable();

                if(response.getStatus() == 200){
                    Stage stage = (Stage) receiveLpoSaveButton.getScene().getWindow();
                    stage.close();
                }



            }

        }else if (optional.isPresent() && optional.get() == ButtonType.CANCEL){
            alert.close();
        }

    }

    private void refreshLpoTable(){
        WebTarget webTarget = ClientBuilder.newClient().target(HOST+"/lpos/findAll");
        GenericType<List<ProductLpos>> lposType = new GenericType<>(){};
        List<ProductLpos> lposList = webTarget.request(MediaType.APPLICATION_JSON).get(lposType);
        ObservableList<ProductLpos> lposObservableList = FXCollections.observableArrayList(lposList);
        getLposTableView().setItems(lposObservableList);
    }

    public ProductLpos getLpo() {
        return lpo;
    }

    public void setLpos(ProductLpos lpo) {
        this.lpo = lpo;
    }

    public List<PODetails> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<PODetails> detailsList) {
        this.detailsList = detailsList;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}
