package com.map_toysocialnetwork_gui.Controller;

import com.map_toysocialnetwork_gui.Domain.DTO.FriendDTO;
import com.map_toysocialnetwork_gui.Domain.FriendshipRequest;
import com.map_toysocialnetwork_gui.Domain.Page;
import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Main;
import com.map_toysocialnetwork_gui.Service.ServiceExceptions.ServiceException;
import com.map_toysocialnetwork_gui.Utils.Observer.Observer;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.util.Objects;
import java.util.function.Predicate;

public class FriendsController extends Controller implements Observer {


    @FXML
    TextField searchUserTextField;
    @FXML
    TableView<FriendDTO> friendsTableView;
    @FXML
    TableColumn<FriendDTO, String> friendsTableColumnLastName;
    @FXML
    TableColumn<FriendDTO, String> friendsTableColumnFirstName;
    @FXML
    TableColumn<FriendDTO, String> friendsTableColumnDate;
    @FXML
    TableColumn<FriendDTO, ImageView> friendsTableColumnDelete;
    @FXML
    TableView<User> searchedUsersTableView;
    @FXML
    TableColumn<User, String> searchedUsersTableColumnLastName;
    @FXML
    TableColumn<User, String> searchedUsersTableColumnFirstName;
    @FXML
    TableColumn<User, ImageView> searchedUsersTableColumnAdd;
    ObservableList<FriendDTO> friendsModel = FXCollections.observableArrayList();
    ObservableList<User> searchedUsersModel = FXCollections.observableArrayList();
    private String loggedUsername;
    private Page userPage;

    @FXML
    public void handleFriendsTableCellMouseClickedAction() {
        var selectionModel = friendsTableView.getSelectionModel();
        var selectedCells = selectionModel.getSelectedCells();
        if (selectedCells.size() > 0) {
            var column = selectedCells.get(0).getTableColumn().getId();
            if (Objects.equals(column, "friendsTableColumnDelete")) {
                FriendDTO friendToBeDeletedDTO = selectionModel.getSelectedItem();
                userPage.getFriends().remove(friendToBeDeletedDTO);
                service.deleteFriend(loggedUsername,friendToBeDeletedDTO.getUser().getId());
                userPage.notifyObservers();
            }
        }
    }

    @FXML
    public void handleSearchedUserTableCellMouseClickedAction() {
        var selectionModel = searchedUsersTableView.getSelectionModel();
        var selectedCells = selectionModel.getSelectedCells();
        if (selectedCells.size() > 0) {
            var column = selectedCells.get(0).getTableColumn().getId();
            if (Objects.equals(column, "searchedUsersTableColumnAdd")) {
                User friendToBeAdded = selectionModel.getSelectedItem();
                try{
                    FriendshipRequest friendshipRequest = service.sendFriendRequest(loggedUsername, friendToBeAdded.getId());
                    MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION,"","Cerere de prietenie trimisa cu succes!");
                    userPage.getSentFriendRequests().add(friendshipRequest);
                    userPage.notifyObservers();
                }
                catch (ServiceException ex){
                    MessageAlert.showErrorMessage(null,ex.getMessage());
                }
            }
        }
    }

    public void initialize() {
        searchUserTextField.textProperty().addListener(o -> refreshSearchedUsersModel());
        initializeFriendsTableView();
        refreshFriendsModel();
        initializeSearchedUsersTableView();
        refreshSearchedUsersModel();
    }

    private void initializeFriendsTableView() {
        friendsTableColumnLastName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendDTO,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FriendDTO, String> param) {
                return new SimpleStringProperty(param.getValue().getUser().getLastName());
            }
        });
        friendsTableColumnFirstName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendDTO,String>, ObservableValue<String>>() {
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
                return new SimpleObjectProperty<ImageView>(new ImageView(String.valueOf(Main.class.getResource("images/remove-icon.png"))));
            }
        });
        friendsTableView.setItems(friendsModel);
        friendsTableView.getSelectionModel().setCellSelectionEnabled(true);
    }

    private void initializeSearchedUsersTableView() {
        searchedUsersTableColumnLastName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new SimpleStringProperty(param.getValue().getLastName());
            }
        });
        searchedUsersTableColumnFirstName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new SimpleStringProperty(param.getValue().getFirstName());
            }
        });
        searchedUsersTableColumnAdd.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, ImageView>, ObservableValue<ImageView>>() {
            @Override
            public ObservableValue<ImageView> call(TableColumn.CellDataFeatures<User, ImageView> param) {
                return new SimpleObjectProperty<ImageView>(new ImageView(String.valueOf(Main.class.getResource("images/add-icon.png"))));
            }
        });
        searchedUsersTableView.setItems(searchedUsersModel);
        searchedUsersTableView.getSelectionModel().setCellSelectionEnabled(true);
    }

    public void setLoggedUsername(String loggedUsername) {
        this.loggedUsername = loggedUsername;
    }

    private void refreshFriendsModel(){
        friendsModel.setAll(userPage.getFriends());
    }

    private void refreshSearchedUsersModel() {
        if (searchUserTextField.getText().isBlank()) {
            searchedUsersModel.clear();
        }
        else {
            Predicate<User> p1 = u -> u.getFirstName().startsWith(searchUserTextField.getText());
            Predicate<User> p2 = u -> u.getLastName().startsWith(searchUserTextField.getText());
            searchedUsersModel.setAll(userPage.getAllUsers().stream()
                    .filter(p1.or(p2))
                    .toList());
        }
    }

    public void setUserPage(Page userPage) {
        this.userPage=userPage;
        userPage.addObserver(this);
    }

    @Override
    public void update() {
        refreshFriendsModel();
        refreshSearchedUsersModel();
    }
}
