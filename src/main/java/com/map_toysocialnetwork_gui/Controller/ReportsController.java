package com.map_toysocialnetwork_gui.Controller;

import com.map_toysocialnetwork_gui.Domain.DTO.FriendDTO;
import com.map_toysocialnetwork_gui.Domain.Page;
import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Main;
import com.map_toysocialnetwork_gui.Utils.FileChooser.FileSaverPDF;
import com.map_toysocialnetwork_gui.Utils.Observer.Observer;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.IOException;

public class ReportsController extends Controller implements Observer {

    @FXML
    TableView<FriendDTO> friendsTableView;
    @FXML
    TableColumn<FriendDTO, String> friendsTableColumnLastName;
    @FXML
    TableColumn<FriendDTO, String> friendsTableColumnFirstName;
    @FXML
    TableColumn<FriendDTO, ImageView> friendsTableColumnReport;
    ObservableList<FriendDTO> friendsModel = FXCollections.observableArrayList();
    private String loggedUsername;
    private Page userPage;

    public void initialize() {
        initializeFriendsTableView();
        refreshFriendsModel();
    }

    public void handleGenerateReportAction() {

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
        friendsTableColumnReport.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FriendDTO, ImageView>, ObservableValue<ImageView>>() {
            @Override
            public ObservableValue<ImageView> call(TableColumn.CellDataFeatures<FriendDTO, ImageView> param) {
                return new SimpleObjectProperty<ImageView>(new ImageView(String.valueOf(Main.class.getResource("images/report-icon.png"))));
            }
        });
        friendsTableView.setItems(friendsModel);
        friendsTableView.getSelectionModel().setCellSelectionEnabled(true);
    }

    @Override
    public void update() {
        refreshFriendsModel();
    }

    private void refreshFriendsModel() {
        friendsModel.setAll(userPage.getFriends());
    }

    public void setUserPage(Page userPage) {
        this.userPage=userPage;
        userPage.addObserver(this);
    }

    public void setLoggedUsername(String loggedUsername) {
        this.loggedUsername = loggedUsername;
    }
}
