package nl.ou.dpd.parsing.template;

import nl.ou.dpd.domain.rule.Operator;
import nl.ou.dpd.domain.rule.Rule;
import nl.ou.dpd.domain.rule.Scope;
import nl.ou.dpd.domain.rule.Topic;

/**
 * Class that applies a Rule using a specified value.
 * Results in the mould, given in the Rule, implementing the Rule in the mould itself.
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 */
public abstract class RuleElementApplicator<T> {

    final private Rule<T> rule;
    final private String value;

    public RuleElementApplicator(Rule<T> rule, String value) {
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

    public Topic getTopic() {
        return rule.getTopic();
    }

    public Scope getScope() {
        return rule.getScope();
    }

    public Operator getOperator() {
        return rule.getOperator();
    }

    public String getValue() {
        return value;
    }
}
