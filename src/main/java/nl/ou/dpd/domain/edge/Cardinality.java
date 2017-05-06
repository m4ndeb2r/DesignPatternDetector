package nl.ou.dpd.domain.edge;

import nl.ou.dpd.domain.rule.Conditions;

import java.util.Arrays;
import java.util.List;
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

    public int getLower() {
        return lower;
    }

    public int getUpper() {
        return upper;
    }

    /**
     * Returns a {@link Cardinality} instance based on a string. The allowed values for {@code value} are "m..n",
     * "m, n" and "p" where m represents the lower and n the upper value of the cardinality, and where p represents
     * a combination of both. The value for n and p may also be "*" for unlimited values.
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
            throw new IllegalArgumentException(String.format("Illegal cardinality value: '%s'.", cardinality));
        }
    }

    private static String[] splitCardinalityString(String cardinality) {
        String[] values = new String[]{cardinality};
        for (int i = 0; i < SEPARATORS.length; i++) {
            values = cardinality.split(SEPARATORS[i]);
            if (values.length > 2) {
                throw new IllegalArgumentException(String.format("Illegal cardinality value: '%s'.", cardinality));
            }
            if (values.length > 1) {
                return values;
            }
        }
        return values;
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
