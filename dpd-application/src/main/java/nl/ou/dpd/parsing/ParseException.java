package nl.ou.dpd.parsing;

/**
 * A {@link RuntimeException} hat is thrown when an exception occurs during parsing.
 *
 * @author Martin de Boer
 */
public class ParseException extends RuntimeException {

    /**
     * A constructor with package protected access so only parsers can instantiate it.
     */
    ParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
