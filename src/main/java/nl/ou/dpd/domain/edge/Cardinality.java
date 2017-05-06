package nl.ou.dpd.domain.edge;

import java.util.Arrays;
import java.util.Objects;

/**
 * A representation of a cardinality, with a lowerbound and upperbound value. Infinity as an upperbound value is
 * represented by -1 ({@link #UNLIMITED}).
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 */
public class Cardinality {

    /**
     * A numeric interpretation of infinity.
     */
    public static final int UNLIMITED = -1;

    private final int lower, upper;

    /**
     * Constructor setting the lower and upper bound of the cardinality. Values are not checked on errors.
     * Infinity can be entered as -1 or {@link Cardinality#UNLIMITED}. Lower and upper bound can be equal e.g when the
     * cardinality is 1.
     *
     * @param lower the lower bound of the cardinality. Must be 0 or greater.
     * @param upper the upper bound of he cardinality. Must be -1 (infinity) or greater.
     */
    public Cardinality(int lower, int upper) {
        if (upper < lower && upper != UNLIMITED) {
            throw new IllegalArgumentException("Upperbound value must be >= lowerbound value.");
        }
        if (lower == UNLIMITED) {
            throw new IllegalArgumentException("Unlimited value not allowed for lowerbound value.");
        }
        if (lower < UNLIMITED || upper < UNLIMITED) {
            throw new IllegalArgumentException("No negative values allowed in cardinality.");
        }
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
     * Returns a {@link Cardinality} instance based on a string. The allowed values for {@code value} are "m..n",
     * "m, n" and "p" where m represents the lower and n the upper value of the cardinality, and where p represents
     * a combination of both. The value for n and p may also be "*" for unlimited values.
     * @return a cardinality [m,n]
     */
    public static Cardinality fromValue(String value) {
        final int[] cardinalityValues = findCardinalityValues(value.trim());
        int lower = cardinalityValues[0];
        int upper = cardinalityValues[cardinalityValues.length - 1];
        return new Cardinality(lower, upper);
    }

    private static int[] findCardinalityValues(String cardinality) {
        if (cardinality.equals("*")) {
            return new int[]{0, UNLIMITED};
        }
        String[] values = splitCardinalityString(cardinality, "\\.\\.", ",");
        int[] result = new int[values.length];
        try {
            for (int i = 0; i < values.length; i++) {
                result[i] = values[i].equals("*") ? UNLIMITED : Integer.parseInt(values[i]);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Illegal cardinality value: '%s'.", cardinality));
        }
        return result;
    }

    private static String[] splitCardinalityString(String cardinality, String... separators) {
        String[] values = new String[]{cardinality};
        for (int i = 0; i < separators.length; i++) {
            values = cardinality.split(separators[i]);
            if (values.length > 2) {
                throw new IllegalArgumentException(String.format("Illegal cardinality value: '%s'.", cardinality));
            }
            if (values.length > 1) {
                return values;
            }
        }
        return values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String s = "";
        String sl = Integer.valueOf(lower).toString();
        String su = Integer.valueOf(upper).toString();
        if (upper == Cardinality.UNLIMITED) {
            su = "*";
        }
        if (lower == upper) {
            s = su;
        } else {
            s = sl + ".." + su;
        }
        return s;
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
