package nl.ou.dpd.domain;

/**
 * A creator for {@link DesignPatternEdge}s.
 *
 * @author Martin de Boer
 * @see EdgeCreator
 * @see SystemUnderConsiderationEdgeCreator
 * @see FourTuple
 * @see SystemUnderConsiderationEdge
 * @see DesignPatternEdge
 */
public class DesignPatternEdgeCreator implements EdgeCreator<DesignPatternEdge> {

    /**
     * Creates a {@link DesignPatternEdge} with the specified class names and edge type.
     *
     * @param class1 the first class name (one end of the edge).
     * @param class2 the second class name (the other end of the edge).
     * @param type   the edge type, representing the type of relation between the classes.
     * @return a new {@link DesignPatternEdge} instance.
     */
    public DesignPatternEdge create(String class1, String class2, EdgeType type) {
        return new DesignPatternEdge(class1, class2, type);
    }

    /**
     * Creates a duplicate of an existing {@link DesignPatternEdge}.
     *
     * @param edge the {@link DesignPatternEdge} to duplicate.
     * @return the duplicated instance.
     */
    public DesignPatternEdge create(DesignPatternEdge edge) {
        return new DesignPatternEdge(edge);
    }

}
