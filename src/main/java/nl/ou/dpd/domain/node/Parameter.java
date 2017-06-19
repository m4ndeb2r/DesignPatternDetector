package nl.ou.dpd.domain.node;

/**
 * Represents an attribute of type {@link Node}.
 *
 * @author Peter Vansweevelt
 */
public class Parameter implements SignatureComparable {

	private final String id;
    private final Operation parentOperation;
    private String name;
    private Node type;

    /**
     * Creates an attribute with the given name and {@link Node} type.
     *
     * @param name the name of the attribute
     * @param type the type of the attribute
     */
    public Parameter(String id, Operation parentOperation) {
        this.id = id;
        this.parentOperation = parentOperation;
        this.name = null;
        this.type = null;
        if (parentOperation != null) {
        	updateOperation();
        }
     }

    /**
	 * @return the parentOperation
	 */
	public Operation getParentOperation() {
		return parentOperation;
	}

	/**
	 * 
	 */
	private void updateOperation() {
		parentOperation.addParameter(this);		
	}

	/**
     * Get the name of the attribute.
     *
     * @return the name of the attribute
     */
    public String getName() {
        return name;
    }

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

	/* 
	 * Compares this parameter with a given one, returning {@code true} if name and type equal.
	 */
	public boolean equalsSignature(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Parameter other = (Parameter) obj;
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
		Parameter other = (Parameter) obj;
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
		if (parentOperation == null) {
			if (other.parentOperation != null)
				return false;
		} else if (!parentOperation.equals(other.parentOperation))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}