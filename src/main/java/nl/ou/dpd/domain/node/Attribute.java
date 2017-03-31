package nl.ou.dpd.domain.node;

/**
 * Represents an attribute of type {@link Node}.
 *
 * @author Peter Vansweevelt
 */
public class Attribute {

    private final String name;
    private final Node type;

    /**
     * Creates an attribute with the given name and {@link Node} type.
     *
     * @param name the name of the attribute
     * @param type the type of the attribute
     */
    public Attribute(String name, Node type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Get the name of the attribute.
     *
     * @return the name of the attribute
     */
    public String getName() {
        return name;
    }

    /**
     * Get the type of the attribute.
     *
     * @return the type of the attribute as {@link Node}
     */
    public Node getType() {
        return type;
    }

}
