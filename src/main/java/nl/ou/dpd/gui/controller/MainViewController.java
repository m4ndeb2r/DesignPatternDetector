package nl.ou.dpd.gui.controller;

import nl.ou.dpd.gui.model.Model;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * A {@link Controller} for the main view of the application.
 *
 * TODO: this should not be a singleton, but there should also not be multiple instance of this class!! Fix it in the ControllerFactoryCreator if possible.
 *
 * @author Martin de Boer
 */
public class MainViewController extends Controller {

//    @FXML
//    private MenuController menuController;

    // Singleton
    private static MainViewController instance = null;

    /**
     * Returns the single instance of this class, or creates it if it does not exist.
     *
     * @param model the {@link Model} this controller updates
     * @return the singleton instance of the {@link MainViewController}.
     */
    public static MainViewController getInstance(Model model) {
        if (instance == null) {
            instance = new MainViewController(model);
        }
        return instance;
    }

    /**
     * Private constructor, because this {@link MainViewController} is a singleton.
     *
     * @param model the model of the MVC pattern
     */
    private MainViewController(Model model) {
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
}

