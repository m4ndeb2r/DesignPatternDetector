package nl.ou.dpd.domain.rule;

/**
 * A {@link Purview} indicates to what extend a {@link Condition} is relevant for the analysis.
 *
 * @author Peter Vansweevelt
 */
public enum Purview {
    MANDATORY,
    FEEDBACK_ONLY,
    IGNORE
}
