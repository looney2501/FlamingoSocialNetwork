package com.map_toysocialnetwork_gui.Controller;

import com.map_toysocialnetwork_gui.Domain.Page;
import com.map_toysocialnetwork_gui.Service.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PageController extends Controller {

    @FXML
    private Label userFullNameLabel;
    private String loggedUsername;
    private Page loggedUserPage;

    public void setLoggedUsername(String loggedUsername) {
        this.loggedUsername = loggedUsername;
    }

    public void initialize() {
        loggedUserPage = new Page(loggedUsername, service);
        userFullNameLabel.setText(loggedUserPage.getUser().getFirstName() + " " + loggedUserPage.getUser().getLastName());
    }
}
