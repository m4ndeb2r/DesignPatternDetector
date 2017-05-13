package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.node.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * This class manages all the {@link Condition}s of a {@code DesignPattern}.
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 */
public class Conditions {

    private final List<Condition> conditions;

    public Conditions() {
        this.conditions = new ArrayList<Condition>();
    }

    public Conditions add(Condition condition) {
        this.conditions.add(condition);
        return this;
    }

    public int size() {
        return conditions.size();
    }

    public boolean isEmpty() {
        return conditions.isEmpty();
    }

    public Condition get(int index) {
        return conditions.get(index);
    }

    public Condition getLast() {
        if (isEmpty()) {
            return null;
        }
        return conditions.get(size() - 1);
    }

    public boolean contains(Condition condition) {
        return conditions.contains(condition);
    }

    public Stream<Condition> stream() {
        return conditions.stream();
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
