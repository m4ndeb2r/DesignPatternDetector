package nl.ou.dpd.domain;

/**
 * A {@link SystemUnderConsiderationEdge} represents an relation between two classes in a system design. When the design
 * is viewed as a graph, these relations can be viewed as edges between vertices (the classes/interfaces).
 *
 * @author Martin de Boer
 */
public class SystemUnderConsiderationEdge extends FourTuple {

    /**
     * Constructs an instance of a {@link SystemUnderConsiderationEdge} with the specified classes/interfaces and edge
     * type. The classes/interfaces represent the vertices in the graph (when the system design is viewed as a graph),
     * and the edge type represents the relation type between the classes/interfaces.
     *
     * @param cl1  the "left" class/interface in the relation
     * @param cl2  the "right" class/interface in the relation
     * @param type the relation type
     */
    public SystemUnderConsiderationEdge(
            SystemUnderConsiderationClass cl1,
            SystemUnderConsiderationClass cl2,
            EdgeType type) {
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
     * @param edge           the {@link DesignPatternEdge} to check.
     * @param matchedClasses the object containing all matches.
     * @return {@code true} if there is a match with the specified {@code edge}, or {@code false} otherwise.
     */
    boolean isMatch(DesignPatternEdge edge, MatchedClasses matchedClasses) {
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
        if (matchedClasses.isEmpty(getClass1())
                && matchedClasses.isEmpty(getClass2())
                && !matchedClasses.valueIsBounded(edge.getClass1())
                && !matchedClasses.valueIsBounded(edge.getClass2())) {
            return true;
        }

        // first name matched, second name empty
        if (matchedClasses.equals(getClass1(), edge.getClass1())
                && matchedClasses.isEmpty(getClass2())
                && !matchedClasses.valueIsBounded(edge.getClass2())) {
            return true;
        }

        // first name empty, second name matched
        if (matchedClasses.isEmpty(getClass1())
                && !matchedClasses.valueIsBounded(edge.getClass1())
                && matchedClasses.equals(getClass2(), edge.getClass2())) {
            return true;
        }

        // both names are already matched.
        if (matchedClasses.equals(getClass1(), edge.getClass1())
                && matchedClasses.equals(getClass2(), edge.getClass2())) {
            return true;
        }

        return false;
    }

}
