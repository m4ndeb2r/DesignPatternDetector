package nl.ou.dpd.domain;

import java.util.List;
import java.util.SortedSet;

/**
 * A {@link Solution} is the result of a matching process between a "system under consideration" and one design pattern.
 * If a design pattern is detected, a {@link Solution} contains the {@link Clazz}es from the "system under
 * consideration" that match that design pattern.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public class Solution {

    private final String designPatternName;
    private final MatchedClasses matchedClasses;
    private final List<Edge> superfluousEdges;

    /**
     * Constructs a {@link Solution} instance with a set of {@code classes} (representing vertices in a graph). Two
     * {@link Solutions} are considered "equal" if they posess the same classes/interfaces. Because the {@code classes}
     * are passed as a {@link SortedSet}, the classes will always be in the right order (ordered by class name).
     * TODO....
     *
     * @param classes a set of classes/interfaces. These classes are part of a matching matchedClasses.
     */
    public Solution(String designPatternName, MatchedClasses matchedClasses, List<Edge> superfluousEdges) {
        this.designPatternName = designPatternName;
        this.matchedClasses = matchedClasses;
        this.superfluousEdges = superfluousEdges;
    }

    /**
     * Returns whether this {@link Solution} contains the same class names as the {@code other} one. In other words:
     * the same set of classes from the "system under consideration" (the keys in the {@link MatchedClasses} object)
     * must be present in both {@link Solution}s.
     *
     * @param other
     * @return
     */
    public boolean isEqual(Solution other) {
        return this.matchedClasses.getBoundedSortedKeySet().equals(other.matchedClasses.getBoundedSortedKeySet());
    }

    /**
     * Getter for the design pattern's name.
     *
     * @return the name of the design pattern.
     */
    public String getDesignPatternName() {
        return designPatternName;
    }

    /**
     * Getter for the matched classes.
     *
     * @return the matched classes.
     */
    public MatchedClasses getMatchedClasses() {
        return matchedClasses;
    }

    /**
     * Getter for the superfluous edges: the edges that are found, but do not necessarily belong to the detected design
     * pattern.
     *
     * @return a list of {@link Edge}s that do not belong to the detected pattern.
     */
    public List<Edge> getSuperfluousEdges() {
        return superfluousEdges;
    }
}
