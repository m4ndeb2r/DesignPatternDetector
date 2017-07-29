package nl.ou.dpd.domain.node;

import java.util.HashSet;
import java.util.Set;

import static nl.ou.dpd.util.Util.nullSafeEquals;

/**
 * Represents an operation of a {@link Node}, which is the {@link Operation}'s {@code parentNode}.
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
     * Creates an {@link Operation} with the specified id and parent node
     *
     * @param id         a unitque identifier for this {@link Operation}
     * @param parentNode the {@link Node} this operation belongs to
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

    public Operation setReturnType(Node returnType) {
        this.returnType = returnType;
        return this;
    }

    public String getName() {
        return name;
    }

    public Operation setName(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public Operation setVisibility(Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public boolean equalsSignature(Operation other) {
        if (other == null) return false;
        if (this.equals(other)) return true;
        if (!nullSafeEquals(name, other.name)) return false;
        if (!nullSafeEqualsReturnType(returnType, other.returnType)) return false;
        if (!equalsParametersSignatures(parameters, other.parameters)) return false;
        return true;
    }

    private boolean nullSafeEqualsReturnType(SignatureComparable a, SignatureComparable b) {
        return (a == null && b == null) || (a != null && a.equalsSignature(b));
    }

    private boolean equalsParametersSignatures(Set<Parameter> sourceParameters, Set<Parameter> targetParameters) {
        if (sourceParameters.size() != targetParameters.size()) {
            return false;
        }
        return sourceParameters.stream().allMatch(par1 -> targetParameters.stream().anyMatch(par2 -> par1.equalsSignature(par2)));
    }

}