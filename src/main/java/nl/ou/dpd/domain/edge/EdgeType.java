package nl.ou.dpd.domain.edge;

/**
 * An enumeration for all edge types (types of relations between classes) in a graph (class diagram).
 *
 * @author Martin de Boer
 */
public enum EdgeType {

    EMPTY(-1, ""),
    ASSOCIATION(1, "ASSOCIATION"),
    ASSOCIATION_DIRECTED(10, "ASSOCIATION_DIRECTED"),
    AGGREGATE(2, "AGGREGATE"),
    COMPOSITE(3, "COMPOSITE"),
    INHERITANCE(4, "INHERITANCE"),
    INHERITANCE_MULTI(40, "INHERITANCE_MULTI"),
    DEPENDENCY(5, "DEPENDENCY"),
    REALIZATION(6, "REALIZATION");

    private final int code;
    private final String name;

    EdgeType(int code, String name) {
        this.code = code;
        this.name = name;

    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static EdgeType valueOfIgnoreCase(String s) {
        if (s == null) {
            return null;
        }
        return valueOf(s.toUpperCase());
    }
}
