/**
 * 
 */
package nl.ou.dpd.data.template;

import nl.ou.dpd.domain.rule.Operator;
import nl.ou.dpd.domain.rule.Rule;
import nl.ou.dpd.domain.rule.Scope;
import nl.ou.dpd.domain.rule.Topic;

/**
 * @author Peter Vansweevelt
 *
 */
public abstract class ApplyRule<T> {
	
	final protected Rule<T> rule;
	final protected String value;

	/**
	 * @param rule
	 * @param value
	 */
	public ApplyRule(Rule<T> rule, String value) {
		this.rule = rule;
		this.value = value;
	}
	
    /**
     * Applies this rule to the specified subject.
     *
     * @param subject the subject to apply this {@link Rule} to.
     * @return {@code true} if this {@link Rule} applies to the specified {@code subject}, or {@code false} otherwise.
     */
    public abstract void apply();

}
