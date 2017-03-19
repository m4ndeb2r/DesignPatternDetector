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
 * <p>
 * TODO: this should not be a singleton, but there should also not be multiple instance of this class!! Fix it in the ControllerFactoryCreator if possible.
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

    // Singleton
    private static MenuController instance = null;

    /**
     * Returns the single instance of this class, or creates it if it does not exist.
     *
     * @param model the {@link Model} this controller updates
     * @return the singleton instance of the {@link MenuController}.
     */
    public static MenuController getInstance(Model model) {
        if (instance == null) {
            instance = new MenuController(model);
        }
        return instance;
    }

    /**
     * A private constructor because the {@link MenuController} is a singleton.
     *
     * @param model he {@link Model} this controller updates
     */
    private MenuController(Model model) {
        super(model);
        model.addObserver(this);
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

                // TODO: show error dialog

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
     * Saves the currently open project to a file with the same name it was previously stored in.
     *
     * @param event is ignored
     */
    @FXML
    protected void saveProjectAction(ActionEvent event) {
        boolean success;
        String detailMsg = null;
        try {
            success = getModel().saveProject();
        } catch (Exception ex) {
            success = false;
            detailMsg = ex.getMessage();
        }

        showSaveFileAlert(success, detailMsg);
    }

    /**
     * Saves the currently open project to a file with a different name it was previously stored in.
     *
     * @param event is ignored
     */
    @FXML
    protected void saveProjectAsAction(ActionEvent event) {
        boolean success;
        String detailMsg = null;
        try {
            success = getModel().saveProjectAs();
        } catch (Exception ex) {
            success = false;
            detailMsg = ex.getMessage();
        }
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
        //actiontarget.setText("You clicked 'About'");
    }

    /**
     * TODO
     * @param actionEvent
     */
    @FXML
    protected void dutchLanguageAction(ActionEvent actionEvent) {
        showNotImplementedAlert("Language");
    }

    /**
     * TODO
     * @param actionEvent
     */
    @FXML
    protected void englishLanguageAction(ActionEvent actionEvent) {
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
     * Ends the application gracefully after user confirmation.
     *
     * @param event is ignored
     */
    @FXML
    protected void exitAction(ActionEvent event) {
        if (getModel().hasOpenProject() && !getModel().canCloseProjectWithoutDataLoss()) {
            if (canCloseWithoutSaving()) {
                Platform.exit();
            }
        }
        else if (okayToExit()) {
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

