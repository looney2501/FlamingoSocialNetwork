package com.map_toysocialnetwork_gui.Controller;

import com.map_toysocialnetwork_gui.Domain.DTO.Conversation;
import com.map_toysocialnetwork_gui.Domain.Entity;
import com.map_toysocialnetwork_gui.Domain.Message;
import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatController extends Controller {

    @FXML
    private ListView<Conversation> listViewConversations;
    @FXML
    private ListView<Message> listViewChat;
    @FXML
    private TextField messageTextField;
    @FXML
    private Label loggedUserLabel;
    private String loggedUsername;
    private ObservableList<Conversation> conversationsModel = FXCollections.observableArrayList();
    private ObservableList<Message> chatModel = FXCollections.observableArrayList();

    @FXML
    public void handleGoBackAction() throws IOException {
        Main.changeSceneToUserView(loggedUsername);
    }

    @FXML
    public void handleRefreshConversations() {
        refreshConversations();
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
        Main.changeSceneToSendNewMessage(loggedUsername);
    }

    public void initialize() {
        loggedUserLabel.setText(" "+loggedUserLabel.getText()+" "+loggedUsername);
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
        refreshConversations();
    }

    public void setLoggedUsername(String loggedUsername) {
        this.loggedUsername = loggedUsername;
    }

    private void refreshConversations() {
        conversationsModel.setAll(service.getAllConversationsFor(service.findUser(loggedUsername)));
    }
}
