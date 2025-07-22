package com.client.clientapplication;

import com.quickrest.entities.Prices;
import com.quickrest.entities.ProductLiveStock;
import com.quickrest.entities.ProductStock;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class PriceConfirmationController implements Initializable {

    @FXML
    private AnchorPane priceConfirmationAnchor;
    @FXML
    private Label priceConfirmationCode;

    @FXML
    private Label priceConfirmationDisc;

    @FXML
    private Label priceConfirmationExcl;

    @FXML
    private Label priceConfirmationIncl;

    @FXML
    private Label priceConfirmationPrice;

    @FXML
    private Label name;

    @FXML
    private AnchorPane priceConfirmationDialog;

    @FXML
    private Label priceConfirmationSupplier;

    @FXML
    private Label priceConfirmationVat;

    @FXML
    private Button priceConfirmationSave;

    @FXML
    private TextField priceConfirmationPackaging;

    @FXML
    private TextField priceConfirmationUnits;

    Prices prices;
    Stage stage;
    TableView<Prices> tableView;
    List<Prices> pricesList = new ArrayList<>();



    private static final String  HOST = ApplicationPath.getHqPath();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Task<ObservableList<Prices>> task = new Task<ObservableList<Prices>>() {
            @Override
            protected ObservableList<Prices> call() throws Exception {
                WebTarget target1 = ClientBuilder.newClient().target(HOST+"/prices/findAll");
                List<Prices> list = target1.request(MediaType.APPLICATION_JSON).get(new GenericType<List<Prices>>(){});
                if(list != null){
                    pricesList = list;
                }
                return FXCollections.observableArrayList(list);
            }
        };
        new Thread(task).start();
    }

    public void setValues(String code,String name,String supplier){
        this.priceConfirmationCode.setText(code);
        this.name.setText(name);
        this.priceConfirmationSupplier.setText(supplier);
    }
    public void setCostValues(String exc,String vat,String disc,String inc,String selling){
        this.priceConfirmationExcl.setText(exc);
        this.priceConfirmationVat.setText(vat);
        this.priceConfirmationDisc.setText(disc);
        this.priceConfirmationIncl.setText(inc);
        this.priceConfirmationPrice.setText(selling);

    }

    public Prices getPrices(){
        return prices;

    }

    public void setPrices(Prices prices,Stage stage,TableView<Prices> tableView){
        this.prices = prices;
        this.stage = stage;
        this.tableView = tableView;
    }

    public void setUnitAndPacksize(String unit,String packSize){
        priceConfirmationPackaging.setText(packSize);
        priceConfirmationUnits.setText(unit);
    }
    @FXML
    public void savePrices(){
        Prices prices1 = getPrices();
        boolean status = true;
        Response response1 = null;
        Response response2 = null;

        if(pricesList.size() == 0){
            WebTarget target = ClientBuilder.newClient().target(HOST+"/prices/create");
            Response response = target.request(MediaType.APPLICATION_JSON).
                    post(Entity.entity(prices1,MediaType.APPLICATION_JSON));

            //Updating stock
            Response response4 = updateStock(prices1);
            Response response5 = updatingLiveStock(prices1);

            if(response.getStatus() == 200 && response4.getStatus() == 200 && response5.getStatus() == 204){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Success");
                alert.setContentText("Product price successfully created");
                Optional<ButtonType> optional3 = alert.showAndWait();

                if(optional3.isPresent() && optional3.get() == ButtonType.OK)
                    alert.close();
                stage.close();
                updatePrcices(response);

            }else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Failed!");
                alert.setContentText("Product price not created");
                Optional<ButtonType> optional2 = alert.showAndWait();

                if (optional2.isPresent() && optional2.get() == ButtonType.OK)
                    alert.close();
                stage.close();

            }
        }else {
            do{


                for(Prices p:pricesList){
                    if(p.getCode().equals(prices1.getCode())){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Failed!");
                        alert.setContentText("Product price already exist");
                        Optional<ButtonType> optional2 = alert.showAndWait();

                        if (optional2.isPresent() && optional2.get() == ButtonType.OK)
                            alert.close();
                        stage.close();
                        status = false;
                        break;
                    }

                }

                if(status){
                    WebTarget target1 = ClientBuilder.newClient().target(HOST+"/prices/create");
                    Response response3 = target1.request(MediaType.APPLICATION_JSON).post(Entity.
                            entity(prices1,MediaType.APPLICATION_JSON));

                    //Updating stock
                    Response response4 = updateStock(prices1);
                    Response response5 = updatingLiveStock(prices1);

                    if(response3.getStatus() == 200 && response4.getStatus() == 200 && response5.getStatus() == 204){
                        System.out.println("Again");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Success");
                        alert.setContentText("Product price successfully created");
                        Optional<ButtonType> optional3 = alert.showAndWait();

                        if(optional3.isPresent() && optional3.get() == ButtonType.OK)
                            alert.close();
                        stage.close();
                        updatePrcices(response3);
                        break;
                    }else {
                        System.out.println("Problem2");
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("Failed!");
                        alert.setContentText("Product price not created");
                        Optional<ButtonType> optional2 = alert.showAndWait();

                        if (optional2.isPresent() && optional2.get() == ButtonType.OK)
                            alert.close();
                        stage.close();
                        break;
                    }

                }
            }while (status);

        }


    }

    @FXML
    public void cancelPrices(){
        stage.close();
    }

    private void updatePrcices(Response response){
        Prices p = response.readEntity(Prices.class);
        ObservableList<Prices> ol = FXCollections.observableArrayList();
        ol.add(p);
        tableView.setItems(ol);
    }

    public  void upddateStage(Stage stage1){
        stage1.close();
        stage1.show();
    }

    private Response updateStock(Prices prices1){
        long stockIndex;
        Response response = null;

        WebTarget target = ClientBuilder.newClient().target(ApplicationPath.getApplicationPath()+"/stock/findAll");
        GenericType<List<ProductStock>> genericType = new GenericType<>(){};
        List<ProductStock> productStock = target.request(MediaType.APPLICATION_JSON).get(genericType);

        if(productStock.size() > 0){
            Collections.sort(productStock, new Comparator<ProductStock>() {
                @Override
                public int compare(ProductStock o1, ProductStock o2) {
                    if(o1.getStockIndex() > o2.getStockIndex())
                        return 1;
                    else if (o2.getStockIndex() > o1.getStockIndex())
                        return -1;
                    else
                        return 0;
                }
            });
            stockIndex = productStock.get(productStock.size()-1).getStockIndex();

            //Setting initial stock balance
            ProductStock stock = new ProductStock();
            stock.setCode(prices1.getCode());
            stock.setDescription(prices1.getDescription());
            stock.setStockIndex(stockIndex + 1);
            stock.setSupplier(prices1.getSupplier());
            stock.setTransactionType("ISB");
            stock.setDateUpdated(new Date());
            stock.setExclCost(prices1.getExcl());
            stock.setVat(prices1.getVat());
            stock.setInclCost(prices1.getInclusive());
            stock.setInitialStockBal(0);

            target = ClientBuilder.newClient().target(ApplicationPath.getApplicationPath()+"/stock/create");
            response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(stock,MediaType.APPLICATION_JSON));


            return response;


        }else {
            //Setting initial stock balance
            ProductStock stock = new ProductStock();
            stock.setCode(prices1.getCode());
            stock.setDescription(prices1.getDescription());
            stock.setStockIndex(1);
            stock.setSupplier(prices1.getSupplier());
            stock.setTransactionType("ISB");
            stock.setDateUpdated(new Date());
            stock.setExclCost(prices1.getExcl());
            stock.setVat(prices1.getVat());
            stock.setInclCost(prices1.getInclusive());
            stock.setInitialStockBal(0);
            stock.setDocNumber(1);

            target = ClientBuilder.newClient().target(ApplicationPath.getApplicationPath()+"/stock/create");
            return target.request(MediaType.APPLICATION_JSON).post(Entity.entity(stock,MediaType.APPLICATION_JSON));


        }

    }

    private Response updatingLiveStock(Prices prices){
        ProductLiveStock productLiveStock = new ProductLiveStock();
        productLiveStock.setCode(prices.getCode());
        productLiveStock.setDescription(prices.getDescription());
        productLiveStock.setCostExclusive(prices.getExcl());
        productLiveStock.setCostInclusive(prices.getInclusive());
        productLiveStock.setDepartment(prices.getDept());
        productLiveStock.setSubDepartment(prices.getSubDepartment());
        productLiveStock.setSupplier(prices.getSupplier());
        productLiveStock.setLastReceived(new Date());

        WebTarget target = ClientBuilder.newClient().target(ApplicationPath.getApplicationPath()+"/liveStock/create");
        return target.request(MediaType.APPLICATION_JSON).post(Entity.entity(productLiveStock,MediaType.APPLICATION_JSON));
    }
}
