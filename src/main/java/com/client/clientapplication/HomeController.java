package com.client.clientapplication;

import com.quickrest.entities.Prices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    public MenuItem modulesMenuItem;
    @FXML
    public MenuItem abtMenuItem;
    @FXML
    private MenuBar topMenuBar;
    @FXML
    private AnchorPane homeAnchor;
    @FXML
    private Button inventory;
    @FXML
    private HBox hbox;
    @FXML
    private Button invButton;
    @FXML
    private Button purchaseButton;
    @FXML
    private Button priButton;
    @FXML
    Button homeProductListButton;
    @FXML
    Button homeProductButton;
    @FXML
    private Button homeStockButton;
    @FXML
    private Button homeDeptButton;
    @FXML
    private Button homeCostButton;
    @FXML
    private Button homeLpoButton;
    @FXML
    private Button homeInvoiceButton;
    @FXML
    private Button homeExpButton;
    @FXML
    private Button homeCnsButton;
    @FXML
    private Button homeClientButton;
    @FXML
    private Button homeInfoButton;
    @FXML
    private Button homeTransporterButton;
    @FXML
    private Button homeQuotationButton;
    @FXML
    private Button homeFraterButton;
    @FXML
    private Button homeTransferButton;
    @FXML
    private Button homeRationButton;
    @FXML
    private Button homeGuideButton;
    @FXML
    private Button homeReportsButton;
    @FXML
    private Button homeSuppliersButton;
    @FXML
    private Button homePriceListButton;
    @FXML
    private Button allIconsButton;
    @FXML
    private Button usersButton;
    @FXML
    private Button homeUsersButton;
    @FXML
    private Label userName;
    @FXML
    private Label timeLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private HBox homeButtonsHbox;
    @FXML
    private HBox homeHbox;



    private ObservableList<String> userList;

    public ObservableList<String> getUserList() {
        return userList;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        hbox.prefWidthProperty().bind(homeAnchor.widthProperty());
        homeButtonsHbox.setVisible(false);
        topMenuBar.prefWidthProperty().bind(homeAnchor.widthProperty());
        homeButtonsHbox.prefWidthProperty().bind(homeAnchor.widthProperty());
        homeHbox.prefWidthProperty().bind(homeAnchor.widthProperty());

    }


    public void getUser(String name){
        userName.setText(name);
        List<String> list = new ArrayList<>();
        list.add(name);
        userList = FXCollections.observableArrayList(list);
        Prices prices = new Prices();
        prices.setUser(name);
    }

    @FXML
    public void showAllIcons(){
        homeButtonsHbox.setVisible(true);
        homeCostButton.setVisible(true);
        homeCostButton.setManaged(true);

        homeProductListButton.setVisible(true);
        homeProductListButton.setManaged(true);

        homeQuotationButton.setVisible(true);
        homeQuotationButton.setManaged(true);

        homeTransporterButton.setVisible(true);
        homeTransporterButton.setManaged(true);

        homeStockButton.setVisible(true);
        homeStockButton.setManaged(true);

        homeProductButton.setVisible(true);
        homeProductButton.setManaged(true);

        homeDeptButton.setVisible(true);
        homeDeptButton.setManaged(true);

        homeLpoButton.setVisible(true);
        homeLpoButton.setManaged(true);

        homeInvoiceButton.setVisible(true);
        homeInvoiceButton.setManaged(true);

        homeExpButton.setVisible(true);
        homeExpButton.setManaged(true);

        homeCnsButton.setVisible(true);
        homeCnsButton.setManaged(true);

        homeClientButton.setVisible(true);
        homeClientButton.setManaged(true);

        homeInfoButton.setVisible(true);
        homeInfoButton.setManaged(true);

        homeReportsButton.setVisible(true);
        homeReportsButton.setManaged(true);

        homeFraterButton.setVisible(true);
        homeFraterButton.setManaged(true);

        homeRationButton.setVisible(true);
        homeRationButton.setManaged(true);

        homeTransferButton.setVisible(true);
        homeTransferButton.setManaged(true);

        homeGuideButton.setVisible(true);
        homeGuideButton.setManaged(true);

        homeSuppliersButton.setVisible(true);
        homeSuppliersButton.setManaged(true);

        homePriceListButton.setVisible(true);
        homePriceListButton.setManaged(true);

        homeUsersButton.setVisible(true);
        homeUsersButton.setManaged(true);

    }
    @FXML
    public void openInventoryMenuBar(){
        homeButtonsHbox.setVisible(true);
        homeCostButton.setVisible(false);
        homeCostButton.setManaged(false);

        homeProductListButton.setVisible(true);
        homeProductListButton.setManaged(true);

        homeQuotationButton.setVisible(false);
        homeQuotationButton.setManaged(false);

        homeTransporterButton.setVisible(false);
        homeTransporterButton.setManaged(false);

        homeStockButton.setVisible(true);
        homeStockButton.setManaged(true);

        homeProductButton.setVisible(true);
        homeProductButton.setManaged(true);

        homeDeptButton.setVisible(true);
        homeDeptButton.setManaged(true);

        homeLpoButton.setVisible(false);
        homeLpoButton.setManaged(false);

        homeInvoiceButton.setVisible(false);
        homeInvoiceButton.setManaged(false);

        homeExpButton.setVisible(false);
        homeExpButton.setManaged(false);

        homeCnsButton.setVisible(false);
        homeCnsButton.setManaged(false);

        homeClientButton.setVisible(false);
        homeClientButton.setManaged(false);

        homeInfoButton.setVisible(false);
        homeInfoButton.setManaged(false);

        homeReportsButton.setVisible(true);
        homeReportsButton.setManaged(true);

        homeFraterButton.setVisible(false);
        homeFraterButton.setManaged(false);

        homeRationButton.setVisible(true);
        homeRationButton.setManaged(true);

        homeTransferButton.setVisible(true);
        homeTransferButton.setManaged(true);

        homeGuideButton.setVisible(false);
        homeGuideButton.setManaged(false);

        homeSuppliersButton.setVisible(true);
        homeSuppliersButton.setManaged(true);

        homePriceListButton.setVisible(true);
        homePriceListButton.setManaged(true);

        homeUsersButton.setVisible(false);
        homeUsersButton.setManaged(false);

    }

    @FXML
    private void openPricingMenuBar(){
        homeButtonsHbox.setVisible(true);

        homeStockButton.setVisible(false);
        homeStockButton.setManaged(false);

        homeCostButton.setVisible(true);
        homeCostButton.setManaged(true);

        homeDeptButton.setVisible(false);
        homeDeptButton.setManaged(false);

        homeProductListButton.setVisible(false);
        homeProductListButton.setManaged(false);

        homeLpoButton.setVisible(false);
        homeLpoButton.setManaged(false);

        homeInvoiceButton.setVisible(false);
        homeInvoiceButton.setManaged(false);

        homeExpButton.setVisible(false);
        homeExpButton.setManaged(false);

        homeCnsButton.setVisible(false);
        homeCnsButton.setManaged(false);

        homeProductButton.setVisible(false);
        homeProductButton.setManaged(false);

        homeClientButton.setVisible(false);
        homeClientButton.setManaged(false);

        homeInfoButton.setVisible(false);
        homeInfoButton.setManaged(false);

        homeTransporterButton.setVisible(false);
        homeTransporterButton.setManaged(false);

        homeQuotationButton.setVisible(false);
        homeQuotationButton.setManaged(false);

        homeReportsButton.setVisible(false);
        homeReportsButton.setManaged(false);

        homeFraterButton.setVisible(false);
        homeFraterButton.setManaged(false);

        homeRationButton.setVisible(false);
        homeRationButton.setManaged(false);

        homeTransferButton.setVisible(false);
        homeTransferButton.setManaged(false);

        homeGuideButton.setVisible(false);
        homeGuideButton.setManaged(false);

        homeSuppliersButton.setVisible(false);
        homeSuppliersButton.setManaged(false);

        homePriceListButton.setVisible(false);
        homePriceListButton.setManaged(false);

        homeUsersButton.setVisible(false);
        homeUsersButton.setManaged(false);
    }
    @FXML
    private void openPurchaseMenuBar(){
        homeButtonsHbox.setVisible(true);

        homeStockButton.setVisible(false);
        homeStockButton.setManaged(false);

        homeCostButton.setVisible(false);
        homeCostButton.setManaged(false);

        homeCostButton.setManaged(false);
        homeCostButton.setManaged(false);

        homeDeptButton.setVisible(false);
        homeDeptButton.setManaged(false);

        homeProductListButton.setVisible(false);
        homeProductListButton.setManaged(false);

        homeLpoButton.setVisible(true);
        homeLpoButton.setManaged(true);

        homeInvoiceButton.setVisible(false);
        homeInvoiceButton.setManaged(false);

        homeExpButton.setVisible(false);
        homeExpButton.setManaged(false);

        homeCnsButton.setVisible(true);
        homeCnsButton.setManaged(true);

        homeProductButton.setVisible(false);
        homeProductButton.setManaged(false);

        homeClientButton.setVisible(false);
        homeClientButton.setManaged(false);

        homeInfoButton.setVisible(false);
        homeInfoButton.setManaged(false);

        homeTransporterButton.setVisible(false);
        homeTransporterButton.setManaged(false);

        homeQuotationButton.setVisible(false);
        homeQuotationButton.setManaged(false);

        homeReportsButton.setVisible(false);
        homeReportsButton.setManaged(false);

        homeFraterButton.setVisible(false);
        homeFraterButton.setManaged(false);

        homeRationButton.setVisible(false);
        homeRationButton.setManaged(false);

        homeTransferButton.setVisible(false);
        homeTransferButton.setManaged(false);

        homeGuideButton.setVisible(true);
        homeGuideButton.setManaged(true);

        homeSuppliersButton.setVisible(false);
        homeSuppliersButton.setManaged(false);

        homePriceListButton.setVisible(false);
        homePriceListButton.setManaged(false);

        homeUsersButton.setVisible(false);
        homeUsersButton.setManaged(false);

    }

    @FXML
    private void openInvoiceMenuBar(){
        String user = userList.get(userList.size()-1);
        if(user.equals("g")){
            homeInvoiceButton.setVisible(false);
            homeInvoiceButton.setManaged(false);
        }else if (user.equals("r")){
            homeInvoiceButton.setVisible(true);
            homeInvoiceButton.setManaged(true);
        }

        homeButtonsHbox.setVisible(true);

        homeStockButton.setVisible(false);
        homeStockButton.setManaged(false);

        homeCostButton.setVisible(false);
        homeCostButton.setManaged(false);

        homeDeptButton.setVisible(false);
        homeDeptButton.setManaged(false);

        homeProductListButton.setVisible(false);
        homeProductListButton.setManaged(false);

        homeLpoButton.setVisible(false);
        homeLpoButton.setManaged(false);

        homeExpButton.setVisible(false);
        homeExpButton.setManaged(false);

        homeCnsButton.setVisible(false);
        homeCnsButton.setManaged(false);

        homeProductButton.setVisible(false);
        homeProductButton.setManaged(false);

        homeClientButton.setVisible(true);
        homeClientButton.setManaged(true);

        homeInfoButton.setVisible(true);
        homeInfoButton.setManaged(true);

        homeTransporterButton.setVisible(true);
        homeTransporterButton.setManaged(true);

        homeQuotationButton.setVisible(true);
        homeQuotationButton.setManaged(true);

        homeReportsButton.setVisible(false);
        homeReportsButton.setManaged(false);

        homeFraterButton.setVisible(false);
        homeFraterButton.setManaged(false);

        homeRationButton.setVisible(false);
        homeRationButton.setManaged(false);

        homeTransferButton.setVisible(false);
        homeTransferButton.setManaged(false);

        homeGuideButton.setVisible(false);
        homeGuideButton.setManaged(false);

        homeSuppliersButton.setVisible(false);
        homeSuppliersButton.setManaged(false);

        homePriceListButton.setVisible(false);
        homePriceListButton.setManaged(false);

        homeUsersButton.setVisible(false);
        homeUsersButton.setManaged(false);

    }
    @FXML
    private void openExpenceMenuBar(){
        homeButtonsHbox.setVisible(true);

        homeStockButton.setVisible(false);
        homeStockButton.setManaged(false);

        homeCostButton.setVisible(false);
        homeCostButton.setManaged(false);

        homeDeptButton.setVisible(false);
        homeDeptButton.setManaged(false);

        homeProductListButton.setVisible(false);
        homeProductListButton.setManaged(false);

        homeLpoButton.setVisible(false);
        homeLpoButton.setManaged(false);

        homeInvoiceButton.setVisible(false);
        homeInvoiceButton.setManaged(false);

        homeExpButton.setVisible(true);
        homeExpButton.setManaged(true);

        homeCnsButton.setVisible(false);
        homeCnsButton.setManaged(false);

        homeProductButton.setVisible(false);
        homeProductButton.setManaged(false);

        homeClientButton.setVisible(false);
        homeClientButton.setManaged(false);

        homeInfoButton.setVisible(false);
        homeInfoButton.setManaged(false);

        homeTransporterButton.setVisible(false);
        homeTransporterButton.setManaged(false);

        homeQuotationButton.setVisible(false);
        homeQuotationButton.setManaged(false);

        homeReportsButton.setVisible(false);
        homeReportsButton.setManaged(false);

        homeFraterButton.setVisible(false);
        homeFraterButton.setManaged(false);

        homeRationButton.setVisible(false);
        homeRationButton.setManaged(false);

        homeTransferButton.setVisible(false);
        homeTransferButton.setManaged(false);

        homeGuideButton.setVisible(false);
        homeGuideButton.setManaged(false);

        homeSuppliersButton.setVisible(false);
        homeSuppliersButton.setManaged(false);

        homePriceListButton.setVisible(false);
        homePriceListButton.setManaged(false);

        homeUsersButton.setVisible(false);
        homeUsersButton.setManaged(false);
    }

    @FXML
    public void manageUsers(){
        homeButtonsHbox.setVisible(true);

        homeUsersButton.setVisible(true);
        homeUsersButton.setManaged(true);

        homeStockButton.setVisible(false);
        homeStockButton.setManaged(false);

        homeCostButton.setVisible(false);
        homeCostButton.setManaged(false);

        homeDeptButton.setVisible(false);
        homeDeptButton.setManaged(false);

        homeProductListButton.setVisible(false);
        homeProductListButton.setManaged(false);

        homeLpoButton.setVisible(false);
        homeLpoButton.setManaged(false);

        homeInvoiceButton.setVisible(false);
        homeInvoiceButton.setManaged(false);

        homeExpButton.setVisible(false);
        homeExpButton.setManaged(false);

        homeCnsButton.setVisible(false);
        homeCnsButton.setManaged(false);

        homeProductButton.setVisible(false);
        homeProductButton.setManaged(false);

        homeClientButton.setVisible(false);
        homeClientButton.setManaged(false);

        homeInfoButton.setVisible(false);
        homeInfoButton.setManaged(false);

        homeTransporterButton.setVisible(false);
        homeTransporterButton.setManaged(false);

        homeQuotationButton.setVisible(false);
        homeQuotationButton.setManaged(false);

        homeReportsButton.setVisible(false);
        homeReportsButton.setManaged(false);

        homeFraterButton.setVisible(false);
        homeFraterButton.setManaged(false);

        homeRationButton.setVisible(false);
        homeRationButton.setManaged(false);

        homeTransferButton.setVisible(false);
        homeTransferButton.setManaged(false);

        homeGuideButton.setVisible(false);
        homeGuideButton.setManaged(false);

        homeSuppliersButton.setVisible(false);
        homeSuppliersButton.setManaged(false);

        homePriceListButton.setVisible(false);
        homePriceListButton.setManaged(false);

    }
    @FXML
    private void openCompany(){
        homeButtonsHbox.setVisible(true);

        homeStockButton.setVisible(false);
        homeStockButton.setManaged(false);

        homeCostButton.setVisible(false);
        homeCostButton.setManaged(false);

        homeDeptButton.setVisible(false);
        homeDeptButton.setManaged(false);

        homeProductListButton.setVisible(false);
        homeProductListButton.setManaged(false);

        homeLpoButton.setVisible(false);
        homeLpoButton.setManaged(false);

        homeInvoiceButton.setVisible(false);
        homeInvoiceButton.setManaged(false);

        homeExpButton.setVisible(false);
        homeExpButton.setManaged(false);

        homeCnsButton.setVisible(false);
        homeCnsButton.setManaged(false);

        homeProductButton.setVisible(false);
        homeProductButton.setManaged(false);

        homeClientButton.setVisible(false);
        homeClientButton.setManaged(false);

        homeInfoButton.setVisible(false);
        homeInfoButton.setManaged(false);

        homeTransporterButton.setVisible(false);
        homeTransporterButton.setManaged(false);

        homeQuotationButton.setVisible(false);
        homeQuotationButton.setManaged(false);

        homeReportsButton.setVisible(false);
        homeReportsButton.setManaged(false);

        homeFraterButton.setVisible(true);
        homeFraterButton.setManaged(true);

        homeRationButton.setVisible(false);
        homeRationButton.setManaged(false);

        homeTransferButton.setVisible(false);
        homeTransferButton.setManaged(false);

        homeGuideButton.setVisible(false);
        homeGuideButton.setManaged(false);

        homeSuppliersButton.setVisible(false);
        homeSuppliersButton.setManaged(false);

        homePriceListButton.setVisible(false);
        homePriceListButton.setManaged(false);

        homeUsersButton.setVisible(false);
        homeUsersButton.setManaged(false);
    }

    @FXML
    public void openNewProduct(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Product.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            ProductController controller = loader.getController();
            controller.setUserName(getUserList().get(0));
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    public void openLposPage(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LPOS.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Purchases");
            LPOSController controller = loader.getController();
            controller.setUser(getUserList().get(0));
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    public void openPricingPage(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Pricing.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            PricingController p = loader.getController();
            p.populateUserField(userList.get(0));
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void openStockPage(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Stock.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            StockController controller = loader.getController();
            controller.setUserName(getUserList().get(0));
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void openInvoicingPage(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("InvoicePage.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();

            InvoicePageController controller = loader.getController();
            controller.setUser(userList.get(0));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void addUser(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(homeAnchor.getScene().getWindow());
        dialog.setTitle("USER REGISTRATION");
        dialog.setHeaderText("All The Fields Are Required");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("RegistrationPage.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());

        } catch (IOException ex) {

        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> optional = dialog.showAndWait();
        if(optional.isPresent() && optional.get() == ButtonType.OK){

        }else{

        }

    }

    @FXML
    public void manageCompany(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MyCompany.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();

            MyCompanyController controller = loader.getController();
            controller.setUserName(userList.get(userList.size()-1));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void manageClients(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Clients.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();

            ClientsController controller = loader.getController();
            controller.setUser(userList.get(0));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void manageSupppliers(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Suppliers.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();

            SuppliersController sc = loader.getController();
            sc.setUser(userList.get(0));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void manageTransporters(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Transporter.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();

           TransporterController controller = loader.getController();
            controller.setUser(userList.get(0));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void manageCreditNotes(){
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("CreditNotes.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);

            CreditNotesController controller = fxmlLoader.getController();
            controller.setUserName(getUserList().get(0));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void manageQuotation(){
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("QuotationHomePage.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);

            QuotationHomePageController controller = fxmlLoader.getController();
            controller.setUserName(getUserList().get(0));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void manageDepartment(){
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Department.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);

            DepartmentController controller = fxmlLoader.getController();
            controller.setUserName(getUserList().get(0));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void manageProductAnalyser(){
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("ProductAnalyser.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
