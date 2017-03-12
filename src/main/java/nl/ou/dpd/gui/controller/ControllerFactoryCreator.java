package nl.ou.dpd.gui.controller;

import javafx.util.Callback;
import nl.ou.dpd.gui.model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;

/**
 * TODO.
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
                final Constructor<?> constructor = type.getDeclaredConstructor(Model.class);
                final Controller controller = (Controller) constructor.newInstance(model);
                return controller;
            } catch (Exception exc) {
                LOGGER.error("Unable to create controller factory.", exc);
                throw new RuntimeException(exc); // fatal, just bail...
            }
        };
    }


}
