package nl.ou.dpd.gui.controller;

import javafx.fxml.Initializable;
import nl.ou.dpd.gui.model.Model;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * An abstract controller for the views of the application. All controllers must extend this abstract controller, making
 * it possible for the {@link ControllerFactoryCreator} to create controllers.
 * <p>
 * Any must have a {@link Model} to communicate with. This abstract controller holds a {@link Model}, and forces all
 * subclasses to provide one through the constructor.
 *
 * @author Martin de Boer
 */
public abstract class Controller implements Initializable {

    private Model model;

    /**
     * This constructor has private access so only subclasses can reference it. Subclasses must always provide a
     * {@link Model}.
     *
     * @param model the model in the MVC pattern.
     */
    protected Controller(Model model) {
        this.model = model;
    }

    /**
     * Returns the {@link Model} in the MVC pattern.
     *
     * @return the {@link Model} in the MVC pattern.
     */
    protected Model getModel() {
        return this.model;
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
