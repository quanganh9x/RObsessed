package com.quanganh9x.rss;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class RegisterController {
    @FXML
    private TextField userLogin;
    @FXML
    private TextField nameLogin;
    @FXML
    private TextField emailLogin;
    @FXML
    private PasswordField passLogin;
    @FXML
    private PasswordField passSecLogin;
    @FXML
    private Button buttonPressed;
    @FXML
    public Text debug;
    // separate
    private String defaultCridential = "root";
    public void initialize() {

    }


    public void goLogin() {
        if (userLogin.getText().equals(defaultCridential) && passLogin.getText().equals(defaultCridential)) {
            debug.setText("okay");

        }
        else debug.setText("fail");
    }
    public void goRegister() {
        try {
            Desktop.getDesktop().browse(new URL("http://localhost/register").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
