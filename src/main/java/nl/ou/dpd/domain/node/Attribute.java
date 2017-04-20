package nl.ou.dpd.domain.node;

/**
 * Represents an attribute of type {@link Node}.
 *
 * @author Peter Vansweevelt
 */
public class Attribute {

    private final String id;
	private final String name;
    private final Node type;
    private Visibility visibility;

    /**
     * Creates an attribute with the given name and {@link Node} type.
     *
     * @param name the name of the attribute
     * @param type the type of the attribute
     */
    public Attribute(String name, Node type) {
    	this.id = name;
        this.name = name;
        this.type = type;
        this.visibility = null;
    }

    /**
     * Creates an attribute with the given name and {@link Node} type.
     *
     * @param name the name of the attribute
     * @param type the type of the attribute
     */
    public Attribute(String id, String name, Node type) {
    	this.id = id;
        this.name = name;
        this.type = type;
        this.visibility = null;
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
     * Get the name of the attribute.
     *
     * @return the name of the attribute
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * Get the type of the attribute.
     *
     * @return the type of the attribute as {@link Node}
     */
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    
}