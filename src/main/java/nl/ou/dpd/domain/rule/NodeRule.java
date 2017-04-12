package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.node.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A {@link NodeRule} is a {@link Rule} that applies to {@link Node}s specificly.
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 * @see EdgeRule
 * @see Rule
 */
public class NodeRule extends Rule<Node> {

    private static final Logger LOGGER = LogManager.getLogger(Rule.class);

    /**
     * Creates a rule with the {@link Node} mould that implements the features needed to apply to this rule.
     *
     * @param mould    the {@link Node} containing the rule's features
     * @param topic    holds the feature to be evaluated (type, visibility, ...)
     * @param scope    the scope of the evaluation (object, attributes, ...)
     * @param operator the evaluation operator (equals, exists, ...)
     */
    public NodeRule(Node mould, Topic topic, Scope scope, Operator operator) {
        super(mould, topic, scope, operator);
    }

    /**
     * Applies the rule on a given {@ink Node}.
     * <p>
     * TODO: Consider returning feedback instead of a boolean
     * <p>
     * TODO: The system class must be bound with a dp-class as it does in MatchedNodes. Now the classtype is still
     * compared by using the name. Maybe a decoratorpattern on the system class can be used to dynamically add the
     * associated dp class to it?
     *
     * @param systemNode the node under consideration
     * @return {@code true} if the {@link Node} meets the conditions set in this {@link NodeRule}, or {@code false}
     * otherwise.
     */
    public boolean process(Node systemNode) {
        switch (getScope()) {
            case OBJECT:
                return processObject(systemNode);
            case ATTRIBUTE:
                return processAttributeScope(systemNode);
            default:
                return error("Unexpected scope: " + getScope() + ".");
        }
    }

    private boolean processObject(Node systemNode) {
        switch (getOperator()) {
            case EXISTS:
                // TODO: This case seems futile, because it always returns true
                return processObjectTopicExists(systemNode);
            case NOT_EXISTS:
                // TODO: This case seems futile, because it always returns false
                return !processObjectTopicExists(systemNode);
            case EQUALS:
                return processObjectTopicEquals(systemNode);
            case NOT_EQUALS:
                return !processObjectTopicEquals(systemNode);
            default:
                return error("Unexpected operator while processing OBJECT: " + getOperator() + ".");
        }
    }

    private boolean processObjectTopicEquals(Node systemNode) {
        switch (getTopic()) {
            case TYPE:
                if (getMould().getType() == null) {
                    return error("Cannot perform rule on topic TYPE. Unable to detect what to check for.");
                }
                return systemNode.getType() == getMould().getType();
            case VISIBILITY:
                if (getMould().getVisibility() == null) {
                    return error("Cannot perform rule on topic VISIBILITY. Unable to detect what to check for.");
                }
                return systemNode.getVisibility() == getMould().getVisibility();
            case MODIFIER_ROOT:
                if (getMould().isRoot() == null) {
                    return error("Cannot perform rule on topic MODIFIER_ROOT. Unable to detect what to check for.");
                }
                return systemNode.isRoot() == getMould().isRoot();
            case MODIFIER_LEAF:
                if (getMould().isLeaf() == null) {
                    return error("Cannot perform rule on topic MODIFIER_LEAF. Unable to detect what to check for.");
                }
                return systemNode.isLeaf() == getMould().isLeaf();
            case MODIFIER_ABSTRACT:
                if (getMould().isAbstract() == null) {
                    return error("Cannot perform rule on topic MODIFIER_ABSTRACT. Unable to detect what to check for.");
                }
                return systemNode.isAbstract() == getMould().isAbstract();
            case MODIFIER_ACTIVE:
                if (getMould().isActive() == null) {
                    return error("Cannot perform rule on topic MODIFIER_ACTIVE. Unable to detect what to check for.");
                }
                return systemNode.isActive() == getMould().isActive();
            default:
                return error("Unexpected topic while processing OBJECT: " + getTopic() + ".");
        }
    }

    // TODO: This method seems futile: system nodes always have a type, visibility, and a value for all modifiers
    // TODO: instead of false, we should perhaps throw a RuleException when a topic does not exist?
    private boolean processObjectTopicExists(Node systemNode) {
        switch (getTopic()) {
            case TYPE:
                return systemNode.getType() != null;
            case VISIBILITY:
                return systemNode.getVisibility() != null;
            case MODIFIER_ROOT:
                return systemNode.isRoot() != null;
            case MODIFIER_LEAF:
                return systemNode.isLeaf() != null;
            case MODIFIER_ABSTRACT:
                return systemNode.isAbstract() != null;
            case MODIFIER_ACTIVE:
                return systemNode.isActive() != null;
            default:
                return error("Unexpected topic while processing OBJECT: " + getTopic() + ".");
        }
    }

    private boolean processAttributeScope(Node systemNode) {
    	switch (getOperator()) {
		    case EXISTS:
		        // TODO: This case seems futile, because it always returns true
		        return processAttributeTopicExists(systemNode);
		    case NOT_EXISTS:
		        // TODO: This case seems futile, because it always returns false
		        return !processAttributeTopicExists(systemNode);
		    case EQUALS:
		        return processAttributeTopicEquals(systemNode);
		    case NOT_EQUALS:
		        return !processAttributeTopicEquals(systemNode);
		    default:
		    	return error("Unexpected operator while processing ATTRIBUTE: " + getOperator() + ".");
    	}
    }

    /**
	 * @param systemNode
	 * @return
	 */
	private boolean processAttributeTopicEquals(Node systemNode) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @param systemNode
	 * @return
	 */
	private boolean processAttributeTopicExists(Node systemNode) {
		
		return false;
	}

	private boolean error(String message) {
        LOGGER.error(message);
        throw new RuleException(message);
    }

}
