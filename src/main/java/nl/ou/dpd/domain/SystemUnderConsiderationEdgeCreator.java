package nl.ou.dpd.domain;

/**
 * A creator for {@link SystemUnderConsiderationEdge}s. Implements part of the FactoryMethod design pattern.
 *
 * @author Martin de Boer
 * @see EdgeCreator
 * @see DesignPatternEdgeCreator
 * @see FourTuple
 * @see SystemUnderConsiderationEdge
 * @see DesignPatternEdge
 */
public class SystemUnderConsiderationEdgeCreator implements EdgeCreator<SystemUnderConsiderationEdge> {

    /**
     * Creates a {@link SystemUnderConsiderationEdge} with the specified class names and edge type.
     *
     * @param class1 the first class name (one end of the edge).
     * @param class2 the second class name (the other end of the edge).
     * @param type   the edge type, representing the type of relation between the classes.
     * @return a new {@link SystemUnderConsiderationEdge} instance.
     */
    public SystemUnderConsiderationEdge create(String class1, String class2, EdgeType type) {
        return new SystemUnderConsiderationEdge(class1, class2, type);
    }

    /**
     * Creates a duplicate of the specified {@link SystemUnderConsiderationEdge}.
     *
     * @param edge the edge to duplicate.
     * @return the duplicated edge.
     */
    public SystemUnderConsiderationEdge create(SystemUnderConsiderationEdge edge) {
        return new SystemUnderConsiderationEdge(edge);
    }

}
