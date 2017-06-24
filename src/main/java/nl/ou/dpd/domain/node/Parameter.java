package nl.ou.dpd.domain.node;

/**
 * Represents an operation parameter. A {@link Parameter} is related to an {@link Operation}, and has a type,
 * represented by a {@link Node} instance.
 *
 * @author Peter Vansweevelt
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

    private boolean nullSafeEquals(Object a, Object b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

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