/**
 * 
 */
package nl.ou.dpd.parsing.template;

import nl.ou.dpd.domain.rule.Rule;

/**
 * Class that applies a Rule using a specified value.
 * Results in the mould, given in the Rule, implementing the Rule in the mould itself.
 * 
 * @author Peter Vansweevelt
 * 
 */
public abstract class ApplyRule<T> {
	
	final protected Rule<T> rule;
	final protected String value;

	/**
	 * Constructor of the class.
	 * @param rule
	 * @param value
	 */
	public ApplyRule(Rule<T> rule, String value) {
		this.rule = rule;
		this.value = value;
	}
	
    /**
     * Applies the Rule of this class with value of this class.
     *
     * @param subject the subject to apply this {@link Rule} to.
     * @return {@code true} if this {@link Rule} applies to the specified {@code subject}, or {@code false} otherwise.
     */
    public abstract void apply();

}
