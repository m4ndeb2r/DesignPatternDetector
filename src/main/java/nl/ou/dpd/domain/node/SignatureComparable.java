package nl.ou.dpd.domain.node;

/**
 * A {@link SignatureComparable} is an object that can be compared to another {@link SignatureComparable} bases on
 * it "signature". For example, operations can have the same signature, while they have a different id.
 *
 * @author Peter Vansweevelt
 */
public interface SignatureComparable <T extends SignatureComparable>{

    /**
     * Compare the "signature" of an element with the "signature" of another element. This equals method is less strict
     * than the default equals() method, e.g. eliminating a comparison of the id.
     *
     * @param other the {@link SignatureComparable} to compare
     * @return {@code true} if the "signatures" of both {@link SignatureComparable}s are equal, or {@code false}
     * otherwise.
     */
    public boolean equalsSignature(T other);
}
