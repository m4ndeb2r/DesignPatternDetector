package nl.ou.dpd.fourtuples;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A {@link Solution} is the result of a matching process between a "system under consideration" and one design pattern.
 * If a design pattern is detected, a {@link Solution} contains the names of the classes from the "system under
 * consideration" that match that design pattern.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public class Solution {
    private SortedSet<String> solution;

    /**
     * Constructs a {@link Solution} instance with a set of {@code names} that represent classes (or vertices in a
     * graph). Two {@link Solutions} are considered "equal" if they posess the same names. Because the {@code names}
     * are passed as a {@link SortedSet}, the class names will always be in the right order.
     *
     * @param names a set of class names. These classes are part of a matching solution.
     */
    public Solution(Set<String> names) {
        solution = new TreeSet();
        solution.addAll(names);
    }

    /**
     * Returns whether this {@link Solution} contains the same names as the {@code other} one.
     *
     * @param other
     * @return
     */
    public boolean isEqual(Solution other) {
        return this.solution.equals(other.solution);
    }

}
