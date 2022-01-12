package com.map_toysocialnetwork_gui.Controller;

import com.map_toysocialnetwork_gui.Utils.FileChooser.FileSaverPDF;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.IOException;

public class ReportsController extends Controller{
    @FXML
    public void handleClicked() throws IOException {
        PDDocument document = new PDDocument();
        PDPage blankPage = new PDPage();
        document.addPage(blankPage);
        File chosenFile = FileSaverPDF.choosePDFSaveFile("haidaaa");
        document.save(chosenFile);
        document.close();
    }
}
