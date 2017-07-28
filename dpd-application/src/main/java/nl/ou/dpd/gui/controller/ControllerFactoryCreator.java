package nl.ou.dpd.gui.controller;

import javafx.util.Callback;
import nl.ou.dpd.gui.model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * A utility class that contains a method for creating a controller factory. A controller factory is a
 * {@link Callback} instance that creates a {@link Controller} when the {@link Callback#call(Object)} method
 * is invoked, expecting a {@link Model} instance for an argument.
 * <p>
 * For every type of {@link Controller} only one single instance is constructed per {@link Model}. This prevents the
 * construction of a new {@link Controller} every time a new view is opened in the application. We don't want a new
 * {@link ProjectViewController} every time the project view is opened. The same accounts for the {@link MenuController}
 * and the menu view, a view that is included in several other views. Instead we want to recycle the one used before,
 * provided we also use the samen {@link Model} for the view. This way we keep the internal memory clean.
 *
 * @author Martin de Boer
 */
public final class ControllerFactoryCreator {

    private static final Logger LOGGER = LogManager.getLogger(ControllerFactoryCreator.class);

    private static Map<Model, Map<String, Controller>> controllers = new HashMap<>();

    // Utility class. No public default constructor.
    private ControllerFactoryCreator() {
    }

    /**
     * Creates a controller factory that can construct {@link Controller}s. If any {@link Controller} has been
     * constructed before, no new instance is constructed, but the previously constructed one is returned.
     *
     * @param model all {@link Controller}s need a reference to a {@link Model}. This model is the one they will
     *              reference to.
     * @return a controller factory, represented bij a {@link Callback} object.
     */
    public static Callback<Class<?>, Object> createControllerFactory(final Model model) {
        return type -> {
            try {
                if (!controllers.containsKey(model)) {
                    controllers.put(model, new HashMap<>());
                }
                final Map<String, Controller> controllersForModel = controllers.get(model);
                if (!controllersForModel.containsKey(type.getName())) {
                    final Constructor<?> constructor = type.getDeclaredConstructor(Model.class);
                    final Controller controller = (Controller) constructor.newInstance(model);
                    controllersForModel.put(type.getName(), controller);
                }
                return controllersForModel.get(type.getName());
            } catch (Exception exc) {
                final String msg = "Unable to create controller factory.";
                LOGGER.error(msg, exc);
                throw new RuntimeException(msg, exc); // fatal, just bail...
            }
        };
    }


}
