package com.map_toysocialnetwork_gui.Controller;

import com.map_toysocialnetwork_gui.Domain.Page;
import com.map_toysocialnetwork_gui.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class PageController extends Controller {

    @FXML
    private Label userFullNameLabel;
    @FXML
    private AnchorPane subsceneAnchorPane;
    private String loggedUsername;
    private Page loggedUserPage;
    private FXMLLoader fxmlLoader;

    @FXML
    public void handleFriendsLabelClicked() throws IOException {
        FriendsController friendsController = new FriendsController();
        friendsController.setService(service);
        friendsController.setLoggedUsername(loggedUsername);
        fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/friends-view.fxml"));
        fxmlLoader.setController(friendsController);
        Parent root = fxmlLoader.load();
        subsceneAnchorPane.getChildren().setAll(root);
    }

    @FXML
    public void handleFriendRequestsLabelClicked() throws IOException {
        FriendRequestsController friendRequestsController = new FriendRequestsController();
        friendRequestsController.setService(service);
        friendRequestsController.setLoggedUsername(loggedUsername);
        fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/friendrequest-view.fxml"));
        fxmlLoader.setController(friendRequestsController);
        Parent root = fxmlLoader.load();
        subsceneAnchorPane.getChildren().setAll(root);
    }

    @FXML
    public void handleChatLabelClicked() throws IOException {
        ChatController chatController = new ChatController();
        chatController.setService(service);
        chatController.setLoggedUsername(loggedUsername);
        chatController.setUserPage(loggedUserPage);
        fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/chat-view.fxml"));
        fxmlLoader.setController(chatController);
        Parent root = fxmlLoader.load();
        subsceneAnchorPane.getChildren().setAll(root);
    }

    @FXML
    public void handleLogOutButtonAction() throws IOException {
        Main.changeSceneToLogin();
    }

    public void initialize() {
        loggedUserPage = new Page(loggedUsername, service);
        userFullNameLabel.setText(loggedUserPage.getUser().getFirstName() + " " + loggedUserPage.getUser().getLastName());
    }

    public void setLoggedUsername(String loggedUsername) {
        this.loggedUsername = loggedUsername;
    }
}
