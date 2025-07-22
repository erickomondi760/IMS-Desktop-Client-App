package com.quickrest.resources;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.text.Format;


public class ColumnFormatter<S,T> implements Callback<TableColumn<S,T>, TableCell<S,T>> {
    private Format format;

    public ColumnFormatter(Format format) {
        super();
        this.format = format;
    }

    @Override
    public TableCell<S, T> call(TableColumn<S, T> stTableColumn) {
        return new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new Label(format.format(item)));
                }
            }
        };
    }
}
