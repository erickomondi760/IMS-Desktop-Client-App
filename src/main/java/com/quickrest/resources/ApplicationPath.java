package com.quickrest.resources;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.swing.text.TabableView;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ApplicationPath {
    private static final String HOST = "http://localhost:9999/1-RUAKA BRANCH/branch";
    private static final String HQ_HOST = "http://localhost:9999/HQ-BRANCH/hq";
    private static ApplicationPath applicationPath = null;
    private static String user;
    private static TextField supplierTextField;

    private ApplicationPath(){

    }

    public static ApplicationPath getInstance(){
        applicationPath = new ApplicationPath();
        return applicationPath;
    }

    public static String getApplicationPath(){
        return HOST;
    }

    public static String getHqPath(){
        return HQ_HOST;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        ApplicationPath.user = user;
    }

    public static TextField getSupplierTextField() {
        return supplierTextField;
    }

    public static void setSupplierTextField(TextField supplierTextField) {
        ApplicationPath.supplierTextField = supplierTextField;
    }

    public static void exportTableData(TableView<?> tableView){
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sample");
        Row row = sheet.createRow(0);

        for(int j = 0; j < tableView.getColumns().size(); j++){
            row.createCell(j).setCellValue(tableView.getColumns().get(j).getText());
        }

        for(int i = 0; i < tableView.getItems().size(); i++){
            row = sheet.createRow(i + 1);
            for(int j = 0; j < tableView.getColumns().size(); j++){
                if(tableView.getColumns().get(j).getCellData(i) != null){
                    row.createCell(j).setCellValue(tableView.getColumns().get(j).getCellData(i).toString());

                }else {
                    row.createCell(j).setCellValue("");
                }
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\System generated dat files\\Workbook.xls");
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File("C:\\System generated dat files\\Workbook.xls"));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
