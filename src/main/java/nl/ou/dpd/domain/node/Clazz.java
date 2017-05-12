package nl.ou.dpd.domain.node;

import java.util.List;

/**
 * Represents a class node in a system design or design pattern.
 * <p>
 * A {@link Clazz} is a {@link Comparable} because it must be possible to add instances to a sorted set.
 *
 * @author Martin de Boer
 */
public class Clazz extends Node {

    /**
     * Constructs a {@link Clazz} instance with the specified {@code name} and similar {@code id}. Use this constructor
     * for design pattern {@link Clazz}es only! In case of {@link Clazz}es in a system under consideration all
     * properties should be known, so then, the constructor with all properties as arguments should be used.
     *
     * @param id   a unique id for this {@link Clazz}
     * @param name the classname of this {@link Clazz}
     */
    public Clazz(String id, String name) {
        this(id, name, null, null, null);
    }

    /**
     * Constructs a {@link Clazz} instance with the specified parameters.
     *
     * @param id         a unique id for this {@link Clazz}
     * @param name       the name of this {@link Clazz}
     * @param visibility the {@link Visibility} of this {@link Clazz}
     * @param attributes the attributes of this {@link Clazz}
     * @param isAbstract {@code true} is this {@link Clazz} is abstract, {@code false} if not, or {@code null} if
     *                   undefined
     */
    public Clazz(String id, String name, Visibility visibility, List<Attribute> attributes, Boolean isAbstract) {
        super(id, name, NodeType.CLASS, visibility, attributes, isAbstract);
    }

}
