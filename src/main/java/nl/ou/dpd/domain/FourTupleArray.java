package nl.ou.dpd.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of {@link FourTuple}s.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public abstract class FourTupleArray {

    private List<FourTuple> fourTuples;

    /**
     * The default constructor. This constructor initialises all the attributes with default values.
     */
    public FourTupleArray() {
        fourTuples = new ArrayList();
    }

    /**
     * Adds a new {@link FoutTuple} to this {@link FourTupleArray}.
     *
     * @param ft the {@link FoutTuple} to add.
     */
    public void add(FourTuple ft) {
        fourTuples.add(new FourTuple(ft));

        if (ft.getTypeRelation() == EdgeType.ASSOCIATION) {
            // For edge (A, B, ....) a second but virtual edge (B, A, ...) will be added.
            FourTuple tmp = new FourTuple(ft);
            tmp.makeVirtual();
            fourTuples.add(tmp);
        }
    }

    /**
     * Getter method for the list of {@link FourTuple}s.
     *
     * @return the list of {@link FourTuple}s, or an empty list if none exist.
     */
    protected List<FourTuple> getFourTuples() {
        return this.fourTuples;
    }

    /**
     * Sets the list of {@link FourTuple}s.
     *
     * @param fourTuples the new list of {@link FourTuple}s.
     */
    protected void setFourTuples(List<FourTuple> fourTuples) {
        this.fourTuples = fourTuples;
    }

    /**
     * @deprecated All show methods will be removed. No more printing to System.out soon.
     */
    public abstract void show();

}
