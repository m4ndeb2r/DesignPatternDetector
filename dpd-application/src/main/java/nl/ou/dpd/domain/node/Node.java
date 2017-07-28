package nl.ou.dpd.domain.node;

import java.util.HashSet;
import java.util.Set;

import static nl.ou.dpd.util.Util.nullSafeEquals;

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
    
}
