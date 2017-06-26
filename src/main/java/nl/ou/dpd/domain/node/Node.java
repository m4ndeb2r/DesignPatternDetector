package nl.ou.dpd.domain.node;

import java.util.HashSet;
import java.util.Set;

/**
 * A {@link Node} represents a class, abstract class or interface in a system design or design pattern.
 *
 * @author Martin de Boer
 * @author Peter Vansweevelt
 */

public class Node implements SignatureComparable<Node> {

    final private String id;
    private String name;
    private Set<NodeType> types;
    private Set<Attribute> attributes;
    private Set<Operation> operations;
    private Visibility visibility; //default PUBLIC

    /**
     * Constructs a {@link Node} with the specified {@code id}, {@code name} and {@code nodeType}. The default
     * visibility is PUBLIC.
     *
     * @param id       the node's id (any unique identifier)
     * @param name     the node's name (e.g. a class name)
     * @param nodeType the node's type
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
     * Constructs a {@link Node} with the specified {@code id}. The default visibility is PUBLIC.
     *
     * @param id the node's id (any unique identifier)
     */
    public Node(String id) {
        this(id, null, null);
    }

    /**
     * Constructs a {@link Node} with the specified {@code id} and {@code name}. The default visibility is PUBLIC.
     *
     * @param id   the node's id (any unique identifier)
     * @param name the node's name (e.g. a class name)
     */
    public Node(String id, String name) {
        this(id, name, null);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<NodeType> getTypes() {
        return types;
    }

    public void addType(NodeType type) {
        this.types.add(type);
    }

    public Set<Attribute> getAttributes() {
        return attributes;
    }

    protected void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }

    public Set<Operation> getOperations() {
        return operations;
    }

    protected void addOperation(Operation operation) {
        this.operations.add(operation);
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public boolean equalsSignature(Node other) {
        if (other == null) return false;
        if (this.equals(other)) return true;
        if (!nullSafeEquals(name, other.name)) return false;
        if (!nullSafeEquals(types, other.types)) return false;
        if (!nullSafeEquals(visibility, other.visibility)) return false;
        if (!equalsAttributesSignatures(attributes, other.attributes))
            return false;
        if (!equalsOperationsSignatures(operations, other.operations))
            return false;
        return true;
    }

    private boolean nullSafeEquals(Object a, Object b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    private boolean equalsAttributesSignatures(Set<Attribute> sourceAttributes, Set<Attribute> targetAttributes) {
        if (sourceAttributes.size() != targetAttributes.size()) {
            return false;
        }
        return sourceAttributes.stream().allMatch(attr1 -> targetAttributes.stream().anyMatch(attr2 -> attr1.equalsSignature(attr2)));
    }

    private boolean equalsOperationsSignatures(Set<Operation> sourceOperations, Set<Operation> targetOperations) {
        if (sourceOperations.size() != targetOperations.size()) {
            return false;
        }
        return sourceOperations.stream().allMatch(op1 -> targetOperations.stream().anyMatch(op2 -> op1.equalsSignature(op2)));
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
