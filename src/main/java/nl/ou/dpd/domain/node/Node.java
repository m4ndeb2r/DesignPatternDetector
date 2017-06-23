package nl.ou.dpd.domain.node;

import java.util.HashSet;

import java.util.Set;

/**
 * A {@link Node} represents a class, abstract class or interface in a system design or design pattern. {@link Node}s
 * are connected to each other via {@link Edge}s.
 * <p>
 * A {@link Node} is a {@link Comparable} because it must be possible to add instances to a sorted set.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */

public class Node {
	
	final private String id;
	private String name;
	private Set<NodeType> types;	
	private Set<Attribute> attributes;
    private Set<Operation> operations;
	private Visibility visibility; //default PUBLIC

    /**
     * Constructs a Class {@link Node}, with the given properties;
     * default value of {@link Visibility} is PUBLIC and default value of {@code isAbstract} is {@code false}.
     * @param id
     * @param name
     * @param nodeType
     */
/*	public Node(String id, String name, Set<NodeType> nodeTypes) {
		this.id = id;
	    this.name = name;
	    if (nodeTypes != null) {
	    	this.types = nodeTypes;
	    } else {
	    	this.types = new HashSet<>();
	    }
    	this.attributes = new HashSet<>();
    	this.operations = new HashSet<>();
	    visibility = Visibility.PUBLIC;
	}
*/	
    /**
     * Constructs a Class {@link Node}, with the given properties;
     * default value of {@link Visibility} is PUBLIC and default value of {@code isAbstract} is {@code false}.
     * @param id
     * @param name
     * @param nodeType
     */
	public Node(String id, String name, NodeType nodeType) {
		this.id = id;
	    this.name = name;
	    this.types = new HashSet<>();
	    if (nodeType != null) {
	    	types.add(nodeType);
	    }
    	this.attributes = new HashSet<>();
    	this.operations = new HashSet<>();
	    visibility = Visibility.PUBLIC;
	}
	
    /**
     * Constructs a Class {@link Node}, with the given id;
     * default value of {@link Visibility} is PUBLIC and default value of {@code isAbstract} is {@code false}.
     * @param id
     */
	public Node(String id) {
	    this(id, null, null);
	}
	
    /**
     * Constructs a Class {@link Node}, with the id and name;
     * default value of {@link Visibility} is PUBLIC and default value of {@code isAbstract} is {@code false}.
     * @param id
     * @param name
     */
	public Node(String id, String name) {
	    this(id, name, null);
	}

	/**
	 * @return the id.
	 */
	public String getId() {
	    return id;
	}
	
	/**
	 * @return the name.
	 */
	public String getName() {
	    return name;
	}
	
	/**
	 * Set the name.
	 * @param name
	 */
	public void setName(String name) {
	    this.name = name;
	}
	
	/**
	 * @return a Set of the types.
	 */
	public Set<NodeType> getTypes() {
	    return types;
	}
	
	/**
	 * Add a {@link NodeType}.
	 * @param name
	 */
	public void addType(NodeType type) {
	    this.types.add(type);
	}
	
	/**
	 * @return a Set of the attributes.
	 */
	public Set<Attribute> getAttributes() {
		return attributes;
	}

	protected void addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
	}
	
	/**
	 * @return a Set of the Operations.
	 */
	public Set<Operation> getOperations() {
		return operations;
	}

	protected void addOperation(Operation operation) {
		this.operations.add(operation);
	}
	
	/**
	 * @return the visibility
	 */
	public Visibility getVisibility() {
		return visibility;
	}

	/**
	 * @param visibility the visibility to set
	 */
	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	/**
	 * Compares the signature of this Node with another Node comparing everything except the id. 
	 * @param o
	 * @return
	 */
	public boolean equalsSignature(Object node) {
		if (this == node)
			return true;
		if (node == null)
			return false;
		if (getClass() != node.getClass())
			return false;
		Node other = (Node) node;
		if (!equalsAttributesSignatures(attributes, other.attributes))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (!equalsOperationsSignatures(operations, other.operations))
			return false;
		if (types == null) {
			if (other.types != null)
				return false;
		} else if (!types.equals(other.types))
			return false;
		if (visibility != other.visibility)
			return false;
		return true;
	}

	/**
	 * @param sourceAttributes
	 * @param targetAttributes
	 * @return
	 */
	private boolean equalsAttributesSignatures(Set<Attribute> sourceAttributes, Set<Attribute> targetAttributes) {
		if (sourceAttributes.size() == 0 && targetAttributes.size() == 0) {
			return true;
		}
		if (sourceAttributes.size() != targetAttributes.size()) {
			return false;
		}
		Boolean allTrue = true;
		for (Attribute sourceAttribute : sourceAttributes) {
			Boolean oneTrue = false;
			for (Attribute targetAttribute : targetAttributes) {
				if (sourceAttribute.equalsSignature(targetAttribute)) {
					oneTrue = true;
				}
			}
			allTrue = allTrue && oneTrue;
		}
		return allTrue;
	}

	
	/**
	 * @param sourceOperations
	 * @param targetOperations
	 * @return
	 */
	private boolean equalsOperationsSignatures(Set<Operation> sourceOperations, Set<Operation> targetOperations) {
		if (sourceOperations.size() == 0 && targetOperations.size() == 0) {
			return true;
		}
		if (sourceOperations.size() != targetOperations.size()) {
			return false;
		}
		Boolean allTrue = true;
		for (Operation sourceOperation : sourceOperations) {
			Boolean oneTrue = false;
			for (Operation targetOperation : targetOperations) {
				if (sourceOperation.equalsSignature(targetOperation)) {
					oneTrue = true;
				}
			}
			allTrue = allTrue && oneTrue;
		}
		return allTrue;
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
		Node other = (Node) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!(attributes.hashCode() == other.attributes.hashCode()))
			return false;
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
		if (operations == null) {
			if (other.operations != null)
				return false;
		} else if (!(operations.hashCode() == other.operations.hashCode()))
			return false;
		if (types == null) {
			if (other.types != null)
				return false;
		} else if (!types.equals(other.types))
			return false;
		if (visibility != other.visibility)
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
		result = prime * result + ((types == null) ? 0 : types.hashCode());
		result = prime * result + ((visibility == null) ? 0 : visibility.hashCode());
		return result;
	}
	
	//TODO: overriden as debug helper
	@Override
	public String toString() {
		return "node:" + name;
	}
}
