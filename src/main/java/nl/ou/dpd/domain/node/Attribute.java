package nl.ou.dpd.domain.node;

/**
 * Represents an attribute of type {@link Node}.
 *
 * @author Peter Vansweevelt
 */
public class Attribute {

    private final String name;
    private final Node classtype;

    /**
     * Creates an attribute with the given name and {@link Node} type.
     *
     * @param name the name of the attribute
     * @param type the type of the attribute
     */
    public Attribute(String name, Node classtype) {
        this.name = name;
        this.classtype = classtype;
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
    public Node getClasstype() {
        return classtype;
    }

}
