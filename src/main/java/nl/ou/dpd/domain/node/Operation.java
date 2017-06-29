package nl.ou.dpd.domain.node;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an operation of a {@link Node}.
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
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
        if (!equalsParametersSignatures(parameters, other.parameters)) return false;
        return true;
    }

    private boolean nullSafeEquals(Object a, Object b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }

    private boolean nullSafeEqualsReturnType(SignatureComparable a, SignatureComparable b) {
        if (a == null) {
            return b == null;
        }
        return a.equalsSignature(b);
    }

    private boolean equalsParametersSignatures(Set<Parameter> sourceParameters, Set<Parameter> targetParameters) {
        if (sourceParameters.size() != targetParameters.size()) {
            return false;
        }
        return sourceParameters.stream().allMatch(par1 -> targetParameters.stream().anyMatch(par2 -> par1.equalsSignature(par2)));
    }

}