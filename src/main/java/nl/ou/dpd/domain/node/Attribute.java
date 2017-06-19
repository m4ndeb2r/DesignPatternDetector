package nl.ou.dpd.domain.node;

/**
 * Represents an attribute of type {@link Node}.
 *
 * @author Peter Vansweevelt
 */
public class Attribute implements SignatureComparable {

	final private String id;
    final private Node parentNode;
    private String name;
    private Node type;
    private Visibility visibility;

    /**
     * Creates an attribute with the given name and {@link Node} type.
     *
     * @param name the name of the attribute
     * @param type the type of the attribute
     */
/*    public Attribute(String name, Node type) {
        this.id = name;
        this.name = name;
        this.type = type;
        this.visibility = null;
    }
*/
    /**
     * Creates an attribute with the given name and {@link Node} type, with a default public {@link Visibility}.
     *
     * @param name the name of the attribute
     * @param type the type of the attribute
     */
    public Attribute(String id, Node parentNode) {
        this.id = id;
        this.parentNode = parentNode;
        this.name = null;
        this.type = null;
        this.visibility = Visibility.PUBLIC;
         if (parentNode != null){
        	updateParentNode();
        }
   }

    /**
	 * set the attribute in the parentNode
	 */
	private void updateParentNode() {
		parentNode.addAttribute(this);		
	}

	/**
	 * @return the parentNode
	 */
	public Node getParentNode() {
		return parentNode;
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
    public void setName(String name) {
        this.name = name;
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

	/**
	 * Compares this Attribute with another Attribute on name and type
	 */
	public boolean equalsSignature(Object attribute) {
		if (this == attribute)
			return true;
		if (attribute == null)
			return false;
		if (getClass() != attribute.getClass())
			return false;
		Attribute other = (Attribute) attribute;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((visibility == null) ? 0 : visibility.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attribute other = (Attribute) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parentNode == null) {
			if (other.parentNode != null)
				return false;
		} else if (!parentNode.equals(other.parentNode))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (visibility != other.visibility)
			return false;
		return true;
	}



}