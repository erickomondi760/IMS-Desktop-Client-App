package com.client.clientapplication;

import com.quickrest.entities.*;
import com.quickrest.resources.ApplicationPath;
import com.quickrest.resources.ColumnFormatter;
import com.quickrest.resources.PODetails;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

public class EditLpoController implements Initializable {
    @FXML
    private TextField editLpoAmountField;
    @FXML
    private Label editLpoAmountLabel;
    @FXML
    private AnchorPane editLpoAnchor;
    @FXML
    private TextField editLpoCodeField;
    @FXML
    private TextField editLpoVatField;
    @FXML
    private TextField editLpoCostField;
    @FXML
    private TextField editLpoDescField;
    @FXML
    private AnchorPane editLpoDetailsAnchor;
    @FXML
    private Button editLpoDoneButton;
    @FXML
    private Label editLpoLpoNumber;
    @FXML
    private TextField editLpoQtyField;
    @FXML
    private Button editLpoSaveButton;
    @FXML
    private Button editLpoCloseButton;
    @FXML
    private TextField editLpoUnitsField;
    @FXML
    TableView<PODetails> editLpoTable;
    @FXML
    TableColumn<PODetails,Double> costInclCol;
    @FXML
    TableColumn<PODetails,Double> vatCol;
    @FXML
    TableColumn<PODetails,Double> amountCol;


    private List<PODetails> poDetailsList = new ArrayList<>();
    private Stage s;
    private int index;
    private Prices prices = null;
    private ProductLpos productLpo;
    EditLpoDetailsConfirmationController c;
    TableView<ProductLpos> lposTableView;

    private static final String HOST = ApplicationPath.getHqPath();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Fomarting cost,vat & amnt cols
        costInclCol.setCellFactory(new ColumnFormatter<>(new DecimalFormat("#,###.00")));
        vatCol.setCellFactory(new ColumnFormatter<>(new DecimalFormat("#,###.00")));
        amountCol.setCellFactory(new ColumnFormatter<>(new DecimalFormat("#,###.00")));


        //Setting details into the table
        editLpoTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //Show product confirmation panel
        editLpoTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                PODetails details = editLpoTable.getSelectionModel().getSelectedItem();
                index = editLpoTable.getSelectionModel().getSelectedIndex();
                if(details != null){
                    //Setting po details into fields
                    editLpoSaveButton.setDisable(false);
                    editLpoCodeField.setText(details.getCode());
                    editLpoDescField.setText(details.getDescription());
                    editLpoUnitsField.setText(details.getUnits());
                    editLpoQtyField.setText(String.valueOf(details.getQuantity()));
                    editLpoCostField.setText(String.format("%,.2f",details.getCostInclusive()));
                    editLpoAmountField.setText(String.format("%,.2f",details.getTotalCost()));
                    editLpoVatField.setText(String.format("%.0f",details.getVat()));


                    //Passing selected details to product details confirmation page
                    c.getCode().setText(details.getCode());
                    c.getExcl().setText(String.format("%,.2f",details.getCostExclusive()));
                    c.getIncl().setText(String.format("%,.2f",details.getCostInclusive()));
                    c.getDescription().setText(details.getDescription());
                    c.getPack().setText(details.getPackaging());
                    c.getUnits().setText(details.getUnits());
                    c.getSupplier().setText(details.getSupplier());
                    c.getVat().setText(String.format("%.0f",details.getVat()));

                    //Fetching current cost
                    WebTarget target = ClientBuilder.newClient().target(HOST+"/prices/findByCode/"+details.getCode());
                    GenericType<List<Prices>> g = new GenericType<>(){};
                    List<Prices> price = target.request(MediaType.APPLICATION_JSON).get(g);

                    if(price.isEmpty()){
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("Faild!");
                        alert.setContentText("Connection to the server is lost");
                        Optional<ButtonType> optional = alert.showAndWait();
                        if(optional.isPresent() && optional.get().equals(ButtonType.OK))
                            alert.close();
                    }else {
                        c.getDiscount().setText(String.format("%,.2f",price.get(0).getDisc()));
                        c.getSysCost().setText(String.format("%,.2f",price.get(0).getInclusive()));
                    }


                }
            }
        });


        //Enable product searching
        editLpoCodeField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode().equals(KeyCode.SHIFT)){
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("EditLpoSearchTable.fxml"));
                    try {
                        stage.setScene(new Scene(fxmlLoader.load()));

                        EditLpoSearchTableController controller = fxmlLoader.getController();
                        controller.setCode(editLpoCodeField);
                        controller.setDesc(editLpoDescField);
                        controller.setUnit(editLpoUnitsField);
                        controller.setVat(editLpoVatField);
                        controller.setCost(editLpoCostField);

                        stage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        //Searching an item by entering code
        fetchProductDetail();

        //Accepting digits for quantity field
        editLpoQtyField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("\\d*")){
                    editLpoQtyField.setText(t1.replaceAll("\\D",""));
                }else {
                    if(!editLpoQtyField.getText().equals("")){
                        int qty = Integer.parseInt(editLpoQtyField.getText());
                        String cost = editLpoCostField.getText().replaceAll(",","");
                        double amnt = Double.parseDouble(cost);
                        editLpoAmountField.setText(String.format("%,.2f",amnt * qty));
                    }
                }
            }
        });

        //Clossing editor
        editLpoCloseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage s = (Stage) getC().getDiscount().getScene().getWindow();
                s.close();
                s = (Stage) editLpoCloseButton.getScene().getWindow();
                s.close();
            }
        });


    }

    @FXML
    public void insertNewChanges(){
        if(!editLpoCodeField.getText().equals("") || editLpoQtyField.getText().equals("") || editLpoQtyField.getText().equals("0")){
            ObservableList<PODetails> list = editLpoTable.getItems();
            System.out.println("size:"+list.size());
            boolean exist = false;

            //Fetching current cost
            WebTarget target = ClientBuilder.newClient().target(HOST+"/prices/findByCode/"+editLpoCodeField.getText());
            GenericType<List<Prices>> g = new GenericType<>(){};
            List<Prices> price = target.request(MediaType.APPLICATION_JSON).get(g);


            for(PODetails poDetails:list){
                if(poDetails.getCode().equals(editLpoCodeField.getText())){
                    exist = true;
                }
            }

            if(exist){
                PODetails poDetail = editLpoTable.getSelectionModel().getSelectedItem();
                poDetail.setUnits(editLpoUnitsField.getText());
                poDetail.setDescription(editLpoDescField.getText());
                poDetail.setCode(editLpoCodeField.getText());
                poDetail.setLpoNumber(Long.parseLong(editLpoLpoNumber.getText()));


                String cost = editLpoCostField.getText().replaceAll(",","");
                String qty = editLpoQtyField.getText().replaceAll(",","");
                String vat = editLpoVatField.getText().replaceAll(",","");
                String total = editLpoAmountField.getText().replaceAll(",","");

                poDetail.setCostInclusive(Double.parseDouble(cost));
                poDetail.setQuantity(Integer.parseInt(qty));
                poDetail.setTotalCost(Double.parseDouble(total));
                poDetail.setVat(Double.parseDouble(vat));

                poDetail.setBarcode(price.get(0).getBarcode());
                poDetail.setSupplier(price.get(0).getSupplier());
                editLpoTable.getItems().set(index,poDetail);
            }else {
                PODetails poDetail = new PODetails();
                poDetail.setUnits(editLpoUnitsField.getText());
                poDetail.setDescription(editLpoDescField.getText());
                poDetail.setCode(editLpoCodeField.getText());
                poDetail.setLpoNumber(Long.parseLong(editLpoLpoNumber.getText()));


                String cost = editLpoCostField.getText().replaceAll(",","");
                String qty = editLpoQtyField.getText().replaceAll(",","");
                String vat = editLpoVatField.getText().replaceAll(",","");
                String total = editLpoAmountField.getText().replaceAll(",","");

                poDetail.setCostInclusive(Double.parseDouble(cost));
                poDetail.setQuantity(Integer.parseInt(qty));
                poDetail.setTotalCost(Double.parseDouble(total));
                poDetail.setVat(Double.parseDouble(vat));

                poDetail.setBarcode(price.get(0).getBarcode());
                poDetail.setSupplier(price.get(0).getSupplier());

                poDetailsList.addAll(editLpoTable.getItems());
                poDetailsList.add(poDetail);
                editLpoTable.setItems(FXCollections.observableArrayList(poDetailsList));
            }
            //Calculating a new amount
            double amount = 0;
            list = editLpoTable.getItems();
            for(PODetails poDetails:list){
                amount += poDetails.getTotalCost();
            }
            editLpoAmountLabel.setText(String.format("%,.2f",amount));


            editLpoCodeField.setText("");
            editLpoDescField.setText("");
            editLpoVatField.setText("");
            editLpoUnitsField.setText("");
            editLpoAmountField.setText("");
            editLpoQtyField.setText("");
            editLpoCostField.setText("");
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Error in updating");
            alert.setContentText("Either code or quantity is invalid");
            Optional<ButtonType> optional = alert.showAndWait();
            if(optional.isPresent() && optional.get() == ButtonType.OK){
                alert.close();
            }
        }

        poDetailsList.addAll(editLpoTable.getItems());
    }

    //Entering code to populate fiels with db data
    private void fetchProductDetail(){
        //Populating fields with data from the database
        editLpoCodeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("\\d*" )){
                    editLpoCodeField.setText(t1.replaceAll("[^\\d]",""));
                } else if (t1.length() >= 7) {
                    editLpoCodeField.setDisable(true);
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Code does not exist");

                    Optional<ButtonType> optional = alert.showAndWait();
                    if(optional.isPresent() && optional.get() == ButtonType.OK){
                        alert.close();
                    }
                }else {
                    String bc = null;
                    if(t1.length() == 6){
                        //Quering the database with products
                        WebTarget target = ClientBuilder.newClient().target(HOST+"/prices/findByCode/"+t1);
                        GenericType<List<Prices>> g = new GenericType<>(){};
                        List<Prices> price = target.request(MediaType.APPLICATION_JSON).get(g);

                        if(!price.isEmpty()){
                            editLpoDescField.setText(price.get(0).getDescription());
                            editLpoUnitsField.setText(price.get(0).getUnit());
                            editLpoCostField.setText(String.format("%,.2f",price.get(0).getInclusive()));
                            editLpoVatField.setText(String.format("%,.2f",price.get(0).getVat()));
                            bc = editLpoCodeField.getText();
                        }

                    }
                    if (t1.length() == 6 && bc == null){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Code does not exist");
                        Optional<ButtonType> optional = alert.showAndWait();
                        if(optional.isPresent() && optional.get() == ButtonType.OK){
                            alert.close();
                        }
                        editLpoCodeField.setText("");
                        editLpoDescField.setText("");
                        editLpoVatField.setText("");
                        editLpoUnitsField.setText("");
                        editLpoAmountField.setText("");
                        editLpoQtyField.setText("");
                        editLpoCostField.setText("");
                    }
                }
            }
        });

    }

    @FXML
    public void saveLpo(){
        //Creates a new product lpo
        //Fetching company details
        URL url = null;
        try {
            url = new URL(ApplicationPath.getApplicationPath());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        WebTarget target = ClientBuilder.newClient().target(HOST+"/company/findByBranchName/"+
                url.getPath().split("/")[1]);

        GenericType<List<CompanyDetails>> g = new GenericType<List<CompanyDetails>>(){};
        List<CompanyDetails> company = target.request(MediaType.APPLICATION_JSON).get(g);

        ProductLpos productLpos = getProductLpo();
        System.out.println("ponum="+productLpos.getPoNumber());
        Date date1 = new Date();
        productLpos.setDateRaised(LocalDate.ofInstant(date1.toInstant(),ZoneId.systemDefault()));
        productLpos.setLpoNumber(Long.parseLong(editLpoLpoNumber.getText()));

        String amnt = editLpoAmountLabel.getText().replaceAll(",","");
        productLpos.setAmount(Double.parseDouble(amnt));
        productLpos.setSupplier(poDetailsList.get(0).getSupplier());
        productLpos.setReceived("Not received");
        productLpos.setStatus("Pending");
        productLpos.setUserName(getProductLpo().getUserName());
        productLpos.setBranchName(getProductLpo().getBranchName());
        LocalDate localDate = LocalDate.now();
        productLpos.setExpiryDate(localDate.plusDays(13));

        List<PODetails> myList = new ArrayList<>();
        poDetailsList.forEach(p->{
            p.setVat((p.getVat() / 100) * (p.getCostExclusive() * p.getQuantity()));
            p.setCostExclusive(p.getCostExclusive() * p.getQuantity());
            p.setCostInclusive(p.getCostInclusive() * p.getQuantity());
            myList.add(p);
        });


        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(myList);
        try {
            JasperDesign jasperDesign = JRXmlLoader.load("src\\main\\resources\\Reports\\" +
                    "Lpo.jrxml");
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("companyName",company.get(0).getName());
            dataMap.put("branch",getProductLpo().getBranchName());
            dataMap.put("collection",dataSource);
            dataMap.put("supplier",productLpos.getSupplier());
            dataMap.put("email",company.get(0).getEmail());
            dataMap.put("phoneNumber",company.get(0).getContact());
            dataMap.put("address",company.get(0).getAddress());
            dataMap.put("userName",getProductLpo().getUserName());
            dataMap.put("lpoNumber",getProductLpo().getLpoNumber());
            ZoneId zoneId = java.time.ZoneId.systemDefault();
            dataMap.put("expiryDate",Date.from(getProductLpo().getExpiryDate().atStartOfDay(zoneId).toInstant()));
            dataMap.put("terms",14+"");

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,dataMap,new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(jasperPrint,
                    new FileOutputStream("src\\main\\resources\\Reports\\Lpo.pdf"));

            productLpos.setLpoFile(Files.readAllBytes(Path.of("src\\main\\resources\\Reports\\Lpo.pdf")));

        } catch (JRException | IOException e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try(ObjectOutputStream oos = new ObjectOutputStream(os)){
            oos.writeObject(poDetailsList);
            oos.flush();
            productLpos.setPoDetails(os.toByteArray());
            productLpos.setId(getProductLpo().getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        System.out.println("Hq amount = "+productLpos);
        WebTarget webTarget = ClientBuilder.newClient().target(ApplicationPath.getApplicationPath()+"/lpos/update");
        Response response = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(productLpos,MediaType.APPLICATION_JSON));
        if(response.getStatus() == 200){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Updated");
            alert.setContentText("Lpo has been successfully edited");
            Optional<ButtonType> optional = alert.showAndWait();
            if(optional.isPresent() && optional.get() == ButtonType.OK){
                alert.close();
            }
            //Clossing side confirmation window
            Stage stage = (Stage) getC().getSysCost().getScene().getWindow();
            stage.close();


            stage = (Stage) getEditLpoDoneButton().getScene().getWindow();
            stage.close();

            //Fetching lpos to update lpo table
            populateLpoTable();
        }else {
            System.out.println("po:"+productLpos.getLpoNumber());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Failed");
            alert.setContentText("An error has occurred, contact system admin");
            Optional<ButtonType> optional = alert.showAndWait();
            if(optional.isPresent() && optional.get() == ButtonType.OK){
                alert.close();
            }
        }

    }

    //Fetch lpos
    private void populateLpoTable() {
        new Thread(() -> {
            WebTarget target = ClientBuilder.newClient().target(ApplicationPath.getApplicationPath() + "/lpos/findAll");
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
                    getLposTableView().setItems(list);
                    getLposTableView().refresh();
                }

            });
        }).start();   }

    public TableView<PODetails> getEditLpoTable() {
        return editLpoTable;
    }

    public void setEditLpoTable(TableView<PODetails> editLpoTable) {
        this.editLpoTable = editLpoTable;
    }

    public ProductLpos getProductLpo() {
        return productLpo;
    }

    public void setProductLpo(ProductLpos productLpo) {
        this.productLpo = productLpo;
    }

    public Label getEditLpoAmountLabel() {
        return editLpoAmountLabel;
    }

    public Label getEditLpoLpoNumber() {
        return editLpoLpoNumber;
    }

    public EditLpoDetailsConfirmationController getC() {
        return c;
    }

    public void setC(EditLpoDetailsConfirmationController c) {
        this.c = c;
    }

    public Button getEditLpoDoneButton() {
        return editLpoDoneButton;
    }

    public void setEditLpoDoneButton(Button editLpoDoneButton) {
        this.editLpoDoneButton = editLpoDoneButton;
    }

    public TableView<ProductLpos> getLposTableView() {
        return lposTableView;
    }

    public void setLposTableView(TableView<ProductLpos> lposTableView) {
        this.lposTableView = lposTableView;
    }
}
