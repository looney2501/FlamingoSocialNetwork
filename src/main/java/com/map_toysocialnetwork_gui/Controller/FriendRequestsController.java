package com.map_toysocialnetwork_gui.Controller;

import com.map_toysocialnetwork_gui.Domain.DTO.FriendDTO;
import com.map_toysocialnetwork_gui.Domain.FriendshipRequest;
import com.map_toysocialnetwork_gui.Domain.Page;
import com.map_toysocialnetwork_gui.Main;
import com.map_toysocialnetwork_gui.Service.ServiceExceptions.ServiceException;
import com.map_toysocialnetwork_gui.Utils.Observer.Observer;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class FriendRequestsController extends Controller implements Observer {

    @FXML
    TableView<FriendshipRequest> incFrReqTableView;
    @FXML
    TableColumn<FriendshipRequest, String> incFrReqTableColumnLastName;
    @FXML
    TableColumn<FriendshipRequest, String> incFrReqTableColumnFirstName;
    @FXML
    TableColumn<FriendshipRequest, String> incFrReqTableColumnDate;
    @FXML
    TableColumn<FriendshipRequest, ImageView> incFrReqTableColumnAccept;
    @FXML
    TableColumn<FriendshipRequest, ImageView> incFrReqTableColumnReject;
    @FXML
    TableView<FriendshipRequest> outFrReqTableView;
    @FXML
    TableColumn<FriendshipRequest, String> outFrReqTableColumnLastName;
    @FXML
    TableColumn<FriendshipRequest, String> outFrReqTableColumnFirstName;
    @FXML
    TableColumn<FriendshipRequest, String> outFrReqTableColumnDate;
    @FXML
    TableColumn<FriendshipRequest, ImageView> outFrReqTableColumnCancel;
    ObservableList<FriendshipRequest> friendshipRequestsReceiversModel = FXCollections.observableArrayList();
    ObservableList<FriendshipRequest> friendshipRequestsSenderModel = FXCollections.observableArrayList();
    private String loggedUser;
    private Page userPage;

    @FXML
    public void handleIncFrReqTableColumnClicked() {
        var selectionModel = incFrReqTableView.getSelectionModel();
        var selectedCells = selectionModel.getSelectedCells();
        if (selectedCells.size() > 0) {
            var column = selectedCells.get(0).getTableColumn().getId();
            if (Objects.equals(column, "incFrReqTableColumnReject")) {
                FriendshipRequest friendshipRequest = selectionModel.getSelectedItem();
                userPage.getReceivedFriendRequests().remove(friendshipRequest);
                userPage.notifyObservers();
                service.rejectFriendRequest(friendshipRequest.getUserSender().getId(), loggedUser);
            }
            if (Objects.equals(column, "incFrReqTableColumnAccept")) {
                FriendshipRequest friendshipRequest = selectionModel.getSelectedItem();
                userPage.getReceivedFriendRequests().remove(friendshipRequest);
                FriendDTO newFriend = new FriendDTO(friendshipRequest.getUserSender());
                newFriend.setDate(LocalDate.now());
                userPage.getFriends().add(newFriend);
                userPage.notifyObservers();
                service.acceptFriendRequest(friendshipRequest.getUserSender().getId(), loggedUser);
            }
        }
    }

    @FXML
    public void handleOutFrReqTableColumnClicked() {
        var selectionModel = outFrReqTableView.getSelectionModel();
        var selectedCells = selectionModel.getSelectedCells();
        if (selectedCells.size() > 0) {
            var column = selectedCells.get(0).getTableColumn().getId();
            if (Objects.equals(column, "outFrReqTableColumnCancel")) {
                FriendshipRequest friendshipRequest = selectionModel.getSelectedItem();
                userPage.getSentFriendRequests().remove(friendshipRequest);
                userPage.notifyObservers();
                service.cancelFriendshipRequest(loggedUser, friendshipRequest.getUserReceiver().getId());
            }
        }
    }

    public void initialize(){
        initializeIncFrReqTableView();
        initializeOutFrReqTableView();
    }

    public void setLoggedUsername(String loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void initializeIncFrReqTableView() {
        incFrReqTableColumnLastName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendshipRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FriendshipRequest, String> param) {
                return new SimpleStringProperty(param.getValue().getUserSender().getLastName());
            }
        });
        incFrReqTableColumnFirstName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendshipRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FriendshipRequest, String> param) {
                return new SimpleStringProperty(param.getValue().getUserSender().getFirstName());
            }
        });
        incFrReqTableColumnDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendshipRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FriendshipRequest, String> param) {
                return new SimpleStringProperty(param.getValue().getDate().toString());
            }
        });
        incFrReqTableColumnAccept.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendshipRequest, ImageView>, ObservableValue<ImageView>>() {
            @Override
            public ObservableValue<ImageView> call(TableColumn.CellDataFeatures<FriendshipRequest, ImageView> param) {
                return new SimpleObjectProperty<ImageView>(new ImageView(String.valueOf(Main.class.getResource("images/accept-icon.png"))));
            }
        });
        incFrReqTableColumnReject.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendshipRequest, ImageView>, ObservableValue<ImageView>>() {
            @Override
            public ObservableValue<ImageView> call(TableColumn.CellDataFeatures<FriendshipRequest, ImageView> param) {
                return new SimpleObjectProperty<ImageView>(new ImageView(String.valueOf(Main.class.getResource("images/reject-icon.png"))));
            }
        });
        incFrReqTableView.setItems(friendshipRequestsReceiversModel);
//        incFrReqTableView.getSelectionModel().setCellSelectionEnabled(true);
        refreshIncomingFriendRequests();
    }

    public void initializeOutFrReqTableView() {
        outFrReqTableColumnLastName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendshipRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FriendshipRequest, String> param) {
                return new SimpleStringProperty(param.getValue().getUserReceiver().getLastName());
            }
        });
        outFrReqTableColumnFirstName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendshipRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FriendshipRequest, String> param) {
                return new SimpleStringProperty(param.getValue().getUserReceiver().getFirstName());
            }
        });
        outFrReqTableColumnDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendshipRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FriendshipRequest, String> param) {
                return new SimpleStringProperty(param.getValue().getDate().toString());
            }
        });
        outFrReqTableColumnCancel.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendshipRequest, ImageView>, ObservableValue<ImageView>>() {
            @Override
            public ObservableValue<ImageView> call(TableColumn.CellDataFeatures<FriendshipRequest, ImageView> param) {
                return new SimpleObjectProperty<ImageView>(new ImageView(String.valueOf(Main.class.getResource("images/remove-icon.png"))));
            }
        });
        outFrReqTableView.setItems(friendshipRequestsSenderModel);
//        outFrReqTableView.getSelectionModel().setCellSelectionEnabled(true);
        refreshOutgoingFriendRequests();
    }

    private void refreshIncomingFriendRequests() {
        friendshipRequestsReceiversModel.setAll(userPage.getReceivedFriendRequests());
    }

    private void refreshOutgoingFriendRequests() {
        friendshipRequestsSenderModel.setAll(userPage.getSentFriendRequests());
    }

    @Override
    public void update() {
        refreshIncomingFriendRequests();
        refreshOutgoingFriendRequests();
    }

    public void setUserPage(Page userPage) {
        this.userPage=userPage;
        userPage.addObserver(this);
    }
}
