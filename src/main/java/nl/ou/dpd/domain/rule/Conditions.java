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

    public Conditions() {
        this.conditions = new ArrayList<Condition>();
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    /**
     * Processes all the conditions for a specified {@link Edge}, and returns the accumulative result.
     *
     * @param edge the edge to be processed
     * @return {@code null} if at least one mandatory condition has not been processed, {@code false} if the combination
     * of mandatory conditions have not been met, or {@code true} if the combination of mandatory conditions have been
     * met.
     */
    public Boolean processConditions(Edge edge) {
        for (Condition c : conditions) {
            c.clearProcessed();
            c.process(edge);
        }
        return getAccumulatedProcessResult();
    }


    /**
     * Processes all the conditions for a specified {@link Edge} and its {@link Node}s, and returns the accumulative
     * result.
     *
     * @param systemEdge  the systemEdge to be processed
     * @param patternEdge the mould holding the desired values of this edge
     * @return {@code null} if at least one mandatory condition has not been processed, {@code false} if the combination
     * of mandatory conditions have not been met, or {@code true} if the combination of mandatory conditions have been
     * met.
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
     * Processes all the conditions for a mapping of system nodes and pattern nodes, and returns the accumulative
     * result.
     *
     * @param systemEdge  the systemEdge to be processed
     * @param patternEdge the mould holding the desired values of this edge
     * @return {@code null} if at least one mandatory condition has not been processed, {@code false} if the combination
     * of mandatory conditions have not been met, or {@code true} if the combination of mandatory conditions have been
     * met.
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
