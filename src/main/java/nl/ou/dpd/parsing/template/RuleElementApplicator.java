package nl.ou.dpd.parsing.template;

import nl.ou.dpd.domain.rule.Operation;
import nl.ou.dpd.domain.rule.Rule;
import nl.ou.dpd.domain.rule.Scope;
import nl.ou.dpd.domain.rule.Topic;

/**
 * This class applies a specfief {@link Rule} to a specified {@code value}. This results in the {@link Rule}'s
 * {@code mould} being updated according to the rule-value combination. We need this class because some {@link Rule}s
 * cannot be instantiated definitively during parsing, and need to be enriched during the parsing process.
 * <p>
 * * Suppose we need to apply the following rule (from a design patterns template XML file):
 * <pre>
 *     <rule applies="SomeSubject" scope="RELATION" topic="CARDINALITY_LEFT" operation="EQUALS" value="0..*" />
 * </pre>
 * Then this class, instantiated with a {@link Rule} (with the correct {@code mould}, {@code scope}, {@code topic}
 * and {@code operation} already in place), and the {@code value} "0..*", will set the left cardinality of the
 * {@link Rule}'s {@code mould} to cardinality[0,*] when the {@link #apply()} method is called.
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
     * Applies a {@link Rule} to a {@code value}. The {@link Rule} and {@code value} are passed as a parameter to the
     * constructor of this class.
     *
     * @return {@code true} if the {@link Rule} applies to the specified {@code value}, or {@code false} otherwise.
     */
    public abstract void apply();

    protected Topic getTopic() {
        return rule.getTopic();
    }

    protected Scope getScope() {
        return rule.getScope();
    }

    protected Operation getOperation() {
        return rule.getOperation();
    }

    protected T getMould() {
        return rule.getMould();
    }

    protected String getValue() {
        return value;
    }
}
