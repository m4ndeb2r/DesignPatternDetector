package nl.ou.dpd.parsing.template;

import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.Visibility;
import nl.ou.dpd.domain.rule.NodeRule;
import nl.ou.dpd.domain.rule.Rule;
import nl.ou.dpd.domain.rule.RuleException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class that applies a NodeRule using a specified value.
 * Results in the Node, given in the Rule, implementing the Rule in the Node itself.
 *
 * @author Peter Vansweevelt
 */
public class NodeRuleElementApplicator extends RuleElementApplicator<Node> {

    private static final Logger LOGGER = LogManager.getLogger(NodeRuleElementApplicator.class);

    private Node node;

    /**
     * @param rule
     * @param value
     */
    public NodeRuleElementApplicator(Rule<Node> rule, String value) {
        super(rule, value);
        node = rule.getMould();
    }

    /**
     * Applies this {@link NodeRule} on a given {@ink Node}.
     * Results in the Node, given in the Rule, implementing the Rule in the Node itself.
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
            case MODIFIER_ROOT:
                applyObjectModifierRoot();
                break;
            case MODIFIER_LEAF:
                applyObjectModifierLeaf();
                node.setLeaf(Boolean.valueOf(getValue()));
                break;
            case MODIFIER_ABSTRACT:
                applyObjectModifierAbstract();
                node.setAbstract(Boolean.valueOf(getValue()));
                break;
            case MODIFIER_ACTIVE:
                applyObjectModifierActive();
                node.setActive(Boolean.valueOf(getValue()));
                break;
            default:
                error(String.format("Unexpected topic while applying OBJECT: '%s'.", getTopic()));
        }
    }

    private void applyObjectVisibility() {
        switch (getOperator()) {
            case EQUALS:
                applyObjectVisibilityEquals();
                break;
            default:
                error(String.format("Unexpected operator while applying CARDINALITY: '%s'.", getOperator()));

        }
    }

    private void applyObjectModifierRoot() {
        switch (getOperator()) {
            case EQUALS:
                applyObjectModifierRootEquals();
                break;
            default:
                error(String.format("Unexpected operator while applying MODIFIER_ROOT: '%s'.", getOperator()));
        }
    }

    private void applyObjectModifierActive() {
        switch (getOperator()) {
            case EQUALS:
                applyObjectModifierActiveEquals();
                break;
            default:
                error(String.format("Unexpected operator while applying MODIFIER_ACTIVE: '%s'.", getOperator()));
        }
    }

    private void applyObjectModifierLeaf() {
        switch (getOperator()) {
            case EQUALS:
                applyObjectModifierLeafEquals();
                break;
            default:
                error(String.format("Unexpected operator while applying MODIFIER_LEAF: '%s'.", getOperator()));
        }
    }

    private void applyObjectModifierAbstract() {
        switch (getOperator()) {
            case EQUALS:
                applyObjectModifierAbstractEquals();
                break;
            default:
                error(String.format("Unexpected operator while applying MODIFIER_ABSTRACT: '%s'.", getOperator()));
        }
    }

    private void applyObjectModifierActiveEquals() {
        node.setActive(Boolean.valueOf(getValue()));
    }

    private void applyObjectModifierAbstractEquals() {
        node.setAbstract(Boolean.valueOf(getValue()));
    }

    private void applyObjectModifierLeafEquals() {
        node.setLeaf(Boolean.valueOf(getValue()));
    }

    private void applyObjectModifierRootEquals() {
        node.setRoot(Boolean.valueOf(getValue()));
    }

    private void applyObjectVisibilityEquals() {
        node.setVisibility(Visibility.valueOf(getValue().toUpperCase()));
    }

    private void error(String message) {
        LOGGER.error(message);
        throw new RuleException(message);
    }
}
