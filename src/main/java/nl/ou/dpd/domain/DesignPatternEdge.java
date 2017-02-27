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
     * @param cl1  the name of the first class
     * @param cl2  the name of the second class
     * @param type the relation type
     */
    public DesignPatternEdge(String cl1, String cl2, EdgeType type) {
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

    /**
     * Makes a match with the specified {@link SystemUnderConsiderationEdge}. When detecting design patterns in a
     * "system under consideration" their edges are matched, and the class names are stored in the specified
     * {@link MatchedNames}.
     *
     * @param edge         the edge in the "system under consideration" to match this {@link DesignPatternEdge} with.
     * @param matchedNames the object to store the matching class names in.
     */
    void makeMatch(SystemUnderConsiderationEdge edge, MatchedNames matchedNames) {
        matchedNames.add(edge.getClassName1(), getClassName1());
        matchedNames.add(edge.getClassName2(), getClassName2());
        setMatched(true);
        edge.setMatched(true);
    }


}
