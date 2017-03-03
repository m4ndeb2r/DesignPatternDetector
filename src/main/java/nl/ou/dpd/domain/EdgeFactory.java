package nl.ou.dpd.domain;

/**
 * Creates {@link FourTuple}s. This class is the AbstractFactory in the Abstract Factory design pattern.
 *
 * @param <EDGE> the type of objects this factory creates.
 * @author Martin de Boer
 * @see DesignPatternEdgeFactory
 * @see SystemUnderConsiderationEdgeFactory
 * @see FourTuple
 * @see SystemUnderConsiderationEdge
 * @see DesignPatternEdge
 */
public interface EdgeFactory<EDGE extends FourTuple, CLASS extends Clazz> {

    /**
     * Creates a {@link EDGE} with the specified class names and edge type.
     *
     * @param class1 the first class (one end of the edge).
     * @param class2 the second class (the other end of the edge).
     * @param type   the edge type, representing the type of relation between the classes.
     * @return a new {@link EDGE} instance.
     */
    EDGE create(CLASS class1, CLASS class2, EdgeType type);

    /**
     * Creates a duplicate of an existing {@link EDGE}.
     *
     * @param edge the edge to duplicate.
     * @return a duplicate of the specified {@code edge}.
     */
    EDGE create(EDGE edge);

    /**
     * Creates a virtual duplicate of an existing {@link EDGE}.
     *
     * @param edge the edge to duplicate and make virtual.
     * @return a virtual duplicate of the specified {@code edge}.
     */
    EDGE createVirtual(EDGE edge);

}
