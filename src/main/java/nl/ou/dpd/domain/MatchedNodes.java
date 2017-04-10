package nl.ou.dpd.domain;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Contains {@link Node}s of the "system under consideration" mapped to {@link Node}s of the design pattern (sys -> dp).
 *
 * @author Martin de Boer
 */

public final class MatchedNodes {

    // A Map to connect nodes. Key: system node; value: design pattern node
    private Map<Node, Node> nodes;

    /**
     * Constructs a new instance with no matched nodes, initially.
     */
    MatchedNodes() {
        nodes = new HashMap<>();
    }

    /**
     * Creates a duplicate of the specified {@code other}.
     *
     * @param other the object to duplicate.
     */
    MatchedNodes(final MatchedNodes other) {
        this();
        other.nodes.keySet().forEach(c -> add(c, other.get(c)));
    }

    /**
     * Gets a value {@link Node} for the specified system {@link Node}. In other words: finds a design pattern
     * {@link Node} that is matched to the specified system under consideration's {@link Node}.
     *
     * @param systemNode the system {@link Node} to look for.
     * @return the matching design pattern {@link Node}.
     */
    public Node get(final Node systemNode) {
        return nodes.get(systemNode);
    }

    /**
     * Filters this {@link MatchedNodes} and returns only the entries with the specified {@code systemNodes}.
     *
     * @param systemNodes the systemNodes to filter
     * @return a new instance of {@link MatchedNodes} containing the entries that were filtered.
     */
    MatchedNodes filter(final Set<Node> systemNodes) {
        MatchedNodes filtered = new MatchedNodes();
        systemNodes.forEach(key -> filtered.add(key, get(key)));
        return filtered;
    }

    /**
     * Returns all the system unders construction's nodes that are bound to a design patter class, in a sorted order.
     *
     * @return a {@link SortedSet} of system under consideration {@link Node}s
     */
    public SortedSet<Node> getBoundSystemNodesSorted() {
        return new TreeSet(nodes.keySet()
                .stream()
                .filter(key -> isSystemNodeBound(key))
                .collect(Collectors.toSet()));
    }

    /**
     * Returns whether the system under construction's node is bound to a design pattern node.
     *
     * @param systemNode the system under construction's node to check
     * @return {@code true} is {@code systemNode} is bound to a design pattern node.
     */
    boolean isSystemNodeBound(final Node systemNode) {
        return !nodes.get(systemNode).equals(Node.EMPTY_NODE);
    }

    /**
     * Returns whether the specified {@link Edge}s can be matched. Edges can be matched when one of the following rules
     * applies:
     * <ol>
     * <li>
     * the edge types must match (be equal or design pattern having INHERITANCE_MULTI and system under
     * consideration having INHERITANCE), and
     * </li><li>
     * the edges should have the same selfRef value, and
     * </li>
     * <li>
     * <ul>
     * <li>
     * both edges (system and design pattern) are unbound on both sides (no class is bound), or
     * </li>
     * <li>
     * one side of the system edge is already matched to one side of the design pattern edge, and the other sides of
     * both (system and design pattern edge are unbound), or
     * </li>
     * <li>
     * the system edge is alaready matched (both sides) to the design pattern edge
     * </li>
     * </ul>
     * </ol>
     *
     * @param systemEdge  the "system under consideration" edge
     * @param patternEdge the design pattern edge
     * @return {@code true} if a match is possible (or already made), or {@code false} otherwise.
     */
    boolean canMatch(final Edge systemEdge, final Edge patternEdge) {

        return areEdgeTypesCompatible(systemEdge, patternEdge)
                && patternEdge.isSelfRef() == systemEdge.isSelfRef()
                && (hasAllNodesUnbound(systemEdge, patternEdge)
                || hasLeftNodeBound(systemEdge, patternEdge)
                || hasRightNodeBound(systemEdge, patternEdge)
                || hasBothNodesMatched(systemEdge, patternEdge)
        );
    }

    private boolean hasBothNodesMatched(final Edge systemEdge, final Edge patternEdge) {
        return this.isMatched(systemEdge.getLeftNode(), patternEdge.getLeftNode())
                && this.isMatched(systemEdge.getRightNode(), patternEdge.getRightNode());
    }

    private boolean hasRightNodeBound(final Edge systemEdge, final Edge patternEdge) {
        return this.isUnbound(systemEdge.getLeftNode())
                && !this.designPatternClassIsBound(patternEdge.getLeftNode())
                && this.isMatched(systemEdge.getRightNode(), patternEdge.getRightNode());
    }

    private boolean hasLeftNodeBound(final Edge systemEdge, final Edge patternEdge) {
        return this.isMatched(systemEdge.getLeftNode(), patternEdge.getLeftNode())
                && this.isUnbound(systemEdge.getRightNode())
                && !this.designPatternClassIsBound(patternEdge.getRightNode());
    }

    private boolean hasAllNodesUnbound(final Edge systemEdge, final Edge patternEdge) {
        return this.isUnbound(systemEdge.getLeftNode())
                && this.isUnbound(systemEdge.getRightNode())
                && !this.designPatternClassIsBound(patternEdge.getLeftNode())
                && !this.designPatternClassIsBound(patternEdge.getRightNode());
    }

    private boolean areEdgeTypesCompatible(final Edge sysEdge, final Edge dpEdge) {
        return dpEdge.getRelationType() == sysEdge.getRelationType() || isInheritanceMultiMatch(sysEdge, dpEdge);
    }

    private boolean isInheritanceMultiMatch(final Edge sysEdge, final Edge dpEdge) {
        return dpEdge.getRelationType() == EdgeType.INHERITANCE_MULTI
                && sysEdge.getRelationType() == EdgeType.INHERITANCE;
    }

    /**
     * Marks two {@link Edge}s as matched. This happens when a design pattern is detected in the "system under
     * consideration", and must be carried out for all the edges in the pattern and all the involved edges in the
     * "system under consideration". Both edges are also locked to prevent them form being matched again (no bigamy
     * allowed here ...)
     *
     * @param systemEdge  the edge for the "system under consideration" to match.
     * @param patternEdge the edge from the design pattern to match.
     */
    void makeMatch(final Edge systemEdge, final Edge patternEdge) {
        add(systemEdge.getLeftNode(), patternEdge.getLeftNode());
        add(systemEdge.getRightNode(), patternEdge.getRightNode());
        systemEdge.lock();
        patternEdge.lock();
    }

    /**
     * Makes an "empty" match. It merely reserve space for the specified {@link Edge} to be matched later.
     *
     * @param systemEdge the edge to prepare space for.
     */
    void prepareMatch(final Edge systemEdge) {
        add(systemEdge.getLeftNode());
        add(systemEdge.getRightNode());
    }

    /**
     * Determines whether a {@link Node}, specified by {@code key}, is matched with an empty node (or, in other words,
     * is not yet connected to another {@link Node}.
     *
     * @param key the {@link Node} to lookup and find a mathed {@link Node} for.
     * @return {@code true} when {@code key}s matched {@link Node} is {@link Node#EMPTY_NODE}.
     */
    private boolean isUnbound(final Node key) {
        return nodes.get(key).equals(Node.EMPTY_NODE);
    }

    /**
     * Determines whether two nodes are already matched.
     *
     * @param key   the {@link Node} to lookup and find a mathed {@link Node} for.
     * @param value the {@link Node} we hope to find
     * @return {@code true} is a match was found, or {@code false} otherwise.
     */
    private boolean isMatched(final Node key, final Node value) {
        return get(key).equals(value);
    }

    /**
     * Detemines whether the specified design pattern {@link Node} is already matched with some "system under
     * consideration" {@link Node}.
     *
     * @param designPatternNode the design pattern node to check for being bound.
     * @return {@code true} if the specified {@code designPatternNode} is already bound, or {@code false} otherwise.
     */
    private boolean designPatternClassIsBound(final Node designPatternNode) {
        for (Node dpNode : nodes.values()) {
            if (dpNode.equals(designPatternNode)) {
                return true;
            }
        }
        return false;
    }

    private void add(final Node key, final Node value) {
        nodes.put(key, value);
    }

    private void add(final Node key) {
        add(key, Node.EMPTY_NODE);
    }

}
