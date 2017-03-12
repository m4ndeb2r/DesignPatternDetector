package nl.ou.dpd.gui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import nl.ou.dpd.gui.model.Model;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * A {@link Controller} for the menu of the application.
 *
 * @author Martin de Boer
 */
public class MenuController extends Controller {

    @FXML
    protected MenuItem newProject;

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
        getModel().newProject();
    }

    /**
     * TODO...
     *
     * @param event
     */
    @FXML
    protected void openProjectAction(ActionEvent event) {
        getModel().openProject();
    }

    /**
     * TODO...
     *
     * @param event
     */
    @FXML
    protected void saveProjectAction(ActionEvent event) {
        getModel().saveProject();
    }

    /**
     * TODO...
     *
     * @param event
     */
    @FXML
    protected void saveProjectAsAction(ActionEvent event) {
        getModel().saveProjectAs();
    }

    /**
     * TODO...
     *
     * @param event
     */
    @FXML
    protected void closeProjectAction(ActionEvent event) {
        getModel().closeProject();
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
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.closeProject.setDisable(!getModel().hasOpenProject());
        this.saveProject.setDisable(!getModel().hasOpenProject());
        this.saveProjectAs.setDisable(!getModel().hasOpenProject());
    }

}

