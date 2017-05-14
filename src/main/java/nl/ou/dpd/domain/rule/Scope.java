package nl.ou.dpd.domain.rule;

/**
 * Represents a scope in the context of a {@link Rule}.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
public enum Scope {
    RELATION,
    OBJECT,
    ATTRIBUTE;

    public static Scope valueOfIgnoreCase(String s) {
        if (s == null) {
            return null;
        }
        return valueOf(s.toUpperCase());
    }

}
