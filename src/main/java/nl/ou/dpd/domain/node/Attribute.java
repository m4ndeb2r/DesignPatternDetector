package nl.ou.dpd.domain.node;

/**
 * Represents an attribute of type {@link Node}.
 *
 * @author Peter Vansweevelt
 */
public class Attribute implements SignatureComparable<Attribute> {

    final private String id;
    final private Node parentNode;
    private String name;
    private Node type;
    private Visibility visibility;

    /**
     * TODO: descent JavaDoc or none at all...
     * TODO: can we get rid of the parent Node?
     * @param id
     * @param parentNode
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

    // TODO: can we get rid of the parentNode?
    public Node getParentNode() {
        return parentNode;
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

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public boolean equalsSignature(Attribute other) {
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

    // TODO: Hashcode and equals are not in sync!!!!
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        Attribute other = (Attribute) obj;
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
        if (parentNode == null) {
            if (other.parentNode != null)
                return false;
        } else if (!parentNode.equals(other.parentNode))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (visibility != other.visibility)
            return false;
        return true;
    }


}