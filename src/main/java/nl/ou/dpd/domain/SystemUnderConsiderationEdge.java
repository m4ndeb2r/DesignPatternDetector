package nl.ou.dpd.domain;

/**
 * A {@link SystemUnderConsiderationEdge} represents an relation between to classes in a system design. When the design
 * is viewed as a graph, these relations can be viewed as edges between vertices (the classes).
 *
 * @author Martin de Boer
 */
public class SystemUnderConsiderationEdge extends FourTuple {

    /**
     * Constructs an instance of a {@link SystemUnderConsiderationEdge} with the specified class names and edge type.
     * The classes represent the vertices in the graph (when the system design is viewed as a graph), and the edge type
     * represents the relation type between the classes.
     *
     * @param cl1  the name of the first class
     * @param cl2  the name of the second class
     * @param type the relation type
     */
    public SystemUnderConsiderationEdge(String cl1, String cl2, EdgeType type) {
        super(cl1, cl2, type);
    }

    /**
     * This constructor has package protected access because it is only available within this package. It duplicates the
     * specified {@code edge}.
     *
     * @param edge a {@link SystemUnderConsiderationEdge} to construct a copy of.
     */
    SystemUnderConsiderationEdge(SystemUnderConsiderationEdge edge) {
        super(edge);
    }

    /**
     * Returns whether there is a match between this {@link SystemUnderConsiderationEdge} and the specified
     * {@link DesignPatternEdge}.
     *
     * @param edge         the {@link DesignPatternEdge} to check.
     * @param matchedNames the object containing all matches.
     * @return {@code true} if there is a match with the specified {@code edge}, or {@code false} otherwise.
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
