package com.map_toysocialnetwork_gui.Utils.FileChooser;

import javafx.stage.FileChooser;

import java.io.File;

public interface FileSaverPDF {
    static File choosePDFSaveFile(String fileName) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save pdf as");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF files (*.pdf)",
                "*.pdf",
                "*.PDF"));
        chooser.setInitialFileName(fileName);
        return chooser.showSaveDialog(null);
    }
}
