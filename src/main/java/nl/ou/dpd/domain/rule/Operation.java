package nl.ou.dpd.domain.rule;

/**
 * Represents an operation. TODO explain this more extensively.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
public enum Operation {
    EQUALS,
    NOT_EQUALS,
    EXISTS,
    NOT_EXISTS;

    public static Operation valueOfIgnoreCase(String s) {
        if (s == null) {
            return null;
        }
        return valueOf(s.toUpperCase());
    }

}
