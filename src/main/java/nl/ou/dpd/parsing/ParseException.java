package nl.ou.dpd.parsing;

/**
 * A {@link RuntimeException} hat is thrown when an exception occurs during parsing.
 *
 * @author Martin de Boer
 */
public class ParseException extends RuntimeException {

    /**
     * {@inheritDoc}
     */
    public ParseException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
