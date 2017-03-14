package nl.ou.dpd;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
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
        final Scene scene = new Scene(new StackPane());
        final Model model = new Model(scene);

        model.showMainView();

        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setTitle(APP_TITLE);
        primaryStage.show();
    }

}
