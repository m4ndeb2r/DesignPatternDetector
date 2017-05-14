package nl.ou.dpd.domain.node;

/**
 * Representation of an interface node in a system design or a design pattern.
 * <p>
 * A {@link Interface} is a {@link Comparable} because it must be possible to add instances to a sorted set.
 *
 * @author Martin de Boer
 */
public class Interface extends Node {

    /**
     * Constructs a {@link Interface} instance with the specified {@code name} and {@code id}. The {@link NodeType} of
     * this {@link Interface} is always {@link NodeType#INTERFACE}, its visibility is always {@link Visibility#PUBLIC},
     * and it is always abstract.
     *
     * @param id       a unique id of this {@link Interface}
     * @param name     the name of this {@link Interface}
     */
    public Interface(String id, String name) {
        super(id, name, NodeType.INTERFACE, Visibility.PUBLIC, null, true);
    }
}
