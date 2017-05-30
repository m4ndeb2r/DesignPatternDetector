package nl.ou.dpd.domain.node;

/**
 * Represents an attribute of type {@link Node}.
 *
 * @author Peter Vansweevelt
 */
public class Parameter {

    private final String id;
    private final String name;
    private Node type;

    /**
     * Creates an attribute with the given name and {@link Node} type.
     *
     * @param name the name of the attribute
     * @param type the type of the attribute
     */
    public Parameter(String id, String name, Node type) {
        this.id = id;
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
     * Get the name of the attribute.
     *
     * @return the name of the attribute
     */
    public String getId() {
        return id;
    }

    /**
     * Get the type of the attribute.
     *
     * @return the type of the attribute as {@link Node}
     */
    public Node getType() {
        return type;
    }

    /**
     * Set the type of the attribute.
     *
     * @param the type of the attribute as {@link Node}
     */
    public void setType(Node type) {
        this.type = type;
    }


}