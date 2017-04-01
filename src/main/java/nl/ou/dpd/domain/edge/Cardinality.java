package nl.ou.dpd.domain.edge;

import java.util.Objects;

/**
 * A representation of a cardinality, with a lowerbound and upperbound value. Infinity as an upperbound value is
 * represented by -1 ({@link #INFINITY}).
 *
 * @author Peter Vansweevelt
 */
public class Cardinality {

    /**
     * A numeric interpretation of infinity.
     */
    public static final int INFINITY = -1;

    private int lower, upper;

    /**
     * Constructor setting the lower and upper bound of the cardinality. Values are not checked on errors.
     * Infinity can be entered as -1 or {@link Cardinality#INFINITY}.
     *
     * @param lower the lower bound of the cardinality. Must be 0 or greater.
     * @param upper the upper bound of he cardinality. Must be -1 (infinity) or greater.
     */
    public Cardinality(int lower, int upper) {
        this.lower = lower;
        this.upper = upper;
    }

    /**
     * Gets the lowerbound value of this {@link Cardinality}.
     *
     * @return the lowerbound value of this {@link Cardinality}
     */
    public int getLower() {
        return lower;
    }

    /**
     * Gets the upperbound value of this {@link Cardinality}.
     *
     * @return the upperbound value of this {@link Cardinality}
     */
    public int getUpper() {
        return upper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cardinality that = (Cardinality) o;
        return lower == that.lower && upper == that.upper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(lower, upper);
    }
}
