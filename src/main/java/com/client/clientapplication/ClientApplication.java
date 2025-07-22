package com.client.clientapplication;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        LoginController controller = new LoginController();
        controller.openLoginWindow();

    }

    public static void main(String[] args) {
        launch();
    }

}