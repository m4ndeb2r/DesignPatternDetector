package nl.ou.dpd.domain.edge;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of {@link Edge}s. This class is abstract, and cannot be instantiated. Essentially this class is
 * a wrapper for a {@link List}, but it implements some special behaviour for adding elements to that {@link List}:
 * whenever an {@link Edge} with a {@link Edge#relationType} equal to {@link EdgeType#ASSOCIATION} is added, an extra
 * virtual (non-visible) {@link Edge} is added, that has the samen nodes, but reversed.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public abstract class Edges {

    private List<Edge> edges;

    /**
     * Constructor that has protected access because it is only accessible from subclasses.
     */
    protected Edges() {
        this.edges = new ArrayList();
    }

    /**
     * Adds a new {@link Edge} to this {@link Edges}. Whenever the {@link EdgeType} of the new {@link Edge}
     * equals {@link EdgeType#ASSOCIATION}, an extra, virtual (non-visible) is also added.
     *
     * @param edge the {@link Edge} to add.
     */
    public void add(Edge edge) {
        edges.add(edge);

        if (edge.getRelationType() == EdgeType.ASSOCIATION) {
            // For edge (A, B, ....) a second but virtual edge (B, A, ...) will be added.
            final Edge duplicate = edge.duplicate();
            duplicate.makeVirtual();
            edges.add(duplicate);
        }
    }

    /**
     * Getter method for the list of {@link Edge}s.
     *
     * @return the list of {@link Edge}s, or an empty list if none exist.
     */
    public List<Edge> getEdges() {
        return this.edges;
    }

    /**
     * Sets the list of {@link Edge}s.
     *
     * @param edges the new list of {@link Edge}s.
     */
    protected void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

}
