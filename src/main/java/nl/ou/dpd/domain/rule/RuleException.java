package nl.ou.dpd.domain.rule;

/**
 * Exception that is thrown when an unexpected situation occurs when processing {@link Rule}s.
 *
 * @author Martin de Boer
 */
public class RuleException extends RuntimeException {

    /**
     * {@inheritDoc}
     */
    public RuleException(String message) {
        super(message);
    }

}
