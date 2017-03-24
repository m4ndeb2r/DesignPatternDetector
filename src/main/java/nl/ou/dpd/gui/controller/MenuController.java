package nl.ou.dpd.gui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import nl.ou.dpd.gui.model.Model;
import nl.ou.dpd.gui.model.Project;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * A {@link Controller} for the menu of the application.
 *
 * @author Martin de Boer
 */
public class MenuController extends Controller implements Observer {

    @FXML
    private MenuItem newProject;

    @FXML
    private MenuItem openProject;

    @FXML
    private MenuItem saveProject;

    @FXML
    private MenuItem saveProjectAs;

    @FXML
    private MenuItem closeProject;

    @FXML
    private MenuItem exit;

    @FXML
    private MenuItem help;

    @FXML
    private MenuItem about;

    /**
     * Constructs a {@link MenuController} with the specified {@link Model}.
     *
     * @param model the model of the MVC pattern
     */
    public MenuController(Model model) {
        super(model);
        model.addObserver(this);
    }

    /**
     * Called to initialize a controller after its root element has been completely processed. It sets some of the
     * menu items' state to disabled (the initial state), because those menu items work on open projects, and initially
     * no project has been opened
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.closeProject.setDisable(true);
        this.saveProject.setDisable(true);
        this.saveProjectAs.setDisable(true);
    }

    /**
     * Opens a new (blank) project. Prompts the user if the project has any changes that might get lost.
     *
     * @param event is ignored
     */
    @FXML
    protected void newProjectAction(ActionEvent event) {
        if (!getModel().hasOpenProject()
                || getModel().canCloseProjectWithoutDataLoss()
                || canCloseWithoutSaving()) {
            getModel().newProject();
        }
    }

    /**
     * Opens a previously saved project. Prompts the user if the project has any changes that might get lost.
     *
     * @param event is ignored
     */
    @FXML
    protected void openProjectAction(ActionEvent event) {
        if (!getModel().hasOpenProject()
                || getModel().canCloseProjectWithoutDataLoss()
                || canCloseWithoutSaving()) {
            try {
                getModel().openProject();
            } catch (FileNotFoundException fnfe) {
                final Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("The file could not be saved");
                alert.setContentText(fnfe.getMessage());
            }
        }
    }

    /**
     * Closes the currently opened project. Prompts the user if the project has any changes that might get lost.
     *
     * @param event is ignored
     */
    @FXML
    protected void closeProjectAction(ActionEvent event) {
        if (!getModel().hasOpenProject()
                || getModel().canCloseProjectWithoutDataLoss()
                || canCloseWithoutSaving()) {
            getModel().closeProject();
        }
    }

    private boolean canCloseWithoutSaving() {
        // Ask user: okay to close project and lose any changes?
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Note: unsaved changes");
        alert.setHeaderText("The current project will be discarded");
        alert.setContentText("Are you sure you want to close the current project and lose unsaved changes?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Saves the currently open project to a file with the same name it was previously stored in. When the project is
     * successfully saved, a confirmation alert message is shown; when an error occurs, an error alter message is shown.
     * When somewhere along the way the action is cancelled, no messages is shown.
     *
     * @param event is ignored
     */
    @FXML
    protected void saveProjectAction(ActionEvent event) {
        boolean success = true;
        String detailMsg = null;
        try {
            if (!getModel().saveProject()) {
                // Cancelled
                return;
            }
        } catch (Exception ex) {
            success = false;
            detailMsg = ex.getMessage();
        }
        // Show success or error message
        showSaveFileAlert(success, detailMsg);
    }

    /**
     * Saves the currently open project to a file with a different name it was previously stored in. When the project is
     * successfully saved, a confirmation alert message is shown; when an error occurs, an error alter message is shown.
     * When somewhere along the way the action is cancelled, no messages is shown.
     *
     * @param event is ignored
     */
    @FXML
    protected void saveProjectAsAction(ActionEvent event) {
        boolean success = true;
        String detailMsg = null;
        try {
            if (!getModel().saveProjectAs()) {
                // Cancelled
                return;
            }
        } catch (Exception ex) {
            success = false;
            detailMsg = ex.getMessage();
        }
        // Show success or error message
        showSaveFileAlert(success, detailMsg);
    }

    private void showSaveFileAlert(boolean success, String detailMsg) {
        Alert alert = null;
        if (success) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("File successfully saved");
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("The file could not be saved");
            if (detailMsg != null) {
                alert.setContentText(detailMsg);
            }
        }
        alert.showAndWait();
    }

    /**
     * Shows information about tha appliication.
     *
     * @param event is ignored
     */
    @FXML
    protected void aboutAction(ActionEvent event) {
        showNotImplementedAlert("About");
    }

    /**
     * Shows help information.
     *
     * @param event is ignored
     */
    @FXML
    protected void helpAction(ActionEvent event) {
        showNotImplementedAlert("Help");
    }

    /**
     * Sets the active language to Dutch.
     *
     * @param actionEvent
     */
    @FXML
    protected void dutchLanguageAction(ActionEvent actionEvent) {
        // TODO: implement this
        showNotImplementedAlert("Language");
    }

    /**
     * Sets the active language to English.
     *
     * @param actionEvent
     */
    @FXML
    protected void englishLanguageAction(ActionEvent actionEvent) {
        // TODOL implement this
        showNotImplementedAlert("Language");
    }

    private void showNotImplementedAlert(String function) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Not implemented yet: " + function);
        alert.setContentText("This function will be implemented in a future version of the application. Our apologies for the inconvenience.");
        alert.showAndWait();
    }

    /**
     * Handles the file > exit action in the menu.
     *
     * @param event is ignored
     */
    @FXML
    protected void exitAction(ActionEvent event) {
        shutdown();
    }

    /**
     * Ends the application gracefully after user confirmation. This method is called from the
     * {@link #exitAction(ActionEvent)} method as well as from the onClose event from the application window.
     */
    public void shutdown() {
        if (getModel().hasOpenProject() && !getModel().canCloseProjectWithoutDataLoss()) {
            if (canCloseWithoutSaving()) {
                Platform.exit();
            }
        } else if (okayToExit()) {
            Platform.exit();
        }
    }

    private boolean okayToExit() {
        // Ask user: okay to close project and lose any changes?
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit application");
        alert.setHeaderText("The application will stop");
        alert.setContentText("Are you sure you want exit the application?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * This method is called when an {@link Observable} calls the {@code notifyObservers} method. At that moment this
     * {@link MenuController}, being an instance of the {@link Observer} interface, will be updated.
     *
     * @param o   the observable object
     * @param arg a {@link Project} that is opened by the user (or {@code null} if none is currently opened.
     */
    @Override
    public void update(Observable o, Object arg) {
        // Synchronize the menu with the state of the opened project
        this.closeProject.setDisable(arg == null);
        this.saveProject.setDisable(arg == null);
        this.saveProjectAs.setDisable(arg == null);
    }
}

