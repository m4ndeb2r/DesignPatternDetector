package nl.ou.dpd;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nl.ou.dpd.gui.controller.MenuController;
import nl.ou.dpd.gui.model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The main class of the Design Pattern Detector application.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public final class DesignPatternDetector extends Application {

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
        final Model model = new Model(scene);

        // Set the scene to the main view of the application
        model.showMainView();

        // Set the primary stage settings and show it
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setTitle(APP_TITLE);
        primaryStage.show();

        // Set a handler for the window close button. Lets the menu controller handle the closing of the application
        // in the same way a File > Exit action is handled.
        scene.getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                LOGGER.info("Application Closed by click on window close button (X)");
                event.consume();
                MenuController.getInstance(model).shutdown();
            }
        });

    }

}
