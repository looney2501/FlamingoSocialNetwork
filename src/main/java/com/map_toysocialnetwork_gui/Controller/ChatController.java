package com.map_toysocialnetwork_gui.Controller;

import com.map_toysocialnetwork_gui.Domain.DTO.Conversation;
import com.map_toysocialnetwork_gui.Domain.Entity;
import com.map_toysocialnetwork_gui.Domain.Message;
import com.map_toysocialnetwork_gui.Domain.Page;
import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Main;
import com.map_toysocialnetwork_gui.Utils.Observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.security.auth.callback.Callback;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatController extends Controller implements Observer {

    @FXML
    private ListView<Conversation> listViewConversations;
    @FXML
    private ListView<Message> listViewChat;
    @FXML
    private TextField messageTextField;
    private String loggedUsername;
    private ObservableList<Conversation> conversationsModel = FXCollections.observableArrayList();
    private ObservableList<Message> chatModel = FXCollections.observableArrayList();
    private Page userPage;

    @FXML
    public void handleKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            handleSendMessageAction();
        }
    }

    @FXML
    public void handleSendMessageAction() {
        Conversation currentConversation = listViewConversations.getSelectionModel().getSelectedItem();
        if (currentConversation != null) {
            Message messageToBeReplied = currentConversation.getAllMessages().get(currentConversation.getAllMessages().size()-1);
            String messageText = messageTextField.getText();
            List<String> receiversUsername = new ArrayList<>(currentConversation.getAllUsers().stream()
                    .map(Entity::getId)
                    .toList()
            );
            receiversUsername.remove(loggedUsername);
            if (!messageText.equals("")) {
                Message replyMessage = service.replyToMessage(loggedUsername, receiversUsername, messageText, LocalDateTime.now(), messageToBeReplied);
                currentConversation.getAllMessages().add(replyMessage);
                userPage.notifyObservers();
                messageTextField.setText("");
                listViewChat.getItems().add(replyMessage);
            }
        }

    }

    @FXML
    public void handleConversationsViewListItemSelection() {
        Conversation currentConversation = listViewConversations.getSelectionModel().getSelectedItem();
        if (currentConversation != null) {
            chatModel.setAll(currentConversation.getAllMessages());
        }
    }

    @FXML
    public void handleSendNewMessageAction() throws IOException {
        Stage newMessageStage = new Stage();
        newMessageStage.setResizable(false);
        newMessageStage.initModality(Modality.APPLICATION_MODAL);

        SendNewMessageController sendNewMessageController = new SendNewMessageController();
        sendNewMessageController.setService(service);
        sendNewMessageController.setLoggedUsername(loggedUsername);
        sendNewMessageController.setUserPage(userPage);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/send-new-message-view.fxml"));
        fxmlLoader.setController(sendNewMessageController);
        Parent root = fxmlLoader.load();
        newMessageStage.setScene(new Scene(root));

        newMessageStage.show();
    }

    public void initialize() {
        listViewConversations.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Conversation conversation, boolean empty) {
                super.updateItem(conversation, empty);
                if (empty || conversation == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    List<User> allParticipants = conversation.getAllUsers();
                    String toBePrinted = "";
                    for (int i = 0; i < allParticipants.size() - 1; i++) {
                        toBePrinted += allParticipants.get(i).getFirstName() + " " + allParticipants.get(i).getLastName();
                        toBePrinted += ", ";
                    }
                    toBePrinted += allParticipants.get(allParticipants.size() - 1).getFirstName() + " " + allParticipants.get(allParticipants.size() - 1).getLastName();
                    setText(toBePrinted);
                }
            }
        });
        listViewConversations.setItems(conversationsModel);
        listViewChat.setItems(chatModel);
        listViewChat.setCellFactory(param -> new ListCell<>() {
            @Override
            public void updateItem(Message message, boolean empty) {
                super.updateItem(message, empty);
                if (empty || message == null) {
                    setText(null);
                    setGraphic(null);
                    setMouseTransparent(true);
                    setFocusTraversable(true);
                } else {
                    setText(message.toString());
                }
            }
        });
        refreshConversations();
    }

    public void setLoggedUsername(String loggedUsername) {
        this.loggedUsername = loggedUsername;
    }

    public void setUserPage(Page userPage) {
        this.userPage=userPage;
        userPage.addObserver(this);
    }

    private void refreshConversations() {
        conversationsModel.setAll(userPage.getConversations());
    }

    @Override
    public void update() {
        refreshConversations();;
    }
}
