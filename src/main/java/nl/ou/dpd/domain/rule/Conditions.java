package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.node.Node;

import java.util.ArrayList;
import java.util.List;

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
            if (c.getPurview() == Purview.MANDATORY) {
                if (!c.isProcessed()) {
                    return null;
                }
                if (c.isProcessedUnsuccessfully()) {
                    return false;
                }
            }
        }
        return true;
    }

}
