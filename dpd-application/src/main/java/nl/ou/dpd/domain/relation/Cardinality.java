package nl.ou.dpd.domain.relation;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A representation of a cardinality, with a lowerbound and upperbound value. Infinity as an upperbound value is
 * represented by -1 ({@link #UNLIMITED}).
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 */
public class Cardinality {

    public static final int UNLIMITED = -1;

    static final String ILLEGAL_CARDINALITY_MSG = "Illegal cardinality value: '%s'.";
    static final String NEGATIVES_NOT_ALLOWED_MSG = "No negative values allowed in cardinality.";
    static final String UNLIMITED_NOT_ALLOWED_MSG = "Unlimited value not allowed for lowerbound value.";
    static final String UPPERBOUND_MUST_BE_GE_LOWERBOUND_MSG = "Upperbound value must be >= lowerbound value.";

    private static final String[] SEPARATORS = new String[]{"\\.\\.", ","};

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
            throw new IllegalArgumentException(UPPERBOUND_MUST_BE_GE_LOWERBOUND_MSG);
        }
        if (lower == UNLIMITED) {
            throw new IllegalArgumentException(UNLIMITED_NOT_ALLOWED_MSG);
        }
        if (lower < UNLIMITED || upper < UNLIMITED) {
            throw new IllegalArgumentException(NEGATIVES_NOT_ALLOWED_MSG);
        }
        this.lower = lower;
        this.upper = upper;
    }

    /**
     * Returns a {@link Cardinality} instance based on a string. The allowed values for {@code value} are "m..n",
     * "m, n" and "p" where m represents the lower and n the upper value of the cardinality, and where p represents
     * a combination of both. The value for n and p may also be "*" for unlimited values.
     *
     * @param value a String value to convert to a {@link Cardinality}
     * @return a cardinality [m,n]
     */
    public static Cardinality valueOf(String value) {
        final Integer[] cardinalityValues = findCardinalityValues(value.trim());
        final int lower = cardinalityValues[0];
        final int upper = cardinalityValues[cardinalityValues.length - 1];
        return new Cardinality(lower, upper);
    }

    private static Integer[] findCardinalityValues(String cardinality) {
        if (cardinality.equals("*")) {
            // A special case: * -> [0,UNLIMITED]
            return new Integer[]{0, UNLIMITED};
        }
        final String[] values = splitCardinalityString(cardinality.replaceAll("\\*", "-1"));
        final Integer[] intArray = convertToIntArray(cardinality, values);
        return intArray;
    }

    private static Integer[] convertToIntArray(String cardinality, String[] values) {
        try {
            return Arrays.stream(values)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList())
                    .toArray(new Integer[values.length]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format(ILLEGAL_CARDINALITY_MSG, cardinality));
        }
    }

    private static String[] splitCardinalityString(String cardinality) {
        String[] values = new String[]{cardinality};
        for (int i = 0; i < SEPARATORS.length; i++) {
            values = cardinality.split(SEPARATORS[i]);
            if (values.length > 2) {
                throw new IllegalArgumentException(String.format(ILLEGAL_CARDINALITY_MSG, cardinality));
            }
            if (values.length > 1) {
                return values;
            }
        }
        return values;
    }

    public int getLower() {
        return lower;
    }

    public int getUpper() {
        return upper;
    }

    /**
     * Determines whether this {@link Cardinality} is within the limits of the specified {@code other}
     * {@link Cardinality}.
     *
     * @param other the {@link Cardinality} that must have lower and upper values that lie outside the lower and upper
     *              values of this {@link Cardinality} in order for this method to return {@code true}.
     * @return {@code true} if the lower and upper values of this {@link Cardinality} lie inside the lower and upper
     * values of the {@code other} {@link Cardinality}.
     */
    public boolean isWithinLimitsOf(Cardinality other) {
        final boolean lowerOk = this.lower >= other.lower;
        final boolean upperOk = (this.upper <= other.upper && !(this.upper == UNLIMITED)) || other.upper == UNLIMITED;
        return lowerOk && upperOk;
    }

    @Override
    public String toString() {
        if (lower == upper) {
            return String.format("%d", upper).replace("-1", "*");
        }
        return String.format("%d..%d", lower, upper).replaceAll("-1", "*").replace("0..*", "*");
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(lower, upper);
    }
}
