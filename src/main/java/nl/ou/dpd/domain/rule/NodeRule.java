package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.node.Node;

/**
 * A {@link NodeRule} represents a part of a condition.
 *
 * @author Peter Vansweevelt
 * @author Martin de Boer
 */
public class NodeRule implements Rule<Node> {

    private final Node ruleNode;
    private final Operator operator;
    private final Target target;
    private final Topic topic;

    /**
     * Creates a rule with the {@link Node} node that implements the features needed to apply to this rule.
     *
     * @param node     the {@link Node} containing the rule's features
     * @param topic    holds the feature to be evaluated (type, visibility, ...)
     * @param target   the target of the evaluation (object, attributes, ...)
     * @param operator the evaluation operator (equals, exists, ...)
     */
    public NodeRule(Node node, Topic topic, Target target, Operator operator) {
        this.ruleNode = node;
        this.topic = topic;
        this.target = target;
        this.operator = operator;
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
        // We always inspect a node of a given class-type. TODO: should we indeed? Why?
        if (!systemNode.getName().equalsIgnoreCase(ruleNode.getName())) {
            return false;
        }
        if (target == Target.OBJECT) {
            return processObjectTarget(systemNode);
        }
        if (target == Target.ATTRIBUTE) {
            return processAttributeTarget(systemNode);
        }
        // TODO: throw exception? This is an unexpected situation.
        return false;
    }

    private boolean processObjectTarget(Node systemNode) {
        switch (topic) {
            case TYPE:
                // If ruleNode.type is not set, return true, otherwise check for equal types
                return ruleNode.getType() == null || systemNode.getType() == ruleNode.getType();
            case VISIBILITY:
                // If ruleNode.visibility is not set, return true; otherwise check for equality
                return ruleNode.getVisibility() == null || systemNode.getVisibility() == ruleNode.getVisibility();
            case MODIFIER_ROOT:
                // If ruleNode.isRoot is not set, return true; otherwise check for equality
                return (ruleNode.isRoot() == null || systemNode.isRoot() == ruleNode.isRoot());
            case MODIFIER_LEAF:
                // If ruleNode.isLeaf is not set, return true; otherwise check for equality
                return (ruleNode.isLeaf() == null || systemNode.isLeaf() == ruleNode.isLeaf());
            case MODIFIER_ABSTRACT:
                // If ruleNode.isAbstract is not set, return true; otherwise check for equality
                return (ruleNode.isAbstract() == null || systemNode.isAbstract() == ruleNode.isAbstract());
            case MODIFIER_ACTIVE:
                // If ruleNode.isActive is not set, return true; otherwise check for equality
                return (ruleNode.isActive() == null || systemNode.isActive() == ruleNode.isActive());
            default:
                // TODO: throw exception? This is an unexpected situation.
                return false;
        }
    }

    private boolean processAttributeTarget(Node systemNode) {
        // TODO: throw exception? This is an unexpected situation (unimplemented).
        return false;
    }

}
