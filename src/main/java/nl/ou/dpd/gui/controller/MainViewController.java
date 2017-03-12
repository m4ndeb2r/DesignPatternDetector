package nl.ou.dpd.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import nl.ou.dpd.gui.model.Model;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * A {@link Controller} for the main view of the application.
 *
 * @author Martin de Boer
 */
public class MainViewController extends Controller {

//    @FXML
//    private MenuController menuController;

    @FXML
    private Text actiontarget;

    /**
     * Constructs a {@link MainViewController} with a reference to the specified {@link Model}.
     *
     * @param model the model of the MVC pattern
     */
    public MainViewController(Model model) {
        super(model);
    }

    @FXML
    protected void toggleButtonAction(ActionEvent event) {
        if (actiontarget.getText() == null || actiontarget.getText().length() == 0) {
            actiontarget.setText("Click again!");
        } else {
            actiontarget.setText("");
        }
    }

    @FXML
    protected void leftButtonAction(ActionEvent event) {
        actiontarget.setText("You clicked 'Left'");
    }

    @FXML
    protected void rightButtonAction(ActionEvent event) {
        actiontarget.setText("You clicked 'Right'");
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
}

