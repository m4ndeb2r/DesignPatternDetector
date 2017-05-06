package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.node.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class manages all the {@link Condition}s of a {@code DesignPattern}.
 *
 * @author Peter Vansweevelt
 */
public class Conditions {

    private List<Condition> conditions;

    /**
     * Constructor of the class {@link Conditions}.
     */
    public Conditions() {
        this.conditions = new ArrayList<Condition>();
    }

    /**
     * Get a {@link List} of all the {@link Condition}s of this {@link Conditions}.
     *
     * @return a {@link List} of all the {@link Condition}s
     */
    public List<Condition> getConditions() {
        return conditions;
    }

    /**
     * Processes all the conditions for a specified {@link Edge}, and returns the accumulative result:
     * <ul>
     * <li>{@code null} if at least one mandatory condition has not been processed.</li>
     * <li>{@code false} if the combination of mandatory conditions have not been met</li>
     * <li>{@code true} if the combination of mandatory conditions have been met</li>
     * </ul>
     *
     * @param edge the edge to be processed
     * @return the accumulative result value of the processed attribute;
     */
    public Boolean processConditions(Edge edge) {
        for (Condition c : conditions) {
            c.clearProcessed();
            c.process(edge);
        }
        return getAccumulatedProcessResult();
    }


    /**
     * Processes all the conditions for a specified {@link Edge} and its {@link Node}s, and returns the accumulative result:
     * <ul>
     * <li>{@code null} if at least one mandatory condition has not been processed.</li>
     * <li>{@code false} if the combination of mandatory conditions have not been met</li>
     * <li>{@code true} if the combination of mandatory conditions have been met</li>
     * </ul>
     *
     * @param systemEdge  the systemEdge to be processed
     * @param patternEdge the mould holding the desired values of this edge
     * @return the accumulative result value of the processed attribute;
     */
    public Boolean processConditions(Edge systemEdge, Edge patternEdge) {
        for (Condition c : conditions) {
            c.clearProcessed();
            c.process(systemEdge, patternEdge);
        }
        return getAccumulatedProcessResult();
    }
    
    /*NEW 12 april 2017*/

    /**
     * Processes all the conditions for a mapping of system nodes and pattern nodes, and returns the accumulative result:
     * <ul>
     * <li>{@code null} if at least one mandatory condition has not been processed.</li>
     * <li>{@code false} if the combination of mandatory conditions have not been met</li>
     * <li>{@code true} if the combination of mandatory conditions have been met</li>
     * </ul>
     *
     * @param systemEdge  the systemEdge to be processed
     * @param patternEdge the mould holding the desired values of this edge
     * @return the accumulative result value of the processed attribute;
     */
    public Boolean processConditions(Map<Node, Node> matchedNodes) {
        for (Condition c : conditions) {
            c.clearProcessed();
            c.process(matchedNodes);
        }
        return getAccumulatedProcessResult();
    }

    private Boolean getAccumulatedProcessResult() {
        for (Condition c : conditions) {
            if (c.getPurview() == Purview.MANDATORY && !c.isProcessed()) {
                return null;
            }
            if (c.getPurview() == Purview.MANDATORY && c.isProcessedUnsuccessfully()) {
                return false;
            }
        }
        return true;
    }
}
