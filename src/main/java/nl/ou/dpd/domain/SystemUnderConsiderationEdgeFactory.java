package nl.ou.dpd.domain;

/**
 * A factory for {@link SystemUnderConsiderationEdge}s. Implements the ConcreteFactory of the Abstract Factory design
 * pattern.
 *
 * @author Martin de Boer
 * @see EdgeFactory
 * @see DesignPatternEdgeFactory
 * @see FourTuple
 * @see SystemUnderConsiderationEdge
 * @see DesignPatternEdge
 */
public class SystemUnderConsiderationEdgeFactory implements EdgeFactory<SystemUnderConsiderationEdge, SystemUnderConsiderationClass> {

    /**
     * {@inheritDoc}
     */
    public SystemUnderConsiderationEdge create(
            SystemUnderConsiderationClass class1,
            SystemUnderConsiderationClass class2,
            EdgeType type) {
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
