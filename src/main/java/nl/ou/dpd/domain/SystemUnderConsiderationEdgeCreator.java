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
     * {@inheritDoc}
     */
    public SystemUnderConsiderationEdge create(String class1, String class2, EdgeType type) {
        return new SystemUnderConsiderationEdge(class1, class2, type);
    }

    /**
     * {@inheritDoc}
     */
    public SystemUnderConsiderationEdge create(SystemUnderConsiderationEdge edge) {
        return new SystemUnderConsiderationEdge(edge);
    }

    /**
     * {@inheritDoc}
     */
    public SystemUnderConsiderationEdge createVirtual(SystemUnderConsiderationEdge edge) {
        final SystemUnderConsiderationEdge duplicate = new SystemUnderConsiderationEdge(edge);
        duplicate.makeVirtual();
        return duplicate;
    }

}
