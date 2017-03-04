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

}
