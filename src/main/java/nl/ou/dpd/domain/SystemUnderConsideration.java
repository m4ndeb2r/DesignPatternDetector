package nl.ou.dpd.domain;

/**
 * Represents a system design that is under consideration. Under consideration meaning that it is being analysed for
 * the presence of any known/detectable design pattern.
 *
 * @author Martin de Boer
 */
public class SystemUnderConsideration extends FourTupleArray {

    /**
     * Shows all tuples in this system design.
     *
     * @deprecated All show methods will be removed. No more printing to System.out soon.
     */
    public void show() {
        for (FourTuple ft : getFourTuples()) {
            ft.show();
        }
        System.out.println();
    }

    /**
     * Show all edges without a corresponding edge in the design pattern.
     *
     * @param matchedClassNames a map containing matched class names.
     * @deprecated All show methods will be removed. No more printing to System.out soon.
     */
    public void showSupplementaryEdges(MatchedNames matchedClassNames) {

        boolean found = false;
        for (FourTuple edge : this.getFourTuples()) {
            if (isEdgeSupplementary(matchedClassNames, edge)) {
                if (!found) {
                    System.out.println("Edges which do not belong to this design pattern:");
                    found = true;
                }
                edge.showSimple();
            }
        }

        if (found) {
            System.out.println("==================================================");
        }
    }

    private boolean isEdgeSupplementary(MatchedNames matchedClassNames, FourTuple edge) {
        return matchedClassNames.keyIsBounded(edge.getClassName1())
                && matchedClassNames.keyIsBounded(edge.getClassName2())
                && !edge.isMatched();
    }

}
