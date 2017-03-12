package nl.ou.dpd.domain;

import java.util.Objects;

/**
 * Represents a class or interface in a design pattern or a "system under consideration". A {@link Clazz} is typically
 * connected to another {@link Clazz} within the same design pattern or "system under consideration". Two connected
 * {@link Clazz}'s form an {@link Edge}. A combination of {@link Edge}s (combined in a subclass of {@link Edges}) form a
 * design pattern or a system design. {@link Edge}s can have an {@link EdgeType}, defining the type of relation between
 * the {@link Clazz}'s.
 * <p>
 * A {@link Clazz} is a {@link Comparable} because it must be possible to add instances to a sorted set.
 *
 * TODO: add an id field (XMI provides id fields for classes that are unique. Names are not stricktly unique)
 * TODO: equals should compare the id's not the names. compareTo can compare names, and stay as is....
 *
 * @author Martin de Boer
 */
public class Clazz implements Comparable<Clazz> {

    /**
     * An "empty" {@link Clazz}. The {@link #EMPTY_CLASS} is used to prepare a "match" (similarity) between a class or
     * interface in a pattern and the system design. It is a placeholder for a matching {@link Clazz} that is not yet
     * identified.
     */
    public static final Clazz EMPTY_CLASS = new Clazz("");

    private final String name;

    /**
     * Constructs a {@link Class} instance with the specified {@code name}.
     *
     * @param name the classname of this {@link Clazz}
     */
    public Clazz(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the class.
     *
     * @return the classname
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clazz clazz = (Clazz) o;
        return Objects.equals(name, clazz.name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Clazz other) {
        if (other == null || other.getName() == null) {
            return 1;
        }
        if (getName() == null) {
            return -1;
        }
        return this.getName().compareTo(other.getName());
    }

}
