package com.client.clientapplication;

import com.quickrest.entities.BranchDetails;
import com.quickrest.resources.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BranchDetailsViewController implements Initializable {
    @FXML
    private AnchorPane branchDetailsViewBodyAnchor;
    @FXML
    private CheckBox searchBranch;
    @FXML
    private TableView<BranchDetails> branchDetailsViewTable;
    @FXML
    private Label branchDetailsViewCount;
    @FXML
    private TextField branchDetailsViewTextField;


    private static final String  HOST = ApplicationPath.getHqPath();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Fetching branches
        WebTarget target = ClientBuilder.newClient().target(HOST + "/branch/findAll");
        GenericType<List<BranchDetails>> genericType = new GenericType<>() {
        };
        List<BranchDetails> list1 = target.request(MediaType.APPLICATION_JSON).get(genericType);
        ObservableList<BranchDetails> list = FXCollections.observableArrayList();
        list.addAll(list1);
        if (!list.isEmpty()) {
            branchDetailsViewTable.setItems(list);
        }
        branchDetailsViewCount.setText(String.format("%,d", list.size()));

        //Searching a branch
        searchBranchTable(list);
    }

    public void searchBranchTable(ObservableList<BranchDetails> branchDetails){
        if(!branchDetails.isEmpty()){
            FilteredList<BranchDetails> filteredList = new FilteredList<>(branchDetails, p->true);
            branchDetailsViewTextField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    filteredList.setPredicate(e->{
                        if(t1.isEmpty()){
                            return true;
                        }
                        else if (e.getBranchName().contains(t1)){
                            return true;
                        } else if (e.getBranchLocation().contains(t1)) {
                            return true;
                        } else if (e.getServerIp().contains(t1)) {
                            return true;
                        } else if (e.getUserName().contains(t1)) {
                            return true;
                        }
                        else {
                            return false;
                        }
                    });
                }
            });
            SortedList<BranchDetails> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(branchDetailsViewTable.comparatorProperty());
            branchDetailsViewTable.setItems(sortedList);
        }
    }

    @FXML
    public void searchBranch(){
        //Enabling search of branch
        if(searchBranch.isSelected()){
            branchDetailsViewTextField.setDisable(false);
            branchDetailsViewTextField.requestFocus();
        }else {
            branchDetailsViewTextField.setDisable(true);
        }

    }

    public AnchorPane getBranchDetailsViewBodyAnchor() {
        return branchDetailsViewBodyAnchor;
    }


}
