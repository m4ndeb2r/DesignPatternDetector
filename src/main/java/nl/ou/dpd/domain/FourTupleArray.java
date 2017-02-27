package nl.ou.dpd.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of {@link FourTuple}s.
 *
 * @author EDGE.M. van Doorn
 * @author Martin de Boer
 */
public abstract class FourTupleArray <EDGE extends FourTuple, CREATOR extends EdgeCreator> {

    private List<EDGE> fourTuples;
    private CREATOR edgeCreator;

    /**
     * TODO....
     */
    public FourTupleArray(CREATOR edgeCreator) {
        this.fourTuples = new ArrayList();
        this.edgeCreator = edgeCreator;
    }

    /**
     * Adds a new {@link EDGE} to this {@link FourTupleArray}. Whenever the {@link EdgeType} of the new {@link EDGE}
     * equals {@link EdgeType#ASSOCIATION}, an extra, virtual (non-visible) is also added.
     *
     * @param edge the {@link EDGE} to add.
     */
    public void add(EDGE edge) {
        fourTuples.add((EDGE) this.edgeCreator.create(edge));

        if (edge.getTypeRelation() == EdgeType.ASSOCIATION) {
            // For edge (A, B, ....) a second but virtual edge (B, A, ...) will be added.
            EDGE tmp = (EDGE) this.edgeCreator.create(edge);
            tmp.makeVirtual();
            fourTuples.add(tmp);
        }
    }

    /**
     * Getter method for the list of {@link EDGE}s.
     *
     * @return the list of {@link EDGE}s, or an empty list if none exist.
     */
    protected List<EDGE> getFourTuples() {
        return this.fourTuples;
    }

    /**
     * Sets the list of {@link EDGE}s.
     *
     * @param edges the new list of {@link EDGE}s.
     */
    protected void setFourTuples(List<EDGE> edges) {
        this.fourTuples = edges;
    }

    /**
     * @deprecated All show methods will be removed. No more printing to System.out soon.
     */
    public abstract void show();

}
