package nl.ou.dpd.domain;

/**
 * A {@link DesignPatternEdge} represents an relation between to classes in a design pattern. When the design pattern
 * is viewed as a graph, these relations can be viewed as edges between vertices (the classes).
 *
 * @author Martin de Boer
 */
public class DesignPatternEdge extends FourTuple {

    /**
     * Constructs an instance of a {@link DesignPatternEdge} with the specified class names and edge type. The classes
     * represent the vertices in the graph (when the design pattern is viewed as a graph), and the edge type represents
     * the relation type between the classes.
     *
     * @param cl1  the "left" class in the relation
     * @param cl2  the "right" class in the relation
     * @param type the relation type
     */
    public DesignPatternEdge(Clazz cl1, Clazz cl2, EdgeType type) {
        super(cl1, cl2, type);
    }

    /**
     * This constructor has package protected access because it is only available within this package. It duplicates the
     * specified {@code edge}.
     *
     * @param edge a {@link DesignPatternEdge} to construct a duplicate of.
     */
    DesignPatternEdge(DesignPatternEdge edge) {
        super(edge);
    }

}
