package nl.ou.dpd.domain.node;

/**
 * Represents a class node in a system design or design pattern.
 * <p>
 * A {@link Clazz} is a {@link Comparable} because it must be possible to add instances to a sorted set.
 *
 * @author Martin de Boer
 */
public class Clazz extends Node {

//    /**
//     * An "empty" {@link Node}. The {@link #EMPTY_NODE} is used to prepare a "match" (similarity) between a node in a
//     * pattern and the system design. It is a placeholder for a matching {@link Node} that is not yet identified.
//     *
//     * @deprecated TODO: use {@link Node#EMPTY_NODE}
//     */
//    public static final Clazz EMPTY_CLASS = new Clazz("");

    /**
     * Constructs a {@link Clazz} instance with the specified {@code name} and similar {@code id}.
     * <p>
     * TODO: this constructor has to go. (deprecated)
     *
     * @param name the classname of this {@link Clazz}
     * @deprecated
     */
    public Clazz(String name) {
        this(name, name);
    }

    /**
     * Constructs a {@link Clazz} instance with the specified {@code name} and similar {@code id}.
     *
     * @param id   a unique id for this {@link Clazz}
     * @param name the classname of this {@link Clazz}
     */
    public Clazz(String id, String name) {
        this(id, name, Visibility.PUBLIC, null, null, null, null);
    }

    /**
     * Constructs a {@link Clazz} instance with the specified parameters.
     *
     * @param id         a unique id for this {@link Clazz}
     * @param name       the name of this {@link Clazz}
     * @param visibility the {@link Visibility} of this {@link Clazz}
     * @param isRoot     {@code true} is this {@link Clazz} is a root node, {@code false} if not, or {@code null} if
     *                   undefined
     * @param isLeaf     {@code true} is this {@link Clazz} is a leaf node, {@code false} if not, or {@code null} if
     *                   undefined
     * @param isAbstract {@code true} is this {@link Clazz} is abstract, {@code false} if not, or {@code null} if
     *                   undefined
     * @param isActive   {@code true} is this {@link Clazz} is active, {@code false} if not, or {@code null} if
     *                   undefined
     */
    public Clazz(String id, String name, Visibility visibility, Boolean isRoot, Boolean isLeaf, Boolean isAbstract, Boolean isActive) {
        super(id, name, NodeType.CLASS, visibility, isRoot, isLeaf, isAbstract, isActive);
    }

}
