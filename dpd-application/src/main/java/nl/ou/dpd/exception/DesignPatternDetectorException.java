package nl.ou.dpd.exception;

/**
 * A {@link RuntimeException} for the application. This is thrown when an unexpected exception occurs during processing.
 *
 * @author Martin de Boer
 */
public class DesignPatternDetectorException extends RuntimeException {

    public DesignPatternDetectorException(String message, Throwable cause) {
        super(message, cause);
    }

}
