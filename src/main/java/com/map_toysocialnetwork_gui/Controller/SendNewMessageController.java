package com.map_toysocialnetwork_gui.Controller;

import com.map_toysocialnetwork_gui.Domain.DTO.FriendDTO;
import com.map_toysocialnetwork_gui.Main;
import com.map_toysocialnetwork_gui.Service.ServiceExceptions.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class SendNewMessageController extends Controller {

    @FXML
    private ListView<FriendDTO> friendsListView;
    @FXML
    private TextField messageTextField;
    private String loggedUsername;
    private ObservableList<FriendDTO> friendsModel = FXCollections.observableArrayList();

    @FXML
    public void handleGoBackAction() throws IOException {
        Main.changeSceneToChatView(loggedUsername);
    }

    @FXML
    public void handleSendNewMessageAction() {
        MultipleSelectionModel<FriendDTO> friendDTOMultipleSelectionModel = friendsListView.getSelectionModel();
        String messageText = messageTextField.getText();
        if (!messageText.equals("") && friendDTOMultipleSelectionModel!=null) {
            ObservableList<FriendDTO> selectedFriends = friendDTOMultipleSelectionModel.getSelectedItems();
            List<String> receiversUsername = selectedFriends.stream()
                                    .map(x->x.getUser().getId())
                                    .toList();
            try {
                service.sendNewMessage(loggedUsername, receiversUsername, messageText, LocalDateTime.now());
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "", "Mesaj trimis cu succes!");
            }
            catch (ServiceException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
            messageTextField.setText("");
        }
    }

    public void initialize() {
        friendsModel.setAll(service.getFriendsOfUser(loggedUsername));
        friendsListView.setItems(friendsModel);
        friendsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        friendsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(FriendDTO friendDTO, boolean empty) {
                super.updateItem(friendDTO, empty);
                if (empty || friendDTO == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(friendDTO.getUser().getFirstName() + " " + friendDTO.getUser().getLastName());
                }
            }
        });
        friendsListView.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
            Node node = evt.getPickResult().getIntersectedNode();

            // go up from the target node until a list cell is found or it's clear
            // it was not a cell that was clicked
            while (node != null && node != friendsListView && !(node instanceof ListCell)) {
                node = node.getParent();
            }

            // if is part of a cell or the cell,
            // handle event instead of using standard handling
            if (node instanceof ListCell) {
                // prevent further handling
                evt.consume();

                ListCell cell = (ListCell) node;
                ListView lv = cell.getListView();

                // focus the listview
                lv.requestFocus();

                if (!cell.isEmpty()) {
                    // handle selection for non-empty cells
                    int index = cell.getIndex();
                    if (cell.isSelected()) {
                        lv.getSelectionModel().clearSelection(index);
                    } else {
                        lv.getSelectionModel().select(index);
                    }
                }
            }
        });
    }

    public void setLoggedUsername(String loggedUsername) {
        this.loggedUsername = loggedUsername;
    }
}
