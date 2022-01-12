package com.map_toysocialnetwork_gui.Controller;

import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Main;
import com.map_toysocialnetwork_gui.Service.Service;
import com.map_toysocialnetwork_gui.Utils.PasswordHasing.MD5Hashing;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class LoginController extends Controller{

    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;

    @FXML
    public void handleEnterPressed(KeyEvent event) throws IOException, NoSuchAlgorithmException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            handleLoginButtonAction();
        }
    }

    @FXML
    public void handleLoginButtonAction() throws IOException, NoSuchAlgorithmException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        //TODO delete this
        if (username.isEmpty() && password.isEmpty()) {
            Main.changeSceneToPageView("mircea2501");
        }
//        if (username.isEmpty()) {
//            MessageAlert.showErrorMessage(null, "Introduceti un nume de utilizator!");
//        }
//        else if (password.isEmpty()) {
//            MessageAlert.showErrorMessage(null, "Introduceti o parola!");
//        }
//        else {
//            User foundUser = service.findUser(username);
//            if (foundUser == null) {
//                MessageAlert.showErrorMessage(null, "Nu exista niciun utilizator cu acest username!");
//            }
//            else {
//                if (!MD5Hashing.hashPassword(password).equals(foundUser.getPassword())) {
//                    MessageAlert.showErrorMessage(null, "Parola incorecta!");
//                }
//                else {
//                    Main.changeSceneToPageView(usernameTextField.getText());
//                }
//            }
//        }
    }

    public TextField getUsernameTextField() {
        return usernameTextField;
    }
}
