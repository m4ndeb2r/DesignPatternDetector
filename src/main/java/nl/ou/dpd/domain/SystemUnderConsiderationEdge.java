package nl.ou.dpd.domain;

/**
 * A {@link SystemUnderConsiderationEdge} represents an relation between two classes in a system design. When the design
 * is viewed as a graph, these relations can be viewed as edges between vertices (the classes/interfaces).
 *
 * @author Martin de Boer
 */
public class SystemUnderConsiderationEdge extends FourTuple {

    /**
     * Constructs an instance of a {@link SystemUnderConsiderationEdge} with the specified classes/interfaces and edge
     * type. The classes/interfaces represent the vertices in the graph (when the system design is viewed as a graph),
     * and the edge type represents the relation type between the classes/interfaces.
     *
     * @param cl1  the "left" class/interface in the relation
     * @param cl2  the "right" class/interface in the relation
     * @param type the relation type
     */
    public SystemUnderConsiderationEdge(
            SystemUnderConsiderationClass cl1,
            SystemUnderConsiderationClass cl2,
            EdgeType type) {
        super(cl1, cl2, type);
    }

    /**
     * This constructor has package protected access because it is only available within this package. It duplicates the
     * specified {@code edge}.
     *
     * @param edge a {@link SystemUnderConsiderationEdge} to construct a copy of.
     */
    SystemUnderConsiderationEdge(SystemUnderConsiderationEdge edge) {
        super(edge);
    }

}
