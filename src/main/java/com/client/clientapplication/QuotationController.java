package com.client.clientapplication;

import com.quickrest.entities.Prices;
import com.quickrest.entities.ProductQuotation;
import com.quickrest.entities.ProductStock;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class QuotationController implements Initializable {

    @FXML
    private AnchorPane quotationAnchor;

    @FXML
    private TextField quotationBarcodeField;

    @FXML
    private TextField quotationCodeField;

    @FXML
    private TextArea quotationCommentField;

    @FXML
    private TextField quotationContactField;

    @FXML
    private TextField quotationCostField;

    @FXML
    private TextField quotationDescriptionField;

    @FXML
    private Button quotationDoneButton;

    @FXML
    private TextField quotationNameField;

    @FXML
    private Button quotationOkayButton;

    @FXML
    private TextField quotationQuantityField;

    @FXML
    private TextField quotationStockField;

    @FXML
    private TableView<ProductQuotation> quotationTable;

    @FXML
    private TextField quotationTotalCostField;

    @FXML
    private TextField quotationVatField;

    @FXML
    private Label quotationAmountLabel;


    private String userName;
    private List<ProductQuotation> productQuotationList;
    private TableView<ProductQuotation> tableView;
    private static final String  HOST = ApplicationPath.getApplicationPath();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        productQuotationList = new ArrayList<>();

        //disale okay button
        quotationOkayButton.setDisable(true);

        //Validating code
        validateCode();
        //Validate quantityField
        validateQuantity();

        //Highlighting text when code field is clicked
        quotationCodeField.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                quotationCodeField.selectAll();
            }
        });

        //Setting focus on okay button
        quotationQuantityField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode().equals(KeyCode.ENTER))
                    quotationOkayButton.requestFocus();
            }
        });

        //Adding a quote to the table
        addToTable();

        //Saving a quotation
        saveQuotation();
    }

    //Validating code field
    private void validateCode(){
        quotationCodeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("\\d*")){
                    quotationCodeField.setText(t1.replaceAll("\\D",""));
                }else {
                    if(t1.length() == 6){
                        WebTarget target = ClientBuilder.newClient().target(ApplicationPath.getHqPath()+"/prices/findByCode/"+t1);
                        GenericType<List<Prices>> genericType = new GenericType<>(){};
                        List<Prices> myList = target.request(MediaType.APPLICATION_JSON).get(genericType);
                        if(myList.size() == 0){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Invalid code");
                            Optional<ButtonType> optional1 = alert.showAndWait();
                            if(optional1.isPresent() && optional1.get() == ButtonType.OK)
                                alert.close();
                        }else {
                            quotationDescriptionField.setText(myList.get(myList.size()-1).getDescription());
                            quotationCostField.setText(myList.get(myList.size()-1).getInclusive()+"");
                            quotationBarcodeField.setText(myList.get(myList.size()-1).getBarcode());
                            quotationVatField.setText(myList.get(myList.size()-1).getVat()+"");
                            quotationQuantityField.requestFocus();
                            quotationTotalCostField.setText("");

                            target = ClientBuilder.newClient().target(HOST+"/stock/find/"+t1);
                            GenericType<List<ProductStock>> generic = new GenericType<>(){};
                            List<ProductStock> stockList = target.request(MediaType.APPLICATION_JSON).get(generic);
                            if(stockList.isEmpty()){
                                quotationStockField.setText("0");
                            }else {
                                Collections.sort(stockList, new Comparator<ProductStock>() {
                                    @Override
                                    public int compare(ProductStock o1, ProductStock o2) {
                                        if(o1.getStockIndex() > o2.getStockIndex())
                                            return 1;
                                        else if(o2.getStockIndex() > o1.getStockIndex())
                                            return -1;
                                        else
                                            return 0;
                                    }
                                });

                                quotationStockField.setText(stockList.get(stockList.size()-1).getBal()+"");
                            }
                        }

                    } else if (t1.length() > 6){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Invalid code");
                        Optional<ButtonType> optional1 = alert.showAndWait();
                        if(optional1.isPresent() && optional1.get() == ButtonType.OK)
                            alert.close();
                    }
                }
            }
        });
    }
    //Validating qty field
    private void validateQuantity(){
        quotationQuantityField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("\\d*")){
                    quotationQuantityField.setText(t1.replaceAll("\\D",""));
                } else if (t1.matches("")) {
                    quotationTotalCostField.setText("");
                    quotationOkayButton.setDisable(true);
                } else {
                    quotationTotalCostField.setText(String.format("%,.2f",Integer.parseInt(quotationQuantityField.getText()) *
                            Double.parseDouble(quotationCostField.getText())));
                    quotationOkayButton.setDisable(false);
                }
            }
        });
    }

    //Inserting an item into the table
    private void addToTable(){
        quotationOkayButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ProductQuotation quotation = new ProductQuotation();
                quotation.setClientName(quotationNameField.getText());
                quotation.setContact(quotationContactField.getText());
                quotation.setDetails(quotationCommentField.getText());
                quotation.setCode(quotationCodeField.getText());
                quotation.setDescription(quotationDescriptionField.getText());
                quotation.setQuantity(Integer.parseInt(quotationQuantityField.getText()));
                quotation.setDateRaised(new Date());

                String cost = quotationCostField.getText().replaceAll(",","");
                quotation.setCost(Double.parseDouble(cost));

                String totalCost = quotationTotalCostField.getText().replaceAll(",","");
                quotation.setTotalCost(Double.parseDouble(totalCost));

                quotation.setUserName(getUserName());
                List<ProductQuotation> list = new ArrayList<>();
                list.add(quotation);

                boolean exist = false;
                for (ProductQuotation q:productQuotationList){
                    if(list.get(list.size()-1).getCode().equals(q.getCode())){
                        exist = true;
                    }
                }

                if (exist){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Item already exist");
                    Optional<ButtonType> optional1 = alert.showAndWait();
                    if(optional1.isPresent() && optional1.get() == ButtonType.OK)
                        alert.close();
                }else {
                    if(quotationNameField.getText().equals("") || quotationContactField.getText().equals("") ||
                            quotationCommentField.getText().equals("")){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Client name/Contact/Details is empty");
                        Optional<ButtonType> optional1 = alert.showAndWait();
                        if(optional1.isPresent() && optional1.get() == ButtonType.OK)
                            alert.close();
                    }else {
                        productQuotationList.add(list.get(list.size()-1));
                        quotationTable.setItems(FXCollections.observableArrayList(productQuotationList));

                        double amount = 0;

                        for (ProductQuotation pq: productQuotationList){
                            amount += pq.getTotalCost();
                        }

                        for (ProductQuotation pq: productQuotationList){
                            pq.setAmount(amount);
                        }

                        //Setting amount
                        quotationAmountLabel.setText(String.format("%,.2f",amount));

                        quotationDescriptionField.setText("");
                        quotationStockField.setText("");
                        quotationCostField.setText("");
                        quotationTotalCostField.setText("");
                        quotationCodeField.setText("");
                        quotationQuantityField.setText("");
                        quotationBarcodeField.setText("");
                        quotationVatField.setText("");

                    }
                }


            }
        });
    }

    //Save quotation
    private void saveQuotation(){
        quotationDoneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                List<ProductQuotation> ol = productQuotationList;
                int count = 0;
                if(ol.size() > 0){

                    for(ProductQuotation p: ol){
                        WebTarget target = ClientBuilder.newClient().target(HOST+"/quotation/create");
                        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(p,MediaType.APPLICATION_JSON));

                        count++;
                        if(count == ol.size()){
                            if(response.getStatus() == 204){
                                Alert alert  = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Success");
                                alert.setContentText("Quotation Succsesfully Created");
                                Optional<ButtonType> optional = alert.showAndWait();
                                if(optional.isPresent() & optional.get() == ButtonType.OK)
                                    alert.close();

                                //Updating quotation table
                                updateQuotationTable();

                                Stage stage = (Stage) quotationDoneButton.getScene().getWindow();
                                stage.close();
                            }
                        }

                    }
                }else {
                    Alert alert  = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Failed");
                    alert.setContentText("There is not item to save!");
                    Optional<ButtonType> optional = alert.showAndWait();
                    if(optional.isPresent() & optional.get() == ButtonType.OK)
                        alert.close();
                }
            }
        });
    }

    private void updateQuotationTable(){
        WebTarget target = ClientBuilder.newClient().target(HOST+"/quotation/findAll");
        GenericType<List<ProductQuotation>> genericType = new GenericType<>(){};
        List<ProductQuotation> myList = target.request(MediaType.APPLICATION_JSON).get(genericType);
        getTableView().setItems(FXCollections.observableArrayList(myList));
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public TableView<ProductQuotation> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<ProductQuotation> tableView) {
        this.tableView = tableView;
    }
}
