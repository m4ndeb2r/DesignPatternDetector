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

    /**
     * Creates an enumation instance with the specified {@code code} and {@code name}.
     *
     * @param code the enumeration's numeric code
     * @param name the enumeration's name (uppercase)
     */
    EdgeType(int code, String name) {
        this.code = code;
        this.name = name;

    }

    /**
     * Return the numeric code value of the enumeration.
     *
     * @return the code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Return the name of the enumeration.
     *
     * @return an uppercase String representing the name of this enumeration
     */
    public String getName() {
        return name;
    }
}
