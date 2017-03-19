package nl.ou.dpd.gui.controller;

import javafx.util.Callback;
import nl.ou.dpd.gui.model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A unitility class that contains a method for creating en controller factory. A controller factory is a
 * {@link Callback} instance that creates a {@link Controller} when the {@link Callback#call(Object)} method
 * is invoked, expecting a {@link Model} instance for an argument.
 *
 * @author Martin de Boer
 */
public final class ControllerFactoryCreator {

    private static final Logger LOGGER = LogManager.getLogger(ControllerFactoryCreator.class);

    private ControllerFactoryCreator() {
        // Utility class. No public default constructor.
    }

    /**
     * Creates a controller factory that can manufacture {@link Controller}s.
     *
     * @param model all {@link Controller}s need a reference to a {@link Model}. This model is the one they will
     *              reference to.
     * @return a controller factory, represented bij a {@link Callback} object.
     */
    public static Callback<Class<?>, Object> createControllerFactory(final Model model) {
        return type -> {
            try {
                if (type == MenuController.class) {
                    return MenuController.getInstance(model);
                }
                if (type == MainViewController.class) {
                    return MainViewController.getInstance(model);
                }
                if (type == ProjectViewController.class) {
                    return ProjectViewController.getInstance(model);
                }
                throw new IllegalArgumentException("Unknown type: " + type.getName());
            } catch (Exception exc) {
                final String msg = "Unable to create controller factory.";
                LOGGER.error(msg, exc);
                throw new RuntimeException(msg, exc); // fatal, just bail...
            }
        };
    }


}
