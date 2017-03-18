package nl.ou.dpd.gui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import nl.ou.dpd.gui.model.Model;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * A {@link Controller} for the menu of the application.
 *
 * @author Martin de Boer
 */
public class MenuController extends Controller {

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

    public MenuController(Model model) {
        super(model);
    }

    /**
     * TODO...
     *
     * @param event
     */
    @FXML
    protected void newProjectAction(ActionEvent event) {

        // TODO: check is there is currently a project opened, that might need saving first

        getModel().newProject();
    }

    /**
     * TODO...
     *
     * @param event
     */
    @FXML
    protected void openProjectAction(ActionEvent event) {

        // TODO: check is there is currently a project opened, that might need saving first

        try {
            getModel().openProject();
        } catch (FileNotFoundException fnfe) {

            // TODO: show error dialog

        }
    }

    /**
     * TODO...
     *
     * @param event
     */
    @FXML
    protected void saveProjectAction(ActionEvent event) {

        // TODO: show dialog success or failure

        getModel().saveProject();
    }

    /**
     * TODO...
     *
     * @param event
     */
    @FXML
    protected void saveProjectAsAction(ActionEvent event) {

        // TODO: show dialog success or failure

        // TODO: figure out how to refresh the name of the project on the project view....

        getModel().saveProjectAs();
    }

    /**
     * TODO...
     *
     * @param event
     */
    @FXML
    protected void closeProjectAction(ActionEvent event) {
        if (getModel().canCloseProjectWithoutDataLoss()) {
            getModel().closeProject();
        } else {
            // Ask user: okay to close project?
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Note");
            alert.setHeaderText("The current project will be discarded");
            alert.setContentText("Are you sure you want to close the project?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // ... user chose OK
                getModel().closeProject();
            }
        }
    }

    /**
     * TODO...
     *
     * @param event
     */
    @FXML
    protected void aboutAction(ActionEvent event) {
        //actiontarget.setText("You clicked 'About'");
    }

    /**
     * TODO...
     *
     * @param event
     */
    @FXML
    protected void helpAction(ActionEvent event) {
        //actiontarget.setText("You clicked 'About'");
    }

    /**
     * Ends the application gracefully.
     *
     * @param event is ignored
     */
    @FXML
    protected void exitAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    protected void dutchLanguageAction(ActionEvent actionEvent) {
    }

    @FXML
    protected void englishLanguageAction(ActionEvent actionEvent) {
    }

    /**
     * Refreshes the menu items, by synchronizing with the model.
     */
    public void refresh() {
        this.closeProject.setDisable(!getModel().hasOpenProject());
        this.saveProject.setDisable(!getModel().hasOpenProject());
        this.saveProjectAs.setDisable(!getModel().hasOpenProject());
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refresh();
    }

}

