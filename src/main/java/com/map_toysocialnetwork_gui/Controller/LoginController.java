package com.map_toysocialnetwork_gui.Controller;

import com.map_toysocialnetwork_gui.Main;
import com.map_toysocialnetwork_gui.Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class LoginController extends Controller{

    @FXML
    private TextField usernameTextField;

    @FXML
    public void handleEnterPressed(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            handleLoginButtonAction();
        }
    }

    @FXML
    public void handleLoginButtonAction() throws IOException {
        String username = usernameTextField.getText();
        if (service.findUser(username) == null) {
            MessageAlert.showErrorMessage(null, "Nu exista niciun utilizator cu acest username!");
        }
        else {
            Main.changeSceneToPageView(usernameTextField.getText());
        }
    }

    public TextField getUsernameTextField() {
        return usernameTextField;
    }
}
