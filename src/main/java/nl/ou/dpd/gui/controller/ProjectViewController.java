package nl.ou.dpd.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import nl.ou.dpd.gui.model.Model;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * A {@link Controller} for the project view of the application.
 *
 * @author Martin de Boer
 */
public class ProjectViewController extends Controller {

//    @FXML
//    private MenuController menuController;

    @FXML
    private TextField systemFileTextField;

    @FXML
    private TextField templateFileTextField;

    /**
     * Constructs a {@link ProjectViewController} with a reference to the specified {@link Model}.
     *
     * @param model the model of the MVC pattern
     */
    public ProjectViewController(Model model) {
        super(model);
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

    }

    /**
     * Select a "system under consideration" file from disk.
     *
     * @param event is ignored
     */
    @FXML
    protected void chooseSystemFile(ActionEvent event) {
        String path = getModel().chooseSystemFile();
        if (path != null) {
            systemFileTextField.setText(path);
        }
    }

    /**
     * Select a dsisng pattern template file from disk.
     *
     * @param event is ignored
     */
    @FXML
    protected void chooseTemplateFile(ActionEvent event) {
        String path = getModel().chooseTemplateFile();
        if (path != null) {
            templateFileTextField.setText(path);
        }
    }

}

