/**
 * 
 */
package nl.ou.dpd.domain.node;

/**
 * @author Peter Vansweevelt
 *
 */
public interface SignatureComparable {
	
	/**
	 * Compare the Signature of an element with another element.
	 * This equals method must less strict then the default equals-method, e.g. eliminating a comparison of the id. 
	 * @param o
	 * @return
	 */
	public boolean equalsSignature(Object o);
}
