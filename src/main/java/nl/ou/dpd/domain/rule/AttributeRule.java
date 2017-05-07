package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.node.Attribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A {@link AttributeRule} is a {@link Rule} that applies to {@link Attribute}s specificly.
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 * @see NodeRule
 * @see EdgeRule
 */
public class AttributeRule extends Rule<Attribute> {

    private static final Logger LOGGER = LogManager.getLogger(Rule.class);

    /**
     * Creates a rule with the {@link Attribute} mould that implements the features needed to apply to this rule.
     *
     * @param mould    the {@link Attribute} containing the rule's features
     * @param topic    holds the feature to be evaluated (type, visibility, ...)
     * @param scope    the scope of the evaluation (object, attributes, ...)
     * @param operator the evaluation operator (equals, exists, ...)
     */
    public AttributeRule(Attribute mould, Scope scope, Topic topic, Operator operator) {
        super(mould, scope, topic, operator);
    }

    /**
     * Applies the rule on a given {@ink Attribute}.
     * <p>
     * TODO: Consider returning feedback instead of a boolean
     * <p>
     *
     * @param systemAttribute the attribute under consideration
     * @return {@code true} if the {@link Attribute} meets the conditions set in this {@link AttributeRule}, or {@code false}
     * otherwise.
     */
    public boolean process(Attribute systemAttribute) {
        switch (getScope()) {
            case OBJECT:
                return processObject(systemAttribute);
            default:
                return error(String.format("Unexpected scope: '%s'.", getScope()));
        }
    }

    private boolean processObject(Attribute systemAttribute) {
        switch (getTopic()) {
            case VISIBILITY:
                return processObjectVisibility(systemAttribute);
            default:
                return error(String.format("Unexpected topic while processing OBJECT: '%s'.", getTopic()));
        }
    }

    private boolean processObjectVisibility(Attribute systemAttribute) {
        switch (getOperator()) {
            case EQUALS:
                return processObjectVisibilityEquals(systemAttribute);
            case NOT_EQUALS:
                return !processObjectVisibilityEquals(systemAttribute);
            default:
                return error(String.format("Unexpected operator while processing VISIBILITY: '%s'.", getOperator()));
        }
    }

    private boolean processObjectVisibilityEquals(Attribute systemAttribute) {
        return systemAttribute.getVisibility() == getMould().getVisibility();
    }

    private boolean error(String message) {
        LOGGER.error(message);
        throw new RuleException(message);
    }

}
