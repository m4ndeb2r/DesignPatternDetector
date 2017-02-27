package nl.ou.dpd.domain;

/**
 * TODO...
 *
 * @author Martin de Boer
 */
public class DesignPatternEdge extends FourTuple {

    /**
     * TODO...
     *
     * @param cl1
     * @param cl2
     * @param type
     */
    public DesignPatternEdge(String cl1, String cl2, EdgeType type) {
        super(cl1, cl2, type);
    }

    /**
     * This constructor has package protected access because it is only available within this package.
     * TODO...
     *
     * @param edge a {@link DesignPatternEdge} to construct a copy of.
     */
    DesignPatternEdge(DesignPatternEdge edge) {
        super(edge);
    }

    /**
     * TODO...
     *
     * @param edge
     * @param matchedNames
     */
    void makeMatch(SystemUnderConsiderationEdge edge, MatchedNames matchedNames) {
        matchedNames.add(edge.getClassName1(), getClassName1());
        matchedNames.add(edge.getClassName2(), getClassName2());
        setMatched(true);
        edge.setMatched(true);
    }


}
