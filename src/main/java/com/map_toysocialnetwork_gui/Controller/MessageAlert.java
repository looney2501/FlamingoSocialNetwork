package com.map_toysocialnetwork_gui.Controller;

import com.map_toysocialnetwork_gui.Main;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MessageAlert {
    public static void showMessage(Stage owner, Alert.AlertType type, String header, String text){
        Alert alert=new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.initOwner(owner);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(String.valueOf(Main.class.getResource("images/flamingo-logo-small.jpg"))));
        ImageView imageView = null;
        if (type == Alert.AlertType.CONFIRMATION) {
            imageView = new ImageView(new Image(String.valueOf(Main.class.getResource("images/approval-icon.png"))));
        }
        alert.setGraphic(imageView);
        alert.getDialogPane().getStylesheets().add(String.valueOf(Main.class.getResource("css/alert.css")));
        alert.showAndWait();
    }

    public static void showErrorMessage(Stage owner, String text){
        Alert alert=new Alert(Alert.AlertType.ERROR);
        alert.initOwner(owner);
        alert.setTitle("Error");
        alert.setContentText(text);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(String.valueOf(Main.class.getResource("images/flamingo-logo-small.jpg"))));
        alert.setGraphic(new ImageView(new Image(String.valueOf(Main.class.getResource("images/error-icon.png")))));
        alert.getDialogPane().getStylesheets().add(String.valueOf(Main.class.getResource("css/alert.css")));
        alert.showAndWait();
    }
}
