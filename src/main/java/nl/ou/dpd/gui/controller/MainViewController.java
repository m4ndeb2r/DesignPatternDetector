package nl.ou.dpd.gui.controller;

import nl.ou.dpd.gui.model.Model;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * A {@link Controller} for the main view of the application.
 *
 * @author Martin de Boer
 */
public class MainViewController extends Controller {

    /**
     * Constructs a {@link MainViewController} with the specified {@link Model}.
     *
     * @param model the model of the MVC pattern
     */
    public MainViewController(Model model) {
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

