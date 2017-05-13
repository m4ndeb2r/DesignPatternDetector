package nl.ou.dpd.domain.rule;

/**
 * Represents a topic for a {@link Rule}. The topic of a {@link Rule} defines the kind of check will be performed. For
 * example, creating a {@link Rule} with the {@link Topic#VISIBILITY} topic implies that the {@link Rule} will check
 * if two nodes have equal visibility.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
public enum Topic {
    TYPE,
    VISIBILITY,
    MODIFIER_ABSTRACT,
    CARDINALITY,
    CARDINALITY_LEFT,
    CARDINALITY_RIGHT;

    public static Topic valueOfIgnoreCase(String s) {
        if (s == null) {
            return null;
        }
        return valueOf(s.toUpperCase());
    }

}
