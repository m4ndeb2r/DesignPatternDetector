package nl.ou.dpd.domain.node;

import nl.ou.dpd.domain.edge.Edge;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A {@link Node} represents a class, abstract class or interface in a system design or design pattern. {@link Node}s
 * are connected to each other via {@link Edge}s.
 * <p>
 * A {@link Node} is a {@link Comparable} because it must be possible to add instances to a sorted set.
 *
 * @author Martin de Boer
 */
public abstract class Node implements Comparable<Node> {

    /**
     * An "empty" {@link Node}. The {@link #EMPTY_NODE} is used to prepare a "match" (similarity) between a node in a
     * pattern and the system design. It is a placeholder for a matching {@link Node} that is not yet identified.
     */
    public static final Node EMPTY_NODE = new EmptyNode();

    private final String id;
    private String name;
    private NodeType type;
    private Visibility visibility;

    private List<Attribute> attributes;
    private List<Method> methods;
    /**
	 * @return the methods
	 */
	public List<Method> getMethods() {
		return methods;
	}

	/**
	 * @param methods the methods to set
	 */
	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}

	/**
	 * @param method the methods to add
	 */

	public void addMethod(Method method) {
		this.methods.add(method);
	}


	private Boolean isAbstract;

    /**
     * Constructor with protected access because it is only accessible from within subclasses.
     *
     * @param id         a unique id of this {@link Node}
     * @param name       the name of this {@link Node}
     * @param type       the type of this {@link Node}: class, abstract class or interface
     * @param visibility the visibility of this node (access modifiers: public, protected, package or private)
     * @param attributes a list of attributes
     * @param isAbstract {@code true} is this {@link Node} is an abstract class or interface, {@code false} if not, or
     *                   {@code null} if undefined
     */
    protected Node(String id,
                   String name,
                   NodeType type,
                   Visibility visibility,
                   List<Attribute> attributes,
                   Boolean isAbstract) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.isAbstract = isAbstract;
        this.visibility = visibility;
        if (attributes == null) {
            this.attributes = new ArrayList<>();
        } else {
            this.attributes = attributes;
        }
        this.methods = new ArrayList<>();
    }

    /**
     * Returns the unique id of this {@link Node}.
     *
     * @return a {@link String} representing this {@link Node}s unique id.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of this {@link Node}.
     *
     * @return a {@link String} representing this {@link Node}s name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this {@link Node}.
     *
     * @param a {@link String} representing this {@link Node}s name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the {@link NodeType} of this {@link Node}: {@link NodeType#CLASS} or {@link NodeType#INTERFACE}.
     *
     * @return the {@link NodeType} of this {@link Node}: {@link NodeType#CLASS} or {@link NodeType#INTERFACE}.
     */
    public NodeType getType() {
        return type;
    }

    /**
     * Return the {@link Visibility} of this {@link Node}. The visibility depends on the access modifier of a class or
     * interface. For example, an interface or a public class always returns: {@link Visibility#PUBLIC}.
     *
     * @return the {@link Visibility} of this {@link Node}.
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /*27/04/17*/

    /**
     * Sets the {@link Visibility} of this {@link Node}. The visibility depends on the access modifier of a class or
     * interface. For example, an interface or a public class always returns: {@link Visibility#PUBLIC}.
     *
     * @param the {@link Visibility} of this {@link Node}.
     */
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    /**
     * Returns all the attributes of this {@link Node}.
     *
     * @return a list of {@link Attribute}s
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * Gets the attributes with the specified name.
     *
     * @return the attributes of this {@link Node} which names equal the given name
     */
    public List<Attribute> getAttributesByName(String attributeName) {
        return attributes.stream()
                .filter(attribute -> attribute.getName().equals(attributeName))
                .collect(Collectors.toList());
    }

    /**
     * Gets the attributes with the specified type.
     *
     * @return the attributes of the class which names equal the given type
     */
    public List<Attribute> getAttributesByType(Node attributeType) {
        return attributes.stream()
                .filter(attribute -> attribute.getType().equals(attributeType))
                .collect(Collectors.toList());
    }

    public Boolean isAbstract() {
        return isAbstract;
    }
    
    public void setAbstract(Boolean value) {
        isAbstract = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return isAbstract == node.isAbstract &&
                Objects.equals(id, node.id) &&
                Objects.equals(name, node.name) &&
                type == node.type &&
                visibility == node.visibility &&
                Objects.equals(attributes, node.attributes);
    }

    /**
     * {@inheritDoc}
     */
/*    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, visibility, attributes, isAbstract);
    }
*/
    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final Node other) {
        if (other == null) {
            return 1;
        }
        final int compareByName = compareByName(other);
        if (compareByName == 0) {
            return compareById(other);
        }
        return compareByName;
    }

    private int compareByName(final Node other) {
        if (getName() == null && other.getName() != null) {
            return -1;
        }
        if (getName() != null && other.getName() == null) {
            return 1;
        }
        if (getName() == null && other.getName() == null) {
            return 0;
        }
        return this.getName().compareTo(other.getName());
    }

    private int compareById(final Node other) {
        if (getId() == null && other.getId() != null) {
            return -1;
        }
        if (getId() != null && other.getId() == null) {
            return 1;
        }
        if (getId() == null && other.getId() == null) {
            return 0;
        }
        return this.getId().compareTo(other.getId());
    }

    /**
     * An empty node, representing an undefined {@link Node}.
     */
    private static class EmptyNode extends Node {

        /**
         * Creates an empty {@link Node}.
         */
        EmptyNode() {
            super("", "", null, null, null,null);
        }
    }
}
