package com.client.clientapplication;

import com.quickrest.entities.*;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class StockAdjustmentController implements Initializable {

    @FXML
    private Button adjustmentButton;
    @FXML
    private TextField adjustmentQtyTextField;
    @FXML
    private RadioButton alignment;
    @FXML
    private RadioButton damage;
    @FXML
    private RadioButton theft;
    @FXML
    private RadioButton stockTake;

    private String code;
    private String userName;
    private TextField balanceQuantityField;
    TableView<ProductStock> stockTableView;
    private static final String  HOST = ApplicationPath.getApplicationPath();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Disallowing non digits for adjustmentQtyTextField
        adjustmentQtyTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("[-\\d*]")){
                    adjustmentQtyTextField.setText(t1.replaceAll("[^-\\d]",""));
                }
            }
        });

    }

    @FXML
    public void adjustStock(){
        //Fetching stock
        ProductStock productStock = new ProductStock();
        WebTarget target = ClientBuilder.newClient().target(HOST+"/stock/find/"+getCode());
        GenericType<List<ProductStock>> genericType = new GenericType<>(){};
        List<ProductStock> list = target.request(MediaType.APPLICATION_JSON).get(genericType);
        //Sorting the list
        Collections.sort(list, new Comparator<ProductStock>() {
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

        target = ClientBuilder.newClient().target(HOST+"/adjustment/findAll");
        GenericType<List<ProductStockAdjustment>> generic = new GenericType<>(){};
        List<ProductStockAdjustment> adjustmentList = target.request(MediaType.APPLICATION_JSON).get(generic);

        int number = 0;
        if(adjustmentList.size() >= 1){
            //Sorting the list
            Collections.sort(adjustmentList, new Comparator<ProductStockAdjustment>() {
                @Override
                public int compare(ProductStockAdjustment o1, ProductStockAdjustment o2) {
                    if(o2.getAdjustmentNumber() > o1.getAdjustmentNumber())
                        return 1;
                    else if(o1.getAdjustmentNumber() > o2.getAdjustmentNumber())
                        return -1;
                    else
                        return 0;
                }
            });
            number = adjustmentList.get(0).getAdjustmentNumber();
        }


        ProductStock stock = list.get(list.size()-1);

        productStock.setStockIndex(stock.getStockIndex() + 1);
        productStock.setCode(getCode());
        productStock.setDateUpdated(new Date());
        productStock.setSupplier(stock.getSupplier());
        productStock.setVat(stock.getVat());
        productStock.setInclCost(stock.getInclCost());
        int qty = Integer.parseInt(adjustmentQtyTextField.getText());
        productStock.setExclCost(stock.getExclCost());
        productStock.setTransactionType("Adjustment");
        productStock.setDescription(stock.getDescription());
        productStock.setQtyIn(qty);
        productStock.setDocNumber(number + 1);
        productStock.setBal(stock.getBal()+qty);

        target = ClientBuilder.newClient().target(HOST+"/stock/create");
        Response response = target.request(MediaType.APPLICATION_JSON).
                post(Entity.entity(productStock,MediaType.APPLICATION_JSON));

        //Updating live stock
        int liveStock = productStock.getBal();
        double costExcl = Double.parseDouble(String.format("%.2f",liveStock * stock.getExclCost()));
        double inclCost = Double.parseDouble(String.format("%.2f",liveStock * stock.getInclCost()));

        target = ClientBuilder.newClient().target(HOST+"/liveStock/update/"+getCode()+"/"+liveStock+"/"
        +inclCost+"/"+costExcl);
        GenericType<List<Integer>> genericType1 = new GenericType<>(){};
        List<Integer> updateResponse = target.request(MediaType.APPLICATION_JSON).get(genericType1);

        if(response.getStatus() == 200 && updateResponse.get(0) >0){
            //Fetching product details
            target = ClientBuilder.newClient().target(ApplicationPath.getHqPath()+"/prices/findByCode/"+getCode());
            GenericType<List<Prices>> priccesGeneric = new GenericType<>(){};
            List<Prices> pricesList = target.request(MediaType.APPLICATION_JSON).get(priccesGeneric);
            Prices prices = pricesList.get(0);

            ProductStockAdjustment adjustment = new ProductStockAdjustment();
            adjustment.setAdjustedQuantity(Integer.parseInt(adjustmentQtyTextField.getText()));
            adjustment.setAdjustmentDate(new Date());
            adjustment.setAdjustmentNumber(number+1);
            adjustment.setCode(prices.getCode());
            adjustment.setDepartment(prices.getDept());
            adjustment.setDescription(prices.getDescription());

            String adjustmentReason;
            //Getting reason
            if(alignment.isSelected()){
                adjustmentReason = "Stock Alignment";
            } else if (damage.isSelected()) {
                adjustmentReason = "Damage";
            } else if (theft.isSelected()) {
                adjustmentReason = "Theft";
            }else {
                adjustmentReason = "StockTake";
            }
            adjustment.setAdjustmentReason(adjustmentReason);
            adjustment.setAdjustmentValue(Integer.parseInt(adjustmentQtyTextField.getText()) * productStock.getInclCost());
            adjustment.setUserName(getUserName());

            target = ClientBuilder.newClient().target(HOST+"/adjustment/create");
            target.request(MediaType.APPLICATION_JSON).
                    post(Entity.entity(adjustment,MediaType.APPLICATION_JSON));


            //Updating stock value
            target = ClientBuilder.newClient().target(HOST+"/stockHolding/findAll");
            GenericType<List<ProductDepartmentalStockholding>> genericType2 = new GenericType<>(){};
            List<ProductDepartmentalStockholding> stockholding = target.request(MediaType.APPLICATION_JSON).get(genericType2);

            if(stockholding.size() > 0){
                //Calculating stock holding
                boolean state = false;
                double valueExclusive = 0,valueInclusive = 0;

                for(ProductDepartmentalStockholding sh:stockholding){
                    if(sh.getSubDepartment().equals(prices.getSubDepartment())){
                        valueExclusive =+ sh.getExclusiveValue()+(productStock.getQtyIn()*productStock.getExclCost());
                        valueInclusive =+sh.getInclusiveValue()+(productStock.getQtyIn()*productStock.getInclCost());
                        state = true;
                    }
                }


                if(state){
                    target = ClientBuilder.newClient().target(HOST+"/stockHolding/update/"+prices.getSubDepartment()+
                            "/"+valueExclusive+"/"+valueInclusive);
                    GenericType<List<Integer>> g = new GenericType<>(){};
                    target.request(MediaType.APPLICATION_JSON).get(g);

                }

            }else {
                ProductDepartmentalStockholding ds = new ProductDepartmentalStockholding();
                ds.setDepartment(prices.getDept());
                ds.setSubDepartment(prices.getSubDepartment());
                ds.setExclusiveValue(Double.parseDouble(String.format("%.2f",
                        productStock.getQtyIn() * productStock.getExclCost())));
                ds.setInclusiveValue(Double.parseDouble(String.format("%.2f",productStock.getQtyIn() *
                        productStock.getInclCost())));

                target = ClientBuilder.newClient().target(HOST+"/stockHolding/create");
                target.request(MediaType.APPLICATION_JSON).post(Entity.entity(ds,MediaType.APPLICATION_JSON));

            }


            Stage stage = (Stage) adjustmentButton.getScene().getWindow();
            stage.close();


        }
        //Refreshing stock table
        refreshingStockTable();

    }

    private void refreshingStockTable(){
        WebTarget target = ClientBuilder.newClient().target(HOST+"/stock/find/"+getCode());
        GenericType<List<ProductStock>> genericType = new GenericType<>(){};
        List<ProductStock> list = target.request(MediaType.APPLICATION_JSON).get(genericType);
        //Sorting the list
        Collections.sort(list, new Comparator<ProductStock>() {
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
        getStockTableView().setItems(FXCollections.observableArrayList(list));
        getBalanceQuantityField().setText(list.get(list.size()-1).getBal()+"");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public TableView<ProductStock> getStockTableView() {
        return stockTableView;
    }

    public void setStockTableView(TableView<ProductStock> stockTableView) {
        this.stockTableView = stockTableView;
    }

    public TextField getBalanceQuantityField() {
        return balanceQuantityField;
    }

    public void setBalanceQuantityField(TextField balanceQuantityField) {
        this.balanceQuantityField = balanceQuantityField;
    }
}
