package nl.ou.dpd.domain.node;

import static nl.ou.dpd.util.Util.nullSafeEquals;

/**
 * Represents an operation parameter. A {@link Parameter} is related to an {@link Operation}, and has a type,
 * represented by a {@link Node} instance.
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 */
public class Parameter implements SignatureComparable<Parameter> {

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

    public Operation getParentOperation() {
        return parentOperation;
    }

    private void updateOperation() {
        parentOperation.addParameter(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public Node getType() {
        return type;
    }

    public void setType(Node type) {
        this.type = type;
    }

    public boolean equalsSignature(Parameter other) {
        if (other == null) return false;
        if (this.equals(other)) return true;
        if (!nullSafeEquals(name, other.name)) return false;
        if (!nullSafeEquals(type, other.type)) return false;
        return true;
    }

}