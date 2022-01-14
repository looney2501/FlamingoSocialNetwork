package com.map_toysocialnetwork_gui.Controller;

import com.map_toysocialnetwork_gui.Domain.Page;
import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class PageController extends Controller {

    @FXML
    private Label userLastNameLabel;
    @FXML
    private Label userFirstNameLabel;
    @FXML
    private AnchorPane subsceneAnchorPane;
    @FXML
    private ImageView friendsImageView;
    @FXML
    private ImageView friendRequestsImageView;
    @FXML
    private ImageView chatImageView;
    @FXML
    private ImageView reportsImageView;
    private String loggedUsername;
    private Page loggedUserPage;
    private FXMLLoader fxmlLoader;

    @FXML
    public void handleFriendsLabelClicked() throws IOException {
        FriendsController friendsController = new FriendsController();
        friendsController.setService(service);
        friendsController.setLoggedUsername(loggedUsername);
        friendsController.setUserPage(loggedUserPage);
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
        friendRequestsController.setUserPage(loggedUserPage);
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
    public void handleReportsLabelClicked() throws IOException {
        ReportsController reportsController = new ReportsController();
        reportsController.setService(service);
        reportsController.setLoggedUsername(loggedUsername);
        reportsController.setUserPage(loggedUserPage);
        fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/reports-view.fxml"));
        fxmlLoader.setController(reportsController);
        Parent root = fxmlLoader.load();
        subsceneAnchorPane.getChildren().setAll(root);
    }

    @FXML
    public void handleLogOutButtonAction() throws IOException {
        Main.changeSceneToLogin();
    }

    public void initialize() {
        loggedUserPage = new Page(loggedUsername, service);
        User logedUser = loggedUserPage.getUser();
        userLastNameLabel.setText(logedUser.getLastName());
        userFirstNameLabel.setText(logedUser.getFirstName());
        initializeImageViews();
    }

    private void initializeImageViews() {
        chatImageView.setImage(new Image(String.valueOf(Main.class.getResource("images/messages-icon.png"))));
        friendsImageView.setImage(new Image(String.valueOf(Main.class.getResource("images/friends-icon.png"))));
        friendRequestsImageView.setImage(new Image(String.valueOf(Main.class.getResource("images/friend-request-icon.png"))));
        reportsImageView.setImage(new Image(String.valueOf(Main.class.getResource("images/report-icon-big.png"))));
    }

    public void setLoggedUsername(String loggedUsername) {
        this.loggedUsername = loggedUsername;
    }
}
