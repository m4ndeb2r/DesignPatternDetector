package nl.ou.dpd.parsing.template;

import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Visibility;
import nl.ou.dpd.domain.rule.Rule;
import nl.ou.dpd.domain.rule.RuleException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An {@link RuleElementApplicator} for {@link Attribute}s.
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 * @see EdgeRuleElementApplicator
 * @see NodeRuleElementApplicator
 */
public class AttributeRuleElementApplicator extends RuleElementApplicator<Attribute> {

    private static final Logger LOGGER = LogManager.getLogger(AttributeRuleElementApplicator.class);

    public AttributeRuleElementApplicator(Rule<Attribute> rule, String value) {
        super(rule, value);
    }

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
        switch (getOperation()) {
            case EQUALS:
                getMould().setVisibility(Visibility.valueOfIgnoreCase(getValue()));
                break;
            default:
                error(String.format("Unexpected operation while applying TOPIC: '%s'.", getOperation()));
        }
    }

    private void error(String message) {
        LOGGER.error(message);
        throw new RuleException(message);
    }

}
