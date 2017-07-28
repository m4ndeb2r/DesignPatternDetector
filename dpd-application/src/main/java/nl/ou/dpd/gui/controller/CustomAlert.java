package nl.ou.dpd.gui.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;

/**
 * A customized {@link Alert}, that loads a CSS resource, and adds a style class to its dialog pane. This enables us
 * to style an alert dialog to our needs. If the CSS resource "/style/style.css" is not found, the alert dialog will
 * be shown in the default JavaFX style.
 *
 * @author Martin de Boer
 */
public class CustomAlert extends Alert {

    public static final String CSS_RESOURCE = "/style/style.css";
    private static final Logger LOGGER = LogManager.getLogger(CustomAlert.class);


    /**
     * {@inheritDoc}
     */
    public CustomAlert(AlertType alertType) {
        super(alertType);
        setStyleClass();
    }

    /**
     * {@inheritDoc}
     */
    public CustomAlert(AlertType alertType, String contentText, ButtonType... buttons) {
        super(alertType, contentText, buttons);
        setStyleClass();
    }

    private void setStyleClass() {
        final URL styleResource = getClass().getResource(CSS_RESOURCE);
        if (styleResource == null) {
            LOGGER.warn("CSS resource not found: " + CSS_RESOURCE);
            return;
        }
        final String externalForm = styleResource.toExternalForm();
        final DialogPane dialogPane = getDialogPane();
        dialogPane.getStylesheets().add(externalForm);
    }
}
