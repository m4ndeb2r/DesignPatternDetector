package nl.ou.dpd.domain.node;

import static nl.ou.dpd.util.Util.nullSafeEquals;

/**
 * Represents an attribute of a {@link Node}.
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 */
public class Attribute implements SignatureComparable<Attribute> {

    final private String id;
    final private Node parentNode;
    private String name;
    private Node type;
    private Visibility visibility;

    /**
     * Constructs an attribute with the specified {@code id} and for the specified {@code parentNode}.
     *
     * @param id         identifies the attrbute
     * @param parentNode the node containing the attribute
     */
    public Attribute(String id, Node parentNode) {
        this.id = id;
        this.parentNode = parentNode;
        this.name = null;
        this.type = null;
        this.visibility = Visibility.PUBLIC;
        if (this.parentNode != null) {
            this.parentNode.addAttribute(this);
        }
    }

    public Node getParentNode() {
        return parentNode;
    }

    public String getName() {
        return name;
    }

    public Attribute setName(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public Node getType() {
        return type;
    }

    public Attribute setType(Node type) {
        this.type = type;
        return this;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public Attribute setVisibility(Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public boolean equalsSignature(Attribute other) {
        if (other == null) return false;
        if (this.equals(other)) return true;
        if (!nullSafeEquals(name, other.name)) return false;
        if (!nullSafeEquals(type, other.type)) return false;
        return true;
    }

}