package nl.ou.dpd.domain;

/**
 * A factory for {@link DesignPatternEdge}s.
 *
 * @author Martin de Boer
 * @see EdgeFactory
 * @see SystemUnderConsiderationEdgeFactory
 * @see FourTuple
 * @see SystemUnderConsiderationEdge
 * @see DesignPatternEdge
 */
public class DesignPatternEdgeFactory implements EdgeFactory<DesignPatternEdge, DesignPatternClass> {

    /**
     * {@inheritDoc}
     */
    public DesignPatternEdge create(
            DesignPatternClass class1,
            DesignPatternClass class2,
            EdgeType type) {
        return new DesignPatternEdge(class1, class2, type);
    }

    /**
     * {@inheritDoc}
     */
    public DesignPatternEdge create(DesignPatternEdge edge) {
        return new DesignPatternEdge(edge);
    }

    /**
     * {@inheritDoc}
     */
    public DesignPatternEdge createVirtual(DesignPatternEdge edge) {
        final DesignPatternEdge duplicate = new DesignPatternEdge(edge);
        duplicate.makeVirtual();
        return duplicate;
    }

}
