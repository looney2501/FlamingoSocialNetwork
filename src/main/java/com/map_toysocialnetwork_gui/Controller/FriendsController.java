package com.map_toysocialnetwork_gui.Controller;

import com.map_toysocialnetwork_gui.Domain.DTO.FriendDTO;
import com.map_toysocialnetwork_gui.Domain.Page;
import com.map_toysocialnetwork_gui.Main;
import com.map_toysocialnetwork_gui.Service.ServiceExceptions.ServiceException;
import com.map_toysocialnetwork_gui.Utils.Observer.Observer;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Objects;

public class FriendsController extends Controller implements Observer {


    @FXML
    TextField searchUserTextField;
    @FXML
    TableColumn<FriendDTO, String> friendsTableColumnNume;
    @FXML
    TableColumn<FriendDTO, String> friendsTableColumnPrenume;
    @FXML
    TableColumn<FriendDTO, String> friendsTableColumnDate;
    @FXML
    TableColumn<FriendDTO, ImageView> friendsTableColumnDelete;
    @FXML
    TableView<FriendDTO> friendsTableView;
    ObservableList<FriendDTO> friendsModel = FXCollections.observableArrayList();
    private String loggedUsername;
    private Page userPage;

    @FXML
    public void handleDeleteFriendButtonAction(){
        FriendDTO selectedFriendDTO = friendsTableView.getSelectionModel().getSelectedItem();
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
    public void handleCellMouseClickedAction() {
        var selectionModel = friendsTableView.getSelectionModel();
        var selectedCells = selectionModel.getSelectedCells();
        var row = selectedCells.get(0).getRow();
        var column = selectedCells.get(0).getTableColumn().getId();
        if (Objects.equals(column, "friendsTableColumnDelete")) {
            FriendDTO friendToBeDeletedDTO = selectionModel.getSelectedItem();
            userPage.getFriends().remove(friendToBeDeletedDTO);
            service.deleteFriend(loggedUsername,friendToBeDeletedDTO.getUser().getId());
            refreshFriendsModel();
        }
    }

    public void initialize() {
        initializeFriendsTableView();
        refreshFriendsModel();
    }

    private void initializeFriendsTableView() {
        friendsTableColumnNume.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendDTO,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FriendDTO, String> param) {
                return new SimpleStringProperty(param.getValue().getUser().getLastName());
            }
        });
        friendsTableColumnPrenume.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendDTO,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FriendDTO, String> param) {
                return new SimpleStringProperty(param.getValue().getUser().getFirstName());
            }
        });
        friendsTableColumnDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FriendDTO, String> param) {
                return new SimpleStringProperty(param.getValue().getDate().toString());
            }
        });
        friendsTableColumnDelete.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendDTO, ImageView>, ObservableValue<ImageView>>() {
            @Override
            public ObservableValue<ImageView> call(TableColumn.CellDataFeatures<FriendDTO, ImageView> param) {
                return new SimpleObjectProperty<ImageView>(new ImageView(String.valueOf(Main.class.getResource("images/rejected-icon.png"))));
            }
        });
        friendsTableView.setItems(friendsModel);
        friendsTableView.getSelectionModel().setCellSelectionEnabled(true);
    }

    public void setLoggedUsername(String loggedUsername) {
        this.loggedUsername = loggedUsername;
    }

    private void refreshFriendsModel(){
        friendsModel.setAll(userPage.getFriends());
    }

    public void setUserPage(Page userPage) {
        this.userPage=userPage;
        userPage.addObserver(this);
    }

    @Override
    public void update() {
        refreshFriendsModel();
    }
}
