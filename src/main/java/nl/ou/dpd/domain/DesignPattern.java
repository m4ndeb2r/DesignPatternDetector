package nl.ou.dpd.domain;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.Edges;
import nl.ou.dpd.domain.node.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Represents an design pattern.
 *
 * @author Martin de Boer
 */
public class DesignPattern extends Edges {

    private static final Logger LOGGER = LogManager.getLogger(DesignPattern.class);

    private final String name;

    /**
     * Constructs a {@link DesignPattern} instance with the specified name.
     *
     * @param name the name of this design pattern
     */
    public DesignPattern(String name) {
        super();
        this.name = name;
    }

    /**
     * Getter for the pattern name.
     *
     * @return the name of this design pattern.
     */
    public String getName() {
        return name;
    }

    /**
     * This method orders the array of {@link Edge}'s. It guarantees that every edge in the graph has at least one
     * vertex that is also present in one or more preceding edges. One exception to this rule is the first edge in the
     * graph, obviously because it has no preceding edge. In other words: for every edge E(v1 -> v2) in the graph
     * (except the first one), a previous edge E(v1 -> x2), E(x1 -> v1), E(x1 -> v2) or E(v2 -> x2) is present. This way
     * every edge is connected to a vertex of a preceding edge.
     * <p/>
     * Example: A->B, C->D, A->C becomes A->B, A->C, C->D.
     */
    void order() {
        order(getEdges(), 0, 1, 2);
    }

    private void order(List<Edge> graph, int base, int switchable, int start) {
        for(int i = start; i < graph.size(); i++) {
            final boolean switchableConnected = isConnectedToPrecedingEdge(graph, switchable, base);
            final boolean currentConnected = isConnectedToPrecedingEdge(graph, i, base);
            if (currentConnected && !switchableConnected) {
                switchEdges(graph, i, switchable);
                order(graph, ++base, ++switchable, ++start);
            }
        }
    }

    private boolean isConnectedToPrecedingEdge(List<Edge> graph, int test, int base) {
        for(int i = 0; i <= base; i++) {
            if (areEdgesConnected(graph.get(test), graph.get(i))) {
                return true;
            }
        }
        return false;
    }

    private void switchEdges(List<Edge> graph, int i, int j) {
        final Edge temp = graph.get(j);
        graph.set(j, graph.get(i));
        graph.set(i, temp);
    }

    private boolean areEdgesConnected(Edge edge1, Edge edge2) {
        final Node v1 = edge1.getLeftNode();
        final Node v2 = edge1.getRightNode();
        final Node v3 = edge2.getLeftNode();
        final Node v4 = edge2.getRightNode();
        return v1.equals(v3) || v1.equals(v4) || v2.equals(v3) || v2.equals(v4);
    }

}
