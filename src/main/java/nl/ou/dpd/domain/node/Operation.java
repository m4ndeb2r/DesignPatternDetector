package nl.ou.dpd.domain.node;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an operation.
 *
 * @author Peter Vansweevelt
 */
public class Operation implements SignatureComparable<Operation> {

    private final String id;
    private final Node parentNode;
    private String name;
    private Visibility visibility;
    private Set<Parameter> parameters;
    private Node returnType; // null if returnType is 'void'

    /**
     * TODO: Write JavaDoc, or leave it out.
     * TODO: Can we get rid of parentNode?
     *
     * @param id
     * @param parentNode
     */
    public Operation(String id, Node parentNode) {
        this.id = id;
        this.parentNode = parentNode;
        this.name = null;
        this.visibility = Visibility.PUBLIC;
        this.parameters = new HashSet<>();
        this.returnType = null;
        if (this.parentNode != null) {
            this.parentNode.addOperation(this);
        }
    }

    // TODO: can we get rid of this method/attribute?
    public Node getParentNode() {
        return parentNode;
    }

    public Set<Parameter> getParameters() {
        return parameters;
    }

    protected void addParameter(Parameter parameter) {
        this.parameters.add(parameter);
    }

    public Node getReturnType() {
        return returnType;
    }

    public void setReturnType(Node returnType) {
        this.returnType = returnType;
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

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public boolean equalsSignature(Operation other) {
        if (other == null) return false;
        if (this.equals(other)) return true;
        if (!nullSafeEquals(name, other.name)) return false;
        if (!nullSafeEqualsReturnType(returnType, other.returnType)) return false;
        if (!equalsSignatures(parameters, other.parameters)) return false;
        return true;
    }

    private boolean nullSafeEquals(Object a, Object b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    private boolean nullSafeEqualsReturnType(SignatureComparable a, SignatureComparable b) {
        if (a == null) {
            return b == null;
        }
        return a.equalsSignature(b);
    }

    private boolean equalsSignatures(Set<Parameter> sourceParameters, Set<Parameter> targetParameters) {
        if (sourceParameters.size() == 0 && targetParameters.size() == 0) {
            return true;
        }
        if (sourceParameters.size() != targetParameters.size()) {
            return false;
        }
        Boolean allTrue = true;
        for (Parameter sourceParameter : sourceParameters) {
            Boolean oneTrue = false;
            for (Parameter targetParameter : targetParameters) {
                if (sourceParameter.equalsSignature(targetParameter)) {
                    oneTrue = true;
                }
            }
            allTrue = allTrue && oneTrue;
        }
        return allTrue;
    }

    // TODO: Hashcode and equals are not in sync!!!!
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((returnType == null) ? 0 : returnType.hashCode());
        result = prime * result + ((visibility == null) ? 0 : visibility.hashCode());
        return result;
    }

    // TODO: Hashcode and equals are not in sync!!!!
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Operation other = (Operation) obj;
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
        if (parameters == null) {
            if (other.parameters != null)
                return false;
        } else if (!(parameters.hashCode() == other.parameters.hashCode()))
            return false;
        if (parentNode == null) {
            if (other.parentNode != null)
                return false;
        } else if (!parentNode.equals(other.parentNode))
            return false;
        if (returnType == null) {
            if (other.returnType != null)
                return false;
        } else if (!returnType.equals(other.returnType))
            return false;
        if (visibility != other.visibility)
            return false;
        return true;
    }

}