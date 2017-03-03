package nl.ou.dpd.domain;

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
     * @deprecated All show methods must go. No more printing to System.out very soon.
     */
    public void show() {
        if (!name.isEmpty()) {
            System.out.println("Design pattern: " + name);
        }

        for (Edge edge : getEdges()) {
            edge.show();
        }

        System.out.println();
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
     * (except the first one), a previous edge E(v1 -> x2) or E(x1 -> v2) is present. This way every edge is connected
     * to a vertex of a preceding edge.
     * <p/>
     * Example: A->B, C->D, A->C becomes A->B, A->C, C->D.
     */
    void order() {
        // Skip the first element. It stays where it is. Start with i = 1.
        final List<Edge> graph = getEdges();
        for (int i = 1; i < graph.size(); i++) {

            boolean found = false;

            // The i-element should have a class that occurs in the elements 0.. (i-1)
            for (int j = i; j < graph.size() && !found; j++) {
                for (int k = 0; k < i && !found; k++) {
                    found = areEdgesConnected(graph.get(j), graph.get(k));
                    if (found && (j != i)) {
                        // Switch elements
                        final Edge temp = graph.get(j);
                        graph.set(j, graph.get(i));
                        graph.set(i, temp);
                    }
                }
            }

            if (!found) {
                LOGGER.warn("Template is not a connected graph.");
            }
        }
        this.setEdges(graph);
    }

    /**
     * Determines if two {@link Edge}s are connected. They are connected if one or more vertices (classes/interfaces)
     * in one are equal to one or more vertices in the other.
     *
     * @param edge1 the edge to compare to {@code edge2}
     * @param edge2 the edge to compare to {@code edge1}
     * @return {@code true} if the edges are connected, or {@code false} otherwise
     */
    private boolean areEdgesConnected(Edge edge1, Edge edge2) {
        final Clazz v1 = edge1.getClass1();
        final Clazz v2 = edge1.getClass2();
        final Clazz v3 = edge2.getClass1();
        final Clazz v4 = edge2.getClass2();
        return v1.equals(v3) || v1.equals(v4) || v2.equals(v3) || v2.equals(v4);
    }

}
