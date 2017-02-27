package nl.ou.dpd.domain;

/**
 * Creates {@link FourTuple}s. This class is the {@code Creator} in the FactoryMethod design pattern.
 *
 * @param <EDGE> the type of objects this creator creates.
 * @author Martin de Boer
 * @see DesignPatternEdgeCreator
 * @see SystemUnderConsiderationEdgeCreator
 * @see FourTuple
 * @see SystemUnderConsiderationEdge
 * @see DesignPatternEdge
 */
public interface EdgeCreator<EDGE extends FourTuple> {

    /**
     * Creates an {@link EDGE}.
     *
     * @return
     */
    EDGE create(String class1, String class2, EdgeType type);

    /**
     * Creates a copy of an existing {@link EDGE}.
     *
     * @param edge the edge to duplicate.
     * @return a duplicate of the specified {@code edge}.
     */
    EDGE create(EDGE edge);

}
