package com.client.clientapplication;

import com.quickrest.resources.ApplicationPath;
import com.quickrest.resources.PODetails;
import com.quickrest.resources.ReceivedGrn;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class ReceivingLpoPriceConfirmation implements Initializable {
    @FXML
    private Button priceConfCancel;
    @FXML
    private TextField priceConfCode;
    @FXML
    private TextField priceConfDiscount;
    @FXML
    private TextField priceConfIncl;
    @FXML
    private TextField priceConfName;
    @FXML
    private Button priceConfOkay;
    @FXML
    private TextField priceConfSystCost;
    @FXML
    private TextField priceConfVat;
    @FXML
    private TextField priceConfexclusiveTxt;


    TextField exclusiveTextField;
    TextField inclusiveTextField;
    TextField vatTextField;
    TextField receiveLpoReceivedQty;
    TextField receiveLpoCode;
    TextField receiveLpoDescription;
    TextField receiveLpoOrderedQty;

    TableView<ReceivedGrn> grnTableView;
    List<Integer> indexList;
    List<ReceivedGrn> received;
    Button receive;

    private int rowIndex;
    int qtyReceived;
    int selectedIndex = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        priceConfexclusiveTxt.setEditable(false);
        priceConfDiscount.setEditable(false);
        priceConfVat.setDisable(true);
        priceConfOkay.requestFocus();

    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public void setQtyReceived(int qtyReceived) {
        this.qtyReceived = qtyReceived;
    }

    public int getQtyReceived() {
        return qtyReceived;
    }

    public TableView<ReceivedGrn> getGrnTableView() {
        return grnTableView;
    }

    public void setGrnTableView(TableView<ReceivedGrn> grnTableView) {
        this.grnTableView = grnTableView;
    }

    public TextField getInclusiveTextField() {
        return inclusiveTextField;
    }

    public void setInclusiveTextField(TextField inclusiveTextField) {
        this.inclusiveTextField = inclusiveTextField;
    }

    public TextField getVatTextField() {
        return vatTextField;
    }

    public void setVatTextField(TextField vatTextField) {
        this.vatTextField = vatTextField;
    }

    public TextField getExclusiveTextField() {
        return exclusiveTextField;
    }

    public void setExclusiveTextField(TextField exclusiveTextField) {
        this.exclusiveTextField = exclusiveTextField;
    }

    public List<ReceivedGrn> getReceived() {
        return received;
    }

    public void setReceived(List<ReceivedGrn> received) {
        this.received = received;
    }

    public TextField getReceiveLpoReceivedQty() {
        return receiveLpoReceivedQty;
    }

    public void setReceiveLpoReceivedQty(TextField receiveLpoReceivedQty) {
        this.receiveLpoReceivedQty = receiveLpoReceivedQty;
    }

    public TextField getReceiveLpoCode() {
        return receiveLpoCode;
    }

    public void setReceiveLpoCode(TextField receiveLpoCode) {
        this.receiveLpoCode = receiveLpoCode;
    }

    public TextField getReceiveLpoDescription() {
        return receiveLpoDescription;
    }

    public void setReceiveLpoDescription(TextField receiveLpoDescription) {
        this.receiveLpoDescription = receiveLpoDescription;
    }

    public TextField getReceiveLpoOrderedQty() {
        return receiveLpoOrderedQty;
    }

    public void setReceiveLpoOrderedQty(TextField receiveLpoOrderedQty) {
        this.receiveLpoOrderedQty = receiveLpoOrderedQty;
    }

    public Button getReceive() {
        return receive;
    }

    public void setReceive(Button receive) {
        this.receive = receive;
    }

    public void populateFields(String code, String desc, double excl, double disc, double incl,double vat){
        priceConfCode.setText(code);
        priceConfName.setText(desc);
        priceConfexclusiveTxt.setText(String.valueOf(excl));
        priceConfDiscount.setText(String.valueOf(disc));
        priceConfIncl.setText(String.valueOf(incl));
        priceConfVat.setText(String.valueOf(Math.round((vat/excl)*100)));
    }
    //Fetching vat and system cost
    public void fetchCurrentSystemCost(double cost){
        priceConfSystCost.setText(String.valueOf(cost));
    }

    //Receive a quantity into the table
    @FXML
    public void receiveAnItemQuantity(){
        ReceivedGrn receivedGrn = getGrnTableView().getSelectionModel().getSelectedItem();
        getReceived().forEach(e->{
            if(e == receivedGrn){
                e.setQtyReceived(getQtyReceived());
                e.setBalQty(e.getQtyOrdered() - e.getQtyReceived());

                double excl = (e.getQtyReceived()*e.getCostExclusive());
                double vat = (e.getQtyReceived()*e.getCostInclusive())-(e.getQtyReceived()*e.getCostExclusive());
                double incl = (e.getQtyReceived()*e.getCostInclusive());

                e.setAmountReceivedExcl(excl);
                e.setAmountReceivedVat(vat);
                e.setAmountReceivedIncl(incl);
            }
        });

        double sumEx = 0;
        double vatSum = 0;
        double inclSum = 0;

        Set<ReceivedGrn> set = new HashSet<>();
        for(int g=getReceived().size()-1; g>=0; g--){
            set.add(getReceived().get(g));
        }

        for(ReceivedGrn g:set){
            if(g != null){
                sumEx += g.getAmountReceivedExcl();
                vatSum += g.getAmountReceivedVat();
                inclSum += g.getAmountReceivedIncl();

                getExclusiveTextField().setText(String.format("%,.2f",sumEx));
                getVatTextField().setText(String.format("%,.2f",vatSum));
                getInclusiveTextField().setText(String.format("%,.2f",inclSum));
            }

        }

        Stage stage = (Stage) priceConfOkay.getScene().getWindow();
        stage.close();
        grnTableView.getItems().set(getGrnTableView().getSelectionModel().getSelectedIndex(), getReceived().get(getReceived().size()-1));

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getGrnTableView().requestFocus();

                int value = getIndexList().size();
                getIndexList().add(value);

                int row = getRowIndex();

                if(row != -1){
                    getGrnTableView().getSelectionModel().select(row + 1);
                    getGrnTableView().getFocusModel().focus(row + 1);
                }else {
                    getGrnTableView().getSelectionModel().select(0);
                    getGrnTableView().getFocusModel().focus(0);
                }
                //getGrnTableView().getSelectionModel().select(getIndexList().get(getIndexList().size()-1));
                //getGrnTableView().getFocusModel().focus(getIndexList().get(getIndexList().size()-1));




                //int newIndex = getGrnTableView().getSelectionModel().getFocusedIndex();
                int newIndex = getGrnTableView().getSelectionModel().getSelectedIndex();
                System.out.println("Index="+newIndex);
                if(newIndex == -1){
                    System.out.println("Null part"+newIndex);
                    getReceived().add(null);
                    getReceive().setDisable(true);
                }else {
                    ReceivedGrn grn = grnTableView.getSelectionModel().getSelectedItem();
                    //getReceived().add(getGrnTableView().getItems().get(newIndex));
                    getReceived().add(grn);
                    getReceiveLpoReceivedQty().setText("");
                    //getReceiveLpoOrderedQty().setText(String.valueOf(received.get(newIndex).getQtyOrdered()));
                    getReceiveLpoOrderedQty().setText(String.valueOf(grn.getQtyOrdered()));
                    //getReceiveLpoCode().setText(received.get(newIndex).getCode());
                    getReceiveLpoCode().setText(grn.getCode());
                    //getReceiveLpoDescription().setText(received.get(newIndex).getDescription());
                    getReceiveLpoDescription().setText(grn.getDescription());

                }
                getReceiveLpoReceivedQty().requestFocus();


            }
        });




    }
    //Close price confirmation window
    @FXML
    public void closeConfirmationWindow(){
        Stage stage = (Stage) priceConfCancel.getScene().getWindow();
        stage.close();
    }

    public int getSelectedIndex(){
        return selectedIndex;
    }
    public void setSelectedIndex(int index){
        this.selectedIndex = index;
    }

    public void setIndexList(List<Integer> index){
        this.indexList = index;
    }

    public List<Integer> getIndexList(){
        return indexList;
    }
}
