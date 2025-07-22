package com.client.clientapplication;

import com.quickrest.entities.*;
import com.quickrest.resources.ApplicationPath;
import com.quickrest.resources.CnSummary;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class NewCreditNotePageController implements Initializable {
    @FXML
    private Label newCnCreditNteType;
    @FXML
    private Label newCnEntytyFeild;
    @FXML
    private TextArea newCnTextArea;
    @FXML
    private TextField newCnCode;
    @FXML
    private TextField newCnDescription;
    @FXML
    private TextField newCnQty;
    @FXML
    private TextField newCnStock;
    @FXML
    private TextField newCnCost;
    @FXML
    private TextField newCnTotalCost;
    @FXML
    private Button newCnOkayutton;
    @FXML
    private TableView<CnSummary> cnTable;
    @FXML
    private TextField newCnAmount;
    @FXML
    private Button newCnSaveButton;
    @FXML
    private AnchorPane cnsPageAnchor;


    private String creditNoteType;
    private String entityType;
    private String userName;
    private TableView<CreditNote> creditNoteTableView;

    ObservableList<CnSummary> creditNoteObservableList;
    List<Prices> priceList;
    private Label cnCountLabel;

    private static final String  HOST = ApplicationPath.getApplicationPath();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        creditNoteObservableList = FXCollections.observableArrayList();

        //Validating item code
        newCnCode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.isEmpty() || t1.isBlank()){
                    newCnCode.setText("");
                }else {
                    if(!t1.matches("\\d*")){
                        newCnCode.setText(t1.replaceAll("\\D",""));
                    }else {
                        if(t1.length()==6){
                            WebTarget webTarget = ClientBuilder.newClient().target(ApplicationPath.getHqPath()+
                                    "/prices/findByCode/"+newCnCode.getText());
                            GenericType<List<Prices>> genericType = new GenericType<>(){};
                            priceList = new ArrayList<>();
                            priceList = webTarget.request(MediaType.APPLICATION_JSON).get(genericType);
                            if(priceList.size()>0){
                                newCnDescription.setText(priceList.get(0).getDescription());
                                newCnCost.setText(priceList.get(0).getInclusive()+"");
                                newCnQty.requestFocus();
                                newCnQty.setText("");
                                webTarget = ClientBuilder.newClient().target(HOST+"/stock/find/"+newCnCode.getText());
                                GenericType<List<ProductStock>> generic = new GenericType<>(){};
                                List<ProductStock> stockList = webTarget.request(MediaType.APPLICATION_JSON).get(generic);
                                if(stockList.size() > 0){
                                    if(stockList.size() > 1){
                                        Collections.sort(stockList, new Comparator<ProductStock>() {
                                            @Override
                                            public int compare(ProductStock o1, ProductStock o2) {
                                                if(o1.getStockIndex() > o2.getStockIndex())
                                                    return 1;
                                                else if (o2.getStockIndex() > o1.getStockIndex())
                                                    return -1;
                                                else return 0;
                                            }
                                        });
                                    }
                                    newCnStock.setText(stockList.get(stockList.size()-1).getBal()+"");
                                }
                            }

                        }
               }
                }
            }
        });



        //Setting total cost
        newCnQty.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.isEmpty() || t1.isBlank()){
                    newCnQty.setText("");
                    newCnTotalCost.setText("");
                    newCnOkayutton.setDisable(true);
                }else {
                    if(t1.matches("\\d*")){
                        newCnTotalCost.setText(String.format("%,.2f",Double.parseDouble(newCnCost.getText()) *
                                Double.parseDouble(newCnQty.getText())));
                        newCnOkayutton.setDisable(false);
                    }else {
                        newCnQty.setText(t1.replaceAll("\\D",""));
                    }
                }
            }
        });

        //Disabling okayButton
        newCnOkayutton.setDisable(true);

        //Moving focus to okay button
        newCnQty.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode().equals(KeyCode.ENTER)){
                    newCnOkayutton.requestFocus();
                }
            }
        });

        //Searching an item from the database
        newCnCode.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode().equals(KeyCode.SHIFT)){
                    Stage stage = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("ProductSearch.fxml"));
                    try {
                        Scene scene = new Scene(loader.load());
                        stage.setScene(scene);
                        CnProductSearchController controller = loader.getController();
                        controller.setCodeField(newCnCode);
                        controller.setDescField(newCnDescription);
                        controller.setCostField(newCnCost);
                        controller.setStockField(newCnStock);
                        stage.show();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }

    public void setCreditNoteType(String creditNoteType) {
        this.creditNoteType = creditNoteType;
        newCnCreditNteType.setText(creditNoteType);
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
        newCnEntytyFeild.setText(entityType);
    }

    @FXML
    public void insertItemIntoCnTable(){
        newCnOkayutton.setOnAction(e->{
            if(newCnTextArea.getText().equals("") || newCnTextArea.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error In Processing Request");
                alert.setContentText("Invalid comment content");
                Optional<ButtonType> optional = alert.showAndWait();
                if(optional.isPresent() && optional.get() == ButtonType.CLOSE)
                    alert.close();
            }else {
                CnSummary cnSummary = new CnSummary();
                cnSummary.setCode(newCnCode.getText());
                cnSummary.setDescription(newCnDescription.getText());
                cnSummary.setQuantity(Integer.valueOf(Integer.parseInt(newCnQty.getText())));
                cnSummary.setCost(Double.parseDouble(newCnCost.getText()));
                cnSummary.setTotalCost(Double.parseDouble(newCnTotalCost.getText().replaceAll(",","")));
                cnSummary.setUnits(priceList.get(0).getUnit());

                if(Integer.parseInt(newCnStock.getText()) == 0){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error In Processing Request");
                    alert.setContentText("Unable to generate a credit note if current is zero");
                    Optional<ButtonType> optional = alert.showAndWait();
                    if(optional.isPresent() && optional.get() == ButtonType.CLOSE)
                        alert.close();
                } else if (Integer.parseInt(newCnQty.getText()) > Integer.parseInt(newCnStock.getText())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error In Processing Request");
                    alert.setContentText("Quantity credited is greater than the current stock");
                    Optional<ButtonType> optional = alert.showAndWait();
                    if(optional.isPresent() && optional.get() == ButtonType.CLOSE)
                        alert.close();
                }else if (Integer.parseInt(newCnQty.getText()) == 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error In Processing Request");
                    alert.setContentText("Quantity must be greater than zero");
                    Optional<ButtonType> optional = alert.showAndWait();
                    if(optional.isPresent() && optional.get() == ButtonType.CLOSE)
                        alert.close();
                } else {
                    boolean status = false;
                    for(CnSummary c: creditNoteObservableList){
                        if(c.getCode().equals(cnSummary.getCode())){
                            status = true;
                        }
                    }
                    if(status){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error In Processing Request");
                        alert.setContentText("Item is already in the grid");
                        Optional<ButtonType> optional = alert.showAndWait();
                        if(optional.isPresent() && optional.get() == ButtonType.CLOSE)
                            alert.close();

                        newCnCode.requestFocus();
                        newCnQty.setText("");
                        newCnCode.setText("");
                        newCnDescription.setText("");
                        newCnStock.setText("");
                        newCnCost.setText("");
                        newCnTotalCost.setText("");

                    }else {
                        creditNoteObservableList.add(cnSummary);
                        cnTable.setItems(creditNoteObservableList);

                        double total = 0;
                        for(CnSummary c:creditNoteObservableList){
                            total += c.getTotalCost();
                        }

                        newCnAmount.setText(String.format("%,.2f",total));
                        newCnQty.setText("");
                        newCnCode.setText("");
                        newCnDescription.setText("");
                        newCnStock.setText("");
                        newCnCost.setText("");
                        newCnTotalCost.setText("");
                        newCnCode.requestFocus();
                    }

                }
            }
        });

    }

    @FXML
    public void generateCreditNote(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Credit Note Confirmation");
        alert.setContentText("Are you sure you want to proceed?");
        Optional<ButtonType> optional = alert.showAndWait();
        if(optional.isPresent() && optional.get() == ButtonType.OK){
            alert.close();
            WebTarget webTarget = ClientBuilder.newClient().target(HOST+"/branchCnNumber/findAll");
            GenericType<List<BranchCnNumber>> genericType = new GenericType<>(){};
            List<BranchCnNumber> branchCnNumbers = webTarget.request(MediaType.APPLICATION_JSON).get(genericType);

            webTarget = ClientBuilder.newClient().target(HOST+"/hqCn/findAll");
            GenericType<List<HqCreditNoteNumber>> gen = new GenericType<>(){};
            List<HqCreditNoteNumber> hq = webTarget.request(MediaType.APPLICATION_JSON).get(gen);


            long branchCnNumber = 0;
            long hqCnNumber = 0;
            if(branchCnNumbers.size() == 0 && hq.size() == 0){
                branchCnNumber = 1;
                hqCnNumber = 1;

            }else {
                Collections.sort(branchCnNumbers, new Comparator<BranchCnNumber>() {
                    @Override
                    public int compare(BranchCnNumber o1, BranchCnNumber o2) {
                        if (o1.getBranchNumber() > o2.getBranchNumber())
                            return 1;
                        else if (o2.getBranchNumber() > o1.getBranchNumber())
                            return -1;
                        else return 0;
                    }
                });

                Collections.sort(hq, new Comparator<HqCreditNoteNumber>() {
                    @Override
                    public int compare(HqCreditNoteNumber o1, HqCreditNoteNumber o2) {
                        if (o1.getCnNumber() > o2.getCnNumber())
                            return 1;
                        else if (o2.getCnNumber() > o1.getCnNumber())
                            return -1;
                        else
                            return 0;
                    }
                });

                hqCnNumber = hq.get(hq.size() - 1).getCnNumber();
                branchCnNumber = branchCnNumbers.get(branchCnNumbers.size() - 1).getBranchNumber();
                hqCnNumber++;
                branchCnNumber++;
            }

            //Fetching branch and company
            URL url = null;
            try {
                url = new URL(HOST);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            webTarget = ClientBuilder.newClient().target(ApplicationPath.getHqPath()+
                    "/branch/find/"+url.getPath().split("/")[1]);
            GenericType<List<BranchDetails>> gt = new GenericType<>(){};
            List<BranchDetails> branchDetailsList = webTarget.request(MediaType.APPLICATION_JSON).get(gt);

            webTarget = ClientBuilder.newClient().target(ApplicationPath.getHqPath()+
                    "/company/findByName/"+branchDetailsList.get(0)
                    .getCompanyName());
            GenericType<List<CompanyDetails>> gt1 = new GenericType<>(){};
            List<CompanyDetails> companyDetailsList = webTarget.request(MediaType.APPLICATION_JSON).get(gt1);


            Response response = null;

            ObservableList<CnSummary> crs = cnTable.getItems();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(crs);
            try {
                JasperDesign jasperDesign = JRXmlLoader.load("src\\main\\resources\\Reports\\CreditNote.jrxml");
                Map<String, Object> map = new HashMap<>();
                map.put("user", ApplicationPath.getUser());
                map.put("particulars", newCnTextArea.getText());
                map.put("date", new Date());
                map.put("hqCnNumber",hqCnNumber );
                map.put("branchCnNo",branchCnNumber);
                map.put("email",companyDetailsList.get(0).getEmail());
                map.put("phone",companyDetailsList.get(0).getContact());
                map.put("entity",newCnEntytyFeild.getText());
                map.put("type",newCnCreditNteType.getText());
                map.put("company",companyDetailsList.get(0).getName());
                map.put("branch",branchDetailsList.get(0).getBranchName());
                map.put("collection",dataSource);

                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,map,new JREmptyDataSource());
                JasperViewer jv = new JasperViewer(jasperPrint,false);
                jv.setExtendedState(Frame.MAXIMIZED_BOTH);
                jv.setVisible(true);

                JasperExportManager.exportReportToPdfStream(jasperPrint,new FileOutputStream(
                        new File("C:\\System generated dat files\\Cn.pdf")));

            } catch (JRException | FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            //updating cn summary table
            CreditNote cn = new CreditNote();
            cn.setDateGenerated(new Date());
            cn.setUserName(ApplicationPath.getUser());
            cn.setEntityName(newCnEntytyFeild.getText());
            cn.setCnCost(Double.parseDouble(newCnAmount.getText().replaceAll(",", "")));
            cn.setHqCnNumber(BigInteger.valueOf(hqCnNumber));
            cn.setBranchCnNumber(BigInteger.valueOf(branchCnNumber));
            cn.setDetails(newCnTextArea.getText());
            cn.setCnType(newCnCreditNteType.getText());
            cn.setBranchName(url.getPath().split("/")[1]);
            
            try {
                cn.setCnFile(Files.readAllBytes(Path.of("C:\\System generated dat files\\Cn.pdf")));
            } catch (IOException e) {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Failed");
                alert.setContentText("Credit note file not found");
                optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK) {
                    alert.close();
                }
            }

            //Converting list of invoiced items into bytes
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try(ObjectOutputStream ous = new ObjectOutputStream(new BufferedOutputStream(os))){
                ous.writeObject(new ArrayList<>(crs));
                ous.flush();
                byte [] data = os.toByteArray();
                cn.setCnDetails(data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String entity = newCnEntytyFeild.getText();

            webTarget = ClientBuilder.newClient().target(ApplicationPath.getHqPath()+"/branch/find/"+entity);
            GenericType<List<BranchDetails>> branch = new GenericType<>(){};
            List<BranchDetails> myBranch = webTarget.request(MediaType.APPLICATION_JSON).get(branch);

            Response response1 = null;

             if(myBranch.isEmpty()){
                 webTarget = ClientBuilder.newClient().target(HOST + "/creditNotes/create");
                 response1 = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(cn, MediaType.APPLICATION_JSON));

             }else {


                 webTarget = ClientBuilder.newClient().target(HOST + "/transit/create");
                 response1 = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(cn, MediaType.APPLICATION_JSON));

             }




            if (response1.getStatus() == 204) {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Success");
                alert.setContentText("Credit note successfully generated");
                optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK) {
                    alert.close();
                    Stage stage = (Stage) newCnSaveButton.getScene().getWindow();
                    stage.close();
                }

                updateCnTable();

            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed");
                alert.setContentText("Failed an error occurred");
                optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK) {
                    alert.close();
                }
            }

        }else {
            alert.close();
        }
    }

    private void updateCnTable(){
        WebTarget webTarget = ClientBuilder.newClient().target(HOST+"/creditNotes/findAll");
        GenericType<List<CreditNote>> generic = new GenericType<>(){};
        List<CreditNote> cn = webTarget.request(MediaType.APPLICATION_JSON).get(generic);
        getCreditNoteTableView().setItems(FXCollections.observableArrayList(cn));

        cnCountLabel.setText(String.format("%,d",getCreditNoteTableView().getItems().size()));
    }

    public TableView<CreditNote> getCreditNoteTableView() {
        return creditNoteTableView;
    }

    public void setCreditNoteTableView(TableView<CreditNote> creditNoteTableView) {
        this.creditNoteTableView = creditNoteTableView;
    }

    public Label getCnCountLabel() {
        return cnCountLabel;
    }

    public void setCnCountLabel(Label cnCountLabel) {
        this.cnCountLabel = cnCountLabel;
    }
}
