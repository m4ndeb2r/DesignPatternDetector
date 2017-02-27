package nl.ou.dpd.domain;

/**
 * TODO...
 *
 * @author Martin de Boer
 */
public class SystemUnderConsiderationEdge extends FourTuple {

    /**
     * TODO...
     *
     * @param cl1
     * @param cl2
     * @param type
     */
    public SystemUnderConsiderationEdge(String cl1, String cl2, EdgeType type) {
        super(cl1, cl2, type);
    }

    /**
     * This constructor has package protected access because it is only available within this package.
     * TODO....
     *
     * @param edge a {@link SystemUnderConsiderationEdge} to construct a copy of.
     */
    SystemUnderConsiderationEdge(SystemUnderConsiderationEdge edge) {
        super(edge);
    }

    /**
     * TODO...
     *
     * @param edge
     * @param matchedNames
     * @return
     */
    boolean isMatch(DesignPatternEdge edge, MatchedNames matchedNames) {
        if (edge.getTypeRelation() != getTypeRelation()) {
            if (edge.getTypeRelation() == EdgeType.INHERITANCE_MULTI
                    && getTypeRelation() == EdgeType.INHERITANCE)
                ; // break; generates a warning.
            else {
                return false;
            }
        }

        if (edge.getSelfRef() != getSelfRef()) {
            return false;
        }

        // two empty names
        if (matchedNames.isEmpty(getClassName1())
                && matchedNames.isEmpty(getClassName2())
                && !matchedNames.valueIsBounded(edge.getClassName1())
                && !matchedNames.valueIsBounded(edge.getClassName2())) {
            return true;
        }

        // first name matched, second name empty
        if (matchedNames.equals(getClassName1(), edge.getClassName1())
                && matchedNames.isEmpty(getClassName2())
                && !matchedNames.valueIsBounded(edge.getClassName2())) {
            return true;
        }

        // first name empty, second name matched
        if (matchedNames.isEmpty(getClassName1())
                && !matchedNames.valueIsBounded(edge.getClassName1())
                && matchedNames.equals(getClassName2(), edge.getClassName2())) {
            return true;
        }

        // both names are already matched.
        if (matchedNames.equals(getClassName1(), edge.getClassName1())
                && matchedNames.equals(getClassName2(), edge.getClassName2())) {
            return true;
        }

        return false;
    }

}
