/**
 * 
 */
package nl.ou.dpd.data.template;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Visibility;
import nl.ou.dpd.domain.rule.AttributeRule;
import nl.ou.dpd.domain.rule.Rule;
import nl.ou.dpd.domain.rule.RuleException;

/**
 * Class that applies a AttributeRule using a specified value.
 * Results in the Attribute, given in the Rule, implementing the Rule in the Attribute itself.
 * 
 * @author Peter Vansweevelt
 *
 */
public class ApplyAttributeRule extends ApplyRule<Attribute> {

    private static final Logger LOGGER = LogManager.getLogger(TemplatesParserWithConditions.class);

	private Attribute attribute;

	/**
	 * @param rule
	 * @param value
	 */
	public ApplyAttributeRule(Rule<Attribute> rule, String value) {
		super(rule, value);
		attribute = rule.getMould();
	}
	
	 /**
	  * Applies this {@link AttributeRule} on a given {@ink Attribute}.
	  * Results in the Attribute, given in the Rule, implementing the Rule in the Attribute itself.
	  */
    public void apply() {
        switch (rule.getScope()) {
            case OBJECT:
                applyObject();
                break;
            default:
                error("Unexpected scope: " + rule.getScope() + ".");
        }
    }

    private void applyObject() {
        switch (rule.getTopic()) {
            case VISIBILITY:
                applyObjectVisibility();
                break;
            default:
                error("Unexpected topic while applying OBJECT: " + rule.getTopic() + ".");
        }
    }

    private void applyObjectVisibility() {
        switch (rule.getOperator()) {
            case EQUALS:
                attribute.setVisibility(findVisibilityByName(value));
                break;
            default:
                error("Unexpected operator while applying TOPIC: " + rule.getOperator() + ".");
        }
    }

    private void applyObjectVisibilityEquals() {
    	attribute.setVisibility(findVisibilityByName(value));
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
