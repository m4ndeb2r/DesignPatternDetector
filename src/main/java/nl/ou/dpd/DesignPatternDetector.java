package nl.ou.dpd;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import nl.ou.dpd.gui.controller.ControllerFactoryCreator;
import nl.ou.dpd.gui.controller.MenuController;
import nl.ou.dpd.gui.model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

/**
 * The main class of the Design Pattern Detector application.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public final class DesignPatternDetector extends Application {

    public static final String STYLE_SHEET = "/style/style.css";
    public static final String ICON_NAME = "/img/cube.png";
    private static final Logger LOGGER = LogManager.getLogger(DesignPatternDetector.class);
    private static final String APP_TITLE = "Design Pattern Detector";

    /**
     * The main method of the application. Starts a GUI.
     *
     * @param args these arguments are omitted
     */
    public static void main(String[] args) {
        try {
            LOGGER.info("Application DesignPatternDetector started.");
            launch(args);
        } catch (Throwable t) {
            LOGGER.error("An unexpected error occurred.", t);
        } finally {
            LOGGER.info("Application DesignPatternDetector stopped.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(final Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);

        final Scene scene = new Scene(new StackPane());
        scene.getStylesheets().add(DesignPatternDetector.class.getResource(STYLE_SHEET).toExternalForm());

        // Set the scene to the main view of the application
        final Model model = new Model(scene);
        model.showMainView();

        // Set the primary stage settings
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setWidth(1280.0);
        primaryStage.setHeight(800.0);
        primaryStage.setTitle(APP_TITLE);

        // Get the app icon
        final InputStream iconResource = DesignPatternDetector.class.getResourceAsStream(ICON_NAME);
        if (iconResource != null) {
            primaryStage.getIcons().add(new Image(iconResource));
        }

        // Initialization is done. Now, show the primary stage
        primaryStage.show();

        // Set a handler for the window close button. Lets the MenuController handle the closing of the application
        // in the same way a File > Exit action is handled.
        final Callback<Class<?>, Object> factory = ControllerFactoryCreator.createControllerFactory(model);
        final MenuController menuController = (MenuController) factory.call(MenuController.class);
        scene.getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                LOGGER.info("Application Closed by click on window close button (X)");
                event.consume();
                menuController.shutdown();
            }
        });

    }

}
