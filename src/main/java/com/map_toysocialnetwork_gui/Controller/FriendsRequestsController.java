package com.map_toysocialnetwork_gui.Controller;

import com.map_toysocialnetwork_gui.Domain.FriendshipRequest;
import com.map_toysocialnetwork_gui.Main;
import com.map_toysocialnetwork_gui.Service.ServiceExceptions.ServiceException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.IOException;

public class FriendsRequestsController extends Controller{

    ObservableList<FriendshipRequest> friendshipRequestsReceiversModel = FXCollections.observableArrayList();
    ObservableList<FriendshipRequest> friendshipRequestsSenderModel = FXCollections.observableArrayList();
    private String loggedUser;

    @FXML
    Label loggedUserLabel;
    @FXML
    Button goBackButton;
    @FXML
    Button acceptFriendrequestButton;
    @FXML
    Button rejectFriendrequestButton;
    @FXML
    Button cancelFriendrequestButton;
    @FXML
    Button refreshButton;
    @FXML
    TableColumn<FriendshipRequest, String> tableColumnFromLastname;
    @FXML
    TableColumn<FriendshipRequest, String> tableColumnFromFirstname;
    @FXML
    TableColumn<FriendshipRequest, String> tableColumnFromDate;
    @FXML
    TableView<FriendshipRequest> tableViewFrom;

    @FXML
    TableColumn<FriendshipRequest, String> tableColumnSendToLastname;
    @FXML
    TableColumn<FriendshipRequest, String> tableColumnSendToFirstname;
    @FXML
    TableColumn<FriendshipRequest, String> tableColumnSendToDate;
    @FXML
    TableView<FriendshipRequest> tableViewSendTo;

    public void initialize(){
        tableColumnFromLastname.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendshipRequest,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FriendshipRequest, String> param) {
                return new SimpleStringProperty(param.getValue().getUserSender().getLastName());
            }
        });
        //tableColumnPrenume.setCellValueFactory(new PropertyValueFactory<FriendDTO, String>("firstName"));
        tableColumnFromFirstname.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendshipRequest,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FriendshipRequest, String> param) {
                return new SimpleStringProperty(param.getValue().getUserSender().getFirstName());
            }
        });
        tableColumnFromDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendshipRequest,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FriendshipRequest, String> param) {
                return new SimpleStringProperty(param.getValue().getDate().toString());
            }
        });

        tableColumnSendToLastname.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendshipRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FriendshipRequest, String> param) {
                return new SimpleStringProperty(param.getValue().getUserReceiver().getLastName());
            }
        });
        tableColumnSendToFirstname.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendshipRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FriendshipRequest, String> param) {
                return new SimpleStringProperty(param.getValue().getUserReceiver().getFirstName());
            }
        });
        tableColumnSendToDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendshipRequest,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FriendshipRequest, String> param) {
                return new SimpleStringProperty(param.getValue().getDate().toString());
            }
        });

        loggedUserLabel.setText(" "+loggedUserLabel.getText() + " "+loggedUser);
        tableViewFrom.setItems(friendshipRequestsReceiversModel);
        tableViewSendTo.setItems(friendshipRequestsSenderModel);
        refreshFriendRequestsModel();
    }

    public void handleGoBackButton() throws IOException {
        Main.changeSceneToUserView(loggedUser);
    }

    @FXML
    public void handleRefreshButtonAction(){
        refreshFriendRequestsModel();
    }

    public String getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(String loggedUser) {
        this.loggedUser = loggedUser;
    }

    @FXML
    public void handleAcceptFriendrequestButtonAction(){
        FriendshipRequest selectedFriendshiprequest = tableViewFrom.getSelectionModel().getSelectedItem();
        if(selectedFriendshiprequest==null){
            MessageAlert.showErrorMessage(null,"Nu ati selectat nicio cerere de prietenie!");
        }
        else{
            try {
                service.acceptFriendRequest(selectedFriendshiprequest.getUserSender().getId(), loggedUser);
                refreshFriendRequestsModel();

            }
            catch (ServiceException exception){
                MessageAlert.showErrorMessage(null,exception.getMessage());
            }
        }
    }

    @FXML
    public void handleRejectFriendrequestButtonAction(){
        FriendshipRequest selectedFriendshiprequest = tableViewFrom.getSelectionModel().getSelectedItem();
        if(selectedFriendshiprequest==null){
            MessageAlert.showErrorMessage(null,"Nu ati selectat nicio cerere de prietenie!");
        }
        else{
            try {
                service.rejectFriendRequest(selectedFriendshiprequest.getUserSender().getId(), loggedUser);
                refreshFriendRequestsModel();
            }
            catch (ServiceException exception){
                MessageAlert.showErrorMessage(null,exception.getMessage());
            }
        }
    }

    @FXML
    private void handleCancelFriendrequestButtonAction(){
        FriendshipRequest selectedFriendshipRequest = tableViewSendTo.getSelectionModel().getSelectedItem();
        if(selectedFriendshipRequest==null){
            MessageAlert.showErrorMessage(null,"Nu ati selectat nicio cerere de prietenie");
        }
        else{
            try{
                service.cancelFriendshipRequest(loggedUser,selectedFriendshipRequest.getUserReceiver().getId());
                refreshFriendRequestsModel();
            }
            catch (ServiceException exception){
                MessageAlert.showErrorMessage(null, exception.getMessage());
            }
        }
    }

    private void refreshFriendRequestsModel(){
        friendshipRequestsReceiversModel.setAll(service.getAllFriendRequestsFor(loggedUser));
        friendshipRequestsSenderModel.setAll(service.getAllFriendRequestsFrom(loggedUser));
    }

}
