package nl.ou.dpd.domain;

import java.util.Set;
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
    private final Set<Edge> superfluousEdges;
    private final Set<Edge> missingEdges;

    /**
     * Constructs a {@link Solution} instance with a set of {@code classes} (representing vertices in a graph). Two
     * {@link Solutions} are considered "equal" if they posess the same classes/interfaces. Because the {@code classes}
     * are passed as a {@link SortedSet}, the classes will always be in the right order (ordered by class name).
     * TODO....
     *
     * @param classes a set of classes/interfaces. These classes are part of a matching matchedClasses.
     */
    public Solution(
            String designPatternName,
            MatchedClasses matchedClasses,
            Set<Edge> superfluousEdges,
            Set<Edge> missingEdges) {
        this.designPatternName = designPatternName;
        this.matchedClasses = matchedClasses;
        this.superfluousEdges = superfluousEdges;
        this.missingEdges = missingEdges;
    }

    /**
     * TODO...
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
     * @return a set of {@link Edge}s that do not belong to the detected pattern.
     */
    public Set<Edge> getSuperfluousEdges() {
        return superfluousEdges;
    }

    /**
     * Getter for the missing edges: the edges that are in the pattern, but are not found in the system design.
     *
     * @return a set of {@link Edge}s that are missing, but do belong to the detected pattern.
     */
    public Set<Edge> getMissingEdges() {
        return missingEdges;
    }

    /**
     * Indicates whether or not this {@link Solution} has any matches. No matches means an empty solution.
     *
     * @return {@code true} when the are no matches (no solutions), or {@code false} otherwise.
     */
    public boolean isEmpty() {
        return this.getMatchedClasses().getBoundedSortedKeySet().isEmpty();
    }
}
