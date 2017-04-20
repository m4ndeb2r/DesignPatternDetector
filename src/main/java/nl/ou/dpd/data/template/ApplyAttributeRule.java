/**
 * 
 */
package nl.ou.dpd.data.template;

import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.Visibility;
import nl.ou.dpd.domain.rule.AttributeRule;
import nl.ou.dpd.domain.rule.NodeRule;
import nl.ou.dpd.domain.rule.Rule;
import nl.ou.dpd.domain.rule.RuleException;

/**
 * @author Peter Vansweevelt
 *
 */
public class ApplyAttributeRule extends ApplyRule<Attribute> {
	
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
     * Applies the rule on a given {@ink Attribute}.
     * <p>
     * TODO: Consider returning feedback instead of a void
     * <p>
     * @param systemAttribute the attribute under consideration
     * @{@code true} if the {@link Attribute} meets the conditions set in this {@link AttributeRule}, or {@code false}
     * otherwise.
     */
    public void apply() {
        switch (rule.getScope()) {
            case OBJECT:
                applyObject(attribute);
                break;
            default:
                error("Unexpected scope: " + rule.getScope() + ".");
        }
    }

    private void applyObject(Attribute attribute) {
        switch (rule.getOperator()) {
            case EQUALS:
                applyObjectTopicEquals(attribute);
                break;
            case NOT_EQUALS:
//                !applyObjectTopicEquals(systemAttribute);
                break;
            default:
                error("Unexpected operator while applying OBJECT: " + rule.getOperator() + ".");
        }
    }

    private void applyObjectTopicEquals(Attribute attribute) {
        switch (rule.getTopic()) {
            case VISIBILITY:
                attribute.setVisibility(findVisibilityByName(value));
                break;
            default:
                error("Unexpected topic while applying OBJECT: " + rule.getTopic() + ".");
        }
    }

	private Visibility findVisibilityByName(String visibilityName) {
		for (Visibility visibility : Visibility.values()) {
		  if (visibility.toString().contains(visibilityName)) {
			  return visibility;
		  }
		}
		return null;
	}

    private void error(String message) {
//        LOGGER.error(message);
        throw new RuleException(message);
    }

	
}
