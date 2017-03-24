package nl.ou.dpd.gui.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

/**
 * A wrapper for a {@link FileChooser}. This wrapper memorizes the most recent directory that was accessed, and opens
 * it by default.
 *
 * @author  Martin de Boer
 */
public class RetentionFileChooser {

    private FileChooser fileChooser;
    private SimpleObjectProperty<File> lastKnownDirectoryProperty = new SimpleObjectProperty<>();

    public RetentionFileChooser(FileChooser fileChooser) {
        this.fileChooser = fileChooser;
        this.lastKnownDirectoryProperty = new SimpleObjectProperty<>();
    }

    public ObservableList<FileChooser.ExtensionFilter> getExtensionFilters() {
        this.fileChooser.initialDirectoryProperty().bindBidirectional(lastKnownDirectoryProperty);
        return this.fileChooser.getExtensionFilters();
    }

    public void setTitle(String title) {
        this.fileChooser.setTitle(title);
    }

    public File showSaveDialog(Window window) {
        final File chosenFile = this.fileChooser.showSaveDialog(window);
        if (chosenFile != null) {
            this.lastKnownDirectoryProperty.setValue(chosenFile.getParentFile());
        }
        return chosenFile;
    }

    public File showOpenDialog(Window window) {
        final File chosenFile = this.fileChooser.showOpenDialog(window);
        if (chosenFile != null) {
            this.lastKnownDirectoryProperty.setValue(chosenFile.getParentFile());
        }
        return chosenFile;
    }
}
