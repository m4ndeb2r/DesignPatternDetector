/**
 * 
 */
package nl.ou.dpd.domain;

/**
 * A Class representing the cardinality of a relation end.
 * It holds a lower and upper number. Infinity can be represented as -1 or with the given constant.
 * 
 * @author Peter Vansweevelt
 *
 */
public class Cardinality {
	public static final int INFINITY = -1;
	public int lower, upper;
	
	/**
	 * Constructor setting the lower and upper bound of the cardinality. Values are not checked on errors.
	 * Infinity can be entered as -1 or Cardinality.INFINTY. 
	 * @param lower the lower bound of the cardinality. Must be 0 or greater.
	 * @param upper the upper bound of he cardinality. Must be -1 (infinity) or greater.
	 */
	public Cardinality(int lower, int upper) {
		this.lower = lower;
		this.upper = upper;
	}
	
	
	

}
