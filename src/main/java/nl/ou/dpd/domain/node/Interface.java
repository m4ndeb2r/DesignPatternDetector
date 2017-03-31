package nl.ou.dpd.domain.node;

/**
 * Representation of an interface node in a system design or a design pattern.
 * <p>
 * A {@link Clazz} is a {@link Comparable} because it must be possible to add instances to a sorted set.
 *
 * @author Martin de Boer
 */
public class Interface extends Node {

    /**
     * Constructs a {@link Clazz} instance with the specified {@code name}.
     * <p>
     * TODO: this constructor has to go. (deprecated)
     *
     * @param name the classname of this {@link Interface}
     * @deprecated
     */
    public Interface(String name) {
        this(name, name);
    }

    /**
     * Constructs a {@link Clazz} instance with the specified {@code name} and {@code id}.
     * <p>
     *
     * @param id   the unique id of this {@link Interface}
     * @param name the classname of this {@link Interface}
     */
    public Interface(String id, String name) {
        this(id, name, null, null, null);
    }

    /**
     * Constructor with protected access because it is only accessable from within subclasses.
     *
     * @param id       a unique id of this {@link Interface}
     * @param name     the name of this {@link Interface}
     * @param isRoot   {@code true} is this {@link Interface} is a root node, {@code false} if not, or {@code null} if
     *                 undefined
     * @param isLeaf   {@code true} is this {@link Interface} is a leaf node, {@code false} if not, or {@code null} if
     *                 undefined
     * @param isActive {@code true} is this {@link Interface} is active, or {@code false} if not, or {@code null} if
     *                 undefined
     */
    public Interface(String id, String name, Boolean isRoot, Boolean isLeaf, Boolean isActive) {
        super(id, name, NodeType.INTERFACE, Visibility.PUBLIC, isRoot, isLeaf, true, isActive);
    }
}
