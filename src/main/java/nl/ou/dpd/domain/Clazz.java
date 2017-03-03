package nl.ou.dpd.domain;

import java.util.Objects;

/**
 * Represents a class or interface in a design pattern or a system under consideration.
 *
 * @author Martin de Boer
 */
public abstract class Clazz implements Comparable<Clazz> {

    /**
     * An "empty" {@link Clazz}
     */
    public static final Clazz EMPTY_CLASS = new EmptyClazz();

    private final String name;

    /**
     * Constructor with protected access, so only subclasses can access it.
     *
     * @param name the classname of this {@link Clazz}
     */
    protected Clazz(String name) {
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
        if (getName() == null) {
            return -1;
        }
        if (other.getName() == null) {
            return 1;
        }
        return this.getName().compareTo(other.getName());
    }

    /**
     * An empty {@link Clazz}.
     */
    private static class EmptyClazz extends Clazz {

        /**
         * Constructs a {@link Clazz} with an empty name.
         */
        EmptyClazz() {
            super("");
        }
    }
}
