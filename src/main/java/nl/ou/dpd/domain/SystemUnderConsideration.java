package nl.ou.dpd.domain;

/**
 * Represents a system design that is under consideration. Under consideration meaning that it is being analysed for
 * the presence of any known/detectable design pattern.
 *
 * @author Martin de Boer
 */
public class SystemUnderConsideration extends Edges {

    /**
     * Shows all edges in this system design.
     *
     * @deprecated All show methods will be removed. No more printing to System.out soon.
     */
    public void show() {
        for (Edge edge : getEdges()) {
            edge.show();
        }
        System.out.println();
    }

    /**
     * Show all edges without a corresponding edge in the design pattern.
     *
     * @param matchedClasses a map containing matched classes/interfaces.
     * @deprecated All show methods will be removed. No more printing to System.out soon.
     */
    public void showSupplementaryEdges(MatchedClasses matchedClasses) {

        boolean found = false;
        for (Edge edge : this.getEdges()) {
            if (isEdgeSupplementary(matchedClasses, edge)) {
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

    private boolean isEdgeSupplementary(MatchedClasses matchedClasses, Edge edge) {
        return matchedClasses.keyIsBounded(edge.getClass1())
                && matchedClasses.keyIsBounded(edge.getClass2())
                && !edge.isLocked();
    }

}
