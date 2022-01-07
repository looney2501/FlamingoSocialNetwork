package com.map_toysocialnetwork_gui.Controller;

import com.map_toysocialnetwork_gui.Domain.DTO.FriendDTO;
import com.map_toysocialnetwork_gui.Main;
import com.map_toysocialnetwork_gui.Service.Service;
import com.map_toysocialnetwork_gui.Service.ServiceExceptions.ServiceException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.IOException;

public class FriendsController extends Controller {

    ObservableList<FriendDTO> friendsModel = FXCollections.observableArrayList();
    private String loggedUsername;

    @FXML
    Label loggedUserLabel;
    @FXML
    Button logOutButton;
    @FXML
    Button addFriend;
    @FXML
    TextField searchUserTextField;
    @FXML
    Button deleteFriend;
    @FXML
    Button viewFriendrequestsButton;
    @FXML
    TableColumn<FriendDTO, String> tableColumnNume;
    @FXML
    TableColumn<FriendDTO, String> tableColumnPrenume;
    @FXML
    TableColumn<FriendDTO, String> tableColumnDate;
    @FXML
    TableView<FriendDTO> tableView;

    @FXML
    public void handleLogOutButton(ActionEvent actionEvent) throws IOException {
        Main.changeSceneToLogin();
    }

    @FXML
    public void handleViewFriendrequestsButton(ActionEvent actionEvent) throws IOException {
        Main.changeSceneToFriendsrequestsView(loggedUsername);
    }

    @FXML
    public void handleViewChat(ActionEvent actionEvent) throws IOException {
        Main.changeSceneToChatView(loggedUsername);
    }

    @FXML
    public void handleDeleteFriendButtonAction(){
        FriendDTO selectedFriendDTO = tableView.getSelectionModel().getSelectedItem();
        if(selectedFriendDTO==null){
            MessageAlert.showErrorMessage(null,"Nu ati selectat niciun user!");
        }
        else{
            service.deleteFriend(loggedUsername,selectedFriendDTO.getUser().getId());
            refreshFriendsModel();
        }
    }

    @FXML
    public void handleAddFriendButtonAction(){
        String username = searchUserTextField.getText();
        if(username.equals("")){
            MessageAlert.showErrorMessage(null,"Username vid!");
        }
        else{
            try{
                service.sendFriendRequest(loggedUsername,username);
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION,"","Cerere de prietenie trimisa cu succes!");
            }
            catch (ServiceException ex){
                MessageAlert.showErrorMessage(null,ex.getMessage());
            }
        }
    }

    @FXML
    public void handleRefreshButtonAction(){
        refreshFriendsModel();
    }

    public void initialize() {
        //tableColumnNume.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableColumnNume.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendDTO,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FriendDTO, String> param) {
                return new SimpleStringProperty(param.getValue().getUser().getLastName());
            }
        });
        //tableColumnPrenume.setCellValueFactory(new PropertyValueFactory<FriendDTO, String>("firstName"));
        tableColumnPrenume.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendDTO,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FriendDTO, String> param) {
                return new SimpleStringProperty(param.getValue().getUser().getFirstName());
            }
        });
        tableColumnDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FriendDTO, String> param) {
                return new SimpleStringProperty(param.getValue().getDate().toString());
            }
        });

        loggedUserLabel.setText(" "+loggedUserLabel.getText()+" "+ loggedUsername);
        tableView.setItems(friendsModel);
        refreshFriendsModel();
    }

    public void setLoggedUsername(String loggedUsername) {
        this.loggedUsername = loggedUsername;
    }

    private void refreshFriendsModel(){
        friendsModel.setAll(service.getFriendsOfUser(loggedUsername));
    }

}
