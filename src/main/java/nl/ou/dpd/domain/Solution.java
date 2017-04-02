package nl.ou.dpd.domain;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.node.Clazz;

import java.util.Set;

/**
 * A {@link Solution} is the result of a matching process between a "system under consideration" and one design pattern.
 * If a design pattern is detected, a {@link Solution} contains the {@link Clazz}es from the "system under
 * consideration" that match that design pattern, the superfluous edges, and the missing edges. It provides feedback
 * information for the user of the application.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public class Solution {

    private final String designPatternName;
    private final MatchedNodes matchedNodes;
    private final Set<Edge> superfluousEdges;
    private final Set<Edge> missingEdges;

    /**
     * Constructs a {@link Solution} instance for a design pattern.
     *
     * @param designPatternName the name of the design pattern
     * @param matchedNodes      the matched nodes
     * @param superfluousEdges  the superfluous edges (not stricktly belonging to the desin pattern)
     * @param missingEdges      the missing edges (that should be in the system design, but were not detected
     */
    public Solution(
            String designPatternName,
            MatchedNodes matchedNodes,
            Set<Edge> superfluousEdges,
            Set<Edge> missingEdges) {
        this.designPatternName = designPatternName;
        this.matchedNodes = matchedNodes;
        this.superfluousEdges = superfluousEdges;
        this.missingEdges = missingEdges;
    }

    /**
     * Indicates whether the matched nodes of this {@link Solution} are equal to the matched nodes of another
     * {@link Solution}.
     *
     * @param other the {@link Solution} to compare to this {@link Solution}.
     * @return {@code true} if the matched nodes of this {@link Solution} are equal to the matched nodes of
     * {@code other}, or {@code false} otherwise.
     */
    public boolean isEqual(Solution other) {
        return this.matchedNodes.getBoundSystemNodesSorted().equals(other.matchedNodes.getBoundSystemNodesSorted());
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
     * Getter for the matched nodes.
     *
     * @return the matched nodes.
     */
    public MatchedNodes getMatchedNodes() {
        return matchedNodes;
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
        return this.getMatchedNodes().getBoundSystemNodesSorted().isEmpty();
    }
}
