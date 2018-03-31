package com.quanganh9x.rss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // loaders
        FXMLLoader loginLoad = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent login = loginLoad.load();
        Scene loginScene = new Scene(login, 640, 480);

        FXMLLoader registerLoad = new FXMLLoader(getClass().getResource("register.fxml"));
        Parent register = registerLoad.load();
        Scene registerScene = new Scene(register, 640, 480);

        FXMLLoader mainLoad = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent main = mainLoad.load();
        Scene mainScene = new Scene(main, 1280, 720);
        // end loaders

        // controllers
        LoginController loginCtl = (LoginController)loginLoad.getController();
        RegisterController registerCtl = (RegisterController)registerLoad.getController();
        MainController mainCtl = (MainController)mainLoad.getController();
        // end controllers

        // load
        loginCtl.upcomingMainScene(mainScene);
        loginCtl.upcomingRegisterScene(registerScene);
        mainCtl.fallbackLoginScene(loginScene);
        // end load

        primaryStage.setTitle("RSS Reader - Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
