package nl.ou.dpd.parsing.template;

import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Visibility;
import nl.ou.dpd.domain.rule.AttributeRule;
import nl.ou.dpd.domain.rule.Rule;
import nl.ou.dpd.domain.rule.RuleException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class that applies a AttributeRule using a specified value.
 * Results in the Attribute, given in the Rule, implementing the Rule in the Attribute itself.
 *
 * @author Peter Vansweevelt
 */
public class AttributeRuleElementApplicator extends RuleElementApplicator<Attribute> {

    private static final Logger LOGGER = LogManager.getLogger(AttributeRuleElementApplicator.class);

    private Attribute attribute;

    /**
     * @param rule
     * @param value
     */
    public AttributeRuleElementApplicator(Rule<Attribute> rule, String value) {
        super(rule, value);
        attribute = rule.getMould();
    }

    /**
     * Applies this {@link AttributeRule} on a given {@ink Attribute}.
     * Results in the Attribute, given in the Rule, implementing the Rule in the Attribute itself.
     */
    public void apply() {
        switch (getScope()) {
            case OBJECT:
                applyObject();
                break;
            default:
                error(String.format("Unexpected scope: '%s'.", getScope()));
        }
    }

    private void applyObject() {
        switch (getTopic()) {
            case VISIBILITY:
                applyObjectVisibility();
                break;
            default:
                error(String.format("Unexpected topic while applying OBJECT: '%s'.", getTopic()));
        }
    }

    private void applyObjectVisibility() {
        switch (getOperator()) {
            case EQUALS:
                attribute.setVisibility(findVisibilityByName(getValue()));
                break;
            default:
                error(String.format("Unexpected operator while applying TOPIC: '%s'.", getOperator()));
        }
    }

    private void applyObjectVisibilityEquals() {
        attribute.setVisibility(findVisibilityByName(getValue()));
    }

    private Visibility findVisibilityByName(String visibilityName) {
        for (Visibility visibility : Visibility.values()) {
            if (visibility.toString().contains(visibilityName.toUpperCase())) {
                return visibility;
            }
        }
        return null;
    }

    private void error(String message) {
        LOGGER.error(message);
        throw new RuleException(message);
    }


}
