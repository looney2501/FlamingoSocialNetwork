package com.map_toysocialnetwork_gui.Controller;

import com.map_toysocialnetwork_gui.Domain.DTO.Conversation;
import com.map_toysocialnetwork_gui.Domain.DTO.FriendDTO;
import com.map_toysocialnetwork_gui.Domain.Message;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ReportsController extends Controller implements Observer {

    @FXML
    TableView<FriendDTO> friendsTableView;
    @FXML
    TableColumn<FriendDTO, String> friendsTableColumnLastName;
    @FXML
    TableColumn<FriendDTO, String> friendsTableColumnFirstName;
    @FXML
    TableColumn<FriendDTO, ImageView> friendsTableColumnReport;
    @FXML
    DatePicker dateChooserStart;
    @FXML
    DatePicker dateChooserEnd;
    ObservableList<FriendDTO> friendsModel = FXCollections.observableArrayList();
    private String loggedUsername;
    private Page userPage;

    public void initialize() {
        initializeFriendsTableView();
        refreshFriendsModel();
    }

    @FXML
    public void handleFriendsTableCellMouseClickedAction() throws IOException {
        var selectionModel = friendsTableView.getSelectionModel();
        var selectedCells = selectionModel.getSelectedCells();
        if (selectedCells.size() > 0) {
            var column = selectedCells.get(0).getTableColumn().getId();
            if (Objects.equals(column, "friendsTableColumnReport")) {
                LocalDate startDate = dateChooserStart.getValue();
                LocalDate endDate = dateChooserEnd.getValue();
                if ((startDate == null || endDate == null) || (startDate.isAfter(endDate))) {
                    MessageAlert.showErrorMessage(null, "Interval de timp invalid!");
                }
                else {
                    FriendDTO friendSelectedDTO = selectionModel.getSelectedItem();
                    List<Conversation> allConversations = userPage.getConversations();
                    List<Conversation> conversationWithUser = allConversations.stream()
                            .filter(c -> c.getAllUsers().size()==2 && c.getAllUsers().contains(friendSelectedDTO.getUser()))
                            .toList();
                    Conversation filteredConversation = service.getConversationBetweenDates(conversationWithUser.get(0), startDate, endDate);
                    generatePrivateChatReportPDF(filteredConversation, startDate, endDate);
                }
            }
        }
    }

    private void generatePrivateChatReportPDF(Conversation conversation, LocalDate startDate, LocalDate endDate) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        User loggedUser = service.findUser(loggedUsername);
        User otherUser = conversation.getAllUsers().get(0);

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
        contentStream.moveTextPositionByAmount(400,20);
        contentStream.drawString("Report made on: " + LocalDate.now().toString());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 20);
        contentStream.moveTextPositionByAmount(50,750);
        contentStream.drawString("Logged user: " + loggedUser.toString());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 20);
        contentStream.moveTextPositionByAmount(50, 730);
        contentStream.drawString("Other user: " + otherUser.toString());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 18);
        contentStream.moveTextPositionByAmount(50, 710);
        contentStream.drawString("Start date: " + startDate.toString());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 18);
        contentStream.moveTextPositionByAmount(50, 690);
        contentStream.drawString("End date: " + endDate.toString());
        contentStream.endText();

        int ty = 600;

        for (Message m: conversation.getAllMessages()) {
            if (ty > 40) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                contentStream.moveTextPositionByAmount(15, ty);
                contentStream.drawString(m.toString());
                contentStream.endText();
                ty-=20;
            }
            else {
                page = new PDPage();
                document.addPage(page);
                contentStream.close();
                contentStream = new PDPageContentStream(document, page);
                ty = 700;
                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
                contentStream.moveTextPositionByAmount(400,20);
                contentStream.drawString("Report made on: " + LocalDate.now().toString());
                contentStream.endText();
                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                contentStream.moveTextPositionByAmount(15, ty);
                contentStream.drawString(m.toString());
                contentStream.endText();
                ty-=20;
            }
        }

        contentStream.close();

        File file = FileSaverPDF.choosePDFSaveFile("Conversation-report");
        if (file!=null) {
            document.save(file);
        }
        document.close();
    }

    @FXML
    public void handleGenerateReportAction() throws IOException {
        LocalDate startDate = dateChooserStart.getValue();
        LocalDate endDate = dateChooserEnd.getValue();
        if ((startDate == null || endDate == null) || (startDate.isAfter(endDate))) {
            MessageAlert.showErrorMessage(null, "Date interval is not valid!");
        }
        else {
            List<FriendDTO> friendshipsMadeBetweenDates = service.getFriendshipsMadeBetweenDates(userPage, startDate, endDate);
            List<Conversation> friendsConversationsBetweenDates = userPage.getConversations().stream()
                    .filter(x -> x.getAllUsers().size() == 2)
                    .map(c -> service.getConversationBetweenDates(c, startDate, endDate))
                    .toList();
            generateAllFriendsChatReportPDF(friendshipsMadeBetweenDates, friendsConversationsBetweenDates, startDate, endDate);
        }
    }

    private void generateAllFriendsChatReportPDF(List<FriendDTO> friendships,
                                                 List<Conversation> conversations,
                                                 LocalDate startDate,
                                                 LocalDate endDate) throws IOException {


        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
        contentStream.moveTextPositionByAmount(400,20);
        contentStream.drawString("Report made on: " + LocalDate.now().toString());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 20);
        contentStream.moveTextPositionByAmount(20,750);
        contentStream.drawString("Friendships report for user " + service.findUser(loggedUsername).toString());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 18);
        contentStream.moveTextPositionByAmount(50, 720);
        contentStream.drawString("Start date: " + startDate.toString());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 18);
        contentStream.moveTextPositionByAmount(50, 700);
        contentStream.drawString("End date: " + endDate.toString());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 18);
        contentStream.moveTextPositionByAmount(30, 670);
        contentStream.drawString("Friendships made: " + friendships.size());
        contentStream.endText();

        int ty = 650;
        for (FriendDTO fr: friendships) {
            if (ty > 30) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 15);
                contentStream.moveTextPositionByAmount(30, ty);
                contentStream.drawString(fr.getUser().toString() + ' ' + fr.getDate().toString());
                contentStream.endText();
                ty-=20;
            }
            else {
                page = new PDPage();
                document.addPage(page);
                contentStream.close();
                contentStream = new PDPageContentStream(document, page);
                ty = 700;
                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
                contentStream.moveTextPositionByAmount(400,20);
                contentStream.drawString("Report made on: " + LocalDate.now().toString());
                contentStream.endText();
                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 15);
                contentStream.moveTextPositionByAmount(30, ty);
                contentStream.drawString(fr.getUser().toString() + ' ' + fr.getDate().toString());
                contentStream.endText();
                ty-=20;
            }
        }

        page = new PDPage();
        document.addPage(page);
        contentStream.close();
        contentStream = new PDPageContentStream(document, page);

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
        contentStream.moveTextPositionByAmount(400,20);
        contentStream.drawString("Report made on: " + LocalDate.now().toString());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 18);
        contentStream.moveTextPositionByAmount(30, 750);
        contentStream.drawString("Message statistics");
        contentStream.endText();

        ty = 730;

        for (Conversation c: conversations) {
            if (ty > 30) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 15);
                contentStream.moveTextPositionByAmount(30, ty);
                contentStream.drawString(c.getAllUsers().get(0).toString() + ", " + c.getAllUsers().get(1).toString() + "; " + c.getAllMessages().size());
                contentStream.endText();
                ty-=20;
            }
            else {
                page = new PDPage();
                document.addPage(page);
                contentStream.close();
                contentStream = new PDPageContentStream(document, page);
                ty = 700;
                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);
                contentStream.moveTextPositionByAmount(400,20);
                contentStream.drawString("Report made on: " + LocalDate.now().toString());
                contentStream.endText();
                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 15);
                contentStream.moveTextPositionByAmount(30, ty);
                contentStream.drawString(c.getAllUsers().get(0).toString() + ", " + c.getAllUsers().get(1).toString() + "; " + c.getAllMessages().size());
                contentStream.endText();
                ty-=20;
            }
        }

        contentStream.close();

        File file = FileSaverPDF.choosePDFSaveFile("Friendships-report");
        if (file!=null) {
            document.save(file);
        }
        document.close();
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
