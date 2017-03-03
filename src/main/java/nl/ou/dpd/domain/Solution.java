package nl.ou.dpd.domain;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A {@link Solution} is the result of a matching process between a "system under consideration" and one design pattern.
 * If a design pattern is detected, a {@link Solution} contains the the classes from the "system under consideration"
 * that match that design pattern.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public class Solution {
    private SortedSet<Clazz> solution;

    /**
     * Constructs a {@link Solution} instance with a set of {@code classes} (representing vertices in a graph). Two
     * {@link Solutions} are considered "equal" if they posess the same classes/interfaces. Because the {@code classes}
     * are passed as a {@link SortedSet}, the classes will always be in the right order (ordered by class name).
     *
     * @param classes a set of classes/interfaces. These classes are part of a matching solution.
     */
    public Solution(Set<Clazz> classes) {
        solution = new TreeSet();
        solution.addAll(classes);
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
