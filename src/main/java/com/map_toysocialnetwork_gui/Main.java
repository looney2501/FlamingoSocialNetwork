package com.map_toysocialnetwork_gui;

import com.map_toysocialnetwork_gui.Controller.*;
import com.map_toysocialnetwork_gui.Domain.Validators.UserValidator;
import com.map_toysocialnetwork_gui.Repository.SQLDataBaseRepository.FriendshipDBRepository;
import com.map_toysocialnetwork_gui.Repository.SQLDataBaseRepository.FriendshipRequestDBRepository;
import com.map_toysocialnetwork_gui.Repository.SQLDataBaseRepository.MessageDBRepository;
import com.map_toysocialnetwork_gui.Repository.SQLDataBaseRepository.UserDBRepository;
import com.map_toysocialnetwork_gui.Service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    private static Stage primaryStage;
    private static UserDBRepository userDBRepository;
    private static FriendshipDBRepository friendshipDBRepository;
    private static MessageDBRepository messageDBRepository;
    private static UserValidator userValidator;
    private static FriendshipRequestDBRepository friendshipRequestDBRepository;
    private static Service service;
    private static FXMLLoader fxmlLoader;


    @Override
    public void start(Stage stage) throws IOException {
        String url = "jdbc:postgresql://localhost:5432/ToySocialNetwork";
        String username = "postgres";
        String password = "postgres";
        userDBRepository = new UserDBRepository(url, username, password);
        friendshipDBRepository = new FriendshipDBRepository(userDBRepository, url, username, password);
        messageDBRepository = new MessageDBRepository(userDBRepository, url, username, password);
        userValidator = new UserValidator();
        friendshipRequestDBRepository = new FriendshipRequestDBRepository(userDBRepository,"jdbc:postgresql://localhost:5432/ToySocialNetwork", "postgres", "postgres");
        service = new Service(userDBRepository, friendshipDBRepository,friendshipRequestDBRepository, messageDBRepository, userValidator);
        primaryStage = stage;
        initView(stage);
    }

    private void initView(Stage stage) throws IOException {
        LoginController currentController = new LoginController();
        currentController.setService(service);
        fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/login-view.fxml"));
        fxmlLoader.setController(currentController);
        Parent root = fxmlLoader.load();
        root.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("css/login-view.css")).toExternalForm());
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void changeSceneToLogin() throws IOException {
        LoginController currentController = new LoginController();
        currentController.setService(service);
        fxmlLoader = new FXMLLoader(Main.class.getResource("../resources/views/login-view.fxml"));
        fxmlLoader.setController(currentController);
        Parent root = fxmlLoader.load();
        primaryStage.getScene().setRoot(root);
    }

    public static void changeSceneToUserView(String username) throws IOException {
        FriendsController currentController = new FriendsController();
        currentController.setService(service);
        currentController.setLoggedUsername(username);
        fxmlLoader = new FXMLLoader(Main.class.getResource("friends-view.fxml"));
        fxmlLoader.setController(currentController);
        Parent root = fxmlLoader.load();
        primaryStage.getScene().setRoot(root);
    }

    public static void changeSceneToFriendsrequestsView(String username) throws IOException {
        FriendsRequestsController currentController = new FriendsRequestsController();
        currentController.setService(service);
        currentController.setLoggedUser(username);
        fxmlLoader = new FXMLLoader(Main.class.getResource("friendrequest-view.fxml"));
        fxmlLoader.setController(currentController);
        Parent root = fxmlLoader.load();
        primaryStage.getScene().setRoot(root);

    }

    public static void changeSceneToChatView(String username) throws IOException {
        ChatController currentController = new ChatController();
        currentController.setService(service);
        currentController.setLoggedUsername(username);
        fxmlLoader = new FXMLLoader(Main.class.getResource("chat-view.fxml"));
        fxmlLoader.setController(currentController);
        Parent root = fxmlLoader.load();
        primaryStage.getScene().setRoot(root);
    }

    public static void changeSceneToSendNewMessage(String username) throws IOException {
        SendNewMessageController currentController = new SendNewMessageController();
        currentController.setService(service);
        currentController.setLoggedUsername(username);
        fxmlLoader = new FXMLLoader(Main.class.getResource("send-new-message-view.fxml"));
        fxmlLoader.setController(currentController);
        Parent root = fxmlLoader.load();
        primaryStage.getScene().setRoot(root);
    }

    public static void main(String[] args) {
        launch();
    }
}