package nl.ou.dpd.exception;

/**
 * A {@link RuntimeException} for the application. This is thrown when an unexpected exception occurs during processing.
 *
 * @author Martin de Boer
 */
public class DesignPatternDetectorException extends RuntimeException {

    /**
     * {@inheritDoc}
     */
    public DesignPatternDetectorException() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public DesignPatternDetectorException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public DesignPatternDetectorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public DesignPatternDetectorException(Throwable cause) {
        super(cause);
    }

    /**
     * {@inheritDoc}
     */
    public DesignPatternDetectorException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
