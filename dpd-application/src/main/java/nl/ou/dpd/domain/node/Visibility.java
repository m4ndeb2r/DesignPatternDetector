package nl.ou.dpd.domain.node;

/**
 * Representation of a node's visibility: public, protected, private or package.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */
public enum Visibility {
    PUBLIC,
    PROTECTED,
    PACKAGE,
    PRIVATE;

    public static Visibility valueOfIgnoreCase(String s) {
        if (s == null) {
            return null;
        }
        return valueOf(s.toUpperCase());
    }
};