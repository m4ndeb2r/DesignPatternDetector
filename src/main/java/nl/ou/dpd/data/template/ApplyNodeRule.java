/**
 * 
 */
package nl.ou.dpd.data.template;

import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.Visibility;
import nl.ou.dpd.domain.rule.NodeRule;
import nl.ou.dpd.domain.rule.Rule;
import nl.ou.dpd.domain.rule.RuleException;

/**
 * @author Peter Vansweevelt
 *
 */
public class ApplyNodeRule extends ApplyRule<Node> {
	
	private Node node;

	/**
	 * @param rule
	 * @param value
	 */
	public ApplyNodeRule(Rule<Node> rule, String value) {
		super(rule, value);
		node = rule.getMould();
	}
	
	   /**
     * Applies the rule on a given {@ink Node}.
     * <p>
     * TODO: Consider returning feedback instead of a void
     * <p>
     * @param node the node under consideration
     * @{@code true} if the {@link Node} meets the conditions set in this {@link NodeRule}, or {@code false}
     * otherwise.
     */
    public void apply() {
        switch (rule.getScope()) {
            case OBJECT:
                applyObject(node);
                break;
           case ATTRIBUTE:
                applyAttributeScope(node);
                break;
            default:
                error("Unexpected scope: " + rule.getScope() + ".");
        }
    }

    private void applyObject(Node node) {
        switch (rule.getOperator()) {
            case EXISTS:
                // TODO: This case seems futile, because it always returns true
                applyObjectTopicExists(node);
                break;
            case NOT_EXISTS:
                // TODO: This case seems futile, because it always returns false
//                !applyObjectTopicExists(node);
                break;
            case EQUALS:
                applyObjectTopicEquals(node);
                break;
            case NOT_EQUALS:
//                !applyObjectTopicEquals(node);
                break;
            default:
                error("Unexpected operator while applying OBJECT: " + rule.getOperator() + ".");
        }
    }

    private void applyObjectTopicEquals(Node node) {
        switch (rule.getTopic()) {
            case TYPE:
//                node.setType()
                break;
            case VISIBILITY:
                node.setVisibility(findVisibilityByName(value)); 
                break;
            case MODIFIER_ROOT:
                node.setRoot(Boolean.valueOf(value));
                break;
            case MODIFIER_LEAF:
            	node.setLeaf(Boolean.valueOf(value));
                break;
            case MODIFIER_ABSTRACT:
            	node.setAbstract(Boolean.valueOf(value));
                break;
            case MODIFIER_ACTIVE:
            	node.setActive(Boolean.valueOf(value));
                break;
            default:
                error("Unexpected topic while applying OBJECT: " + rule.getTopic() + ".");
        }
    }

    // TODO: This method seems futile: system nodes always have a type, visibility, and a value for all modifiers
    // TODO: instead of false, we should perhaps throw a RuleException when a topic does not exist?
    private void applyObjectTopicExists(Node node) {
/*        switch (getTopic()) {
            case TYPE:
                node.getType() != null;
            case VISIBILITY:
                node.getVisibility() != null;
            case MODIFIER_ROOT:
                node.isRoot() != null;
            case MODIFIER_LEAF:
                node.isLeaf() != null;
            case MODIFIER_ABSTRACT:
                node.isAbstract() != null;
            case MODIFIER_ACTIVE:
                node.isActive() != null;
            default:
                error("Unexpected topic while applying OBJECT: " + getTopic() + ".");
        }
*/    }

    private void applyAttributeScope(Node node) {
 /*   	switch (getOperator()) {
		    case EXISTS:
		        // TODO: This case seems futile, because it always returns true
		        applyAttributeTopicExists(node);
		    case NOT_EXISTS:
		        // TODO: This case seems futile, because it always returns false
		        !applyAttributeTopicExists(node);
		    case EQUALS:
		        applyAttributeTopicEquals(node);
		    case NOT_EQUALS:
		        !applyAttributeTopicEquals(node);
		    default:
		    	error("Unexpected operator while applying ATTRIBUTE: " + getOperator() + ".");
    	}
*/    }

    /**
	 * @param node
	 * @return
	 */
	private void applyAttributeTopicEquals(Node node) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Returns if an attribute with the given type exists
	 * @param node
	 * @return
	 */
	private void applyAttributeTopicExists(Node node) {
		// TODO Auto-generated method stub

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
 //       LOGGER.error(message);
        throw new RuleException(message);
    }


}
