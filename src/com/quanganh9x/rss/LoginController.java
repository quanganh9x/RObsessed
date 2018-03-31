package com.quanganh9x.rss;


import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.io.File;
import java.lang.*;

public class LoginController extends XMLUserController {
    @FXML
    private TextField userLogin;
    @FXML
    private PasswordField passLogin;
    @FXML
    public Text debug;
    private String defaultCridential = "root";
    // scenes
    private Scene registerScene;
    private Scene mainScene;
    // end scenes
    private String userDefault;
    private String passDafault;
    // init
    public void initialize() {
        setPath(); // got the path from @override
        read(); // ~~ new instance
    }
    // end init
    // functions
    public void upcomingRegisterScene(Scene scene) {
        this.registerScene = scene;
    }
    public void upcomingMainScene(Scene scene) {
        this.mainScene = scene;
    }
    public void sceneChange(String where) {
        Stage current = (Stage)userLogin.getScene().getWindow();
        if (where.equals("register")) {
            current.setScene(this.registerScene);
        } else if (where.equals("main")) {
            current.setTitle("RSS Reader - Main");
            current.setScene(this.mainScene);
        }
    }
    public void goLogin() {
        String path = "/Users/quanganh9x/Desktop/quanganh9x/userdata.xml";
        File f = new File(path);
        if (f.exists() && !f.isDirectory()) {
            this.userDefault = getChildName(root, "username");
            this.passDafault = getChildName(root, "password");
        } else {
            this.userDefault = defaultCridential;
            this.passDafault = defaultCridential;
        }
        if (userLogin.getText().equals(userDefault) && passLogin.getText().equals(passDafault)) {
            sceneChange("main");
        }
        else debug.setText("fail");
    }
    public void goRegister() {
        sceneChange("register");
    }
}